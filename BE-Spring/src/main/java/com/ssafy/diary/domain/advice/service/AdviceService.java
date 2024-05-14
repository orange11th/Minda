package com.ssafy.diary.domain.advice.service;

import com.ssafy.diary.domain.advice.dto.AdviceRequestDto;
import com.ssafy.diary.domain.advice.dto.AdviceResponseDto;
import com.ssafy.diary.domain.advice.dto.SingleAdviceRequestDto;
import com.ssafy.diary.domain.advice.dto.SingleAdviceResponseDto;
import com.ssafy.diary.domain.advice.entity.Advice;
import com.ssafy.diary.domain.advice.repository.AdviceRepository;
import com.ssafy.diary.domain.analyze.entity.Analyze;
import com.ssafy.diary.domain.analyze.repository.AnalyzeRepository;
import com.ssafy.diary.domain.diary.entity.Diary;
import com.ssafy.diary.domain.diary.repository.DiaryRepository;
import com.ssafy.diary.global.exception.DiaryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdviceService {

    private final DiaryRepository diaryRepository;
    private final AdviceRepository adviceRepository;
    private final AnalyzeRepository analyzeRepository;
    @Autowired
    private final WebClient webClient;

    private String[] emotionArray = {"중립", "분노", "슬픔", "놀람", "불안", "기쁨"};

    @Transactional
    public SingleAdviceResponseDto getAdvice(Long memberIndex, SingleAdviceRequestDto singleAdviceRequestDto) {
        Diary diary = diaryRepository.findByMemberIndexAndDiarySetDate(memberIndex, singleAdviceRequestDto.getDate())
                .orElseThrow(() -> new DiaryNotFoundException("다이어리를 찾을 수 없습니다."));

        Advice advice = adviceRepository.findByMemberIndexAndPeriod(memberIndex, singleAdviceRequestDto.getDate(), singleAdviceRequestDto.getDate())
                .orElseThrow(() -> new IllegalStateException("저장된 조언을 찾을 수 없습니다."));

        Long diaryIndex = diary.getDiaryIndex();
        Analyze analyze = analyzeRepository.findByDiaryIndex(diaryIndex)
                .orElseThrow(() -> new IllegalStateException("저장된 분석 결과가 없습니다."));

        HashMap<String, Double[]> emotion = analyze.getEmotion();
        List<String> emotionList = new ArrayList<>();

        for (Double[] value : emotion.values()) {
            double max = -100;
            int maxIndex = -1;
            for (int i = 0; i < value.length; i++) {
                if (value[i] > max) {
                    max = value[i];
                    maxIndex = i;
                }
            }
            emotionList.add(emotionArray[maxIndex]);
        }
        HashMap<String, Double> statusMap = new HashMap<>();

        statusMap.put("분노", diary.getDiaryAnger());
        statusMap.put("슬픔", diary.getDiarySadness());
        statusMap.put("놀람", diary.getDiarySurprise());
        statusMap.put("불안", diary.getDiaryFear());
        statusMap.put("기쁨", diary.getDiaryHappiness());

        return SingleAdviceResponseDto.builder()
                .sentence(analyze.getSentence())
                .emotion(emotionList)
                .adviceContent(advice.getAdviceContent())
                .status(statusMap).build();
    }

    @Transactional
    public AdviceResponseDto getAdviceByPeriod(Long memberIndex, AdviceRequestDto adviceRequestDto) {
//        Advice advice = adviceRepository.findByMemberIndexAndPeriod(memberIndex, adviceRequestDto.getStartDate(), adviceRequestDto.getEndDate())
//                 .orElseThrow(()-> new IllegalStateException("저장된 조언을 찾을 수 없습니다."));

        List<Diary> diaryList = diaryRepository.findByMemberIndexAndDiarySetDateOrderByDiarySetDate(memberIndex, adviceRequestDto.getStartDate(), adviceRequestDto.getEndDate());

        HashMap<String, Double> statusMap = new HashMap<>();

        for (Diary diary : diaryList) {

            // statusMap에 각 다이어리의 감정값을 누적하여 넣습니다.
            statusMap.put("분노", statusMap.getOrDefault("분노", 0.0) + diary.getDiaryAnger());
            statusMap.put("슬픔", statusMap.getOrDefault("슬픔", 0.0) + diary.getDiarySadness());
            statusMap.put("놀람", statusMap.getOrDefault("놀람", 0.0) + diary.getDiarySurprise());
            statusMap.put("불안", statusMap.getOrDefault("불안", 0.0) + diary.getDiaryFear());
            statusMap.put("기쁨", statusMap.getOrDefault("기쁨", 0.0) + diary.getDiaryHappiness());
        }

        // 다이어리들의 감정값을 평균내어 statusMap에 넣습니다.
        int diaryCount = diaryList.size();
        if (diaryCount > 0) {
            statusMap.put("분노", statusMap.getOrDefault("분노", 0.0) / diaryCount);
            statusMap.put("슬픔", statusMap.getOrDefault("슬픔", 0.0) / diaryCount);
            statusMap.put("놀람", statusMap.getOrDefault("놀람", 0.0) / diaryCount);
            statusMap.put("불안", statusMap.getOrDefault("불안", 0.0) / diaryCount);
            statusMap.put("기쁨", statusMap.getOrDefault("기쁨", 0.0) / diaryCount);
        }

        return AdviceResponseDto.builder()
                .adviceContent("null")
//                .adviceContent(advice.getAdviceContent())
                .status(statusMap).build();
    }

    public String getWordcloudByPeriod(Long memberIndex, LocalDate startDate, LocalDate endDate) {
        List<Diary> diaryList = diaryRepository.findByMemberIndexAndDiarySetDateOrderByDiarySetDate(memberIndex, startDate, endDate);
        List<Integer> diaryIndexList = diaryList.stream().map(Diary::getDiaryIndex).map(Long::intValue).collect(Collectors.toList());

        Map<String, List<Integer>> payload = new HashMap<>();
        payload.put("diary_index_list", diaryIndexList);

        Mono<String> response = webClient.post()
                .uri("http://192.168.31.35:8000/api/ai/wordcloud")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class);

        return response.block();  // 이 부분은 비동기 처리로 변경하는 것이 바람직
    }
}

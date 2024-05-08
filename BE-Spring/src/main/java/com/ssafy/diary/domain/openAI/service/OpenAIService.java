package com.ssafy.diary.domain.openAI.service;

import com.ssafy.diary.domain.diary.entity.Diary;
import com.ssafy.diary.domain.diary.entity.Image;
import com.ssafy.diary.domain.diary.repository.DiaryRepository;
import com.ssafy.diary.domain.diary.repository.ImageRepository;
import com.ssafy.diary.domain.openAI.dto.DallERequest;
import com.ssafy.diary.domain.openAI.dto.DallEResponse;
import com.ssafy.diary.domain.s3.service.S3Service;
import com.ssafy.diary.global.exception.AlreadyExistsImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.swing.text.html.Option;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService {
    private final DiaryRepository diaryRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate restTemplate;

    public String generateImage(Long diaryIndex) {
        if(imageRepository.existsByDiaryIndex(diaryIndex)){
          throw new AlreadyExistsImageException("이미 이미지가 존재합니다.");
        }
        Diary diary = diaryRepository.findById(diaryIndex).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 다이어리입니다."));
        DallERequest request = DallERequest.builder()
                .model("dall-e-3")
                .prompt(diary.getDiaryContent())
                .n(1)
                .size("1024x1024").build();
        DallEResponse response = restTemplate.postForObject(apiURL+"images/generations", request, DallEResponse.class);

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            throw new RuntimeException("Failed to receive a valid response from DALL-E API");
        }
        String imageUrl = response.getData().get(0).getUrl();
        try {
            String s3ImageUrl = s3Service.saveImageFromUrl(imageUrl);
            imageRepository.save(Image.builder()
                    .diaryIndex(diaryIndex)
                    .imageLink(s3ImageUrl)
                    .imageName("AI").build());
            return s3ImageUrl;
        }catch(IOException e){
            return e.getMessage();
        }
    }
}

import 'package:diary_fe/src/screens/intro_page.dart';
import 'package:diary_fe/src/widgets/background.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Scaffold(
        body: Stack(
          children: <Widget>[
            Background(),
            IntroPage(),
          ],
        ),
      ),
    );
  }
}

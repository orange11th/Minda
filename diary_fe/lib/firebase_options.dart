// File generated by FlutterFire CLI.
// ignore_for_file: type=lint
import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        return windows;
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyBq358c85xFpsEiAiVK3uaxoYSEr_UDHpw',
    appId: '1:901079657910:web:989a470a4267d7e680a517',
    messagingSenderId: '901079657910',
    projectId: 'coldiary-72f1c',
    authDomain: 'coldiary-72f1c.firebaseapp.com',
    storageBucket: 'coldiary-72f1c.appspot.com',
    measurementId: 'G-0PC9BW4VS3',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyDtFlSBsRMF1f9Mbl-07vLCPVRJpSPu0PY',
    appId: '1:901079657910:android:3ebd6f43c7ea08cb80a517',
    messagingSenderId: '901079657910',
    projectId: 'coldiary-72f1c',
    storageBucket: 'coldiary-72f1c.appspot.com',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyD6oc5_sZttXJaLCDoD3r3-oMHyo0o2kw0',
    appId: '1:901079657910:ios:b0506970d639b49f80a517',
    messagingSenderId: '901079657910',
    projectId: 'coldiary-72f1c',
    storageBucket: 'coldiary-72f1c.appspot.com',
    iosBundleId: 'com.minda.app.diaryFe',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'AIzaSyD6oc5_sZttXJaLCDoD3r3-oMHyo0o2kw0',
    appId: '1:901079657910:ios:b0506970d639b49f80a517',
    messagingSenderId: '901079657910',
    projectId: 'coldiary-72f1c',
    storageBucket: 'coldiary-72f1c.appspot.com',
    iosBundleId: 'com.minda.app.diaryFe',
  );

  static const FirebaseOptions windows = FirebaseOptions(
    apiKey: 'AIzaSyBq358c85xFpsEiAiVK3uaxoYSEr_UDHpw',
    appId: '1:901079657910:web:d46e4ca7addeec3380a517',
    messagingSenderId: '901079657910',
    projectId: 'coldiary-72f1c',
    authDomain: 'coldiary-72f1c.firebaseapp.com',
    storageBucket: 'coldiary-72f1c.appspot.com',
    measurementId: 'G-SG3Q8627DZ',
  );
}

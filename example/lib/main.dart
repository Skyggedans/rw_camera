import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:rw_camera/rw_camera.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Camera plugin for RealWear HMT-1(Z1) example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              RaisedButton(
                child: const Text('Take Photo'),
                onPressed: () async {
                  await RwCamera.takePhotoToFile();
                },
              ),
              RaisedButton(
                child: const Text('Record Video'),
                onPressed: () async {
                  await RwCamera.recordVideo();
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}

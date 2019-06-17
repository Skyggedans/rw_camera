import 'dart:async';

import 'package:flutter/foundation.dart';

import 'package:flutter/services.dart';
import 'dart:typed_data';

enum CompressFormat { JPEG, PNG, WEBP }

class RwCamera {
  static MethodChannel _channel =
      MethodChannel('com.rockwellits.rw_plugins/rw_camera');

  static Future<Uint8List> takePhoto({
    CompressFormat format: CompressFormat.PNG,
    quality: 100,
  }) async {
    return await _channel.invokeMethod('takePhoto', {
      'format': describeEnum(format),
      'quality': quality,
    });
  }

  static Future<String> recordVideo() async {
    return await _channel.invokeMethod('recordVideo');
  }
}

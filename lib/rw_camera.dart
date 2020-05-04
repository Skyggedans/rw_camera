import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

enum CompressFormat { JPEG, PNG, WEBP }

class RwCamera {
  static MethodChannel _channel =
      MethodChannel('com.rockwellits.rw_plugins/rw_camera');

  /// Takes photo to array of bytes.
  ///
  /// Takes photo to array of bytes in [format] with [quality].
  static Future<Uint8List> takePhotoToBytes({
    CompressFormat format: CompressFormat.PNG,
    quality: 100,
  }) async {
    return await _channel.invokeMethod('takePhotoToBytes', {
      'format': describeEnum(format),
      'quality': quality,
    });
  }

  /// Takes photo to file.
  ///
  /// Takes photo to file and [returns] its path.
  static Future<String> takePhotoToFile() async {
    return await _channel.invokeMethod('takePhotoToFile');
  }


  /// Records video to file.
  ///
  /// Records video to file and [returns] its path.
  static Future<String> recordVideo() async {
    return await _channel.invokeMethod('recordVideo');
  }
}

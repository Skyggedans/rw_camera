import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:rw_camera/rw_camera.dart';

void main() {
  const MethodChannel channel = MethodChannel('rw_camera');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    //expect(await RwCamera.platformVersion, '42');
  });
}

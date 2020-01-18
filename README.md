# rw_camera

Camera Plugin for RealWear HMT-1(Z1)

## Usage

```dart
import 'package:rw_camera/rw_camera.dart';

// Take photo
final Uint8List bytes = await RwCamera.takePhotoToBytes(CompressFormat.PNG, 100);
// or
final String fileName = await RwCamera.takePhotoToFile();

// Record video
final String fileName = await RwCamera.recordVideo();
```
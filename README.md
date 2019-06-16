# rw_camera

Camera Plugin for RealWear HMT-1

## Usage

```dart
// Take photo
final Uint8List bytes = await RwCamera.takePhoto();

// Record video
final String fileName = await RwCamera.recordVideo();
```
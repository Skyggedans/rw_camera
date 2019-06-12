#import "RwCameraPlugin.h"
#import <rw_camera/rw_camera-Swift.h>

@implementation RwCameraPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftRwCameraPlugin registerWithRegistrar:registrar];
}
@end

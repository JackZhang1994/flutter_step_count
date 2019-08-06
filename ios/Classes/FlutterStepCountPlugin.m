#import "FlutterStepCountPlugin.h"
#import "StepCountUtils.h"

@implementation FlutterStepCountPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_step_count"
            binaryMessenger:[registrar messenger]];
  FlutterStepCountPlugin* instance = [[FlutterStepCountPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getStepCount" isEqualToString:call.method]) {
      StepCountUtils *vc = [[StepCountUtils alloc] init];
      [vc getStepCountWithReslut:^(NSString * _Nonnull step) {
          result(step);
      }];
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end

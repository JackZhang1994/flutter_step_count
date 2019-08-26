import 'dart:async';

import 'package:flutter/services.dart';

class FlutterStepCount {
  static const MethodChannel _channel = const MethodChannel('flutter_step_count');

  static Future<void> get init async {
    await _channel.invokeMethod('method_init');
  }

  static Future<String> get stepCount async {
    final String stepCount = await _channel.invokeMethod('getStepCount');
    return stepCount;
  }
}

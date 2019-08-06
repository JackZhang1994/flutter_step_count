import 'dart:async';

import 'package:flutter/services.dart';

class FlutterStepCount {
  static const MethodChannel _channel =
      const MethodChannel('flutter_step_count');

  static Future<int> get stepCount async {
    final int stepCount = await _channel.invokeMethod('getStepCount');
    return stepCount;
  }
}

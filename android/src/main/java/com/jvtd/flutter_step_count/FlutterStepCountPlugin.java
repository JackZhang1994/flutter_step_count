package com.jvtd.flutter_step_count;

import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterStepCountPlugin */
public class FlutterStepCountPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_step_count");
    channel.setMethodCallHandler(new FlutterStepCountPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getStepCount")) {
      Log.d("步数", String.valueOf(StepApplication.getStepSum()));
      result.success(StepApplication.getStepSum());
    } else {
      result.notImplemented();
    }
  }
}

package com.jvtd.flutter_step_count;

import android.content.Context;
import android.content.Intent;

import com.today.step.lib.BaseClickBroadcast;

public class MyReceiver extends BaseClickBroadcast
{

  private static final String TAG = "MyReceiver";

  @Override
  public void onReceive(Context context, Intent intent)
  {
    StepApplication tsApplication = (StepApplication) context.getApplicationContext();
    if (!tsApplication.isForeground())
    {
      Intent mainIntent = tsApplication.getActivity();
      mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(mainIntent);
    }
  }
}
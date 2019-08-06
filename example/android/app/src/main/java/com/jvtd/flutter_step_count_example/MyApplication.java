package com.jvtd.flutter_step_count_example;

import android.content.Intent;
import android.util.Log;

import com.jvtd.flutter_step_count.StepApplication;

/**
 * 作者:chenlei
 * 时间:2019-08-06 13:07
 */
public class MyApplication extends StepApplication
{
  @Override
  public void onCreate()
  {
    super.onCreate();
    Log.d("初始化", "成功");
  }

  @Override
  public String getPackageName()
  {
    return "com.jvtd.flutter_step_count_example";
  }

  @Override
  public Intent getActivity()
  {
    return new Intent(getApplicationContext(), MainActivity.class);
  }
}

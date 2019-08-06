package com.jvtd.flutter_step_count;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.today.step.lib.TodayStepService;

import io.flutter.app.FlutterApplication;

/**
 * 作者:chenlei
 * 时间:2019-08-06 12:45
 */
public abstract class StepApplication extends FlutterApplication
{
  private static StepApplication sApplication;

  private int appCount = 0;

  @Override
  public void onCreate()
  {
    super.onCreate();
    MultiDex.install(this);

    sApplication = this;

    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
    {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState)
      {

      }

      @Override
      public void onActivityStarted(Activity activity)
      {
        appCount++;
      }

      @Override
      public void onActivityResumed(Activity activity)
      {

      }

      @Override
      public void onActivityPaused(Activity activity)
      {

      }

      @Override
      public void onActivityStopped(Activity activity)
      {
        appCount--;
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState)
      {

      }

      @Override
      public void onActivityDestroyed(Activity activity)
      {

      }
    });

    initStepCount();
  }

  /**
   * app是否在前台
   *
   * @return true前台，false后台
   */
  public boolean isForeground()
  {
    return appCount > 0;
  }

  public static StepApplication getApplication()
  {
    return sApplication;
  }


  private static ISportStepInterface iSportStepInterface;

  public static int getStepSum()
  {
    if (iSportStepInterface != null)
    {
      try
      {
        Log.d("步数", String.valueOf(iSportStepInterface.getCurrentTimeSportStep()));
        return iSportStepInterface.getCurrentTimeSportStep();
      } catch (RemoteException e)
      {
        e.printStackTrace();
      }
    }
    return 0;
  }

  public abstract String getPackageName();

  public abstract Intent getActivity();


  private void initStepCount()
  {
    Log.d("步数", "初始化计步器");

    //初始化计步模块
    TodayStepManager.startTodayStepService(getApplication());

    //开启计步Service，同时绑定Activity进行aidl通信
    Intent intent = new Intent(this, TodayStepService.class);
    intent.setPackage(getPackageName());
    startService(intent);
    bindService(intent, new ServiceConnection()
    {
      @Override
      public void onServiceConnected(ComponentName name, IBinder service)
      {
        //Activity和Service通过aidl进行通信
        Log.d("步数", "service绑定");
        iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
      }

      @Override
      public void onServiceDisconnected(ComponentName name)
      {
        Log.d("步数", "onServiceDisconnected");
      }
    }, Context.BIND_AUTO_CREATE);
  }
}

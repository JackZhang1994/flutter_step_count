package com.jvtd.flutter_step_count;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.TodayStepManager;
import com.today.step.lib.TodayStepService;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterStepCountPlugin
 */
public class FlutterStepCountPlugin implements MethodCallHandler
{

  private static final String CHANNEL = "flutter_step_count";
  private static final String METHOD_INIT = "method_init";
  private static final String METHOD_GET_STEP_COUNT = "getStepCount";

  private final MethodChannel mChannel;
  private final Activity mActivity;
  private ISportStepInterface iSportStepInterface;

  private FlutterStepCountPlugin(final MethodChannel channel, Activity activity)
  {
    this.mChannel = channel;
    this.mActivity = activity;
  }

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar)
  {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
    channel.setMethodCallHandler(new FlutterStepCountPlugin(channel, registrar.activity()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result)
  {
    if (call.method.equals(METHOD_INIT))
    {
      initStepCount();
    } else if (call.method.equals(METHOD_GET_STEP_COUNT))
    {
      Log.d("步数", String.valueOf(getStepSum()));
      result.success(String.valueOf(getStepSum()));
    } else
    {
      result.notImplemented();
    }
  }

  private void initStepCount()
  {
    Log.d("步数", "初始化计步器");

    //初始化计步模块
    TodayStepManager.startTodayStepService(mActivity.getApplication());

    //开启计步Service，同时绑定Activity进行aidl通信
    Intent intent = new Intent(mActivity, TodayStepService.class);
    intent.setPackage(mActivity.getPackageName());
    mActivity.startService(intent);
    mActivity.bindService(intent, new ServiceConnection()
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

  private int getStepSum()
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


}

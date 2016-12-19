

package com.gdmss.activities;


import com.Player.web.response.ResponseServer;
import com.Player.web.websocket.ClientCore;
import com.gdmss.base.BaseActivity;
import com.utils.CommonData;
import com.utils.L;
import com.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class AcLoading extends BaseActivity
{
    ClientCore client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.gdmss.R.layout.ac_loading);
        initClient();
    }

    void initClient()
    {
        client = ClientCore.getInstance();
        int language = Utils.isZh(context) ? 2 : 1;
        client.setupHost(context,CommonData.ServerAddress,0,Utils.getImsi(context),language,CommonData.ClientID,Utils.getVersionName(context),"");
        client.getCurrentBestServer(context,han);


//        PackageManager pkgManager = getPackageManager();
//        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
//        boolean sdCardWritePermission =
//                pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,getPackageName()) == PackageManager.PERMISSION_GRANTED;
//
//        // read phone state用于获取 imei 设备信息
//        boolean phoneSatePermission =
//                pkgManager.checkPermission(android.Manifest.permission.READ_PHONE_STATE,getPackageName()) == PackageManager.PERMISSION_GRANTED;
//        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission)
//        {
//            requestPermission();
//        }
//        else
//        {
//        PushManager.getInstance().initialize(context,AlarmService.class);
//        }
//        PushManager.getInstance().registerPushIntentService(context,AlarmIntentService.class);
    }

//    private void requestPermission()
//    {
//        ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
//                0);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
//    {
//        if (requestCode == 0)
//        {
//            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED))
//            {
//                PushManager.getInstance().initialize(this.getApplicationContext(),AlarmService.class);
//            }
//            else
//            {
//                L.e("We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
//                        + "functions will not work");
//                PushManager.getInstance().initialize(this.getApplicationContext(),AlarmService.class);
//            }
//        }
//        else
//        {
//            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        }
//    }

    private Handler han = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            ResponseServer response = (ResponseServer) msg.obj;
            if (null != response && null != response.h)
            {
                if (response.h.e != 200)
                {
                    L.e("获取最优服务器失败");
                }
            }
            startActivity(new Intent(context,AcLogin.class));
            finish();
            return false;
        }
    });
}

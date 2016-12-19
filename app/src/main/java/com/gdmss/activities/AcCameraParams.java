package com.gdmss.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.Player.Core.PlayerClient;
import com.Player.Source.TCameraParam;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.T;
import com.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置镜头参数(镜头翻转)
 */

public class AcCameraParams extends BaseActivity
{
    @BindView(R.id.tg_scale)
    ToggleButton tgScale;

    TCameraParam tCameraParam;
    PlayerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cameraparams);
        ButterKnife.bind(this);
        initParam();
        tgScale.setOnToggleChanged(new ToggleButton.OnToggleChanged()
        {
            @Override
            public void onToggle(View v,boolean on)
            {
                if (null != tCameraParam)
                {
                    tCameraParam.iIfPictureFlip = on ? 1 : 0;
                    showProgress();
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            int result = client.CameraSetCameraParam(node.getDeviceId(),tCameraParam);
                            setHandler.sendEmptyMessage(result);
                        }
                    }).start();
                }
            }
        });
    }

    PlayNode node;

    void initParam()
    {
        node = (PlayNode) getIntent().getSerializableExtra("node");
        showProgress();
        startThread();
    }

    void getCameraParam()
    {
        tCameraParam = new TCameraParam();
        client = new PlayerClient();
        int result = client.CameraGetCameraParam(node.getDeviceId(),tCameraParam);
        getHandler.sendEmptyMessage(result);
    }

    @Override
    protected void runInThread()
    {
        getCameraParam();
    }

    private Handler getHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            if (msg.what == 0)
            {
                tgScale.setChecked(tCameraParam.iIfPictureFlip == 1);
            }
            else
            {
                T.showS(R.string.msg_get_params_fail);
                finish();
            }
            return false;
        }
    });

    private Handler setHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            if (msg.what == 0)
            {
                T.showS(R.string.msg_set_params_success);
                finish();
            }
            else
            {
                T.showS(R.string.msg_set_params_fail);
                tgScale.setChecked(!tgScale.toggleOn);
            }
            return false;
        }
    });
}

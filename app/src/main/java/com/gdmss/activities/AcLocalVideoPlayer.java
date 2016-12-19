package com.gdmss.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.Player.Core.PlayerCore;
import com.Player.Source.SDKError;
import com.Player.Source.TMp4FileInfo;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.utils.L;
import com.utils.Path;
import com.utils.ScreenUtils;
import com.utils.T;
import com.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/5.
 */

public class AcLocalVideoPlayer extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    PlayerCore player;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.ibtn_pause_play)
    ImageButton ibtnPausePlay;
    @BindView(R.id.ibtn_stop)
    ImageButton ibtnStop;
    @BindView(R.id.ibtn_snap)
    ImageButton ibtnSnap;
    @BindView(R.id.img)
    ImageView img;

    String fileName;

    int totalTime = 0;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_localvideoplayer);
        ButterKnife.bind(this);
        initParam();
        initPlayer();
        initViews();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        player.Play();
        startMonitorThread();
    }

    void initParam()
    {
        fileName = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(fileName))
        {
            finish();
        }
    }

    void initViews()
    {
        ibtnPausePlay.setOnClickListener(this);
        ibtnStop.setOnClickListener(this);
        ibtnSnap.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        img.setOnClickListener(this);

        TMp4FileInfo info = player.GetMp4FileInfo(fileName);
        totalTime = info.totaltime;
        seekBar.setMax(totalTime);
        tvTotal.setText(Utils.timeConvertion(totalTime));
    }

    void initPlayer()
    {
        player = new PlayerCore(this,100);
        player.InitParam(fileName,-1,img);
        player.OpenAudio();
    }

    void startMonitorThread()
    {
        isRunning = true;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (isRunning)
                {
                    int state = player.GetPlayerState();
                    stateHan.sendEmptyMessage(state);
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Handler stateHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            switch (message.what)
            {
                case SDKError.Statue_PLAYING:
                    ibtnPausePlay.setImageResource(R.drawable.live_bottom_pause_n);
                    int progress = player.GetCurrentPlayTime_Int();
                    seekBar.setProgress(progress);
                    if (progress == totalTime)
                    {
                        player.Stop();
                        seekBar.setProgress(0);
                    }
                    tvCurrent.setText(Utils.timeConvertion(progress));
                    break;
                case SDKError.Statue_Pause:
                    ibtnPausePlay.setImageResource(R.drawable.live_bottom_play_n);
                    break;
                case SDKError.Statue_STOP:
                    ibtnPausePlay.setImageResource(R.drawable.live_bottom_play_n);
                    break;
                case SDKError.NET_NODATA_ERROR:
                    player.Stop();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onStop()
    {
        isRunning = false;
        player.Stop();
        player.Realse();
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            ScreenUtils.hideStatusBar(this);
        }
        else
        {
            ScreenUtils.showStatusBar(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ibtn_pause_play:
                int state = player.GetPlayerState();
                if (state == SDKError.Statue_PLAYING)
                {
                    player.Pause();
                }
                else if (state == SDKError.Statue_Pause)
                {
                    player.Resume();
                }
                else if (state == SDKError.Statue_STOP)
                {
                    player.Play();
                }
                break;
            case R.id.ibtn_stop:
                player.Stop();
                break;
            case R.id.ibtn_snap:
                player.SetAlbumPath(Path.SNAPSHOT);
                player.SetSnapPicture(true);
                T.showS(R.string.msg_snap_success);
                break;
            case R.id.img:
                boolean isShow = rlTitle.getVisibility() == View.VISIBLE;
                if (isShow)
                {
                    rlTitle.setVisibility(View.GONE);
                    llBottom.setVisibility(View.GONE);
                }
                else
                {
                    rlTitle.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar,int i,boolean b)
    {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        player.Pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        final int progress = seekBar.getProgress();
        player.SetCurrentPlayTime(progress);
        player.SeekFilePos(progress / 1000,0);
        player.Resume();
        L.e("--------progress" + progress);
    }
}

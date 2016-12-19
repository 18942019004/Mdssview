

package com.widget;


import java.io.File;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.Player.Core.PlayerCore;
import com.Player.Source.SDKError;
import com.Player.Source.TDateTime;
import com.gdmss.R;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.VideoListResult;
import com.utils.L;
import com.utils.LocalFile;
import com.utils.Path;
import com.utils.T;
import com.utils.Utility;
import com.utils.Utils;


public class VideoCanvas
{
    public static final int MAX_RECONNECT_COUNT = 10;// 最多连接次数

    protected static final int STATE = 0;

    protected static final int RECONENT = 1;

    private static final String SEPERATER = "-";

    public static final byte READY = 0; // 准备就绪

    public static final byte CONNECTTING = 1;// 连接中

    public static final byte PLAYING = 2;// 播放中

    public static final byte CONNECTFAIL = 3;// 播放失败

    public static final byte STOP = 4;// 停止

    public boolean isShow = false;

    public RelativeLayout rlBackground;// 背景

    public ImageView imgVideo;// 视频显示

//    public ScaleableImageview imgVideo;// 视频显示

    public ProgressBar progress;

    public PlayerCore player;// 播放器

    private int channel = -1;// 通道号

    private Context context;

    public String Name;// 设备名

    public String DevId;// 设备ID

    public String Address;// 地址

    public int Port;// 端口

    public String UserName;// 用户名

    public String Password;// 密码

    public int Channel;// 通道号

    public int MediaStreamType;// 播放的媒体类型

    // public int mediaStreamType1;//播放的媒体类型

    public int StreamParserType;// 流解析类型

    public boolean hasPrepare = false;// 是否初始化

    private int ReconnectCount = 0;

    // private boolean isReconnecting = false;// 是否在重连之中
    public int tag;

    private boolean isFullScreen = false;// 是否全屏

    private boolean isAutoPlay = false;// 是否自动播放

    public boolean IsOpenSound = false;

    public TextView tvFps;

    public TextView tvRec;

    public TextView btnChoose;

    private MotionEvent event;

    int index;

    // boolean isScale = false;
    long datacount = 0;

    public ImageButton[] imgBtn = new ImageButton[8];

    private String imageDir;// 录像路径

    private String vdoDir;// 图片路径

    public boolean IsAudio;

    public boolean monitor = true;

    public PlayStateChangeListener stateChangeListener;

    private boolean isPlayback = false;

    private int laststate = 0;

    public PlayNode node;

    public Imagedeal deal;

    // public VideoCanvas()
    // {
    // context = getContext();
    // inflater = LayoutInflater.from(context);
    // view = inflater.inflate(R.layout.view_player,null,false);
    // rlBackground = (RelativeLayout) view.findViewById(R.id.background);
    // // LayoutParams lp = new RelativeLayout.LayoutParams(w,h);
    // // lp.rightMargin = -2 * w;
    // // lp.bottomMargin = -2 * h;
    // // rlBackground.setLayoutParams(lp);
    // // rlBackground.setPadding(2,2,2,2);
    // imgVideo = (ImageView) view.findViewById(R.id.img);
    // tvFps = (TextView) view.findViewById(R.id.tv_fps);
    // btnChoose = (Button) view.findViewById(R.id.btn_choose);
    // progress = (ProgressBar) view.findViewById(R.id.progress);
    // progress.setVisibility(View.GONE);
    // player = new PlayerCore(context);
    // }

    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */

    int margin = 0;


    public VideoCanvas(Context context)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.view_player,null,false);
        rlBackground = (RelativeLayout) view.findViewById(R.id.background);
        imgVideo = (ImageView) view.findViewById(R.id.img);
        tvFps = (TextView) view.findViewById(R.id.tv_fps);
        btnChoose = (TextView) view.findViewById(R.id.btn_choose);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        tvRec = (TextView) view.findViewById(R.id.tv_rec);
        progress.setVisibility(View.GONE);
        player = new PlayerCore(context);
        player.SetOpenLog(false);
        player.isQueryDevInfo = true;
    }

    public void setView(Context context,View v)
    {
        this.context = context;
        rlBackground = (RelativeLayout) v.findViewById(R.id.background);
        imgVideo = (ImageView) v.findViewById(R.id.img);
        tvFps = (TextView) v.findViewById(R.id.tv_fps);
        btnChoose = (TextView) v.findViewById(R.id.btn_choose);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        tvRec = (TextView) v.findViewById(R.id.tv_rec);
        progress.setVisibility(View.GONE);
        if (null == player)
        {
            player = new PlayerCore(context);
        }
        player.SetOpenLog(false);
        player.isQueryDevInfo = true;
    }

    LayoutInflater inflater;

    View view;

    public MotionEvent getEvent()
    {
        return event;
    }

    Handler stateChangeHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            // if(isPlayback)
            // {
            switch (msg.what)
            {
                case PLAYING:
                    btnChoose.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    break;
                case CONNECTTING:
                    btnChoose.setVisibility(View.INVISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    break;
                case STOP:
                    btnChoose.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    break;
                case READY:
                    btnChoose.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    break;
                case CONNECTFAIL:
//                    player.Stop();
                    btnChoose.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                    break;
                default:
                    progress.setVisibility(View.INVISIBLE);
                    break;
            }
            // }
            // else
            // {
            // progress.setVisibility(View.GONE);
            // }
            return false;
        }
    });

    class stateThread extends Thread
    {
        @Override
        public void run()
        {
            while (monitor)
            {
                int state = player.PlayCoreGetCameraPlayerState();
//                L.e("state:" + state);
                if (state != laststate)
                {
                    if (state == PLAYING)
                    {
                        stateChangeHandler.sendEmptyMessage(PLAYING);
                    }
                    else if (state == CONNECTFAIL)
                    {
                        stateChangeHandler.sendEmptyMessage(CONNECTFAIL);
                    }
                    else if (state == CONNECTTING || state == 10)
                    {
                        stateChangeHandler.sendEmptyMessage(CONNECTTING);
                    }
                    else
                    {
                        stateChangeHandler.sendEmptyMessage(CONNECTFAIL);
                    }
                }
                if (state == PLAYING)
                {
                    fpsHandler.sendEmptyMessage(1);
                }
                laststate = state;
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler fpsHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            tvFps.setText(getPlayFrameRate());
            return false;
        }
    });

    public void setScreenScale(boolean isScreenScale)
    {
        imgVideo.setScaleType(isScreenScale ? ScaleType.FIT_CENTER : ScaleType.FIT_XY);
        Log.i("isScreenScale","isScreenScale : " + isScreenScale);
    }

    public boolean isIsOpenSound()
    {
        return IsOpenSound;
    }

    public void setIsOpenSound(boolean isOpenSound)
    {
        IsOpenSound = isOpenSound;
    }

    public void SetStreamParserType(int type)
    {

    }

    /**
     * 使用播放前的初始化
     */
    public void Prepare(String name,String deviceId,boolean isRealTime,String route)
    {
        Log.d("playcoreinit","playcoreinit"); // 运行
        this.Name = name;
        player.SetPPtMode(false);
        player.SetOpenLog(false);
        player.CloseAudio();
        IsAudio = false;
        // imageDir = Config.UserImageDir;
        // vdoDir = Config.UserVideoDir;
        player.SetAlbumPath(imageDir);
        player.SetVideoPath(vdoDir);
        this.DevId = deviceId;
        player.InitParam(deviceId,-1,imgVideo);
        hasPrepare = true;
        ReconnectCount = 0;
    }

    void setMeadiaPlayType(final int type)
    {
//        if (isPrepared())
//        {
//            player.setMediaStreamType(type);
//        }
        if (isPlayed())
        {
//            player.setMediaStreamType(type);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    player.CameraSwitchChannel(type);
                }
            }).start();
        }
        else
        {
            player.setMediaStreamType(type);
            Play();
        }
    }

    void setMediaStreamType()
    {

    }

    public int getMediaStreamType()
    {
        return player.tDevNodeInfor.streamtype;
    }

    public boolean setIsAudio(boolean open)
    {
        IsAudio = open;
        if (isPrepared())
        {
            if (open)
            {
                player.OpenAudio();
            }
            else
            {
                player.CloseAudio();
            }
            return !player.GetIsVoicePause();
        }
        return true;
    }

    public void openAudio()
    {
        player.OpenAudio();
    }

    public void closeAudio()
    {
        player.CloseAudio();
    }

    public boolean IsAudio()
    {
        // IsAudio = isAudio;
        if (isPrepared())
        {
            return !player.GetIsVoicePause();
        }
        return false;

    }

    public boolean IsPPt()
    {
        // IsAudio = isAudio;
        if (isPrepared())
        {
            return player.GetIsPPT();
        }
        return false;
    }

    public void setPPT(boolean isOpen)
    {
        // IsAudio = isAudio;
        if (isPrepared())
        {
            if (isOpen)
            {
                player.StartPPTAudio();
            }
            else
            {
                player.StopPPTAudio();
            }
        }
    }

    public void setIsPlayback(boolean isplayback)
    {
        this.isPlayback = isplayback;
    }

    public boolean isPlayed()
    {
        return isPrepared() ? (player.PlayCoreGetCameraPlayerState() == PlayLayout.PLAYING) : false;
    }

    public boolean getHasPrepare()
    {
        return hasPrepare;
    }

    public String getPlayFrameRate()
    {
        if (isPrepared() && isPlayed())
        {
            return player.GetPlayFrameRate() + "fps" + "|" + player.GetFrameBitRate() + "kbps";
        }
        else
        {
            return "0fps|0kbps";
        }
    }

    public int[] getPlayRate()
    {
        int[] rate = new int[2];
        if (isPrepared() && isPlayed())
        {

            rate[0] = player.GetPlayFrameRate();
            rate[1] = player.GetFrameBitRate();
            return rate;
        }
        else
        {
            return null;
        }
    }

    public boolean getVideoRecordState()
    {
        if (isPrepared())
        {
            boolean isRecord = player.GetIsSnapVideo();
            setRecordingState(isRecord);
            return isRecord;
        }
        else
        {
            return false;
        }

    }

    public int getState()
    {
        if (isPrepared())
        {
            return player.PlayCoreGetCameraPlayerState();
        }
        return READY;
    }

    public int getplayerstate()
    {
        if (isPrepared())
        {
            int state = player.GetPlayerState();
            return state;
        }
        else
        {
            return SDKError.Statue_Ready;
        }
    }

    /**
     * 是否初始化播放地址
     */
    public boolean isPrepared()
    {
        return player == null ? false : !TextUtils.isEmpty(player.DeviceNo);
    }

    public void release()
    {
        // player.DeviceNo = null;
    }

    /**
     * 播放视频
     */
    public synchronized void Play()
    {
        // setScreenScale(false);
        if (isPrepared() && getState() != PLAYING)
        {
            player.Play();
            monitor = true;
            btnChoose.setVisibility(View.GONE);
            Log.e("Start Play","开始播放第" + channel + "个画面");
            new stateThread().start();
        }
    }

    /**
     * 重新连接
     */

    public synchronized void Reconnect()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                player.Stop();// Realse();
                handler.sendEmptyMessage(RECONENT);
            }
        }.start();
    }

    /**
     * 返回重连次数
     *
     * @return
     */
    public int getReconnectCount()
    {
        return ReconnectCount;
    }

    public int getChannel()
    {
        return channel;
    }

    public synchronized void stop()
    {
        datacount = player.getDataCount() + datacount; // getDataCount
        // 必须在stop前调用
        // 停止后datacount就清空了
        player.Stop();
        monitor = false;
    }

    public synchronized void stop(boolean clean)
    {
        datacount = player.getDataCount() + datacount; // getDataCount
        // 必须在stop前调用
        // 停止后datacount就清空了
        player.SetbCleanLastView(clean);
        Stop();
    }

    /**
     * 停止播放
     */
    public void Stop()
    {
        try
        {
            monitor = false;
            tvFps.setText("0fps|0kbps");
            if (player.GetIsSnapVideo())
            {
                player.SetSnapVideo(false);
                setRecordingState(false);
            }
            if (player.GetIsPPT())
            {
                Log.d("setPPT","Stop :StopPPTAudio");
                player.StopPPTAudio();
            }
            if (!player.GetIsVoicePause())
            {
                player.CloseAudio();
            }
            new Thread()
            {
                @Override
                public void run()
                {
                    if (player != null)
                    {
                        player.Stop();
                        player.Realse();
                    }
                    // if (VideoCanvas.this.getState() == 1)
                    // {
                    // player.Stop();// Realse();
                    // }
                    // else
                    // {
                    // VideoCanvas.this.stop();
                    // }
                    handler.sendEmptyMessage(STATE);
                    stateChangeHandler.sendEmptyMessage(STOP);
                }
            }.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public RelativeLayout getView()
    {
        return rlBackground;
    }

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (msg.what == STATE)
            {

            }
            else if (msg.what == RECONENT)
            {
                player = new PlayerCore(context);
                player.SetPPtMode(false);
                player.SetOpenLog(false);
                if (IsAudio)
                {
                    player.OpenAudio();
                }
                else
                {
                    player.CloseAudio();
                }
                player.SetAlbumPath(imageDir);
                player.SetVideoPath(vdoDir);
                player.InitParam(DevId,-1,imgVideo);
                Play();
            }
            return false;
        }
    });

    /**
     * 设置位置
     *
     * @param left
     * @param top
     */
    public void setPosition(int left,int top)
    {
//        LayoutParams p = (LayoutParams) rlBackground.getLayoutParams();
//        if (null == p)
//        {
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        }
        L.d("RelativeLayout------");

        p.leftMargin = left;
        p.topMargin = top;
        rlBackground.setLayoutParams(p);
    }

    public boolean isVisible = true;

    public int getLeft()
    {
//        return (int) rlBackground.getX();
        return rlBackground.getLeft();
    }

    public int getTop()
    {
        return rlBackground.getTop();
    }

    public int getRight()
    {
        return rlBackground.getRight();
    }

    public int getBottom()
    {
        return rlBackground.getBottom();
    }

    public int getWidth()
    {
        LayoutParams p = (LayoutParams)
                rlBackground.getLayoutParams();
        return p.width;
    }

    public int getHeight()
    {
        LayoutParams p = (LayoutParams)
                rlBackground.getLayoutParams();
        return p.height;
    }

    /**
     * 重新设置尺寸
     *
     * @param w
     * @param h
     */
    public void setSize(int w,int h)
    {
        deal = Imagedeal.getdeal(imgVideo);
        LayoutParams p = (LayoutParams) rlBackground.getLayoutParams();
        p.width = w;
        p.height = h;
        if (w == 0 || h == 0)
        {
            isVisible = false;
        }
        else
        {
            isVisible = true;
        }
        rlBackground.setLayoutParams(p);
    }

    /**
     * 设置可视性
     *
     * @param visible
     */
    public void setVisibility(int visible)
    {
        rlBackground.setVisibility(visible);
        imgVideo.setVisibility(visible);
    }

    /**
     * 设置高亮
     *
     * @param isHighLight
     */
    public void setHighLight(boolean isHighLight)
    {
        if (isHighLight)
        {
            //            rlBackground.setBackgroundResource(R.drawable.bg_highlight_h);
            rlBackground.setBackgroundResource(R.color.blue);
        }
        else
        {
            //            rlBackground.setBackgroundResource(R.drawable.bg_highlight_n);
            rlBackground.setBackgroundResource(R.color.transparent);
        }
    }

    /**
     * 设置Canvas背景
     *
     * @param resId
     */
    public void setBackground(int resId)
    {
        // imgVideo.setScaleType(ScaleType.FIT_CENTER);
        imgVideo.setBackgroundResource(resId);
    }

    public void setImgBtnVisible(int visible)
    {
        for (int i = 0; i < 8; i++)
        {
            imgBtn[i].setVisibility(visible);
        }
    }

    /**
     * 查看视频是否可用（是否被隐藏)
     *
     * @return 可用返回True，否则为false
     */
    public boolean isEnable()
    {
        return rlBackground.getVisibility() == View.VISIBLE;
    }

    public void setFullScreen(boolean isFullScreen)
    {
        this.isFullScreen = isFullScreen;
        if (isFullScreen)
        {
            rlBackground.setPadding(0,0,0,0);
        }
        else
        {
            rlBackground.setPadding(1,1,1,1);
        }
    }

    /**
     * 返回是否全屏
     *
     * @return
     */
    public boolean IsFullScreen()
    {
        return isFullScreen;
    }

    /**
     * 设置是否自动播放
     *
     * @param auto
     */
    public void setAutoPlay(boolean auto)
    {
        isAutoPlay = auto;
    }

    /**
     * 是否自动播放
     *
     * @return
     */
    public boolean IsAutoPlay()
    {
        return isAutoPlay;
    }

    public void setRecordingState(boolean isShow)
    {
        if (isShow)
        {
            tvRec.setVisibility(View.VISIBLE);
        }
        else
        {
            tvRec.setVisibility(View.GONE);
        }
    }

    /**
     * 流量数据统计
     *
     * @return
     */
    public long getDatacount()
    {
        if (datacount == 0)
        {
            return player.getDataCount();
        }
        return datacount;
    }

    public void setDatacount(long datacount)
    {
        this.datacount = datacount;
    }

    /**
     * 全部停止
     *
     */

    /**
     * 截图
     */
    public void setSnap()
    {
        if (!Utility.isSDCardAvaible())
        {
            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!LocalFile.CreateDirectory(imageDir))
        {
            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
            return;
        }
        player.SetAlbumPath(imageDir + player.DeviceNo + "/");
        player.SetSnapPicture(true,player.DeviceNo + System.currentTimeMillis() + ".jpg");
        Toast.makeText(context,imageDir + player.DeviceNo + "/",Toast.LENGTH_SHORT).show();
    }

    public void snap()
    {
        Utils.pathCompletion(Path.SNAPSHOT);
        player.SetAlbumPath(Path.SNAPSHOT);
        player.SetSnapPicture(true,System.currentTimeMillis() + ".jpg");
        T.showS("截图成功");
    }

    public void setThumbnail(String picPath,String picname)
    {
        if (!Utility.isSDCardAvaible())
        {
            return;
        }
        if (!LocalFile.CreateDirectory(picPath + "/"))
        {
            return;
        }
        player.SetAlbumPath(picPath,picname);
        player.SetSnapPicture(true);
    }

    public void setSnap(String deviceName)
    {
        if (!Utility.isSDCardAvaible())
        {
            return;
        }
        if (!LocalFile.CreateDirectory(imageDir + deviceName + "/"))
        {
            return;
        }
        Date d = new Date(System.currentTimeMillis());
        String picName = deviceName + d.toLocaleString();
        player.SetAlbumPath(imageDir + "/",picName + ".jpeg");
        player.SetSnapPicture(true);
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri uri = Uri.fromFile(new File(imageDir + deviceName + "/",picName + ".jpeg"));
        intent.setData(uri);
    }

    /**
     * 删除已经存在的缩略图
     *
     * @param path
     * @param deviceId
     */
    void deleteSameThumb(String path,String deviceId)
    {
        File dir = new File(path);
        File[] listFile = dir.listFiles();
        for (int i = 0; i < listFile.length; i++)
        {
            File file = listFile[i];
            Log.w("listFile","listFile:" + file.getName());
            if (file.getName().contains(SEPERATER))
            {
                if (deviceId.equals(file.getName().split(SEPERATER)[0]))
                {
                    file.delete();
                    return;
                }
            }
        }
    }

    /**
     * 录像
     */
    public boolean setVideo()
    {

//        if (!Utility.isSDCardAvaible())
//        {
//            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!LocalFile.CreateDirectory(vdoDir))
//        {
//            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
//            return false;
//        }


        Utils.pathCompletion(Path.VIDEORECORD);
        player.SetVideoPath(Path.VIDEORECORD);
        if (player.GetIsSnapVideo())
        {
            Log.e("","停止录像");
            player.SetSnapVideo(false);
            setRecordingState(false);
            Toast.makeText(context,"录像完成",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,"开始录像",Toast.LENGTH_SHORT).show();
            Log.e("","开始录像");
            player.SetSnapVideo(true);
            setRecordingState(true);
        }
        return true;
    }

    public void playByTime(TDateTime starttime,TDateTime endtime,int streamtype)
    {
        player.PlayTimeFile(starttime,endtime,0);
        monitor = true;
        // new stateThread().start();
    }

    public void goFoward(int seekTime)
    {
        player.SeekFilePos(seekTime,1);
    }

    public void goBackward(int seekTime)
    {
        player.SeekFilePos(seekTime,0);
    }

    public PlayStateChangeListener getStateChangeListener()
    {
        return stateChangeListener;
    }

    /**
     * 设置播放状态改变监听 TODO
     */
    public void setStateChangeListener(PlayStateChangeListener stateChangeListener)
    {
        this.stateChangeListener = stateChangeListener;
    }

    private View.OnClickListener listener;

    /**
     * 获取字符串 TODO
     *
     * @param id
     *
     * @return
     */
    String getString(int id)
    {
        return context.getString(id);
    }

    /**
     * 设置左上角字符 TODO
     */
    void SetDisplayText(String sChannel)
    {
        tvFps.setText(sChannel);
    }

    /**
     * 获取当前播放时间
     */
    public int getCurrentPlayTime()
    {
        return player.GetCurrentPlayTime_Int();
    }


    public TDateTime startTime;
    public TDateTime endTime;
    private boolean isPlayBack;
    public VideoListResult vediodData;

    public void initePlayBackTime(TDateTime startDateTime,
                                  TDateTime endTDateTime)
    {
        this.isPlayBack = true;
        this.startTime = startDateTime;
        this.endTime = endTDateTime;
    }

    public synchronized void PlayBack()
    {
        if (isPrepared())
        {
//            if (isStoping)
//            {
//                return;
//            }
//            setProgressVisible(true);
            btnChoose.setVisibility(View.GONE);
            player.PlayTimeFile(startTime,endTime,0);
        }
    }

    public synchronized void Resume()
    {
        if (isPrepared() && getState() == PlayBackLayout.Statue_Pause)
        {
            player.Resume();
        }
    }

    public synchronized void Pause()
    {
        if (isPrepared() && isPlayed())
        {
            player.Pause();
        }
    }

    public boolean contains(MotionEvent ev)
    {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (x>getLeft()&&x<getRight()&&y>getTop()&&y<getBottom())
            return true;
//        Rect rec = new Rect();
//        rlBackground.getGlobalVisibleRect(rec);
//        return rec.contains(x,y);
        return false;
    }
}

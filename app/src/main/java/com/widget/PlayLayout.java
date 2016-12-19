

package com.widget;


import java.util.List;

import com.Player.Source.TDateTime;
import com.gdmss.entity.PlayNode;
import com.utils.L;
import com.utils.ScreenUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;


public class PlayLayout extends RelativeLayout
{
    public static final int ROW_TOTAL_1 = 1;

    public static final int ROW_TOTAL_4 = 4;

    public static final int ROW_TOTAL_8 = 8;

    public static final int ROW_TOTAL_9 = 9;

    public static final int ROW_TOTAL_12 = 12;

    public static final int ROW_TOTAL_16 = 16;

    public static final int ROW_TOTAL_25 = 25;

    public static final int STATE = 0;

    public static final byte READY = 0; // ׼������

    public static final byte CONNECTTING = 1;// ������

    public static final byte PLAYING = 2;// ������

    public static final byte STOP = 4;// ֹͣ

    public static final byte CONNECTTING_FAIL = 3;// ����ʧ��

    private static final int NPC_D_MPI_MON_ERROR_USERID_ERROR = -101; // �û�ID���û�������

    private static final int NPC_D_MPI_MON_ERROR_USERPWD_ERROR = -102; // �û��������

    private static final int NPC_D_MPI_MON_ERROR_REJECT_ACCESS = -111; // Ȩ�޲���

    public int PlayNumber = 16;

    public VideoCanvas canvas[];

    private CanvasInfo[] ci;

    public boolean isRun = false;

    public int index = 0;

    private int count = 0;

    // private long fTime = 0;
    float cx = 0;

    float cy = 0;

    boolean isMoved = false;

    public boolean isLand = true;

    int playState;

    boolean isMoveStop = false;

    boolean isShowPtz = false;

    public Handler stopHandler = new Handler();

    StopRunable stopRunble = new StopRunable();

    public Handler clickHandler = new Handler();

    int playWidth = 0, playHeight = 0;

    int row = 0;

    public int total = 0;

    public int audioIndex = -1;

    // public int viewsNum;

    ShowStopLayoutListener showListener;

    OnTouchListener listener;

    StateChangeListener stateChange;

    changeChannelListener changeChannelListener;

    public boolean fragmentIsStop = false;

    long reconnectTime;

    int[] iPlayFlowState = new int[PlayNumber];

    public ViewGroup controlbtn;

    public boolean[] isAudio = new boolean[16];

    public int[] recount = new int[16];

    float x1 = 0;

    float y1 = 0;

    float x2 = 0;

    float y2 = 0;

    long timeTemp = 0;// ������¼ʱ��,���ж�ʱ����

    public CheckBox cb_sound;

    private int LastState;

    private static final int SHOWPROGRESS = 10;

    public boolean getThumb = true;

    private VelocityTracker mTracker;

    public int iGroup = 0;

    public boolean isPlayBack = false;

    public boolean isPlayBack()
    {
        return isPlayBack;
    }

    public void setPlayBack(boolean isPlayBack)
    {
        this.isPlayBack = isPlayBack;
    }

    public ViewGroup getControlbtn()
    {
        return controlbtn;
    }

    public void setControlbtn(ViewGroup controlbtn)
    {
        this.controlbtn = controlbtn;
    }

    Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message arg0)
        {
            switch (arg0.what)
            {
                case STATE:
                    if (arg0.arg2 == CONNECTTING)
                    {
                        canvas[arg0.arg1].progress.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        canvas[arg0.arg1].progress.setVisibility(View.GONE);
                    }
                    if (arg0.arg1 == index)
                    {
                        sendSateMessage(arg0);
                    }
                    break;

                case SHOWPROGRESS:

                    break;
            }
            return false;
        }
    });

    public SeekTimeBar seekTimeBar;

    public void sendSateMessage(Message msg)
    {
        if (seekTimeBar != null)
        {
            long ti = canvas[index].player.GetCurrentTime_Int();
            seekTimeBar.setTime(ti);
        }
    }

    public void sendSateMessage(int msg)
    {

    }

    public PlayLayout(Context context)
    {
        super(context);
    }

    public PlayLayout(Context context,AttributeSet attrs,int defStyle)
    {
        super(context,attrs,defStyle);
    }

    public PlayLayout(Context context,AttributeSet attrs)
    {
        super(context,attrs);
    }

    /**
     * @param con
     * @param isScreenScale �Ƿ�����Ӧ��Ļ�ߴ�
     */
    public void initeView(Context con,boolean isScreenScale)
    {
        this.canvas = new VideoCanvas[PlayNumber];
        this.ci = new CanvasInfo[PlayNumber];
        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i] = new VideoCanvas(con);
            ci[i] = new CanvasInfo();// ��¼������Ļ��λ�ã�����
            addView(canvas[i].rlBackground);
        }
    }

    public void initeData(PlayNode node)
    {
        if (canvas[index] != null)
        {
            if (canvas[index].isPrepared() && iPlayFlowState[index] == PLAYING)
            {
                try
                {
                    canvas[index].stop();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (node.node.dwNodeId == 0)
            {
                canvas[index].monitor = false;
            }
            canvas[index].node = node;
            canvas[index].Prepare(node.getName(),node.getDeviceId(),true,node.getName());
        }
        if (isRun)
        {
            return;
        }
        else
        {
            isRun = true;
        }

        /**
         * ���ƽ�ͼ���߳� TODO
         */
        Thread thumbThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (getThumb)
                {
                    if (canvas[0] != null)
                    {
                        int state = canvas[0].getState();
                        if (state != LastState && state == PLAYING)// ����״̬תΪ������
                        {
                            if (stateChange != null)
                            {
                                stateChange.ToPlaying();
                                getThumb = false;
                            }
                        }
                        else if (state != LastState && state == STOP)
                        {
                            if (stateChange != null)
                            {
                                stateChange.ToStop();
                            }
                        }
                        LastState = state;
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
            }
        });
        // thumbThread.start();
    }

    public void initDevice(PlayNode parentNodeID)
    {
        if (canvas[index] != null)
        {
            if (canvas[index].isPrepared() && iPlayFlowState[index] == PLAYING)
            {
                try
                {
                    canvas[index].stop();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (parentNodeID.node.dwNodeId == 0)
            {
                canvas[index].monitor = false;
            }
            canvas[index].node = parentNodeID;
            canvas[index].Prepare(parentNodeID.getName(),parentNodeID.getDeviceId(),true,parentNodeID.getName());
        }
        if (isRun)
        {
            return;
        }
        else
        {
            isRun = true;
        }

        /**
         * ���ƽ�ͼ���߳� TODO
         */
        Thread thumbThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (getThumb)
                {
                    if (canvas[0] != null)
                    {
                        int state = canvas[0].getState();
                        if (state != LastState && state == PLAYING)// ����״̬תΪ������
                        {
                            if (stateChange != null)
                            {
                                stateChange.ToPlaying();
                                getThumb = false;
                            }
                        }
                        else if (state != LastState && state == STOP)
                        {
                            if (stateChange != null)
                            {
                                stateChange.ToStop();
                            }
                        }
                        LastState = state;
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
            }
        });
        // thumbThread.start();
    }

    public void initDevice(List<PlayNode> channels)
    {
        int channel = channels.size();
        for (int i = 0; i < channel; i++)
        {
            if (i < canvas.length)
            {
                PlayNode tempNode = channels.get(i);
                canvas[i].Prepare(tempNode.getName(),tempNode.getDeviceId(),true,"");
            }
        }
    }

    public void reconnet(int index,int state)
    {
        if (iPlayFlowState[index] == STOP)
        {
            return;
        }
        if (state == NPC_D_MPI_MON_ERROR_USERID_ERROR || state == NPC_D_MPI_MON_ERROR_REJECT_ACCESS || state == NPC_D_MPI_MON_ERROR_USERPWD_ERROR)
        { // ����û������������Ȩ�ޣ�����Ҫ����
            iPlayFlowState[index] = state;
            return;
        }
        switch (iPlayFlowState[index])
        {
            case READY:

                break;
            case CONNECTTING:// �ȴ�����
            {
                if (state <= 0)
                {
                    Log.i("playState","�ȴ������У�state=" + state);
                    iPlayFlowState[index] = CONNECTTING_FAIL;
                }
                else if (state == 2)
                {
                    iPlayFlowState[index] = PLAYING;
                    reconnectTime = System.currentTimeMillis();
                }
                else if (state == 3)
                {
                    iPlayFlowState[index] = CONNECTTING_FAIL;
                    reconnectTime = System.currentTimeMillis();
                    Log.e("playState","�����豸ʧ��.");
                }
                break;
            }
            case PLAYING:// ���ڲ���
            {
                if (state < 0)
                {
                    long current = System.currentTimeMillis();
                    if (current - reconnectTime > 10000)
                    {
                        iPlayFlowState[index] = CONNECTTING_FAIL;
                        reconnectTime = current;
                    }
                }
                else if (state != 2)
                {
                    iPlayFlowState[index] = CONNECTTING_FAIL;
                    reconnectTime = System.currentTimeMillis();
                }
                break;
            }
            case CONNECTTING_FAIL:// ����ʧ��
            {
                long current = System.currentTimeMillis();
                if (current - reconnectTime > 6000)
                {
                    Log.e("playState","����ʧ�ܣ������豸.");
                    iPlayFlowState[index] = CONNECTTING;
                    handler.post(new PlayRunnble(index));
                }
                break;
            }
        }
    }

    class ReconnectRun implements Runnable
    {

        int index;

        public ReconnectRun(int index)
        {
            this.index = index;
        }

        @Override
        public void run()
        {
            reconnet(index,index);
        }

    }

    public boolean isLand()
    {
        return isLand;
    }

    public void setLand(final boolean isLand)
    {
        this.isLand = isLand;
        final ViewTreeObserver vto = getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
//        {
//            @Override
//            public boolean onPreDraw()
//            {
//                getViewTreeObserver().removeOnPreDrawListener(this);
//                int height =getMeasuredHeight();
//                int width = getMeasuredWidth();
//                if (width != playWidth && playHeight != height)
//                {
//                    playHeight = height;
//                    playWidth = width;
//                    L.e("getMeasuredWidth:" + width + " getMeasuredHeight:" + height);
//                    // selectCanvas(canvasIndex);
//                }
//                canvas();
//                return true;
//            }
//        });

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                int width = getWidth();//getMeasuredWidth();
                int height = getHeight();// getMeasuredHeight();
                if (width != playWidth && playHeight != height)
                {
                    playHeight = height;
                    playWidth = width;
                    L.e("getWidth:" + width + " getHeight:" + height);
                    // selectCanvas(canvasIndex);
                    canvas();
                }
            }
        });
    }


    public int gettotal()
    {
        return total;
    }

    public void settotal(int total)
    {
        this.total = total;
    }

    public boolean isRun()
    {
        return isRun;
    }

    public void stopRun()
    {
        this.isRun = false;
        stop();
    }

    public void release()
    {

        if (canvas[index].isPrepared())
        {
            canvas[index].release();
        }

    }

    public void setPTZ(int command,int length)
    {
        if (canvas[index].isPlayed())
        {
            canvas[index].player.SetPtz(command,length);
        }
    }

    class PlayRunnble implements Runnable
    {
        int index;

        public PlayRunnble(int index)
        {
            this.index = index;
        }

        @Override
        public void run()
        {
            if (recount[index] >= 3)
            {
                return;
            }
            else
            {
                recount[index]++;
                Reconnect();
            }
        }
    }

    public synchronized void Reconnect()
    {

        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
                canvas[index].stop();
                return;
            }
            iPlayFlowState[index] = CONNECTTING;
            reconnectTime = System.currentTimeMillis();
            canvas[index].Reconnect();
        }
    }

    public void play()
    {

        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
                canvas[index].stop();
                return;
            }
            iPlayFlowState[index] = CONNECTTING;
            reconnectTime = System.currentTimeMillis();
            canvas[index].Play();
        }
    }

    public void playAll()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i].isPrepared() && canvas[i].isShow)
            {
                L.e("play canvasIndex:" + i);
                canvas[i].Play();
                canvas[i].monitor = true;
            }
        }
    }

    public void snap(String path) throws Exception
    {
        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
                canvas[index].setSnap(path);
            }
        }
    }

    public void setThumbnail(String picPath,String picName)
    {
        canvas[index].setThumbnail(picPath,picName);
    }

    public void snap()
    {
        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
                // canvas[canvasIndex].setSnap();
                canvas[index].snap();
            }
            else
            {
                // Toast.makeText(con,R.string.only_play_snap,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean record() throws Exception
    {
        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
                canvas[index].setVideo();
                return true;
            }
            else
            {
                // Toast.makeText(con,R.string.only_play_snap,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    public void stop()
    {
        iPlayFlowState[index] = STOP;

        canvas[index].Stop();

        sendSateMessage(iPlayFlowState[index]);
    }

    public boolean stopAll()
    {
        int size = canvas.length;
        for (int i = 0; i < size; i++)
        {
            canvas[i].Stop();
            canvas[i].monitor = false;
            //            final int finalI = i;
            //            new Thread(new Runnable()
            //            {
            //                @Override
            //                public void run()
            //                {
            //                    canvas[finalI].stop();
            //                    canvas[finalI].monitor = false;
            //                }
            //            });
        }

        return true;
    }

    public void stopChannel()
    {

        if (canvas[index].isPrepared())
        {
            // canvas.Play(canvasIndex);
            try
            {
                iPlayFlowState[index] = STOP;
                canvas[index].Stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param style ROW_TOTAL_1 = 1; ROW_TOTAL_4 = 4;ROW_TOTAL_8 = 8; ROW_TOTAL_9
     *              = 9; ROW_TOTAL_12 = 12; ROW_TOTAL_16 = 16;
     */
    public void initeCanvas(int style)
    {
        displayType = style;
        switch (style)
        {
            case ROW_TOTAL_1:
                this.row = 1;
                total = 1;
                break;
            case ROW_TOTAL_4:
                this.row = 2;
                total = 4;
                break;
            case ROW_TOTAL_8:
                this.row = 2;
                total = 8;
                break;
            case ROW_TOTAL_9:
                this.row = 3;
                total = 9;
                break;
            case ROW_TOTAL_12:
                this.row = 3;
                total = 12;
                break;
            case ROW_TOTAL_16:
                this.row = 4;
                total = 16;
                break;
            case ROW_TOTAL_25:
                // this.row = 5;
                // this.total = 25;
                break;
        }
        setLand(isLand);
        lastDisplayType = style;
    }

    /**
     * ��ʱ20��ر�û����ʾ�Ļ���
     *
     * @param fragmentIsStop fragment�Ƿ�ֹͣ
     */
    public void stopDelayed(boolean fragmentIsStop)
    {
        this.fragmentIsStop = fragmentIsStop;
        stopHandler.removeCallbacks(stopRunble);
        stopHandler.postDelayed(stopRunble,20000);
    }

    public void playCanvas(int style)
    {
        initeCanvas(style);
        canvas();
    }


    public void canvas()
    {
        // this.isLand = isLand;
        if (canvas[0] == null)
        {
            // Toast.makeText(con,R.string.nodataerro,Toast.LENGTH_SHORT).show();
            return;
        }
        int width = playWidth;
        int height = playHeight;
        int w = 0;
        int h = 0;
        int row2 = 0;
        if (isLand)
        {
            h = height / row;// ��
            row2 = (total / row);// ÿ�и���
            w = width / row2;// ��
        }
        else
        {
            row2 = row;
            w = width / row2;
            h = height / (total / row2);
        }
        int j = 0;
        int k = 0;
        int m = playHeight / 2 - ((total / row2) * h) / 2;

        if (total == 1)
        {
            for (int i = 0; i < canvas.length; i++)
            {
                if (i == index)
                {
                    canvas[index].isShow = true;
                    canvas[index].setPosition(0,0);
                    canvas[index].setSize(w,h);
                    ci[i].left = j * w;
                    ci[i].right = j * w + w;
                    ci[i].top = k * h + m;
                    ci[i].bottom = k * h + h + m;
                }
                else
                {
                    canvas[i].isShow = false;
                    canvas[i].setPosition(0,0);
                    canvas[i].setSize(0,0);
                    ci[i].left = 0;
                    ci[i].right = 0;
                    ci[i].top = 0;
                    ci[i].bottom = 0;
                }
                canvas[index].imgVideo.setScaleType(ScaleType.FIT_XY);
            }
        }
        else
        {
            for (int i = 0; i < canvas.length; i++)
            {
                if (i < total)
                {
                    canvas[i].setPosition(j * w,k * h + m);
                    canvas[i].setSize(w,h);

                    ci[i].left = j * w;
                    ci[i].right = j * w + w;
                    ci[i].top = k * h + m;
                    ci[i].bottom = k * h + h + m;
                    j++;
                    if ((i + 1) % row2 == 0)
                    {
                        k++;
                        j = 0;
                    }
                    canvas[i].isShow = true;
                }
                else
                {
                    canvas[i].isShow = false;
                    canvas[i].setPosition(0,0);
                    canvas[i].setSize(0,0);
                    ci[i].left = 0;
                    ci[i].right = 0;
                    ci[i].top = 0;
                    ci[i].bottom = 0;
                }
                LayoutParams params = new LayoutParams((int) (w / 4),(int) (h / 4));//
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                canvas[i].progress.setLayoutParams(params);
            }
        }
        initPageInfo();
    }

    /**
     * �������л�
     */
    public void canvasToOne(int index)
    {
        // windowstate = 1;
        int w = playWidth;

        int h;
        if (this.isLand)
        {
            h = getHeight();
        }
        else
        {
            h = getHeight();
        }
        int m = playHeight / 2 - h / 2;
        for (int i = 0; i < canvas.length; i++)
        {
            if (i == index)
            {
                canvas[i].setPosition(0,m);
                canvas[i].setSize(w,h);
                canvas[i].isShow = true;
                // Log.i("width--&--height", "width" + w + " height" + h);
            }
            else
            {
                canvas[i].isShow = false;
                canvas[i].setPosition(0,0);
                canvas[i].setSize(0,0);
            }
        }
    }

    // 获取当前第一个画面的通道号

    int getFirstDisplayIndex()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i].getWidth() > 0)
            {
                return i;
            }
        }
        return -1;
    }

    // 获取当前显示画面数
    public int getDisplayNumber()
    {
        int count = 0;
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i].rlBackground.getWidth() > 0)
            {
                count++;
            }
        }
        return count;
    }

    public void canvasToFour()
    {
        // this.isLand = isLand;
        // windowstate = 4;
        int firstindex = getFirstDisplayIndex();
        if (canvas[0] == null)
        {
            // Toast.makeText(con,R.string.nodataerro,Toast.LENGTH_SHORT).show();
            return;
        }
        int width = playWidth;
        int height = playHeight;
        int w = 0;
        int h = 0;
        int row2 = 0;
        if (isLand)
        {
            h = height / 2;// ��
            row2 = (4 / 2);// ÿ�и���
            w = width / row2;// ��
            Log.d("hmaaaa","land , w:" + width + ",h" + height + ",rows:" + row2);
        }
        else
        {
            row2 = 2;
            w = width / row2;
            h = height / (4 / row2);
            Log.d("hmaaaa","potrat , w:" + width + ",h" + height + ",rows:" + row2);
        }
        int j = 0;
        int k = 0;
        int m = playHeight / 2 - ((4 / row2) * h) / 2;

        for (int i = 0; i < canvas.length; i++)
        {
            if (i >= firstindex && i < firstindex + 4)
            {
                canvas[i].setPosition(j * w,k * h + m);
                canvas[i].setSize(w,h);

                ci[i].left = j * w;
                ci[i].right = j * w + w;
                ci[i].top = k * h + m;
                ci[i].bottom = k * h + h + m;
                j++;
                if (i == firstindex + 1)
                {
                    k++;
                    j = 0;
                }
                canvas[i].isShow = true;
            }
            else
            {
                canvas[i].isShow = false;
                canvas[i].setPosition(0,0);
                canvas[i].setSize(0,0);

                ci[i].left = 0;
                ci[i].right = 0;
                ci[i].top = 0;
                ci[i].bottom = 0;
            }
        }
    }

    public int pageNum = 1;

    public int currentPage = 1;

    public int displayType = 4;

    public void initPageInfo()
    {
//        displayType = getDisplayNumber();
        if (displayType != 0)
        {
            pageNum = canvas.length / displayType;
        }
    }


    /**
     * �Ż����л� TODO
     */
    public void canvasToNine(int index)
    {
        // windowstate = 9;
        if (canvas[0] == null)
        {
            // Toast.makeText(con,R.string.nodataerro,Toast.LENGTH_SHORT).show();
            return;
        }
        int width = playWidth;
        int height = playHeight;
        int w = 0;
        int h = 0;
        int row2 = 0;
        if (isLand)
        {
            h = height / row;// ��
            row2 = (total / row);// ÿ�и���
            w = width / row2;// ��
        }
        else
        {
            row2 = row;
            w = width / row2;
            h = height / (total / row2);
        }
        int j = 0;
        int k = 0;
        // int m = playHeight / 2 - ((total / row2) * h) / 2;

        for (int i = 0; i < canvas.length; i++)
        {
            if (index == 0)// ��һҳ
            {
                if (i <= 8)
                {
                    canvas[i].setPosition(j * w,k * h);
                    canvas[i].setSize(w,h);

                    ci[i].left = j * w;
                    ci[i].right = (j + 1) * w;
                    ci[i].top = k * h;
                    ci[i].bottom = (k + 1) * h;
                    j++;
                    if (j > 2)
                    {
                        k++;
                        j = 0;
                    }
                    canvas[i].isShow = true;
                }
                else
                {
                    canvas[i].isShow = false;
                    canvas[i].setPosition(0,0);
                    canvas[i].setSize(0,0);

                    ci[i].left = 0;
                    ci[i].right = 0;
                    ci[i].top = 0;
                    ci[i].bottom = 0;
                }
            }
            else if (index == 8)// �ڶ�ҳ
            {
                if (i >= 7)
                {
                    canvas[i].setPosition(j * w,k * h);
                    canvas[i].setSize(w,h);

                    ci[i].left = j * w;
                    ci[i].right = (j + 1) * w;
                    ci[i].top = k * h;
                    ci[i].bottom = (k + 1) * h;
                    j++;
                    if (j > 2)
                    {
                        k++;
                        j = 0;
                    }
                    canvas[i].isShow = true;
                }
                else
                {
                    canvas[i].isShow = false;
                    canvas[i].setPosition(0,0);
                    canvas[i].setSize(0,0);

                    ci[i].left = 0;
                    ci[i].right = 0;
                    ci[i].top = 0;
                    ci[i].bottom = 0;
                }
            }
            LayoutParams params = new LayoutParams((int) (w / 4),(int) (h / 4));//
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            canvas[i].progress.setLayoutParams(params);
        }
    }

    class StopRunable implements Runnable
    {
        @Override
        public void run()
        {
            if (canvas[index].isPrepared())
            {
                if (fragmentIsStop)
                {
                    Log.e("isShow","��" + 0 + "����ֹͣ");
                    try
                    {
                        iPlayFlowState[index] = STOP;
                        canvas[index].Stop();
                        canvas[index].release();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    if (!canvas[index].isShow)
                    {
                        Log.e("isShow","��" + 0 + "����ֹͣ");
                        try
                        {
                            iPlayFlowState[index] = STOP;
                            canvas[index].Stop();
                            canvas[index].release();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

    }

    public int getCanvasIndex(float x,float y)
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (x >= ci[i].left && x <= ci[i].right && y >= ci[i].top && y <= ci[i].bottom)
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        // ����ѡ�еĻ���
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            int i = getCanvasIndex(ev.getX(),ev.getY());
            if (i == -1)
            {
                return true;
            }
            else
            {
                index = i;
            }
            selectCanvas(index);
        }
//        onTouchEvent(ev);
//        return true;

        return super.dispatchTouchEvent(ev);
    }

    private int currentSpeed = 0;

    private int pointerId = 0;

    private int startx, endx;

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev)
//    {
//        int number = getDisplayNumber();
//        L.e("onInterceptTouchEvent displayNumber:" + number);
//        if (number == 1)
//        {
//            return false;
//        }
//        return true;
//    }

    boolean finished = false;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (listener != null)
        {
            listener.OnTouch(this,event);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                performClick = true;
                x1 = event.getRawX();
                y1 = event.getRawY();
                DoubleClick(index);
                startx = (int) event.getRawX();
                pointerId = event.getPointerId(0);
                if (mTracker == null)
                {
                    mTracker = VelocityTracker.obtain();
                }
                mTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_UP:
                endx = (int) event.getRawX();
                if (!isPlayBack && Math.abs(startx - endx) > 100)
                {
                    if (Math.abs(currentSpeed) > 100)
                    {
                        if (currentSpeed > 0)// 切换上一个
                        {
                            if (displayType == 1)
                            {
                                if (index > 0)
                                {
                                    index--;
                                    canvas();
                                }
                            }
                            else if (displayType == 4)
                            {
                                if (currentPage > 0)
                                {
                                    currentPage--;
                                    canvasToFour();
                                }
                            }
                        }
                        else
                        {
                            if (displayType == 1)
                            {
                                if (index < canvas.length - 1)
                                {
                                    index++;
                                    canvas();
                                }
                            }
                            else if (displayType == 4)
                            {
                                if (currentPage < pageNum - 1)
                                {
                                    currentPage++;
                                    canvasToFour();
                                }
                            }
                        }
                        canvas[index].setHighLight(true);
                    }
                    performClick = false;
                }
                if (mTracker != null)
                {
                    mTracker.recycle();
                }
                mTracker = null;
                if (performClick)
                {
                    performClick();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mTracker.addMovement(event);
                mTracker.computeCurrentVelocity(100);
                currentSpeed = (int) mTracker.getXVelocity(pointerId);
                performClick = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                performClick = false;
                if (getDisplayNumber() == 1)
                {
                    canvas[index].imgVideo.setScaleType(ScaleType.MATRIX);
                }
                break;
        }
        canvas[index].deal.set(canvas[index].imgVideo,event);
        return true;
    }

    boolean performClick = true;

    public interface changeChannelListener
    {
        public void playNext();

        public void PlayLast();
    }

    public interface OnTouchListener
    {
        abstract void OnTouch(View v,MotionEvent event);
    }

    public void setOnTouchListener(OnTouchListener l)
    {
        this.listener = l;
    }

    public changeChannelListener getChangeChannelListener()
    {
        return changeChannelListener;
    }

    /**
     * ͨ���л����� TODO
     *
     * @return
     */
    public void setChangeChannelListener(changeChannelListener changeChannelListener)
    {
        this.changeChannelListener = changeChannelListener;
    }

    /**
     * �϶�״̬��ʾ��ֹͣ ����
     *
     * @author Administrator
     */
    public interface ShowStopLayoutListener
    {
        abstract public void showStateListener(boolean state);

        abstract public void isMoveVisibleListener(boolean isVisible);

        abstract public void isStop(boolean isStop);
    }

    public interface StateChangeListener
    {
        abstract public void ToPlaying();

        abstract public void ToStop();
    }

    public void SetStateChangeListener(StateChangeListener listener)
    {
        this.stateChange = listener;
    }

    public ShowStopLayoutListener getShowListener()
    {
        return showListener;
    }

    public void setShowListener(ShowStopLayoutListener showListener)
    {
        this.showListener = showListener;
    }

    public void setOnAddToPlayListener(OnClickListener listener)
    {
        if (null == listener)
        {
            return;
        }
        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i].btnChoose.setOnClickListener(listener);
            L.e("画面监听:" + i);
        }
    }

    /**
     * @param index
     */
    public void selectCanvas(int index)
    {
        this.index = index;
        ResetCanvasBackground();
        canvas[index].setHighLight(true);
        canvas[index].getView().bringToFront();
        if (null != cb_sound)
        {
            cb_sound.setChecked(index == audioIndex);
//            cb_sound.setSelected(canvasIndex == audioIndex);
        }
    }

    public void ResetCanvasBackground()
    {

        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i].setHighLight(false);
        }
    }

    int lastwindowstate = 16;

    /**
     * ˫��ȫ��
     */
    private int lastindex = 0;

    private long time1, time2;

    // private boolean hasDoubleClick;
    public SlidingMenu menu;

    private void DoubleClick(int i)
    {
        switch (count)
        {
            case 0:
                time1 = System.currentTimeMillis();
                count++;
                lastindex = index;
                break;
            case 1:
                time2 = System.currentTimeMillis();
                if (i == lastindex)
                {
                    if (time2 - time1 < 300)
                    {
                        if (getDisplayNumber() == 1)
                        {
                            displayType = lastDisplayType;
                            if (null != controlbtn)
                            {
                                if (controlbtn.getVisibility() == VISIBLE)
                                {
                                    controlbtn.setVisibility(GONE);
                                }
                            }
                            if (null != menu)
                            {
                                menu.setEnabled(true);
                            }
                            initeCanvas(lastDisplayType);
                        }
                        else
                        {
                            displayType = 1;
                            initeCanvas(1);
                            if (null != menu)
                            {
                                menu.setEnabled(false);
                            }
                            lastDisplayType = getDisplayNumber();
                        }
                        canvas();
                    }
                    else
                    {
                        count = 1;
                        time1 = time2;
                    }
                }
                else
                {
                    count = 1;
                    time1 = time2;
                    lastindex = i;
                    //                    canvas[lastindex].closeAudio();
                    //                    canvas[canvasIndex].openAudio();
                }
                break;
        }
    }

    int lastDisplayType = 4;

    public int getLastIndex()
    {
        return lastindex;
    }

    public void setScreenScale(boolean isScreenScale)
    {
        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i].setScreenScale(isScreenScale);
        }
    }

    public void setIsAudio(boolean open)
    {
        if (index == audioIndex)
        {
            canvas[index].setIsAudio(open);
            return;
        }
        canvas[audioIndex].setIsAudio(false);
        canvas[index].setIsAudio(open);
    }

    public void setAudio()
    {
        if (audioIndex == -1)
        {
            canvas[index].setIsAudio(true);
            audioIndex = index;
            return;
        }
        if (audioIndex == index)
        {
            canvas[index].setIsAudio(false);
            audioIndex = -1;
        }
        else if (audioIndex != index)
        {
            canvas[audioIndex].setIsAudio(false);
            canvas[index].setIsAudio(true);
            audioIndex = index;
        }
    }

    public boolean IsAudio()
    {
        return canvas[index].IsAudio();
    }

    public boolean getAudio()
    {
        if (canvas[index].isPrepared())
        {
            return !canvas[index].player.GetIsVoicePause();
        }
        else
        {
            return false;
        }
    }

    public void setPPT(boolean isOpen)
    {
        canvas[index].setPPT(isOpen);
    }

    public boolean isPPT()
    {

        return canvas[index].IsPPt();
    }

    public boolean isPrepared()
    {
        return canvas[index].isPrepared();
    }

    public int[] getPlayRate()
    {
        if (canvas[index].isPrepared() && canvas[index].isPlayed())
        {
            return canvas[index].getPlayRate();
        }
        else
        {
            return null;
        }
    }

    public String getPlayFrameRate()
    {
        return canvas[index].getPlayFrameRate();
    }

    public int getPlayState()
    {
        return canvas[index].getState();
    }

    public boolean getRecordVideoState()
    {
        return canvas[index].getVideoRecordState();
    }

    // ���ò���ģʽ(0ʵʱ 1���� 2 I֡)
    public void setPlayMode(int mode)
    {
        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i].player.SetPlayModel(mode);
        }
    }

    public void setMediaPlayType(int type)
    {
        getCanvas().setMeadiaPlayType(type);
    }

    /**
     * ��������ͳ��
     *
     * @return
     */
    public long getDataCount()
    {
        return canvas[index].getDatacount();
    }

    /**
     * �л�ҳ��
     */
    class changeData implements Runnable
    {
        @Override
        public void run()
        {

        }
    }

    /**
     * ��ͣ
     */

    public void pause()
    {
        canvas[index].player.Pause();
        canvas[index].setIsPlayback(false);
    }

    /**
     * ��������
     */
    public void resume()
    {
        canvas[index].player.Resume();
    }

    /**
     * ���ñ���
     *
     * @param resId
     */
    public void setBackground(int resId)
    {
        for (int i = 0; i < canvas.length; i++)
        {
            canvas[i].setBackground(resId);
        }
    }

    /**
     * ���ñ���
     *
     * @param resId
     */
    public void setSingleBackground(int resId)
    {
        canvas[index].imgVideo.setScaleType(ScaleType.FIT_XY);
        canvas[index].setBackground(resId);
    }

    public void playByTime(TDateTime starttime,TDateTime endtime)
    {
        canvas[index].playByTime(starttime,endtime,0);
    }

    public int getCurrentPlayTime()
    {
        return canvas[index].getCurrentPlayTime();
    }

    public void goFoward(int seekTime)
    {
        canvas[index].goFoward(seekTime);
    }

    public void goBackward(int seekTime)
    {
        canvas[index].goBackward(seekTime);
    }

    @Override
    public boolean performClick()
    {
        if (canvas[index].btnChoose.getVisibility() == VISIBLE)
        {
//            canvas[canvasIndex].btnChoose.performClick();
        }
        return super.performClick();
    }

    public class CanvasInfo
    {
        public int left;

        public int right;

        public int top;

        public int bottom;
    }

    public PlayNode getCurrentNode()
    {
        return canvas[index].node;
    }

    public void ChangeViewMode()
    {
        row++;
        if (row > 4)
        {
            row = 1;
        }
        initeCanvas(row * row);
        canvas();
    }

    public void replay()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < canvas.length; i++)
                {
                    canvas[i].stop();
                    rePlayHan.sendEmptyMessage(i);
                }
            }
        }).start();
    }

    private Handler rePlayHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (isPlayBack)
            {
                canvas[msg.what].PlayBack();
            }
            else
            {
                canvas[msg.what].Play();
            }
            canvas[msg.what].tvFps.setText("0fps | 0kbps");
            return false;
        }
    });

    public boolean isAllClosed()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i].getState() == PLAYING)
            {
                return false;
            }
        }
        return true;
    }

    public VideoCanvas getCanvas()
    {
        return canvas[index];
    }

    public void playback(int index)
    {
        canvas[index].PlayBack();
        startGetState();
    }

    public void startGetState()
    {
        if (isRun)
        {
            return;
        }
        else
        {
            isRun = true;
        }
        new Thread()
        {

            @Override
            public void run()
            {
                try
                {
                    synchronized (canvas)
                    {
                        while (isRun)
                        {
                            if (canvas == null)
                            {
                                continue;
                            }
                            for (int i = 0; i < 4; i++)
                            {
                                Thread.sleep(100);
                                if (canvas[i] == null || !canvas[i].isPrepared())
                                {
                                    break;
                                }
                                int state = canvas[i].getState();
                                reconnet(i,state);
                                Message message = new Message();
                                message.what = STATE;
                                message.arg1 = i;
                                message.arg2 = state;
                                handler.sendMessage(message);
                            }
                        }
                    }
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void hideFps()
    {
        for (VideoCanvas can : canvas)
        {
            if (null != can)
            {
                if (null != can.tvFps)
                {
                    can.tvFps.setVisibility(View.GONE);
                }
            }

        }
    }
}
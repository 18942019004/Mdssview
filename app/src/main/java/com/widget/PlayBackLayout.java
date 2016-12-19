package com.widget;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Player.Source.SDKError;
import com.Player.Source.TDateTime;
import com.gdmss.activities.AcChooseDevice;
import com.gdmss.adapter.PlayViewPagerAdapter;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.VideoListResult;
import com.utils.L;

public class PlayBackLayout extends ViewPager implements Runnable
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
    public static final byte Statue_Pause = 6;// ��ͣ
    public static final byte CONNECTTING_FAIL = 3;// ����ʧ��
    public static final byte RECONNECT = 12;// ��������
    private static final int NPC_D_MPI_MON_ERROR_USERID_ERROR = -101; // �û�ID���û�������
    private static final int NPC_D_MPI_MON_ERROR_USERPWD_ERROR = -102; // �û��������
    private static final int NPC_D_MPI_MON_ERROR_REJECT_ACCESS = -111; // Ȩ�޲���
    public static final int ADD_TO_PLAYBACK_MULTI = 3;
    public static final int ADD_TO_PLAYBACK_SINGLE = 5;
    TextView tvTitle;
    int pageNums = 0;// ����ҳ
    int onePageNums = 4;// ÿһҳ���ٸ�����
    int currentPage = 0;// ��ǰ����ҳ
    public VideoCanvas[] canvas = new VideoCanvas[pageNums];
    public RelativeLayout[] contentViews;// װ�ڻ����ҳ��
    private Context con;
    List<PlayNode> nodeList;
    private boolean isRun = false;
    public int index = 0;
    private int count = 0;
    private long fTime = 0;
    float cx = 0;
    float cy = 0;
    boolean isMoved = false;
    private boolean isSinglePlayView = false;
    public boolean isLand = false;
    int playState;
    boolean isMoveStop = false;
    boolean isShowPtz = false;
    public Handler stopHandler = new Handler();
    StopRunable stopRunble = new StopRunable();
    public Handler clickHandler = new Handler();
    int playWidth = 0, playHight = 0;
    int row = 0;
    public int total = 0;
    public int viewsNum;
    public boolean fragmentIsStop = false;
    StateChangeListener stateChangeListener;
    long reconnectTime[];
    int iPlayFlowState[];
    SeekTimeBar seekTimeBar;
    PlayViewPagerAdapter playViewPagerAdapter;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            switch (msg.what)
            {

                case STATE:
                    if (msg.arg1 >= canvas.length)
                    {
                        return;
                    }
                    if (msg.arg2 != CONNECTTING)
                    {
                        canvas[msg.arg1].progress.setVisibility(View.GONE);
                        if (msg.arg2 == STOP || msg.arg2 == READY)
                        {
                            canvas[msg.arg1].btnChoose.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            canvas[msg.arg1].btnChoose.setVisibility(View.GONE);
                        }
                    }
//                    canvas[msg.arg1].setState(
//                            showState(msg.arg2) + " " + (msg.arg2 == PLAYING ? canvas[msg.arg1].getPlayFrameRate() : ""));
                    ;
                    if (index == (msg.arg1))
                    {
                        // Log.i("playCoreGetState", "playCoreGetState:" +
                        // msg.arg2);
                        sendSateMessage(msg.arg1,msg.arg2);
                    }
                    break;

                default:
                    break;
            }

        }
    };
//    String showState(int state)
//    {
//        String ret;
//        if (state == 1)
//        {// ������
//            ret = con.getString(R.string.connecting);
//        }
//        else if (state == 2)
//        {// ������
//            ret = con.getString(R.string.playing);
//        }
//        else if (state == 3)
//        {// ����ʧ��
//            ret = con.getString(R.string.connect_fail);
//        }
//        else if (state == 4)
//        {// ֹͣ��
//            ret = con.getString(R.string.stop);
//        }
//        else if (state == -102)
//        {// �������
//            ret = con.getString(R.string.passworderro);
//        }
//        else if (state == -101)
//        {// �û�ID���û�������
//            ret = con.getString(R.string.usererro);
//        }
//        else if (state == -111)
//        {//
//            ret = con.getString(R.string.NPC_D_MPI_MON_ERROR_REJECT_ACCESS);
//        }
//        else if (state == 0)
//        {// ׼������
//            ret = con.getString(R.string.ready);
//        }
//        else if (state == 10)
//        {
//            ret = con.getString(R.string.buffering);
//        }
//        else if (state == Statue_Pause)
//        {//
//            ret = con.getString(R.string.pause);
//        }
//        else if (state == 7)
//        {//
//            ret = con.getString(R.string.stop);
//        }
//        else
//        {//
//            ret = con.getString(R.string.connect_fail);
//        }
//        return ret;
//    }

    public void sendSateMessage(int index,int arg2)
    {
        Log.i("handleMessage",index + "����" + canvas[index].Name + "״̬�ǣ�"
                + arg2);
        stateChangeListener.stateChange(index,arg2,
                arg2 == PLAYING ? canvas[index].getPlayFrameRate() : "",
                canvas[index].Name,onePageNums);
        stateChangeListener.isTalk(index,canvas[index].IsPPt());
        stateChangeListener.isPlaying(index,arg2 == PLAYING ? true : false);
        stateChangeListener
                .isRecord(index,canvas[index].getVideoRecordState());
        stateChangeListener.isAudio(index,canvas[index].IsAudio());
        stateChangeListener.isMainStream(index,canvas[index].MediaStreamType);
        if (seekTimeBar != null)
        {
            long ti = canvas[index].player.GetCurrentTime_Int();
            seekTimeBar.setTime(ti);
        }

    }

    private boolean isShow;
    private boolean isScreenScale;
    private int preOnePageNums = 1;// ˫������֮ǰ����״̬

    // private CanvasInfo[] ci = new CanvasInfo[16];

    public PlayBackLayout(Context context)
    {

        super(context);
        setOnPageChangeListener(new ViewOnPageListener());
        // TODO Auto-generated constructor stub
    }

    public PlayBackLayout(Context context,AttributeSet attrs)
    {

        super(context,attrs);
        setOnPageChangeListener(new ViewOnPageListener());
        // TODO Auto-generated constructor stub

    }

    public void initeData(PlayNode node)
    {
        canvas[index].Prepare(node.getName(),node.getDeviceId(),
                true,"");
    }

    public void initeData(PlayNode node,TDateTime startDateTime,
                          TDateTime endTDateTime,SeekTimeBar seekTimeBar,
                          VideoListResult data)
    {
        canvas[index].Prepare(node.getName(),node.getDeviceId(),true,"");
        canvas[index].initePlayBackTime(startDateTime,endTDateTime);
        canvas[index].vediodData = data;
        this.seekTimeBar = seekTimeBar;
        this.seekTimeBar.setDate(TDateTimeToData(startDateTime));
        this.seekTimeBar.setPlayCoreAndParameters(canvas[index].player,
                startDateTime,endTDateTime,0);
        this.seekTimeBar.setTimeArea(data.multiData);
    }

    public void initeData(List<PlayNode> nodeList,TDateTime startDateTime,
                          TDateTime endTDateTime,SeekTimeBar seekTimeBar,
                          List<VideoListResult> videoListResults)
    {
        for (int i = 0; i < nodeList.size(); i++)
        {
            PlayNode node = nodeList.get(i);
            canvas[i].Prepare(nodeList.get(i).getName(),node.getDeviceId(),
                    true,"");
            canvas[i].initePlayBackTime(startDateTime,endTDateTime);
            canvas[i].vediodData = videoListResults.get(i);

        }
        this.seekTimeBar = seekTimeBar;
        this.seekTimeBar.setDate(TDateTimeToData(startDateTime));
        this.seekTimeBar.setPlayCoreAndParameters(canvas[index].player,
                startDateTime,endTDateTime,0);
        this.seekTimeBar.setTimeArea(canvas[index].vediodData.multiData);
    }

    public void setTimeArea(List<VideoListResult> datas)
    {

        if (datas != null)
        {
            for (int i = 0; i < datas.size(); i++)
            {
                canvas[i].vediodData = datas.get(i);
            }
        }

    }

    public String TDateTimeToData(TDateTime tstart)
    {

        return tstart.iYear + "-" + tstart.iMonth + "-" + tstart.iDay;

    }

    /**
     * ��ʼ����������
     *
     * @param con
     * @param nodeList
     * @param isScreenScale
     */
    public void initeData(Activity con,List<PlayNode> nodeList,
                          boolean isScreenScale)
    {
        this.isScreenScale = isScreenScale;
        this.con = con;
        if (nodeList == null)
        {
            Log.i("initeData","nodeList Ϊ��");
            con.finish();
        }
        this.nodeList = nodeList;
        total = nodeList.size(); // �ܻ���������Ҳ����ͨ����

        if (total == 1)
        {
            onePageNums = 1;
        }
        else
        {
            if (total == 0)
            { // ��ʼ��ʱtotalΪ0
                total = 4;
            }
            onePageNums = 4;
        }
        pageNums = (total - 1) / onePageNums + 1;// viewPAger����ҳ
        this.currentPage = 0; // ��ǰ�ڼ�ҳ
        this.index = 0;// ��ǰѡ�еڼ���
        contentViews = new RelativeLayout[pageNums];// ÿһҳװ�ص�����
        canvas = new VideoCanvas[pageNums * onePageNums];
        iPlayFlowState = new int[pageNums * onePageNums];
        reconnectTime = new long[pageNums * onePageNums];
        for (int i = 0; i < pageNums * onePageNums; i++)
        {
            canvas[i] = new VideoCanvas(con);
            canvas[i].btnChoose.setOnClickListener(new AddOnClick(i));
        }
        for (int i = 0; i < nodeList.size(); i++)
        {
            PlayNode node = nodeList.get(i);
            if (node != null)
            {
                canvas[i].Prepare(node.getName(),node.getDeviceId(),
                        true,"");

            }

        }

        initeCanvas(total);
        initeViewPagerContents();
    }

    /**
     * ��ʼ��viewPager
     */
    private void initeViewPagerContents()
    {
        /**
         * ���Ƴ�֮ǰ��View
         */
        if (contentViews != null)
        {
            for (int i = 0; i < contentViews.length; i++)
            {
                if (contentViews[i] != null)
                {
                    contentViews[i].removeAllViews();
                }
            }
        }
        VideoCanvas[] tempCanvas = new VideoCanvas[onePageNums * pageNums];
        int[] tiPlayFlowState = new int[pageNums * onePageNums];
        long[] treconnectTime = new long[pageNums * onePageNums];
        for (int i = 0; i < tempCanvas.length; i++)
        {
            if (i < canvas.length)
            {
                tempCanvas[i] = canvas[i];
                tiPlayFlowState[i] = iPlayFlowState[i];
                treconnectTime[i] = reconnectTime[i];
            }
            else
            {
                tempCanvas[i] = new VideoCanvas(con);
                tiPlayFlowState[i] = 0;
                treconnectTime[i] = System.currentTimeMillis();
                tempCanvas[i].btnChoose.setOnClickListener(new AddOnClick(i));
            }
        }
        iPlayFlowState = tiPlayFlowState;
        reconnectTime = treconnectTime;
        canvas = tempCanvas;// canvas���¸�ֵ

        for (int i = 0; i < canvas.length; i++)
        {
            int page = i / onePageNums;
            if (i % onePageNums == 0)
            {
                RelativeLayout view = new RelativeLayout(con);
                canvas(view,page,onePageNums);
                view.setOnTouchListener(new ParentViewListener());
                contentViews[page] = view;
            }
        }
        playViewPagerAdapter = new PlayViewPagerAdapter(contentViews);

        setAdapter(playViewPagerAdapter);
        this.currentPage = index / onePageNums;
        // ���û�в��ŵ�ͨ�� �Ͳ��� //�����4���浽9 ���� �Ͳ��ź���5��
        for (int index = 0; index < canvas.length; index++)
        {
            if (index < total)
            {
                if (index >= currentPage * onePageNums
                        && index < (currentPage * onePageNums + onePageNums))
                {

                    if (!canvas[index].isPlayed()
                            && canvas[index].getState() != 10
                            && canvas[index].isPrepared())//&& !canvas[canvasIndex].tempbCleanLastView)
                    {
                        iPlayFlowState[index] = CONNECTTING;
                        canvas[index].PlayBack();
                    }

                }
                else
                {
                    if (canvas[index].isPlayed())
                    {
                        try
                        {
//                            canvas[canvasIndex].SetbCleanLastView(false);
                            canvas[index].Stop();
                        }
                        catch (Exception e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        setCurrentItem(currentPage);
    }

    /**
     * ��������ҳ �������³�ʼ�����Ż���
     *
     * @param num
     */
    public void setOnePageNum(int num)
    {
        // int tempOnePageNums = onePageNums;
        if (num == onePageNums)
        {
            return;
        }
        this.onePageNums = num;
        this.pageNums = (total - 1) / onePageNums + 1;
        isShowPtz = false;
        stateChangeListener.showControlBtn(index,isShowPtz);
        // canvasIndex = 0;
        for (int i = 0; i < contentViews.length; i++)
        {
            contentViews[i].removeAllViews();
        }
        contentViews = new RelativeLayout[pageNums];
        // iPlayFlowState = new int[pageNums * onePageNums];
        initeViewPagerContents();
        // setCurrentItem(canvasIndex);
        selectCanvas(index);
    }

    /**
     * @param index ��ǰҳ��
     */
    private void setOnePageOneNum(int index)
    {
        // int tempOnePageNums = onePageNums;
        preOnePageNums = onePageNums;// ��һ��
        this.onePageNums = 1;
        this.pageNums = pageNums * preOnePageNums;
        this.index = index;
        for (int i = 0; i < contentViews.length; i++)
        {
            contentViews[i].removeAllViews();
        }
        contentViews = new RelativeLayout[pageNums];
        // iPlayFlowState = new int[pageNums * onePageNums];
        initeViewPagerContents();

        Log.d("setOnePageOneNum","canvas length=" + canvas.length
                + ",onePageNums=" + onePageNums + ",pageNums=" + pageNums
                + ",currentPage=" + currentPage + ",canvasIndex=" + index);
        selectCanvas(index);
    }

    private void reconnet(int index,int state)
    {
        if (index >= iPlayFlowState.length)
        {
            return;
        }
        if (iPlayFlowState[index] == STOP)
        {
            return;
        }
        if (state == NPC_D_MPI_MON_ERROR_USERID_ERROR

                || state == NPC_D_MPI_MON_ERROR_USERPWD_ERROR)
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
                // Log.i("handleMessage", "CONNECTTING" + canvasIndex + "����״̬�ǣ�" + state);
                if (state <= 0)
                {
                    Log.i("playState","�ȴ������У�state=" + state);
                    long current = System.currentTimeMillis();
                    iPlayFlowState[index] = CONNECTTING_FAIL;
                    if (current - reconnectTime[index] > 20000)
                    {

                        reconnectTime[index] = current;
//                        handler.post(new PlayRunnble(canvasIndex));
                        Log.e("playState","�ȴ������У���ʱ�����豸22222.");
                    }
                }
                else if (state == 1)
                {
                    Log.i("playState","1  �ȴ������У�state=" + state);
                    long current = System.currentTimeMillis();
                    if (current - reconnectTime[index] > 20000)
                    {
                        reconnectTime[index] = current;
//                        handler.post(new PlayRunnble(canvasIndex));
                        Log.e("playState","�ȴ������У���ʱ�����豸22222.");
                    }
                }
                else if (state == 2)
                {
                    Log.d("playState","���ӳɹ�-->" + index);
                    iPlayFlowState[index] = PLAYING;
                    reconnectTime[index] = System.currentTimeMillis();
                }
                else if (state == 3)
                {
                    iPlayFlowState[index] = CONNECTTING_FAIL;
                    reconnectTime[index] = System.currentTimeMillis();

                    Log.e("playState","�����豸ʧ��.");
                }
                break;
            }
            case PLAYING:// ���ڲ���
            {
                // Log.i("handleMessage", "PLAYING" + canvasIndex + "����״̬�ǣ�" + state);
                if (state < 0)
                {
                    long current = System.currentTimeMillis();
                    if (current - reconnectTime[index] > 10000)
                    {
                        iPlayFlowState[index] = CONNECTTING_FAIL;
                        reconnectTime[index] = current;
                    }
                }
                else if (state != 2 && state != 4 && state != 10)
                {
                    iPlayFlowState[index] = CONNECTTING_FAIL;
                    reconnectTime[index] = System.currentTimeMillis();
                }
                break;
            }
            case CONNECTTING_FAIL:// ����ʧ��
            {
                // Log.i("handleMessage", "CONNECTTING_FAIL" + canvasIndex + "����״̬�ǣ�"
                // + state);
                long current = System.currentTimeMillis();
                if (current - reconnectTime[index] > 6000)
                {
                    // Reconnect(i);
//                    handler.post(new PlayRunnble(canvasIndex));
                    Log.e("playState","����ʧ�ܣ������豸1111111111.");
                    iPlayFlowState[index] = CONNECTTING;
                    reconnectTime[index] = current;
                }
                break;
            }
            case RECONNECT:// ��������
            {
                Log.e("playState","�����豸44444.");
//                handler.post(new PlayRunnble(canvasIndex));
                break;
            }
        }

    }

    public boolean isLand()
    {
        return isLand;
    }

    @SuppressWarnings("deprecation")
    public void setLand(boolean isLand1)
    {
        this.isLand = isLand1;
        playHight = 0;
        playWidth = 0;
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {

                int hight = getMeasuredHeight();
                int width = getMeasuredWidth();

                if (width != playWidth && playHight != hight)
                {

                    WindowManager wm = (WindowManager) con
                            .getSystemService(Context.WINDOW_SERVICE);

                    int screenwidth = wm.getDefaultDisplay().getWidth();
                    int height = wm.getDefaultDisplay().getHeight();
                    playWidth = screenwidth;
                    Log.w("setLand","isLand ����:" + isLand
                            + ",getMeasuredHeight=" + hight
                            + ",getMeasuredWidth=" + width + ",screenwidth="
                            + screenwidth + ",height=" + height);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                    if (isLand)
                    {

                        layoutParams.height = height;
                        playHight = layoutParams.height;
                    }
                    else
                    {

                        layoutParams.height = (int) (playWidth * 4 / 5);
                        playHight = layoutParams.height;

                    }
                    layoutParams.width = playWidth;
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    setLayoutParams(layoutParams);
                    playWidth = screenwidth;
                    initeViewPagerContents();
                    selectCanvas(index);
                }
                return true;
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

    public void setRun(boolean isRun)
    {
        this.isRun = isRun;
    }

    public void stopRun()
    {
        stop(index);
    }

    public void release()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i].isPrepared())
            {
                canvas[i].release();
            }
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
            // TODO Auto-generated method stub
            Reconnect(index);
        }

        public synchronized void Reconnect(int index)
        {

            if (canvas[index].isPrepared())
            {
                if (canvas[index].isPlayed())
                {
                    canvas[index].stop();
                    return;
                }
                iPlayFlowState[index] = CONNECTTING;
                reconnectTime[index] = System.currentTimeMillis();
//                canvas[canvasIndex].PlaybackReconnect();
            }

        }
    }

    public void playCurrentPage()
    {
        for (int index = 0; index < onePageNums; index++)
        {
            if (index < total)
            {
                if (canvas[index].isPrepared())
                {
                    if (canvas[index].isPlayed())
                    {
                        canvas[index].stop();
                        return;
                    }
                    playBack(index);
                }
            }

        }

    }

    public void playBack()
    {
        if (canvas[index].isPrepared())
        {
            playBack(index);
        }

    }

    public void playBack(int index)
    {
        if (canvas[index].getState() == SDKError.Statue_Pause)
        {
            canvas[index].Resume();
        }
        else
        {
//            canvas[canvasIndex].SetbCleanLastView(false);
            iPlayFlowState[index] = CONNECTTING;
            reconnectTime[index] = System.currentTimeMillis();
            canvas[index].PlayBack();
            startGetState();
        }
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
//                            if (ApplicationUtils.netOK)
//                            {
                            if (canvas == null)
                            {
                                continue;
                            }
                            int currentPageFirstIndex = currentPage
                                    * onePageNums;
                            for (int i = currentPageFirstIndex; i < currentPageFirstIndex
                                    + onePageNums; i++)
                            {
                                if (onePageNums == 1)
                                {
                                    Thread.sleep(500);
                                }
                                else if (onePageNums == 4)
                                {
                                    Thread.sleep(100);
                                }
                                else
                                {
                                    Thread.sleep(100);
                                }
                                if (i >= canvas.length && i >= total)
                                {
                                    break;
                                }
                                if (canvas[i] == null)
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
//                                }
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

    public void playAndStop()
    {
        if (canvas[index].getState() == SDKError.Statue_Pause)
        {
            if (canvas[index].isPrepared())
            {
                playBack(index);
            }

        }
        else if (canvas[index].getState() == PLAYING)
        {
            try
            {
                // iPlayFlowState[canvasIndex] = STOP;
                canvas[index].Pause();
                // canvas[canvasIndex].SetbCleanLastView(true);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public int setStream()
    {
        if (canvas[index].getState() == PLAYING)
        {
            iPlayFlowState[index] = STOP;
            canvas[index].stop();
        }
        iPlayFlowState[index] = CONNECTTING;
        reconnectTime[index] = System.currentTimeMillis();
        return 0;
//        return canvas[canvasIndex].setStream();
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

    public boolean GetIsSnapPicture(int index)
    {
        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
//                return canvas[canvasIndex].GetIsSnapPicture();
            }
        }
        return false;

    }

    public void snap() throws Exception
    {

        if (canvas[index].isPrepared())
        {
            if (canvas[index].isPlayed())
            {
                canvas[index].setSnap();
            }
            else
            {
                Toast.makeText(con,"截图成功",Toast.LENGTH_SHORT)
                        .show();
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
                Toast.makeText(con,"开始录像",Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        }
        return false;
    }

    /**
     * ��ȡ��ǰѡ�е�id
     *
     * @return
     */
    public String getCurrentSelectId()
    {

        return canvas[index].DevId;

    }

    /**
     * ֹͣ
     *
     * @param index
     */
    public void stop(final int index)
    {
        iPlayFlowState[index] = STOP;
        new Thread()
        {

            @Override
            public void run()
            {

                if (canvas[index].isPrepared())
                {
                    try
                    {

                        Log.e("Stop()","Stop()---->");
                        canvas[index].Stop();

                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }

        }.start();
        sendSateMessage(index,iPlayFlowState[index]);
    }

    /**
     * ���²��ŵ�ǰҳ����ӻ���
     */
    public void RefreshAll()
    {
        for (int i = currentPage * onePageNums; i < currentPage * onePageNums
                + onePageNums; i++)
        {
            RefreshChannel(i);

        }

    }

    /**
     * ���²���
     *
     * @param index
     */
    public void RefreshChannel(int index)
    {

        if (canvas[index].isPrepared())
        {
            // canvas.Play(canvasIndex);
            if (canvas[index].getState() != CONNECTTING)
            {
                try
                {
                    if (canvas[index].isPrepared())
                    {
                        canvas[index].stop();
                        playBack(index);
                    }

                    // sendSateMessage(canvasIndex, iPlayFlowState[canvasIndex]);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * @param style ROW_TOTAL_1 = 1; ROW_TOTAL_4 = 4;ROW_TOTAL_8 = 8; ROW_TOTAL_9
     *              = 9; ROW_TOTAL_12 = 12; ROW_TOTAL_16 = 16;
     */
    public void initeCanvas(int style)
    {
        viewsNum = style;
        switch (style)
        {
            case ROW_TOTAL_1:
                this.row = 1;
                total = 1;
                isSinglePlayView = true;
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
        stopDelayed(false,0);

    }

    /**
     * ��ʱ5��ر���ʾ�Ļ���
     *
     * @param fragmentIsStop fragment�Ƿ�ֹͣ
     */
    public void stopDelayed(boolean fragmentIsStop,int timeMils)
    {
        this.fragmentIsStop = fragmentIsStop;
        stopHandler.removeCallbacks(stopRunble);
        if (fragmentIsStop)
        {
            stopHandler.postDelayed(stopRunble,timeMils);
        }

    }

    public void playCanvas(int style)
    {

        initeCanvas(style);
        // canvas();
    }


    public void canvas(RelativeLayout view,int currentPage,int onePageNum)
    {
        int width = playWidth;
        int height = playHight;
        int w = 0;
        int h = 0;
        int row2 = 0;
        if (isLand)
        {
            row2 = (int) Math.sqrt(onePageNum);
            h = height / row2;
            w = width / row2;
            Log.d("hmaaaa","land , w:" + width + ",h" + height + ",rows:"
                    + row2);
        }
        else
        {
            row2 = (int) Math.sqrt(onePageNum);
            w = width / row2;
            h = height / row2;
            Log.d("hmaaaa","potrat , w:" + width + ",h" + height + ",rows:"
                    + row2);
        }
        int j = 0;// ����
        int k = 0;// ����
        for (int i = currentPage * onePageNum; i < currentPage * onePageNum
                + onePageNum; i++)
        {
            if (i >= canvas.length || canvas[i] == null)
            {
                break;
            }
            L.d("canvasIndex:" + i);
            view.addView(layout(canvas[i],j * w,k * h,w,h).getView());
            j++;

            if ((i + 1) % row2 == 0)
            {
                k++;
                j = 0;
            }

        }

    }

    /**
     * ��¼�����ö໭��Ĵ�С��λ�ã�
     *
     * @param vc
     * @param left
     * @param top
     * @param w
     * @param h
     */
    private VideoCanvas layout(VideoCanvas vc,int left,int top,int w,int h)
    {

        vc.setPosition(left,top);
        vc.setSize(w,h);

//        vc.ci.left = left;
//        vc.ci.right = left + w;
//        vc.ci.top = top;
//        vc.ci.bottom = top + h;
//        Log.d("VideoCanvas","" + vc.ci.toString());
        return vc;
    }

    /**
     * ֹͣ���л����߳�
     *
     * @author Administrator
     */
    class StopRunable implements Runnable
    {

        @Override
        public void run()
        {
            isRun = false;
            // TODO Auto-generated method stub
            for (int i = 0; i < canvas.length; i++)
            {
                if (canvas[i].isPrepared())
                {
                    // LogOut.e("isShow", "��" + 0 + "����ֹͣ");

                    try
                    {
                        // iPlayFlowState[(i*canvas[i].length)+j] = STOP;
                        stateChangeListener.stateChange(i,4,"",
                                canvas[i].Name,onePageNums);
                        if (iPlayFlowState[i] != STOP)
                        {
                            iPlayFlowState[i] = RECONNECT;
                        }

                        canvas[i].Stop();

                        // canvas[].SetbCleanLastView(true);
                        // canvas[i].release();

                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        isRun = false;
                    }
                }

            }

        }

    }

    /**
     * ���ݴ���λ�û�ȡ��ǰѡ��
     *
     * @param x
     * @param y
     *
     * @return
     */
//    public int getCanvasIndex(float x,float y)
//    {
//        Log.d("VideoCanvas","currentPage:" + currentPage + ",pageNums:"
//                + pageNums + ",currentPage * pageNums="
//                + (currentPage * pageNums + onePageNums));
//        for (int canvasIndex = 0; canvasIndex < canvas.length; canvasIndex++)
//        {
//            if (canvasIndex >= currentPage * onePageNums
//                    && canvasIndex < (currentPage * onePageNums + onePageNums))
//            {
//
//                PlayLayout.CanvasInfo canvasInfo = canvas[canvasIndex].ci;
//
//                if (x >= canvasInfo.left && x <= canvasInfo.right
//                        && y >= canvasInfo.top && y <= canvasInfo.bottom)
//                {
//                    return canvasIndex;
//                    // }
//                }
//            }
//        }
//        return -1;
//    }
    public int getCanvasIndex(float x,float y)
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (x >= canvas[i].getLeft() && x <= canvas[i].getRight() && y >= canvas[i].getTop() && y <= canvas[i].getBottom())
            {
                return i;
            }
        }
        return -1;
    }

    int mode = -1;
    private float oldDist;
    private float newDist;

    /**
     * @author Administrator
     */
    class ParentViewListener implements OnTouchListener
    {

        @Override
        public boolean onTouch(View v,MotionEvent event)
        {
            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:

                    int i = getCanvasIndex(event.getX(),event.getY());
                    if (i == -1)
                    {
                        return true;
                    }
                    // LogOut.i("selectCanvas", "֮ǰѡ�У� " + canvasIndex + ",��ǰѡ�У�" + i);

                    if (total != 1)
                    {// ֻ��һ��ͨ���Ͳ���Ҫ
                        isDoubleClick(i,preOnePageNums);
                    }
                    if (index != i)
                    {
                        selectCanvas(i);
                    }
                    else if (onePageNums == 1)
                    {
                        if (isShowPtz)
                        {
                            isShowPtz = false;
                        }
                        else
                        {
                            isShowPtz = true;
                        }
                        stateChangeListener.showControlBtn(index,isShowPtz);
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    // canvas[canvasIndex].imgVideo.onPointerDown(event);

                    if (event.getPointerCount() > 1)
                    {
                        int i1 = getCanvasIndex(event.getX(1),event.getY(1));
                        if (i1 == index)
                        {// ������ָ����ͬһ������
                            if (canvas[index].getState() == 2 && onePageNums == 1)
                            {
                                canvas[index].imgVideo
                                        .setScaleType(ScaleType.MATRIX);
                            }
                        }
                        else
                        {
                            oldDist = spacing(event);
                            mode = 1;
                        }

                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (mode == 1)
                    {
                        if (event.getPointerCount() > 1)
                        {
                            newDist = spacing(event);
                            if (newDist > oldDist && newDist - oldDist > 100f)
                            {
                                if (onePageNums == 4)
                                {
                                    setOnePageOneNum(index);
                                    isSinglePlayView = true;
                                }
                                // else if (onePageNums == 16) {
                                // isSinglePlayView = false;
                                // setOnePageNum(9);
                                // } else if (onePageNums == 9) {
                                // isSinglePlayView = false;
                                // setOnePageNum(4);
                                // }
                                mode = -1;
                            }
                            else if (newDist < oldDist
                                    && oldDist - newDist > 100f)
                            {
                                // if (onePageNums == 4) {
                                // isSinglePlayView = false;
                                // setOnePageNum(9);
                                // } else if (onePageNums == 9) {
                                // isSinglePlayView = false;
                                // setOnePageNum(16);
                                // }
                                mode = -1;
                            }
                        }
                    }
                    else
                    {
                        mode = -1;
                    }
                    break;
                default:
                    break;
            }
            // if (!isShow)
            if (canvas[index].imgVideo.getScaleType() == ScaleType.MATRIX)
            {
//                if (canvas[canvasIndex].deal.set(canvas[canvasIndex].imgVideo,event))
//                {
//                    canvas[canvasIndex].imgVideo.setScaleType(ScaleType.FIT_XY);
//                }
            }
            return true;
        }

    }

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
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

    public StateChangeListener getStateChangeListener()
    {
        return stateChangeListener;
    }

    public void setStateChangeListener(StateChangeListener stateChangeListener)
    {
        this.stateChangeListener = stateChangeListener;
    }

    /**
     * @param index
     */
    public void selectCanvas(int index)
    {
        this.index = index;
        // currentPage = this.getCurrentItem();
        ResetCanvasBackground();
        canvas[index].setHighLight(true);
        canvas[index].getView().bringToFront();
        if (!TextUtils.isEmpty(canvas[index].Name))
        {
            tvTitle.setText("" + canvas[index].Name);
        }
        else
        {
            tvTitle.setText("");
        }
        if (seekTimeBar != null)
        {
            seekTimeBar.setPlayCore(canvas[index].player);
            seekTimeBar.setPlayCoreAndParameters(canvas[index].player,
                    canvas[index].startTime,canvas[index].endTime,0);
        }

    }

    public void ResetCanvasBackground()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i] != null)
            {
                canvas[i].setHighLight(false);
                canvas[i].imgVideo.setScaleType(ScaleType.FIT_XY);
            }

        }

    }

    /**
     * �ж�˫��
     */
    public boolean isDoubleClick(int n,int preOnePageViews)
    {

        if (count == 0 && index == n)
        {

            ++count;
            fTime = System.currentTimeMillis();
            clickHandler.postDelayed(this,200);
        }
        else if (count == 1 && index == n)
        {
            if (System.currentTimeMillis() - fTime < 500)
            {
                Log.i("count","click~~~~~~!!!count is " + count);
                if (!isSinglePlayView)
                {
                    setOnePageOneNum(n);
                    isSinglePlayView = true;
                    // canvas();
                }
                else
                {
                    isSinglePlayView = false;
                    setOnePageNum(preOnePageViews);
                }
                return false;
            }
            else
            {
                count = 0;
            }
        }

        return true;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        count = 0;
    }

//    public static String GetDescription(Context con,int state)
//    {
//        String des = con.getString(R.string.stop);
//
//        switch (state)
//        {
//            case SDKError.Statue_Ready:
//                des = con.getString(R.string.ready);
//                break;
//            case SDKError.Statue_PLAYING:
//                // des = con.getString(R.string.playing);
//                des = "";
//                break;
//            case SDKError.Statue_STOP:
//                des = con.getString(R.string.stop);
//                break;
//            case SDKError.Statue_ConnectFail:
//                des = con.getString(R.string.connect_fail);
//                break;
//            case SDKError.NET_ERROR:
//                des = con.getString(R.string.net_error);
//                break;
//            case SDKError.NET_NODATA_ERROR:
//                des = con.getString(R.string.no_data);
//
//            case SDKError.Exception_ERROR:
//                des = con.getString(R.string.exception_error);
//                break;
//            case SDKError.NosupportDevice_ERROR:
//                des = con.getString(R.string.unsupport_device);
//                break;
//            case SDKError.Beyondmaxchannels_ERROR:
//                des = con.getString(R.string.max_channel);
//                break;
//        }
//        return des;
//    }

    public void setScreenScale(boolean isScreenScale)
    {

        canvas[index].setScreenScale(isScreenScale);
        // Log.i("isScreenScale", "isScreenScale : " + isScreenScale);
    }

    public void setAudio()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (index != i)
            {
                canvas[i].setIsAudio(false);
            }
            else
            {
                if (canvas[i].IsAudio())
                {
                    canvas[i].setIsAudio(false);
                }
                else
                {
                    canvas[i].setIsAudio(true);
                }
            }

        }

    }

    public void setIsAudio(boolean setAudio)
    {
        if (canvas[index].isPrepared())
        {
            canvas[index].setIsAudio(setAudio);
        }

    }

    public boolean IsAudio()
    {
        // IsAudio = isAudio;
        // for (int i = 0; i < canvas.length; i++) {
        //
        // }

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

    public void setPPT()
    {
        for (int i = 0; i < canvas.length; i++)
        {
            if (index != i)
            {
                canvas[i].setPPT(false);
            }
            else
            {
                if (canvas[i].IsPPt())
                {
                    canvas[i].setPPT(false);
                }
                else
                {
                    canvas[i].setPPT(true);
                }
            }

        }

    }

    public void setPPT(boolean isPPt)
    {
        if (canvas[index].isPrepared())
        {
            canvas[index].setPPT(isPPt);
        }

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

    /**
     * ��������ͳ��
     *
     * @return
     */
    public long getDataCount()
    {
        return canvas[index].getDatacount();
    }

    boolean isScroll = false;

    class ViewOnPageListener implements OnPageChangeListener
    {

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
            // TODO Auto-generated method stub
            isScroll = true;

        }

        @Override
        public void onPageScrolled(int arg0,float arg1,int arg2)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0)
        {
            // TODO Auto-generated method stub
            isScroll = false;
            currentPage = arg0;
            for (int index = 0; index < canvas.length; index++)
            {
                if (index >= currentPage * onePageNums
                        && index < (currentPage * onePageNums + onePageNums))
                {
                    if (index < total)
                    {
                        if (!canvas[index].isPlayed()
                                && canvas[index].isPrepared())
//                                && !canvas[canvasIndex].tempbCleanLastView)
                        {
                            iPlayFlowState[index] = CONNECTTING;
                            canvas[index].PlayBack();
                        }
                    }
                }
                else
                {
                    if (canvas[index].isPlayed())
                    {
                        try
                        {
//                            canvas[canvasIndex].SetbCleanLastView(false);
                            canvas[index].Stop();

                        }
                        catch (Exception e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

            selectCanvas(currentPage * onePageNums);

        }

    }

    public void StopAll()
    {
        tvTitle.setText("" + canvas[index].Name);
        for (int i = 0; i < canvas.length; i++)
        {
            if (canvas[i].isPrepared())
            {
                try
                {
                    iPlayFlowState[i] = STOP;
//                    canvas[i].SetbCleanLastView(true);
//                    canvas[i].stopAndClearData();
                    canvas[i].Stop();

                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    public void showDialog(String messString)
    {
    }

    public void setTitleView(TextView tvTitle)
    {
        // TODO Auto-generated method stub
        this.tvTitle = tvTitle;
    }

    class AddOnClick implements OnClickListener
    {
        int index;

        public AddOnClick(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            selectCanvas(index);
            Activity ac = (Activity) con;
            ac.startActivityForResult(
                    new Intent(con,AcChooseDevice.class).putExtra(
                            "isSingleSelect",true)
                            .putExtra("isPlayBack",true),
                    ADD_TO_PLAYBACK_SINGLE);
        }

    }

}

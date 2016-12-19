package com.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.Player.Core.PlayerCore;
import com.Player.Source.TDateTime;
import com.Player.Source.TVideoFile;
import com.gdmss.R;

public class SeekTimeBar extends View
{
    boolean isSmallscaleMode = true;// �Ƿ���С�̶�ģʽ����һ���̶ȳ��ȱȽ϶�
    final int LONG_MARGIN = 80; // ��̶�
    final int SHORT_MARGIN = 20;// С�̶�
    int MARGIN = 20; // �̶ȼ��
    final int SCALE_TOTAL = 144;// �̶�����
    final int HOUR_SCALES = 6;// ÿСʱ�̶���
    final int DAY_TIME_MILLES = 24 * 3600; // һ����ʱ����
    final String SPLITS = "&";
    final String SPLITS1 = ":";
    long dateStart = 0;
    int PANDDING = 20;
    final int PANDDING_TOP = 30;
    public boolean mAlwaysOverrideTouch = true;
    private boolean isSeeking = false; // �Ƿ����϶���

    private boolean isSeeked = false; // �Ƿ��϶��� ����϶��� ��Ҫ�����ŵ��϶���ָ����ʱ��
    protected int mCurrentX;
    protected double mNextX;
    private int mMaxX = Integer.MAX_VALUE;
    protected Scroller mScroller;
    private GestureDetector mGesture;
    private Bitmap bmpLongScale, bmpShortScale, bmpCenter;
    private boolean mDataChanged = false;
    Context context;
    Paint paint, txtPaint, timePaint, fileFildPaint;
    int txtPaintWidth = 0, txtPaintHeight = 0;// ��ʾ�̶�ʱ�� �ı� ���
    int txtDateWidth = 0, txtDateHeight = 0;// ��ʾ�����ı� ���
    int txtTimeWidth = 0, txtTimeHeight = 0;// ��ʾ��ǰָ��ʱ���ı� ���
    int scaleLong = 0;// �̶ܿȳ��� ��λ ����
    double lastX = 0;
    boolean isStoping = false;
    double currentIndex = 0;
    String date = "";
    OnTimeListener timeListener;
    double tempTimes = 0;

    PlayerCore playCore;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd&HH:mm:ss");
    private TDateTime tPlayEndTime;
    private int iPlayType;
    private TDateTime tPlayBeginTime;
    private List<TVideoFile> data;

    public SeekTimeBar(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        this.context = context;
        initView();
    }

    public float getRawSize(float size)
    {
        Resources r;
        if (context == null)
        {
            r = Resources.getSystem();
        }
        else
        {
            r = context.getResources();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,size,
                r.getDisplayMetrics());
    }

    private synchronized void initView()
    {
        mCurrentX = 0;
        mNextX = 0;
        mMaxX = Integer.MAX_VALUE;
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setTextSize(getRawSize(12));
        txtPaint.setColor(Color.WHITE);

        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setTextSize(getRawSize(16));
        timePaint.setColor(Color.WHITE);

        fileFildPaint = new Paint();
        fileFildPaint.setAntiAlias(true);
        fileFildPaint.setColor(Color.GREEN);

        // ������ʾ�̶�ʱ���ı��Ŀ��

        String format = "00:00";
        Rect rect = computeTextScale(txtPaint,format);
        txtPaintWidth = rect.width();
        txtPaintHeight = rect.height();
        PANDDING = txtPaintWidth / 2 + 5;

        String format1 = "2014-07-24";
        Rect rect1 = computeTextScale(timePaint,format1);
        txtDateWidth = rect1.width();
        txtDateHeight = rect1.height();
        // setDate("20140724185812");

        String format2 = "14:55:55";
        Rect rect2 = computeTextScale(timePaint,format2);
        txtTimeWidth = rect2.width();
        txtTimeHeight = rect2.height();

        bmpLongScale = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.img_long);

        bmpShortScale = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.img_short);
        bmpCenter = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.img_center);

        mScroller = new Scroller(getContext());
        mGesture = new GestureDetector(getContext(),mOnGesture);
    }

    public PlayerCore getPlayCore()
    {
        return playCore;
    }

    public void setPlayCoreAndParameters(PlayerCore playCore,
                                         TDateTime tPlayBeginTime,TDateTime tPlayEndTime,int iPlayType)
    {
        this.playCore = playCore;
        this.tPlayBeginTime = tPlayBeginTime;
        this.tPlayEndTime = tPlayEndTime;
        this.iPlayType = iPlayType;

    }

    public void setPlayCore(PlayerCore playCore)
    {
        this.playCore = playCore;

    }

    public OnTimeListener getTimeListener()
    {
        return timeListener;
    }

    public void setTimeListener(OnTimeListener timeListener)
    {
        this.timeListener = timeListener;
    }

    public void setDate(String date)
    {
        // Calendar c = Calendar.getInstance();
        // try {
        // c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(date));
        // dateStart = c.getTimeInMillis();
        // Log.i("mNextX", "ʱ��ת����ĺ�����Ϊ��" + dateStart);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //
        // Log.i("mNextX", "ʱ�������ת������Ϊ��" + sdf.format(new Date(dateStart)));
        // } catch (ParseException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        this.date = date;

    }

    public int getScaleLong()
    {
        return scaleLong;
    }

    public void setScaleLong(int scaleLong)
    {
        this.scaleLong = scaleLong;
    }

    @Override
    protected synchronized void onLayout(boolean changed,int left,int top,
                                         int right,int bottom)
    {
        super.onLayout(changed,left,top,right,bottom);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub

        int left = 0;
        int center = getWidth() / 2;
        // if (scaleLong == 0) {
        scaleLong = computeScaleLong();
        // }
        if (scaleLong != 0)
        {

            if (mNextX > scaleLong - center + PANDDING
                    || mNextX < -(center - PANDDING))
            {
                mNextX = lastX;

                mScroller.forceFinished(true);
                // return;
            }
            else
            {
                lastX = mNextX;
            }
        }
        currentIndex = mNextX - PANDDING + center; // ��ǰ ָ���λ��
        left = (int) (-mNextX + PANDDING);
        if (data != null && data.size() > 0)
        {
            for (int i = 0; i < data.size(); i++)
            {
                drawFildRect(canvas,data.get(i));
            }
        }
        for (int i = 0; i < SCALE_TOTAL + 1; i++)
        {

            if (i % HOUR_SCALES == 0)
            {

                canvas.drawBitmap(bmpLongScale,left,txtPaintHeight
                        + txtDateHeight + 35,paint);
                canvas.drawText(formatTime((i / HOUR_SCALES)),left
                        - txtPaintWidth / 2,txtPaintHeight + txtDateHeight
                        + 20,txtPaint);
                left = left + bmpLongScale.getWidth();
            }
            else
            {

                canvas.drawBitmap(bmpShortScale,left,txtPaintHeight
                        + txtDateHeight + 45,paint);
                left = left + bmpShortScale.getWidth();
            }

            left = left + MARGIN;
        }

        // canvas.drawBitmap(bmpCenter, center, 0, paint);
        // canvas.drawLine(, paint);

        Rect rect = new Rect(center,2,center + 4,getHeight() - 2);
        // Log.i("drawFildRect", "rect:" + rect.toString());
        canvas.drawRect(rect,paint);
        //
        canvas.drawText(date,center - txtDateWidth - 5,txtDateHeight + 10,
                timePaint);
        canvas.drawText(hms,center + 5,txtDateHeight + 10,timePaint);

        super.onDraw(canvas);
    }

    public String formatTime(int time)
    {
        return (time < 9 ? "0" + time : "" + time) + ":00";

    }

    public double getProgress()
    {
        return currentIndex;

    }

    public void setTime(int hour,int min,int sec)
    {
        if (isSeeking)
        {
            return;
        }
        int dtime = hour * 3600 + min * 60 + sec;
        currentIndex = 1.0 * scaleLong * dtime / DAY_TIME_MILLES;
        mNextX = currentIndex + PANDDING - getWidth() / 2;
        postInvalidate();

    }

    public void drawFildRect(Canvas canvas,TVideoFile tvideFile)
    {
        int stime = tvideFile.shour * 3600 + tvideFile.sminute * 60
                + tvideFile.ssecond;
        int etime = tvideFile.ehour * 3600 + tvideFile.eminute * 60
                + tvideFile.esecond;
        // int stime = 9 * 3600 + 35 * 60 + 40;
        // int etime = 12 * 3600 + 35 * 60 + 50;
        double sIndex = 1.0 * scaleLong * stime / DAY_TIME_MILLES;
        double eIndex = 1.0 * scaleLong * etime / DAY_TIME_MILLES;
        // canvas.drawRect((float) sIndex, txtPaintHeight + PANDDING_TOP,
        // (float) eIndex, txtPaintHeight + PANDDING_TOP + 100,
        // fileFildPaint);
        sIndex = PANDDING - mNextX + sIndex;
        eIndex = PANDDING - mNextX + eIndex;
        Rect rect = new Rect((int) sIndex,txtPaintHeight + txtDateHeight + 25,
                (int) eIndex,getHeight() - 2);
        canvas.drawRect(rect,fileFildPaint);
        //Log.d("drawFildRect", "rect:" + rect.toString());

    }

    public void setTimeArea(List<TVideoFile> data)
    {
        this.data = data;
        postInvalidate();
    }

    private long tempTime = 0;// ��¼��һ������ʱ�䳤��
    private String hms = "";
    private int count;
    private long fTime;

    public void setTime(long l)
    {
        if (l == 0)
        {
            return;
        }
//		if (l > tempTime || Math.abs(l - tempTime) > 20) {
//			tempTime = l;
//		} else {
//			return;
//		}
        if (isSeeking)
        {

            return;
        }
        //
        if (isSeeked)
        {
            isSeeked = false;
            seekToTime(currentIndex,scaleLong);
            return;
        }
        String sdate = sdf.format(new Date(l * 1000));

        int dtime = 0;
        if (sdate.contains(SPLITS))
        {
            date = sdate.split(SPLITS)[0];
            hms = sdate.split(SPLITS)[1];
            if (hms.contains(SPLITS1))
            {
                String[] aHms = hms.split(SPLITS1);
                if (aHms.length == 3)
                {
                    dtime = Integer.parseInt(aHms[0]) * 3600
                            + Integer.parseInt(aHms[1]) * 60
                            + Integer.parseInt(aHms[2]);
                }
            }
        }
        currentIndex = 1.0 * scaleLong * dtime / DAY_TIME_MILLES;
        mNextX = currentIndex + PANDDING - getWidth() / 2;
        postInvalidate();

    }

    /**
     * �϶���ĳ��ʱ���
     *
     * @param currentIndex
     * @param scaleLong
     */
    public void seekToTime(double currentIndex,double scaleLong)
    {
        double dtime = DAY_TIME_MILLES * currentIndex / scaleLong;

        int hour = (int) (dtime / 3600);
        int min = (int) (dtime % 3600 / 60);
        int sec = (int) (dtime % 3600 % 60);
        tPlayBeginTime.iHour = hour;
        tPlayBeginTime.iMinute = min;
        tPlayBeginTime.iSecond = 0;
        Calendar c = Calendar.getInstance();

        boolean isOutTime = false;
        long currentTime = System.currentTimeMillis();
        try
        {
            c.setTime(new SimpleDateFormat("yyyyMMddHHmmss")
                    .parse(tPlayBeginTime.iYear
                            + String.format("%02d",tPlayBeginTime.iMonth)
                            + String.format("%02d",tPlayBeginTime.iDay)
                            + String.format("%02d",tPlayBeginTime.iHour)
                            + String.format("%02d",tPlayBeginTime.iMinute)
                            + "00"));

        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (currentTime > c.getTimeInMillis())
        {
            isOutTime = false;
//            AcRemotePlay.timeMills = c.getTimeInMillis();
        }
        else
        {
            isOutTime = true;
        }

        // Show.toast(context, "��ѡʱ�䣺" + hour + ":" + min + ":" + sec);
        if (!isOutTime)
        {
            Log.d("PlayTimeFile",tPlayBeginTime.toString() + "\n" + tPlayEndTime.toString());
            playCore.PlayTimeFile(tPlayBeginTime,tPlayEndTime,iPlayType);
        }
        else
        {
//            Show.toast(context,R.string.start_below_current);
        }
    }

    @Override
    protected void onScrollChanged(int l,int t,int oldl,int oldt)
    {
        // TODO Auto-generated method stub
        super.onScrollChanged(l,t,oldl,oldt);
    }

    public String getTime()
    {

        double dtime = DAY_TIME_MILLES * currentIndex / scaleLong;
        int hour = (int) (dtime / 3600);
        int min = (int) (dtime % 3600 / 60);
        int sec = (int) (dtime % 3600 % 60);
        Log.i("mNextX","��ǰλ�ã�" + currentIndex + ",����λ�ã�" + mNextX);
        if (timeListener != null)
        {
            timeListener.setTime(hour,min,sec);
            timeListener.setTime((int) dtime);
        }
        String stime = (hour > 9 ? String.valueOf(hour) : "0" + hour) + ":"
                + (min > 9 ? String.valueOf(min) : "0" + min) + ":"
                + (sec > 9 ? String.valueOf(sec) : "0" + sec);

        return stime;

    }

    public int computeScaleLong()
    {
        int scaleLong = 0;
        for (int i = 0; i < SCALE_TOTAL + 1; i++)
        {

            if (i % HOUR_SCALES == 0)
            {

                scaleLong = scaleLong + bmpLongScale.getWidth();
            }
            else
            {
                scaleLong = scaleLong + bmpShortScale.getWidth();
            }

            scaleLong = scaleLong + MARGIN;
        }
        Log.i("mNextX","���ȣ�" + (scaleLong - MARGIN));
        return scaleLong - MARGIN - bmpLongScale.getWidth();

    }

    /**
     * �����ı����
     *
     * @param paint
     * @param format
     *
     * @return
     */
    public Rect computeTextScale(Paint paint,String format)
    {
        Rect rect = new Rect();
        paint.getTextBounds(format,0,format.length(),rect);

        return rect;
    }

    // public synchronized void scrollTo(int x) {
    // mScroller.startScroll(mNextX, 0, x - mNextX, 0);
    // requestLayout();
    // }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        boolean handled = super.dispatchTouchEvent(ev);
        handled |= mGesture.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_MOVE)
        {
            isSeeking = true;
        }
        else if (ev.getAction() == MotionEvent.ACTION_UP)
        {
            isSeeking = false;
        }
        return handled;
    }

    protected boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,
                              float velocityY)
    {
        // synchronized (SeekTimeBar.this) {
        // mScroller.fling(mNextX, 0, (int) -(velocityX), 0, 0, mMaxX, 0, 0);
        // }
        // postInvalidate();

        return true;
    }

    protected boolean onDown(MotionEvent e)
    {
        if (isDoubleClick())
        {
            Log.i("isDoubleClick","��⵽˫��~~");
            if (isSmallscaleMode)
            {
                isSmallscaleMode = false;
                MARGIN = LONG_MARGIN;
            }
            else
            {
                isSmallscaleMode = true;
                MARGIN = SHORT_MARGIN;
            }
            postInvalidate();
        }
        mScroller.forceFinished(true);
        return true;
    }

    /**
     * �ж�˫��
     */
    public boolean isDoubleClick()
    {

        if (count == 0)
        {
            ++count;
            fTime = System.currentTimeMillis();
            new Handler().postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    count = 0;
                }
            },250);
        }
        else if (count == 1)
        {
            if (System.currentTimeMillis() - fTime < 250)
            {

                return true;
            }
            else
            {
                count = 0;
            }
        }

        return false;
    }

    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener()
    {

        @Override
        public boolean onDown(MotionEvent e)
        {

            return SeekTimeBar.this.onDown(e);

        }

        @Override
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,
                               float velocityY)
        {

            return SeekTimeBar.this.onFling(e1,e2,velocityX,velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1,MotionEvent e2,
                                float distanceX,float distanceY)
        {

            synchronized (SeekTimeBar.this)
            {

                mNextX += (int) distanceX;

            }
            if (Math.abs(distanceX) > 5)
            {
                isSeeked = true;
            }
            postInvalidate();

            return true;
        }

    };

    public interface OnTimeListener
    {
        public abstract void setTime(int secondMilles);

        public abstract void setTime(int hour,int min,int sec);
    }

}

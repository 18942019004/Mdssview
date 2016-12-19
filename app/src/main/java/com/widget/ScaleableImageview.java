package com.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;


/****
 * ������Ҫ���׼�������ִ�е����̣� ����ImageView�Ǽ̳���View������.
 * onLayout��������һ���ص�����.�÷���������View�е�layout������ִ�У���ִ��layout����ǰ
 * �������ִ��setFrame����. layout������
 * setFrame�������ж����ǵ�View�Ƿ����仯����������仯����ô�����µ�l��t��r��b���ݸ�View��
 * Ȼ��ˢ�½��ж�̬����UI. ���ҷ���ture.û�б仯����false.
 * <p>
 * invalidate����������ˢ�µ�ǰ�ؼ�,
 *
 * @author zhangjia
 */
public class ScaleableImageview extends ImageView
{

    private Activity mActivity;

    private int screen_W, screen_H;// �ɼ���Ļ�Ŀ�߶�

    private int bitmap_W, bitmap_H;// ��ǰͼƬ���

    private int MAX_W, MAX_H, MIN_W, MIN_H;// ����ֵ

    private int current_Top, current_Right, current_Bottom, current_Left;// ��ǰͼƬ������������

    private int start_Top = -1, start_Right = -1, start_Bottom = -1, start_Left = -1;// ��ʼ��Ĭ��λ��.

    private int start_x, start_y, current_x, current_y;// ����λ��

    private float beforeLenght, afterLenght;// ���������

    private float scale_temp;// ���ű���

    private int startL, startT, startR, startB;

    /**
     * ģʽ NONE���� DRAG����ק. ZOOM:����
     *
     * @author zhangjia
     */
    private enum MODE
    {
        NONE,DRAG,ZOOM

    }

    ;

    private MODE mode = MODE.NONE;// Ĭ��ģʽ

    private boolean isControl_V = false;// ��ֱ���

    private boolean isControl_H = false;// ˮƽ���

    private ScaleAnimation scaleAnimation;// ���Ŷ���

    private boolean isScaleAnim = false;// ���Ŷ���

    private MyAsyncTask myAsyncTask;// �첽����

    /**
     * ���췽��
     **/
    public ScaleableImageview(Context context)
    {
        super(context);
    }

    public void setmActivity(Activity mActivity)
    {
        this.mActivity = mActivity;
    }

    /**
     * �ɼ���Ļ���
     **/
    public void setScreen_W(int screen_W)
    {
        this.screen_W = screen_W;
        // Log.i("setScreen_W", "" + screen_W);
    }

    /**
     * �ɼ���Ļ�߶�
     **/
    public void setScreen_H(int screen_H)
    {
        this.screen_H = screen_H;
        // Log.i("setScreen_H", "" + screen_H);
    }

    public ScaleableImageview(Context context,AttributeSet attrs)
    {
        super(context,attrs);
    }

    public void setInitaValue(int startL,int startT,int startR,int startB)
    {
        this.startL = startL;
        this.startT = startT;
        this.startR = startR;
        this.startB = startB;
    }

    /***
     * ������ʾͼƬ
     */
    @Override
    public void setImageBitmap(Bitmap bm)
    {
        if (bm == null)
        {
            return;
        }
        super.setImageBitmap(bm);
        /** ��ȡͼƬ��� **/
        bitmap_W = bm.getWidth();
        bitmap_H = bm.getHeight();

        MAX_W = screen_W * 3;
        MAX_H = screen_W * 3;

        MIN_W = screen_W;
        MIN_H = screen_H;
    }

    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom)
    {
        super.onLayout(changed,left,top,right,bottom);
        if (start_Top == -1)
        {
            start_Top = top;
            start_Left = left;
            start_Bottom = bottom;
            start_Right = right;
        }
    }

    /**
     * ������dispatchTouchEvent��Ԥ����action_down���¼�
     */
    public void OnDown(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            onTouchDown(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        try
        {
            super.onDraw(canvas);
        }
        catch (Exception e)
        {
            Log.e("Exception","MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        /** �����㡢��㴥�� **/
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                performClick();
                onTouchDown(event);
                break;
            // ��㴥��
            case MotionEvent.ACTION_POINTER_DOWN:
                onPointerDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                // }
                break;
            case MotionEvent.ACTION_UP:
                DoubleClick();
                mode = MODE.NONE;
                break;
            // ����ɿ�
            case MotionEvent.ACTION_POINTER_UP:
                if (getHasChanged() == false)
                {
                    return false;
                }
                mode = MODE.NONE;
                /** ִ�����Ż�ԭ **/
                if (isScaleAnim)
                {
                    // doScaleAnim();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean performClick()
    {
        return super.performClick();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        return super.dispatchTouchEvent(event);
    }

    private int count = 0;

    private long time1, time2;

    /**
     * ˫����λ
     **/
    boolean DoubleClick()
    {
        if (getHasChanged())
        {
            switch (count)
            {
                case 0:
                    count++;
                    time1 = System.currentTimeMillis();
                    break;
                case 1:
                    time2 = System.currentTimeMillis();
                    if (time2 - time1 < 500)
                    {
                        // Log.i("ScaleableImageView", "˫��������");
                        Log.i("getischanged","currentwidth" + this.getWidth());
                        Log.i("getischanged","currentheight" + this.getHeight());
                        Log.i("getischanged","screenwidth" + screen_W);
                        Log.i("getischanged","screenheight" + screen_H);
                        setPosition(0,0,screen_W,screen_H);
                    }
                    else
                    {
                        count = 1;
                        time1 = time2;
                    }
                    break;
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * ��ǰ�Ƿ�Ϊ��С�ı�״̬
     **/
    public boolean getHasChanged()
    {
        if (screen_W == this.getWidth() && screen_H == this.getHeight())
        {
            Log.e("changeInfo","screen_W:" + screen_W + "  this.getWidth():" + this.getWidth() + " screen_H:" + screen_H + "this.getHeight():" + this.getHeight());
            Log.i("getHasChanged","����");
            return false;
        }
        else
        {
            Log.e("changeInfo","screen_W:" + screen_W + "  this.getWidth():" + this.getWidth() + " screen_H:" + screen_H + "this.getHeight():" + this.getHeight());
            Log.i("getHasChanged","�ı�");
            return true;
        }
    }

    /**
     * ����
     **/
    void onTouchDown(MotionEvent event)
    {
        mode = MODE.DRAG;

        current_x = (int) event.getRawX();
        current_y = (int) event.getRawY();

        start_x = (int) event.getRawX();
        start_y = (int) event.getRawY();
        // Log.i("onTouchDown", this.getLeft() + "," + this.getTop() + "," +
        // this.getRight() + "," + this.getBottom());

        start_Left = this.getLeft();
        start_Top = this.getTop();
        start_Right = this.getRight();
        start_Bottom = this.getBottom();
        // start_y = current_y - this.getTop();
    }

    /**
     * ������ָ ֻ�ܷŴ���С
     **/
    void onPointerDown(MotionEvent event)
    {
        if (event.getPointerCount() == 2)
        {
            mode = MODE.ZOOM;
            beforeLenght = getDistance(event);// ��ȡ����ľ���
        }
    }

    /**
     * �ƶ��Ĵ���
     **/
    void onTouchMove(MotionEvent event)
    {
        int left = 0, top = 0, right = 0, bottom = 0;
        /** �����϶� **/
        if (mode == MODE.DRAG)
        {
            /** ������Ҫ�����жϴ�����ֹ��dragʱ��Խ�� **/

            /** ��ȡ��Ӧ��l��t,r ,b **/
            left = start_Left + current_x - start_x;
            right = start_Right + current_x - start_x;
            top = start_Top + current_y - start_y;
            bottom = start_Bottom + current_y - start_y;

            // Log.i("onTouchMove", left + "," + top + "," + right + "," +
            // bottom);
            /** ˮƽ�����ж� **/
            if (isControl_H)
            {
                if (left >= 0)
                {
                    left = 0;
                    right = this.getWidth();
                }
                if (right <= screen_W)
                {
                    left = screen_W - this.getWidth();
                    right = screen_W;
                }
            }
            else
            {
                left = this.getLeft();
                right = this.getRight();
            }
            /** ��ֱ�ж� **/
            if (isControl_V)
            {
                if (top >= 0)
                {
                    top = 0;
                    bottom = this.getHeight();
                }

                if (bottom <= screen_H)
                {
                    top = screen_H - this.getHeight();
                    bottom = screen_H;
                }
            }
            else
            {
                top = this.getTop();
                bottom = this.getBottom();
            }
            if (isControl_H || isControl_V)
            {
                this.setPosition(left,top,right,bottom);
            }

            current_x = (int) event.getRawX();
            current_y = (int) event.getRawY();
        }
        /** �������� **/
        if (mode == MODE.ZOOM)
        {
            afterLenght = getDistance(event);// ��ȡ����ľ���

            float gapLenght = afterLenght - beforeLenght;// �仯�ĳ���

            scale_temp = afterLenght / beforeLenght;// ������ŵı���
            // Log.i("scale_temp", "" + scale_temp);
            this.setScale(scale_temp);

            beforeLenght = afterLenght;
        }
    }

    /**
     * ��ȡ����ľ���
     **/
    float getDistance(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * ʵ�ִ����϶�
     **/
    private void setPosition(int left,int top,int right,int bottom)
    {
        this.layout(left,top,right,bottom);
    }

    /**
     * ��������
     **/
    private void setScale(float scale)
    {
        int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// ��ȡ����ˮƽ����
        int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// ��ȡ���Ŵ�ֱ����
        // Log.i("currentWidtn", "" + this.getWidth());
        // Log.i("MAX_W", "" + MAX_W);
        // �Ŵ�
        if (scale > 1 && this.getWidth() <= MAX_W)
        {
            current_Left = this.getLeft() - disX;
            current_Top = this.getTop() - disY;
            current_Right = this.getRight() + disX;
            current_Bottom = this.getBottom() + disY;

            this.setFrame(current_Left,current_Top,current_Right,current_Bottom);

            /***
             * ��ʱ��Ϊ���ǵ��Գƣ�����ֻ��һ���жϾͿ����ˡ�
             */
            if (current_Top <= 0 && current_Bottom >= screen_H)
            {
                // Log.e("jj", "��Ļ�߶�=" + this.getHeight());
                isControl_V = true;// ������ֱ���
            }
            else
            {
                isControl_V = false;
            }
            if (current_Left <= 0 && current_Right >= screen_W)
            {
                isControl_H = true;// ����ˮƽ���
            }
            else
            {
                isControl_H = false;
            }
        }
        // ��С
        else if (scale < 1 && this.getWidth() > MIN_W)
        {
            current_Left = this.getLeft() + disX;
            current_Top = this.getTop() + disY;
            current_Right = this.getRight() - disX;
            current_Bottom = this.getBottom() - disY;

            /***
             * ������Ҫ�������Ŵ���
             */
            // �ϱ�Խ��
            if (isControl_V && current_Top > 0)
            {
                current_Top = 0;
                current_Bottom = this.getBottom() - 2 * disY;
                if (current_Bottom < screen_H)
                {
                    current_Bottom = screen_H;
                    isControl_V = false;// �رմ�ֱ����
                }
            }
            // �±�Խ��
            if (isControl_V && current_Bottom < screen_H)
            {
                current_Bottom = screen_H;
                current_Top = this.getTop() + 2 * disY;
                if (current_Top > 0)
                {
                    current_Top = 0;
                    isControl_V = false;// �رմ�ֱ����
                }
            }

            // ���Խ��
            if (isControl_H && current_Left >= 0)
            {
                current_Left = 0;
                current_Right = this.getRight() - 2 * disX;
                if (current_Right <= screen_W)
                {
                    current_Right = screen_W;
                    isControl_H = false;// �ر�
                }
            }
            // �ұ�Խ��
            if (isControl_H && current_Right <= screen_W)
            {
                current_Right = screen_W;
                current_Left = this.getLeft() + 2 * disX;
                if (current_Left >= 0)
                {
                    current_Left = 0;
                    isControl_H = false;// �ر�
                }
            }

            if (isControl_H || isControl_V)
            {
                this.setFrame(current_Left,current_Top,current_Right,current_Bottom);
            }
            else
            {
                this.setFrame(current_Left,current_Top,current_Right,current_Bottom);
                isScaleAnim = true;// �������Ŷ���
            }
            // Log.i("imageview", "" + current_Left + " " + current_Top +
            // " " + current_Right + " " + current_Bottom);
        }
        else
        {

        }
    }

    /***
     * ���Ŷ�������
     */
    public void doScaleAnim()
    {
        myAsyncTask = new MyAsyncTask(screen_W,this.getWidth(),this.getHeight());
        myAsyncTask.setLTRB(this.getLeft(),this.getTop(),this.getRight(),this.getBottom());
        myAsyncTask.execute();
        isScaleAnim = false;// �رն���
    }

    /***
     * ������������
     */
    class MyAsyncTask extends AsyncTask<Void, Integer, Void>
    {
        private int screen_W, current_Width, current_Height;

        private int left, top, right, bottom;

        private float scale_WH;// ��ߵı���

        /**
         * ��ǰ��λ������
         **/
        public void setLTRB(int left,int top,int right,int bottom)
        {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        private float STEP = 8f;// ����

        private float step_H, step_V;// ˮƽ��������ֱ����

        public MyAsyncTask(int screen_W,int current_Width,int current_Height)
        {
            super();
            this.screen_W = screen_W;
            this.current_Width = current_Width;
            this.current_Height = current_Height;
            scale_WH = (float) current_Height / current_Width;
            step_H = STEP;
            step_V = scale_WH * STEP;
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            while (current_Width <= screen_W)
            {

                left -= step_H;
                top -= step_V;
                right += step_H;
                bottom += step_V;

                current_Width += 2 * step_H;

                left = Math.max(left,start_Left);
                top = Math.max(top,start_Top);
                right = Math.min(right,start_Right);
                bottom = Math.min(bottom,start_Bottom);
                Log.e("jj","top=" + top + ",bottom=" + bottom + ",left=" + left + ",right=" + right);
                onProgressUpdate(new Integer[] {left,top,right,bottom});
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values)
        {
            super.onProgressUpdate(values);
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    setFrame(values[0],values[1],values[2],values[3]);
                }
            });

        }
    }
}

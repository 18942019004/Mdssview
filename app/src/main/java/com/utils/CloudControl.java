package com.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.gdmss.R;
import com.widget.MyPlayer;
import com.widget.PlayLayout;

/**
 * 云台控制的监听类，封装了云台指令
 * <p>
 * 1：给控件设置相应的tag
 * 2：设置监听
 */

public class CloudControl implements View.OnTouchListener
{
    public static final int MD_STOP = 0; // 停止
    public static final int MD_LEFT = 11; // 左
    public static final int MD_RIGHT = 12; // 右
    public static final int MD_UP = 9; // 上
    public static final int MD_DOWN = 10; // 下
    public static final int CORE_LEFT_UP = 35; // 左上
    public static final int CORE_LEFT_DOWN = 37; // 左下
    public static final int CORE_RIGHT_UP = 36; // 右上
    public static final int CORE_RIGHT_DOWN = 38; // 右下
    public static final int ACTION_ZOOMADD = 6; // 拉近
    public static final int ACTION_ZOOMReduce = 5;// 拉远
    public static final int ACTION_FOCUSADD = 7;// 焦距+
    public static final int ACTION_FOCUSReduce = 8;// 焦距减
    public static final int ACTION_Circle_Add = 13; // 光圈+
    public static final int ACTION_Circle_Reduce = 14;// 光圈-

    private int ptzLength = 5;

    public void setPtzLength(int ptzLength)
    {
        this.ptzLength = ptzLength;
    }

    Context context;

    MyPlayer player;

    public CloudControl(Context context,MyPlayer player)
    {
        this.context = context;
        this.player = player;
    }

    @Override
    public boolean onTouch(View v,MotionEvent event)
    {
        String tag = v.getTag().toString();
        if (!tag.contains("ptz"))
        {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int command = -1;
            if (tag.equals(context.getString(R.string.ptz_up)))
            {
                command = MD_UP;
            }
            else if (tag.equals(context.getString(R.string.ptz_down)))
            {
                command = MD_DOWN;
            }
            else if (tag.equals(context.getString(R.string.ptz_left)))
            {
                command = MD_LEFT;
            }
            else if (tag.equals(context.getString(R.string.ptz_right)))
            {
                command = MD_RIGHT;
            }
            else if (tag.equals(context.getString(R.string.ptz_left_up)))
            {
                command = CORE_LEFT_UP;
            }
            else if (tag.equals(context.getString(R.string.ptz_left_down)))
            {
                command = CORE_LEFT_DOWN;
            }
            else if (tag.equals(context.getString(R.string.ptz_right_up)))
            {
                command = CORE_RIGHT_UP;
            }
            else if (tag.equals(context.getString(R.string.ptz_right_down)))
            {
                command = CORE_RIGHT_DOWN;
            }
            else if (tag.equals(context.getString(R.string.ptz_aperture_add)))
            {
                command = ACTION_ZOOMADD;
                player.setPTZ(command,2);
                return false;
            }
            else if (tag.equals(context.getString(R.string.ptz_aperture_reduce)))
            {
                command = ACTION_ZOOMReduce;
                player.setPTZ(command,2);
                return false;
            }
            else if (tag.equals(context.getString(R.string.ptz_focus_add)))
            {
                command = ACTION_FOCUSADD;
                player.setPTZ(command,2);
                return false;
            }
            else if (tag.equals(context.getString(R.string.ptz_focus_reduce)))
            {
                command = ACTION_FOCUSReduce;
                player.setPTZ(command,2);
                return false;
            }
            player.setPTZ(command,ptzLength);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
        {
            player.setPTZ(MD_STOP,0);
        }
        return false;
    }
}
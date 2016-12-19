package com.widget;

import com.gdmss.R;
import com.utils.TextUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


public class DateTimePicker extends LinearLayout
{
    private LayoutInflater inflater;

    private MyDatePicker datePicker;

    private MyTimePicker timePicker;

    private int mWidth, mHeight, dateWidth, TimeWidth;

    public DateTimePicker(Context context,AttributeSet attrs,int defStyle)
    {
        super(context,attrs,defStyle);
        initLayout(context);
    }

    public DateTimePicker(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        initLayout(context);
    }

    public DateTimePicker(Context context)
    {
        super(context);
        initLayout(context);
    }

    public void initLayout(Context context)
    {
        if (!isInEditMode())
        {
            LayoutInflater.from(context).inflate(R.layout.picker,this,true);
            datePicker = (MyDatePicker) findViewById(R.id.mdatepicker);
            timePicker = (MyTimePicker) findViewById(R.id.mtimepicker);
        }
    }

    public String getDate()
    {
        String year = datePicker.getYear() + "";
        String month = (datePicker.getMonth() + 1) + "";
        String day = datePicker.getDayOfMonth() + "";
        return TextUtil.dateTimeComplete(new String[] {year,month,day},"-");
    }

    public String getTime()
    {
        String hour = timePicker.getCurrentHour() + "";
        String minute = timePicker.getCurrentMinute() + "";
        return TextUtil.dateTimeComplete(new String[] {hour,minute},":");
    }

    public String getDateTime()
    {
        return getDate() + "  " + getTime();
    }

    public int getYear()
    {
        return datePicker.getYear();
    }

    public int getMonth()
    {
        return datePicker.getMonth() + 1;
    }

    public int getDay()
    {
        return datePicker.getDayOfMonth();
    }

    public int getHour()
    {
        return timePicker.getCurrentHour();
    }

    public int getMinute()
    {
        return timePicker.getCurrentMinute();
    }
}

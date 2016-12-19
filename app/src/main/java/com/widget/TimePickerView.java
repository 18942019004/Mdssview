package com.widget;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.gdmss.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class TimePickerView extends LinearLayout
{
    public static int START_YEAR = 2010;
    public static int END_YEAR = 2050;
    WheelView wv_year, wv_month, wv_day, wv_hours, wv_mins;
    private OnDateTimeSetListener mCallBack;
    private Calendar mCalendar;
    private int curr_year, curr_month, curr_day, curr_hour, curr_minute;
    // ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж�
    String[] months_big = {"1","3","5","7","8","10","12"};
    String[] months_little = {"4","6","9","11"};
    List<String> list_big, list_little;

    public OnDateTimeSetListener getmCallBack()
    {
        return mCallBack;
    }

    public void setmCallBack(OnDateTimeSetListener mCallBack)
    {
        this.mCallBack = mCallBack;
    }

    public TimePickerView(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        // TODO Auto-generated constructor stub
        initeView(context);
    }

    public TimePickerView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        // TODO Auto-generated constructor stub
        initeView(context);
    }

    public TimePickerView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        initeView(context);
    }

    void initeView(Context context)
    {

        mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_select_layout,null);

        // ��
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR,END_YEAR));// ����"��"����ʾ����
        wv_year.setCyclic(true);// ��ѭ������

        wv_year.setCurrentItem(year - START_YEAR);// ��ʼ��ʱ��ʾ������

        // ��
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1,12));
        wv_month.setCyclic(true);
        wv_month.setCurrentItem(month);

        // ��
        wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setCyclic(true);
        // �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
        if (list_big.contains(String.valueOf(month + 1)))
        {
            wv_day.setAdapter(new NumericWheelAdapter(1,31));
        }
        else if (list_little.contains(String.valueOf(month + 1)))
        {
            wv_day.setAdapter(new NumericWheelAdapter(1,30));
        }
        else
        {
            // ����
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
            {
                wv_day.setAdapter(new NumericWheelAdapter(1,29));
            }
            else
            {
                wv_day.setAdapter(new NumericWheelAdapter(1,28));
            }
        }
        wv_day.setCurrentItem(day - 1);

        // ʱ
        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0,23));
        wv_hours.setCyclic(true);
        wv_hours.setCurrentItem(hour);

        // ��
        wv_mins = (WheelView) view.findViewById(R.id.mins);
        wv_mins.setAdapter(new NumericWheelAdapter(0,59,"%02d"));
        wv_mins.setCyclic(true);
        wv_mins.setCurrentItem(minute);
        // ���"��"����
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener()
        {
            @Override
            public void onChanged(WheelView wheel,int oldValue,int newValue)
            {
                int year_num = newValue + START_YEAR;
                // �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1,31));
                }
                else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1,30));
                }
                else
                {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0)
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1,29));
                    }
                    else
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1,28));
                    }
                }
                setData();
            }
        };
        // ���"��"����
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener()
        {
            @Override
            public void onChanged(WheelView wheel,int oldValue,int newValue)
            {
                int month_num = newValue + 1;
                // �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
                if (list_big.contains(String.valueOf(month_num)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1,31));
                }
                else if (list_little.contains(String.valueOf(month_num)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1,30));
                }
                else
                {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1,29));
                    }
                    else
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1,28));
                    }
                }
                setData();
            }
        };
        OnWheelListener listener = new OnWheelListener();
        wv_day.addChangingListener(listener);
        wv_hours.addChangingListener(listener);
        wv_mins.addChangingListener(listener);
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        int textSize = 0;
        textSize = (int) context.getResources().getDimension(
                R.dimen.whell_textsize);
        wv_day.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        addView(view);
    }

    class OnWheelListener implements OnWheelChangedListener
    {

        @Override
        public void onChanged(WheelView wheel,int oldValue,int newValue)
        {
            // TODO Auto-generated method stub
            setData();
        }

    }

    public void setData()
    {

        curr_year = wv_year.getCurrentItem() + START_YEAR;
        curr_month = wv_month.getCurrentItem() + 1;
        curr_day = wv_day.getCurrentItem() + 1;
        curr_hour = wv_hours.getCurrentItem();
        curr_minute = wv_mins.getCurrentItem();
        if (mCallBack != null)
        {
            mCallBack.onDateTimeSet(curr_year,curr_month,curr_day,curr_hour,

                    curr_minute);
        }
    }

    public interface OnDateTimeSetListener
    {
        void onDateTimeSet(int year,int monthOfYear,int dayOfMonth,int hour,int minute);
    }
}

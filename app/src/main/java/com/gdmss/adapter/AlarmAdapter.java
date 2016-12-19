

package com.gdmss.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.gdmss.adapter.AlarmAdapter.LinearHolder;
import com.gdmss.entity.AlarmMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdmss.R;
import com.gdmss.adapter.AlarmAdapter.GridHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AlarmAdapter extends DifferItemAdapter<LinearHolder, GridHolder>
{

    private List<AlarmMessage> data;

    private Map<String, List<AlarmMessage>> grid_data;

    private List<String> grid_key;

    private LayoutInflater inflater;

    private Context context;

    public AlarmAdapter(Context context,List<AlarmMessage> data)
    {
        setData(data);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount()
    {
        if (displayMode == 0)
        {
            return data.size();
        }
        else
        {
            return grid_data.keySet().size();
        }
    }

    @Override
    public int getDisplayMode()
    {
        return super.getDisplayMode();
    }

    public void changeData()
    {
        if (displayMode == 1)
        {
            displayMode = 0;
        }
        else
        {
            displayMode = 1;
        }
        setData(data);
    }

    @Override
    public LinearHolder onCreateLinearHolder(ViewGroup parentView)
    {
        // TODO Auto-generated method stub
        LinearHolder holder = new LinearHolder(inflater.inflate(R.layout.item_alarminfo,parentView,false));
        return holder;
    }

    @Override
    public GridHolder onCreateGridHolder(ViewGroup parentView)
    {
        // TODO Auto-generated method stub
        GridHolder holder = new GridHolder(inflater.inflate(R.layout.item_alarminfo_grid,parentView,false));
        return holder;
    }

    /**
     * 绑定数据
     * <p>
     * recyclerViewAdapter内的绑定数据方法
     * </p>
     */

    @Override
    public void onBindLinearHolder(LinearHolder holder,int position)
    {
        setLinearView(position,holder);
        holder.itemView.setOnClickListener(new OnLinearClickListener(position));
    }

    @Override
    public void onBindGridHolder(GridHolder holder,int position)
    {
        setGridView(position,holder);
    }

    /**
     * 分组 1：天 2：时间段 3：报警
     *
     * @param i
     * @param holder
     */
    void setLinearView(int i,LinearHolder holder)
    {
        AlarmMessage msg = data.get(i);
        if (i == 0)
        {
            //            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(msg.getDate());
            //            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(msg.getTime());
        }
        else
        {
            AlarmMessage lastMsg = data.get(i - 1);
            // 不在同一天显示日期
            if (msg.alarmDate.get(Calendar.DAY_OF_YEAR) != lastMsg.alarmDate.get(Calendar.DAY_OF_YEAR))
            {
                //                holder.tv_date.setVisibility(View.VISIBLE);
                holder.tv_date.setText(msg.getDate());
            }
            else
            {
                holder.tv_date.setVisibility(View.GONE);
                // holder.tv_time.setText(msg.alarm_time.split(" ")[1]);
            }
            // 同一天显示时间
            if (Math.abs((lastMsg.alarmDate.getTimeInMillis() - msg.alarmDate.getTimeInMillis())) / 1000 > 60)
            {
                //                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(msg.getTime());
            }
            else
            {
                holder.tv_time.setVisibility(View.GONE);
            }
        }
        holder.tv_alarmTime.setText(msg.getTime());
        holder.tv_devName.setText("设备:" + msg.devName);
        holder.tv_alarmInfo.setText("报警类型:" + msg.alarm_info);
    }

    void setGridView(int i,GridHolder holder)
    {
        String key = grid_key.get(i);
        String devName = grid_data.get(key).get(0).devName;
        holder.tv_devName.setText(devName);
        holder.tv_msgCount.setText("报警条数：" + grid_data.get(key).size());
    }

    class LinearHolder extends ViewHolder
    {
        TextView tv_time;

        TextView tv_devName;

        TextView tv_alarmInfo;

        TextView tv_divider;

        TextView tv_date;

        TextView tv_alarmTime;

        public LinearHolder(View convertView)
        {
            super(convertView);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_devName = (TextView) convertView.findViewById(R.id.tv_devicename);
            tv_alarmInfo = (TextView) convertView.findViewById(R.id.tv_eventtype);
            tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            tv_alarmTime = (TextView) convertView.findViewById(R.id.tv_alarmTime);
        }
    }

    class OnLinearClickListener implements View.OnClickListener
    {
        int index;

        public OnLinearClickListener(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View view)
        {
//            AlarmMessage tmpInfo = data.get(index);
//            AlertDialog dialog = new AlertDialog.Builder(context).setMessage(tmpInfo.toString()).setPositiveButton("确认", null).setNegativeButton("取消", null).create();
//            dialog.show();
        }
    }


    class GridHolder extends ViewHolder
    {
        TextView tv_devName;

        TextView tv_msgCount;

        public GridHolder(View itemView)
        {
            super(itemView);
            tv_devName = (TextView) itemView.findViewById(R.id.tv_devName);
            tv_msgCount = (TextView) itemView.findViewById(R.id.tv_msgCount);
        }
    }

    public void setData(List<AlarmMessage> data)
    {
        this.data = data;

        if (null == grid_data)
        {
            grid_data = new HashMap<>();
        }
        if (null == grid_key)
        {
            grid_key = new ArrayList<>();
        }
        grid_data.clear();
        grid_key.clear();

        if (displayMode == 0)
        {
            return;
        }
        for (int i = 0; i < data.size(); i++)
        {
            AlarmMessage tmpMsg = data.get(i);
            String id = tmpMsg.devId;
            if (grid_data.containsKey(id))
            {
                grid_data.get(id).add(tmpMsg);
            }
            else
            {
                List<AlarmMessage> tmpList = new ArrayList<>();
                tmpList.add(tmpMsg);
                // grid_key.add(tmpMsg.devName);
                grid_data.put(id,tmpList);
            }
        }
        grid_key.addAll(grid_data.keySet());
    }
}

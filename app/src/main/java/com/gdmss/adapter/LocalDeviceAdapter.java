package com.gdmss.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Player.Source.TSearchDev;
import com.gdmss.R;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class LocalDeviceAdapter extends BaseAdapter
{
    List<TSearchDev> list;

    Context context;

    LayoutInflater inflater;

    public LocalDeviceAdapter(Context context,List<TSearchDev> list)
    {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i,View view,ViewGroup viewGroup)
    {
        Holder holder;
        if (null == view)
        {
            holder = new Holder();
            view = inflater.inflate(R.layout.item_localdevice,viewGroup,false);
            holder.tvName = (TextView) view.findViewById(R.id.tv_devName);
            holder.tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            holder.tvChannelNum = (TextView) view.findViewById(R.id.tv_channelNum);
            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }
        TSearchDev info = list.get(i);

        //设备名
        if (TextUtils.isEmpty(info.sDevName))
        {
            if (isIp)
            {
                String[] ip = info.sIpaddr_1.split("\\.");
                String name = ip[ip.length - 1];
                info.sDevName = "IP" + name;
            }
            else
            {
                info.sDevName = info.sDevId;
            }
        }
        holder.tvName.setText(info.sDevName);
        //ip地址
        if (isIp)
        {
            holder.tvAddress.setText("Ip:  " + info.sIpaddr_1);
        }
        else
        {
            holder.tvAddress.setText("Device ID:  " + info.sDevId);
        }
        //通道数
        holder.tvChannelNum.setText(context.getString(R.string.searchdevice_channel) + info.usChNum);
        return view;
    }

    public boolean isIp = false;

    class Holder
    {
        TextView tvName, tvAddress, tvChannelNum;
    }
}

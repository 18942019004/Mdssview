package com.gdmss.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Player.Source.TWifiApInfor;
import com.gdmss.R;

public class WifiAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    Context context;
    private List<TWifiApInfor> nodeList;

    public WifiAdapter(Context context)
    {

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.nodeList = new ArrayList<TWifiApInfor>();
    }

    public List<TWifiApInfor> getNodeList()
    {
        return nodeList;
    }

    public void setNodeList(List<TWifiApInfor> nodeList)
    {
        this.nodeList = nodeList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return nodeList.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder vh = null;
        if (convertView == null)
        {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_wifi_item,null);
            vh.tv = (TextView) convertView.findViewById(R.id.show_name);
            vh.img = (ImageView) convertView.findViewById(R.id.wifi_rssi);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }
        TWifiApInfor node = nodeList.get(position);
        vh.tv.setText(node.sSSID + "");
        int resId = 0;
        if (node.iRSSI <= 33)
        {
            resId = R.drawable.wifi_rssi_2;
        }
        else if (node.iRSSI > 33 && node.iRSSI <= 66)
        {
            resId = R.drawable.wifi_rssi_1;
        }
        else
        {
            resId = R.drawable.wifi_rssi_0;
        }
        vh.img.setImageResource(resId);
        return convertView;
    }

    class ViewHolder
    {
        TextView tv;
        ImageView img;
    }
}

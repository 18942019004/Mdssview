package com.gdmss.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdmss.activities.AcCameraSetting;
import com.gdmss.activities.AcDeviceSetting;
import com.gdmss.R;
import com.gdmss.base.APP;
import com.gdmss.entity.PlayNode;


public class DeviceManageAdapter extends BaseExpandableListAdapter
{
    APP app;

//    List<String> parentList;

    LayoutInflater inflater;

    Context context;

    public DeviceManageAdapter(Context context,APP app)
    {
        this.app = app;
        this.context = context;
        inflater = LayoutInflater.from(context);
//        parentList = new ArrayList<>(app.cameraMap.keySet());
    }

    @Override
    public int getGroupCount()
    {
        return app.cameraMap.keySet().size();
    }

    @Override
    public int getChildrenCount(int i)
    {
        return app.cameraMap.get(getKey(i)).size();
    }

    @Override
    public Object getGroup(int i)
    {
        return app.parentList.get(i);
    }


    @Override
    public Object getChild(int i,int i1)
    {
        return app.cameraMap.get(getKey(i)).get(i1);
    }

    String getKey(int index)
    {
        return app.parentList.get(index).getNodeId() + "";
    }

    @Override
    public long getGroupId(int i)
    {
        return i;
    }

    @Override
    public long getChildId(int i,int i1)
    {
        return i1;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int i,boolean b,View view,ViewGroup viewGroup)
    {
        GroupHolder holder;
        if (null == view)
        {
            view = inflater.inflate(R.layout.item_device_group,viewGroup,false);
            holder = new GroupHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_devicename);
            holder.ibtn_about = (ImageView) view.findViewById(R.id.ibtn_edit);
            holder.img_indicator = (ImageView) view.findViewById(R.id.img_indicator);
            view.setTag(holder);
        }
        else
        {
            holder = (GroupHolder) view.getTag();
        }
        PlayNode tempNode = (PlayNode) getGroup(i);// app.parentMap.get(getKey(i));
        holder.tv_name.setText(tempNode.getName());
        holder.ibtn_about.setOnClickListener(new OnModify(tempNode));
        {
            int resource = b ? R.drawable.left_icon_devicelist_group_h : R.drawable.left_icon_devicelist_group_n;
            holder.img_indicator.setBackgroundResource(resource);
        }
        return view;
    }

    @Override
    public View getChildView(int i,int i1,boolean b,View view,ViewGroup viewGroup)
    {
        ChildHolder holder;
        if (null == view)
        {
            view = inflater.inflate(R.layout.item_device_child,viewGroup,false);
            holder = new ChildHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_devicename);
            holder.ibtn_about = (ImageView) view.findViewById(R.id.ibtn_edit);
            view.setTag(holder);
        }
        else
        {
            holder = (ChildHolder) view.getTag();
        }
        PlayNode tempNode = (PlayNode) getChild(i,i1);
        holder.tv_name.setText(tempNode.getName());
        holder.ibtn_about.setOnClickListener(new OnModify(tempNode));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i,int i1)
    {
        return true;
    }

    class OnModify implements View.OnClickListener
    {
        PlayNode node;

        public OnModify(PlayNode node)
        {
            this.node = node;
        }

        @Override
        public void onClick(View v)
        {
            if (node.getParentId() == 0)
            {
                context.startActivity(new Intent(context,AcDeviceSetting.class).putExtra("node",node));
            }
            else
            {
                context.startActivity(new Intent(context,AcCameraSetting.class).putExtra("node",node));
            }
        }
    }

    class GroupHolder
    {
        TextView tv_name;

        ImageView ibtn_about;

        ImageView img_indicator;
    }

    class ChildHolder
    {
        ImageView img_ico;

        TextView tv_name;

        ImageView ibtn_about;
    }
}
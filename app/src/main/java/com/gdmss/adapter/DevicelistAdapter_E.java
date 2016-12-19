

package com.gdmss.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gdmss.R;
import com.gdmss.base.APP;
import com.gdmss.entity.PlayNode;
import com.utils.DataUtils;
import com.utils.L;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DevicelistAdapter_E extends BaseExpandableListAdapter
{
    private APP app;

    private Context context;

    private LayoutInflater inflater;

    private boolean isSelectMode = false;

    public DevicelistAdapter_E(Context context)
    {
        app = (APP) context.getApplicationContext();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount()
    {
        return app.parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        String key = getKey(groupPosition);
        L.e("childCound:" + app.cameraMap.get(key).size());
        return app.cameraMap.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return app.parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition,int childPosition)
    {
        String key = getKey(groupPosition);
        return app.cameraMap.get(key).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition,int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition,boolean isExpanded,View convertView,ViewGroup parent)
    {
        GroupHolder holder;
        if (null == convertView)
        {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.groupitem_dl_devicelist,parent,false);
            holder.tv_deviceName = (TextView) convertView.findViewById(R.id.tv_devicename);
            holder.img_indicator = (ImageView) convertView.findViewById(R.id.img_indicator);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_select);
            convertView.setTag(holder);
        }
        else
        {
            holder = (GroupHolder) convertView.getTag();
        }
        setGroupView(holder,groupPosition,isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,int childPosition,boolean isLastChild,View convertView,ViewGroup parent)
    {
        ChildHolder holder;
        if (null == convertView)
        {
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.childitem_dl_devicelist,parent,false);
            holder.tv_channelName = (TextView) convertView.findViewById(R.id.tv_channelname);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_select);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ChildHolder) convertView.getTag();
        }
        setChildView(holder,groupPosition,childPosition);
        return convertView;
    }

    /**
     * groupView内容
     *
     * @param holder
     * @param groupPosition
     * @param childPosition
     */
    void setGroupView(GroupHolder holder,int groupPosition,boolean isExpand)
    {
        PlayNode tempNode = (PlayNode) getGroup(groupPosition);
        holder.tv_deviceName.setText(tempNode.getName());
        holder.img_check.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
        holder.img_check.setImageResource(tempNode.isSelected ? R.drawable.ico_checked_h : R.drawable.ico_checked_n);
        if (tempNode.isCamera())
        {
            holder.img_indicator.setBackgroundResource(R.drawable.left_icon_devicelist_child);
        }
        else
        {
            int resource = isExpand ? R.drawable.left_icon_devicelist_group_h : R.drawable.left_icon_devicelist_group_n;
            holder.img_indicator.setBackgroundResource(resource);
        }
        holder.img_check.setOnClickListener(new SelectChangeListener(groupPosition));
    }

    /**
     * childView内容
     *
     * @param holder
     * @param groupPosition
     * @param childPosition
     */
    private void setChildView(ChildHolder holder,int groupPosition,int childPosition)
    {
//        holder.tv_channelName.setText("通道 " + channelComplition(childPosition + 1));
        holder.tv_channelName.setText(((PlayNode) getChild(groupPosition,childPosition)).getName());
        holder.img_check.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
        if (isSelectMode)
        {
            PlayNode tempNode = (PlayNode) getChild(groupPosition,childPosition);
            int i = tempNode.isSelected ? R.drawable.ico_checked_h : R.drawable.ico_checked_n;
            holder.img_check.setImageResource(i);
        }
    }

    String channelComplition(int channel)
    {
        String result = channel + "";
        if (result.length() < 2)
        {
            result = "0" + result;
        }
        return result;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,int childPosition)
    {
        return true;
    }

    public String getKey(int groupPosition)
    {
        return app.parentList.get(groupPosition).getNodeId() + "";
    }

    class SelectChangeListener implements OnClickListener
    {
        PlayNode node;

        public SelectChangeListener(int index)
        {
            node = (PlayNode) getGroup(index);
        }

        @Override
        public void onClick(View v)
        {
            node.isSelected = !node.isSelected;
            List<PlayNode> childList = DataUtils.getChannels(app,node.getNodeId() + "");
            for (PlayNode tempNode : childList)
            {
                if (node.isSelected)
                {
                    if (getSelectedCount() >= maxChnnelCount)
                    {
                        return;
                    }
                }
                tempNode.isSelected = this.node.isSelected;
            }
            getSelectedCount();
        }
    }

    // public int getSelectedCount()
    // {
    // // Entry<String, List<PlayNode>> entry =
    // // app.cameraMap.entrySet().iterator();
    // Iterator<?> iterator = app.cameraMap.entrySet().iterator();
    // }

    class GroupHolder
    {
        TextView tv_deviceName;

        ImageView img_indicator;

        ImageView img_check;
    }

    class ChildHolder
    {
        TextView tv_channelName;

        ImageView img_check;
    }

    public boolean isSelectMode()
    {
        return isSelectMode;
    }

    public void setSelectMode(boolean isSelectMode)
    {
        this.isSelectMode = isSelectMode;
        notifyDataSetChanged();
    }

    public int maxChnnelCount = 16;

    public int getSelectedCount()
    {
        int count = 0;
        for (String key : app.cameraMap.keySet())
        {
            List<PlayNode> childList = app.cameraMap.get(key);
            for (int i = 0; i < childList.size(); i++)
            {
                if (childList.get(i).isSelected)
                {
                    count++;
                }
            }
        }
        btnPlay.setEnabled(count == 0 ? false : true);
        btnPlay.setText(context.getString(R.string.choosedevice_startplay) + "(" + count + ")");
        notifyDataSetChanged();
        return count;
    }

    public List<PlayNode> getSelectedNode()
    {
        List<PlayNode> result = new ArrayList<>();
        for (String key : app.cameraMap.keySet())
        {
            List<PlayNode> childList = app.cameraMap.get(key);
            for (int i = 0; i < childList.size(); i++)
            {
                if (childList.get(i).isSelected)
                {
                    result.add(childList.get(i));
                }
            }
        }
        return result;
    }

    private Button btnPlay;

    public void setBtnPlay(Button btnPlay)
    {
        this.btnPlay = btnPlay;
    }
}


package com.gdmss.adapter;


import com.gdmss.R;
import com.gdmss.base.APP;
import com.gdmss.entity.PlayNode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteAdapter_E extends BaseExpandableListAdapter
{
	private LayoutInflater inflater;

	private APP app;

	boolean isSelectMode = false;

	public FavoriteAdapter_E(Context con)
	{
		inflater = LayoutInflater.from(con);
		app = (APP) con.getApplicationContext();
	}

	@Override
	public int getGroupCount()
	{
		return app.favorite_group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		String key = getKey(groupPosition);
		return app.favorites.get(key).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return app.favorite_group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return app.favorites.get(getKey(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		GroupHolder holder = null;
		if (null == convertView)
		{
			holder = new GroupHolder();
			convertView = inflater.inflate(R.layout.groupitem_dl_devicelist, parent, false);
			holder.tv_deviceName = (TextView) convertView.findViewById(R.id.tv_devicename);
			holder.img_indicator = (ImageView) convertView.findViewById(R.id.img_indicator);
			holder.img_check = (ImageView) convertView.findViewById(R.id.img_select);
			convertView.setTag(holder);
		}
		else
		{
			holder = (GroupHolder) convertView.getTag();
		}
		if (isExpanded)
		{
			holder.img_indicator.setBackgroundResource(R.drawable.left_icon_devicelist_group_h);
		}
		else
		{
			holder.img_indicator.setBackgroundResource(R.drawable.left_icon_devicelist_group_n);
		}

		setGroupView(holder, groupPosition, isExpanded);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		ChildHolder holder = null;
		if (null == convertView)
		{
			holder = new ChildHolder();
			convertView = inflater.inflate(R.layout.childitem_dl_devicelist, parent, false);
			holder.tv_channelName = (TextView) convertView.findViewById(R.id.tv_channelname);
			holder.img_check = (ImageView) convertView.findViewById(R.id.img_select);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ChildHolder) convertView.getTag();
		}
		// List<PlayNode> nodeList = app.favorites.get(getKey(groupPosition));
		// PlayNode tempNode = (PlayNode) getChild(groupPosition,
		// childPosition);;//nodeList.get(childPosition);
		// int i = tempNode.isSelected ? R.drawable.ico_checked_h :
		// R.drawable.ico_checked_n;
		// holder.img_check.setImageResource(i);
		// holder.tv_channelName.setText(tempNode.getName() + " " +
		// channelComplition(childPosition + 1));
		setChildView(holder, groupPosition, childPosition);
		return convertView;
	}

	/**
	 * groupView内容
	 * 
	 * @param holder
	 * @param groupPosition
	 * @param childPosition
	 */
	void setGroupView(GroupHolder holder, int groupPosition, boolean isExpand)
	{
		holder.tv_deviceName.setText(app.favorite_group.get(groupPosition));
		holder.img_check.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
		int resource = isExpand ? R.drawable.left_icon_devicelist_group_h : R.drawable.left_icon_devicelist_group_n;
		holder.img_indicator.setBackgroundResource(resource);
	}

	/**
	 * childView内容
	 * 
	 * @param holder
	 * @param groupPosition
	 * @param childPosition
	 */
	void setChildView(ChildHolder holder, int groupPosition, int childPosition)
	{
		holder.tv_channelName.setText("通道 " + channelComplition(childPosition + 1));
		holder.img_check.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
		PlayNode tempNode = (PlayNode) getChild(groupPosition, childPosition);
		holder.tv_channelName.setText(tempNode.getName() + "  " + channelComplition(childPosition + 1));
		if (isSelectMode)
		{
			int i = tempNode.isSelected ? R.drawable.ico_checked_h : R.drawable.ico_checked_n;
			holder.img_check.setImageResource(i);
		}
	}

	/**
	 * 通道号前补0
	 * 
	 * @param channel
	 * @return
	 */
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
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	public String getKey(int index)
	{
		return app.favorite_group.get(index);
	}

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
}

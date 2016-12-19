

package com.gdmss.adapter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gdmss.R;
import com.gdmss.entity.AlarmMessage;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gdmss.adapter.AlarmListAdapter.NormalHolder;


public class AlarmListAdapter extends RecyclerView.Adapter<NormalHolder>
{
	private List<AlarmMessage> data;

	private Map<String, List<AlarmMessage>> grid_data;

	private LayoutInflater inflater;

	public AlarmListAdapter(Context context, List<AlarmMessage> data)
	{
		setData(data);
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount()
	{
		return data.size();
	}

	@Override
	public NormalHolder onCreateViewHolder(ViewGroup container, int arg1)
	{
		NormalHolder holder = null;
		if (displayMode == 0)
		{
			holder = new NormalHolder(inflater.inflate(R.layout.item_alarminfo, container, false));
		}
		else
		{
			holder = new NormalHolder(inflater.inflate(R.layout.item_alarminfo, container, false));
		}
		return holder;
	}


	@Override
	public void onBindViewHolder(NormalHolder paramVH, int paramInt)
	{
		setView(paramInt, paramVH);
	}

	@Override
	public int getItemViewType(int position)
	{
		return displayMode;
	}

	/**
	 * 分组 1：天 2：时间段 3：报警
	 * 
	 * @param i
	 * @param holder
	 */
	void setView(int i, NormalHolder holder)
	{
		AlarmMessage msg = data.get(i);
		if (i == 0)
		{
			holder.tv_date.setVisibility(View.VISIBLE);
			holder.tv_date.setText(msg.getDate());
			holder.tv_time.setVisibility(View.VISIBLE);
			holder.tv_time.setText(msg.getTime());
		}
		else
		{
			AlarmMessage lastMsg = data.get(i - 1);
			// 不在同一天显示日期
			if (msg.alarmDate.get(Calendar.DAY_OF_YEAR) != lastMsg.alarmDate.get(Calendar.DAY_OF_YEAR))
			{
				holder.tv_date.setVisibility(View.VISIBLE);
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
				holder.tv_time.setVisibility(View.VISIBLE);
				holder.tv_time.setText(msg.getTime());
			}
			else
			{
				holder.tv_time.setVisibility(View.GONE);
			}
		}
		holder.tv_devName.setText("设备:" + msg.devName);
		holder.tv_alarmInfo.setText("报警类型:" + msg.alarm_info);
	}


	public void changeData(int displayType)
	{
		this.displayMode = displayType;
		setData(data);
		notifyDataSetChanged();
	}

	int displayMode = 0;

	public void setData(List<AlarmMessage> data)
	{
		this.data = data;
		if (null == grid_data)
		{
			grid_data = new HashMap<>();
		}
		if (displayMode == 0)
		{
			return;
		}
		grid_data.clear();
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
				grid_data.put(id, tmpList);
			}
		}
	}

	class NormalHolder extends ViewHolder
	{
		TextView tv_time;

		TextView tv_devName;

		TextView tv_alarmInfo;

		TextView tv_divider;

		TextView tv_date;

		public NormalHolder(View convertView)
		{
			super(convertView);
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_devName = (TextView) convertView.findViewById(R.id.tv_devicename);
			tv_alarmInfo = (TextView) convertView.findViewById(R.id.tv_eventtype);
			tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		}
	}

	class GridHolder extends ViewHolder
	{
		public GridHolder(View itemView)
		{
			super(itemView);
		}
	}
}


package com.gdmss.adapter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.gdmss.R;
import com.gdmss.entity.AlarmMessage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AlarmMessageAdapter extends BaseAdapter
{
	private List<AlarmMessage> alarmInfos;
	
	private LayoutInflater inflater;

	public AlarmMessageAdapter(Context con)
	{
		alarmInfos = new ArrayList<>();
		this.inflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return alarmInfos.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return alarmInfos.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		Holder holder = null;
		if (null == convertView)
		{
			convertView = inflater.inflate(R.layout.item_alarminfo, parent, false);
			holder = new Holder();
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_devName = (TextView) convertView.findViewById(R.id.tv_devicename);
			holder.tv_alarmInfo = (TextView) convertView.findViewById(R.id.tv_eventtype);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		setView(position, holder);
		return convertView;
	}

	/**
	 * 分组 1：天 2：时间段 3：报警
	 * 
	 * @param i
	 * @param holder
	 */
	void setView(int i, Holder holder)
	{
		AlarmMessage msg = alarmInfos.get(i);
		if (i == 0)
		{
			holder.tv_date.setVisibility(View.VISIBLE);
			holder.tv_date.setText(msg.getDate());
			holder.tv_time.setVisibility(View.VISIBLE);
			holder.tv_time.setText(msg.getTime());
		}
		else
		{
			AlarmMessage lastMsg = alarmInfos.get(i - 1);
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

	long timeMillis = System.currentTimeMillis();


	// @Override
	// public void notifyDataSetChanged()
	// {
	// group.clear();
	// for (int i = 0; i < alarmInfos.size(); i++)
	// {
	// if (i == 0)
	// {
	// group.add(alarmInfos.get(i).getDate());
	// continue;
	// }
	// if (alarmInfos.get(i).alarmDate.get(Calendar.DAY_OF_YEAR) !=
	// alarmInfos.get(i - 1).alarmDate.get(Calendar.DAY_OF_YEAR))
	// {
	// group.add(alarmInfos.get(i).getDate());
	// }
	// }
	// L.e("groupSize:" + group.size());
	// super.notifyDataSetChanged();
	// }


	@Override
	public int getViewTypeCount()
	{
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (position == 0)
		{
			return 0;
		}
		else if (alarmInfos.get(position).alarmDate.get(Calendar.DAY_OF_YEAR) != alarmInfos.get(position - 1).alarmDate.get(Calendar.DAY_OF_YEAR))
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

	public List<AlarmMessage> getAlarmInfos()
	{
		return alarmInfos;
	}

	public void setAlarmInfos(List<AlarmMessage> alarmInfos)
	{
		this.alarmInfos = alarmInfos;
	}


	class Holder
	{
		TextView tv_time;

		TextView tv_devName;

		TextView tv_alarmInfo;

		TextView tv_divider;

		TextView tv_date;
	}
}

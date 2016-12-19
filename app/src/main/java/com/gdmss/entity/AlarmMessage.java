

package com.gdmss.entity;


import java.io.Serializable;
import java.util.Calendar;
import com.Player.Core.PlayerClient;
import com.Player.web.response.AlarmInfo;
import com.gdmss.R;
import com.utils.Utils;
import android.content.Context;


public class AlarmMessage implements Serializable
{
	private static final long serialVersionUID = 5631869580223337065L;

	public String devId = "";

	public String devName = "";

	public int eventType;

	public String alarm_time = "";

	public String alarm_info = "";

	public Calendar alarmDate = Calendar.getInstance();

	public AlarmMessage(Context con, AlarmInfo info)
	{
		devId = info.dev_id;
		alarm_time = info.alarm_time;
		alarmDate = Utils.parseToCalendar(alarm_time);
		eventType = info.alarm_event;
		alarm_info = toMessage(con, eventType);
	}

	public String toMessage(Context con, int type)
	{
		if (type == PlayerClient.NPC_D_MON_ALARM_TYPE_VIDEO_BLIND)
		{
			return con.getString(R.string.alarminfo_video_cover);
		}
		else if (type == PlayerClient.NPC_D_MON_ALARM_TYPE_DEV_FAULT)
		{
			return con.getString(R.string.alarminfo_equipment);
		}
		else if (type == PlayerClient.NPC_D_MON_ALARM_TYPE_VIDEO_LOSS)
		{
			return con.getString(R.string.alarminfo_video_lose);
		}
		else if (type == PlayerClient.NPC_D_MON_ALARM_TYPE_PROBE)
		{
			return con.getString(R.string.alarminfo_probe);
		}
		else
		{
			return con.getString(R.string.alarminfo_move);
		}
	}

	public void setAlarm_time(String alarm_time)
	{
		this.alarm_time = alarm_time;
	}
	
	public String getDate()
	{
		return alarm_time.split(" ")[0];
	}
	
	public String getTime()
	{
		return alarm_time.split(" ")[1];
	}
}



package com.gdmss.entity;


import com.utils.Path;
import com.utils.Utils;


public class OptionInfo
{
	// 云台步长
	private int ptzLength;

	// 比例缩放
	private boolean screenScale;

	// 勿扰模式
	private boolean disturbMode;

	// 报警声音
	private boolean openAlarmSound;

	// 播放模式
	private int playMode;

	public int getPtzLength()
	{
		return ptzLength;
	}

	public static OptionInfo getInstance()
	{
		OptionInfo result = Utils.readJsonObject(Path.optionInfo, OptionInfo.class);
		if (null == result)
		{
			result = new OptionInfo();
		}
		return result;
	}

	public OptionInfo()
	{
		ptzLength = 5;

		screenScale = false;

		disturbMode = false;

		openAlarmSound = true;

		playMode = 1;
	}

	public void setPtzLength(int ptzLength)
	{
		this.ptzLength = ptzLength;
	}

	public boolean isScreenScale()
	{
		return screenScale;
	}

	public void setScreenScale(boolean screenScale)
	{
		this.screenScale = screenScale;
	}

	public boolean isDisturbMode()
	{
		return disturbMode;
	}

	public void setDisturbMode(boolean disturbMode)
	{
		this.disturbMode = disturbMode;
	}

	public boolean isOpenAlarmSound()
	{
		return openAlarmSound;
	}

	public void setOpenAlarmSound(boolean openAlarmSound)
	{
		this.openAlarmSound = openAlarmSound;
	}

	public int getPlayMode()
	{
		return playMode;
	}

	public void setPlayMode(int playMode)
	{
		this.playMode = playMode;
	}
}

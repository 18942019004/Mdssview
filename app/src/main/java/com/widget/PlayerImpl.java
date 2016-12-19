

package com.widget;


import com.gdmss.entity.PlayNode;


public interface PlayerImpl
{
	public abstract void initParam();

	public abstract void play();

	public abstract void play(int index, PlayNode node);

	public abstract void stop();

	public abstract void snap(boolean getSnap);

	public abstract void record(boolean getRecord);

	public abstract void openPPT();

	public abstract void openSound();
}

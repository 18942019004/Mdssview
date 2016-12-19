package com.widget;

public interface PlayStateChangeListener
{
	public abstract void ToPlaying();
	
	public abstract void ToStop();
	
	public abstract void ToConnecting();
}

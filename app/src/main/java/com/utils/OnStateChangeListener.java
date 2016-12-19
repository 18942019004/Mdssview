package com.utils;

public interface OnStateChangeListener
{
	abstract void ToPlaying();
	
	abstract void ToStop();
	
	abstract void ToConnect();
	
	abstract void ToConnectFail();
}

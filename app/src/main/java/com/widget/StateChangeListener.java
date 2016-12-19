package com.widget;

public interface StateChangeListener
{
    abstract public void stateChange(int index,int state,String framRate,String devName,int onePageNum);

    abstract public void isPlaying(int index,boolean isPlaying);

    abstract public void isRecord(int index,boolean isRecord);

    abstract public void isAudio(int index,boolean isAudio);

    abstract public void isTalk(int index,boolean isTalk);

    abstract public void showControlBtn(int index,boolean isShow);

    abstract public void isMainStream(int index,int stream);
}

package com.gdmss.entity;

import java.io.Serializable;

public class ShareBean implements Serializable
{
    public String devName;

    public String seria;

    public int chNo;

    public String user;

    public String pwd;

    public int devType;

    public ShareBean(PlayNode node)
    {
        devName = node.getName();
        seria = node.umid;
        chNo = node.channels.size();
    }

}

package com.gdmss.entity;

import java.io.Serializable;



public class UserInfo implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 4600921373240138986L;
    
    private String sUserName;
    
    private String sPassWord;
    
    private boolean bAutoLogin;
    
    public UserInfo()
    {
        
    }
    
    public UserInfo(String sUserName, String sPassWord, boolean bAutoLogin)
    {
        this.sUserName = sUserName;
        this.sPassWord = sPassWord;
        this.bAutoLogin = bAutoLogin;
    }
    
    public String getsUserName()
    {
        return sUserName;
    }
    
    public void setsUserName(String sUserName)
    {
        this.sUserName = sUserName;
    }
    
    public String getsPassWord()
    {
        return sPassWord;
    }
    
    public void setsPassWord(String sPassWord)
    {
        this.sPassWord = sPassWord;
    }
    
    public boolean isbAutoLogin()
    {
        return bAutoLogin;
    }
    
    public void setbAutoLogin(boolean bAutoLogin)
    {
        this.bAutoLogin = bAutoLogin;
    }
    
    @Override
    public String toString()
    {
        return super.toString();
    }
}

package com.utils;

import android.content.Context;
import android.widget.Toast;



public class T
{
    public static Context context = null;
    
    public static String TAG = "LogOut";
    
    public static void init(Context con)
    {
        context = con;
        TAG = con.getPackageName();
    }
    
    public static void showS(String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    
    public static void showL(String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    
    public static void showS(int resID)
    {
        Toast.makeText(context,context.getString(resID),Toast.LENGTH_SHORT).show();
    }
    
    public static void showL(int resID)
    {
        Toast.makeText(context,context.getString(resID),Toast.LENGTH_LONG).show();
    }
}

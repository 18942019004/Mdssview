package com.utils;

import android.text.TextUtils;


public class TextUtil
{
    public static String dateTimeComplete(String[] str,String splitChar)
    {
        if (TextUtils.isEmpty(splitChar))
        {
            splitChar = "-";
        }
        String tempStr = "";
        if (str != null && str.length > 0)
        {
            for (int i = 0; i < str.length; i++)
            {
                if (str[i].length() < 2)
                {
                    str[i] = "0" + str[i];
                }
                if (i != str.length - 1)
                {
                    tempStr += str[i] + splitChar;
                }
                else
                {
                    tempStr += str[i];
                }
            }
        }
        return tempStr;
    }
}

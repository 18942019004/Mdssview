package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

import com.Player.Core.PlayerClient;
import com.Player.web.websocket.ClientCore;

public class WriteLogThread extends Thread
{
    static final String TAG = "WriteLogThread";
    static final String DIR = "/sdcard/log/";
    int MAXSIZE = 10;// �ļ������ �����ڵ��ڴˣ�ɾ����һ���ļ�
    boolean isRun = true;
    FileOutputStream outputStream;

    public WriteLogThread()
    {
        if (!Utility.isSDCardAvaible())
        {
            isRun = false;
        }

    }

    public boolean isRun()
    {
        return isRun;
    }

    public void setRun(boolean isRun)
    {
        this.isRun = isRun;
    }

    @Override
    public void run()
    {
        try
        {
            String s;
            String r = System.getProperty("line.separator");// ����
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            outputStream = isFileExsitToOutStream(DIR,
                    format.format(new Date()) + ".txt");
            Log.d(TAG,"��������ģʽ");
            if (outputStream == null)
            {
                isRun = false;
            }
            while (isRun)
            {
                ClientCore clientCore = ClientCore.getInstance();
                if (clientCore != null)
                {
                    s = clientCore.CLTLogData(1000);
                    if (!TextUtils.isEmpty(s))
                    {
                        Log.d(TAG,s);
                        s = s + r;
                        outputStream.write(s.getBytes());
                    }

                }

            }
            if (outputStream != null)
            {
                outputStream.flush();
                outputStream.close();
            }

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    FileOutputStream isFileExsitToOutStream(String dir,String fileName)
    {
        FileOutputStream outFileStream = null;
        try
        {

            File Configfile = new File(dir + '/' + fileName);
            File fileDir = new File(dir);
            if (!fileDir.exists())
            {
                fileDir.mkdir();
            }
            /**
             *
             */
            File[] listFile = fileDir.listFiles();
            if (listFile != null)
            {
                if (listFile.length >= MAXSIZE)
                {
                    File deleteFile = null;
                    for (int i = 0; i < listFile.length; i++)
                    {
                        if (i == 0)
                        {
                            deleteFile = listFile[i];
                        }
                        else
                        {
                            if (deleteFile.lastModified() > listFile[i]
                                    .lastModified())
                            {
                                deleteFile = listFile[i];
                            }
                        }

                    }
                    if (deleteFile != null)
                    {
                        deleteFile.delete();
                    }
                }
            }

            if (!Configfile.exists())
            {

                Configfile.createNewFile();

            }
            outFileStream = new FileOutputStream(Configfile);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outFileStream;
    }
}

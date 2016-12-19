package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;



public class Utility
{
    final static String DEMO_THUMB_PATH = "/sdcard/NewUMEye/demo/";
    
    public static boolean isZh(Context con)
    {
        Locale locale = con.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if(language.endsWith("zh"))
            return true;
        else
            return false;
    }
    
    public static void Logout(Activity c)// ע����Ϊ
    {
        c.finish();
    }
    
    public static String FormateTime(int time)// hh:mm:ss,time�Ǻ��뼶��ʱ��
    {
        int hour = time / (1000 * 60 * 60);
        int minute = (time % (1000 * 60 * 60)) / (1000 * 60);
        int second = ((time % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
        String h, m, s;
        if(hour < 10)
        {
            h = "0" + hour;
        }
        else
        {
            h = String.valueOf(hour);
        }
        if(minute < 10)
        {
            m = "0" + minute;
        }
        else
        {
            m = String.valueOf(minute);
        }
        if(second < 10)
        {
            s = "0" + second;
        }
        else
        {
            s = String.valueOf(second);
        }
        String str = h + ":" + m + ":" + s;
        
        return str;
    }
    
    /**
     * ��������IP��ַ�Ƿ�Ϸ�
     * 
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip)
    {
        ip = ip.replace('.','#');
        String[] numbers = ip.split("#");
        if(numbers.length != 4)
            return false;
        for (int i = 0; i < numbers.length; i++)
        {
            int number;
            try
            {
                number = Integer.parseInt(numbers[i]);
            }
            catch (Exception e)
            {
                return false;
            }
            if((number < 0) || (number > 255))
                return false;
        }
        return true;
    }
    
    /**
     * ����Ƿ���Ч����
     * 
     * @param n
     * @return
     */
    public static boolean isValidNumber(String n)
    {
        
        try
        {
            Integer.parseInt(n);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
        
    }
    
    /**
     * �õ����ĵĵ�ǰʱ�䣬��ȷ����
     * 
     * @return
     */
    public static String GetCurrentTime()
    {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String s = c.get(Calendar.YEAR) + "��" + (c.get(Calendar.MONDAY) + 1) + "��" + c.get(Calendar.DAY_OF_MONTH) + "��" + c.get(Calendar.HOUR_OF_DAY) + "ʱ" + c.get(Calendar.MINUTE) + "��" + c.get(Calendar.SECOND) + "��";
        return s;
    }
    
    /**
     * ��ȡͼƬ��С����ͼ
     * 
     * @param fileName
     *            �ļ�����
     * @param scale
     * @return
     */
    public static Bitmap GetThumbImage(String fileName, int w, int h)
    {
        Bitmap result = null;
        try
        {
            BitmapFactory.Options op = new Options();
            Bitmap bmp = BitmapFactory.decodeFile(fileName,op);
            int x = (int) (op.outWidth / (w * 1.0));
            int y = (int) (op.outHeight / (h * 1.0));
            int scale = x <= y ? x : y;
            bmp.recycle();
            op.inSampleSize = scale;
            result = BitmapFactory.decodeFile(fileName,op);
            // System.out.println("scale:"+scale);
        }
        catch (RuntimeException e)
        {
            System.out.println("RuntimeException��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            System.out.println("��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return result;
    }
    
    public static Bitmap GetThumbImage(String fileName)
    {
        Bitmap result = null;
        try
        {
            
            int scale = 3;
            
            BitmapFactory.Options options = new Options();
            options.inSampleSize = scale;
            result = BitmapFactory.decodeFile(fileName,options);
            // System.out.println("scale:"+scale);
        }
        catch (RuntimeException e)
        {
            System.out.println("RuntimeException��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            System.out.println("��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return result;
    }
    
    public static Bitmap GetThumbImage(String fileName, int maxNumOfPixel)
    {
        Bitmap result = null;
        try
        {
            BitmapFactory.Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName,options);
            options.inSampleSize = computeSampleSize(options,-1,maxNumOfPixel);
            options.inJustDecodeBounds = false;
            
            result = BitmapFactory.decodeFile(fileName,options);
            // System.out.println("scale:"+scale);
        }
        catch (RuntimeException e)
        {
            System.out.println("RuntimeException��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            System.out.println("��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return result;
    }
    
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
    {
        int initialSize = computeInitialSampleSize(options,minSideLength,maxNumOfPixels);
        int roundedSize;
        if(initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        }
        else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
    {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),Math.floor(h / minSideLength));
        if(upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if((maxNumOfPixels == -1) && (minSideLength == -1))
        {
            return 1;
        }
        else if(minSideLength == -1)
        {
            return lowerBound;
        }
        else
        {
            return upperBound;
        }
    }
    
    public static Bitmap getBitmap(String fileName)
    {
        Bitmap bmp = null;
        try
        {
            
            bmp = BitmapFactory.decodeFile(fileName);
            
        }
        catch (RuntimeException e)
        {
            System.out.println("RuntimeException��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            System.out.println("��ȡ����ͼ����" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return bmp;
    }
    
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
        
    }
    
    /**
     * ͬ�������ȡͼƬ
     * 
     * @param imageUrl
     * @return
     */
    public static Bitmap loadImageFromNetwork(String imageUrl, int maxNumOfPixel)
    {
        Bitmap tmpBitmap = null;
        try
        {
            // ����������ͨ���ļ������жϣ��Ƿ񱾵��д�ͼƬ
            if(!Utility.isSDCardAvaible())
            {
                
                return null;
            }
            File dirFile = new File(DEMO_THUMB_PATH);
            if(!dirFile.exists())
            {
                dirFile.mkdirs();
            }
            File[] files = dirFile.listFiles();
            boolean isExsit = false;
            String picPath = "";
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            for (int i = 0; i < files.length; i++)
            {
                if(files[i].getName().equals(fileName))
                {
                    isExsit = true;
                    picPath = files[i].getPath();
                    break;
                }
            }
            if(isExsit)
            {
                tmpBitmap = GetThumbImage(picPath,maxNumOfPixel);
            }
            else
            {
                InputStream is = new java.net.URL(imageUrl).openStream();
                tmpBitmap = BitmapFactory.decodeStream(is);
                is.close();
                File myCaptureFile = getFilePath(DEMO_THUMB_PATH,fileName);
                FileOutputStream out = new FileOutputStream(myCaptureFile);
                tmpBitmap.compress(Bitmap.CompressFormat.PNG,80,out);
                out.flush();
                out.close();
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return tmpBitmap;
    }
    
    public static File getFilePath(String filePath, String fileName)
    {
        File file = null;
        makeRootDirectory(filePath);
        try
        {
            file = new File(filePath,fileName);
            if(!file.exists())
            {
                file.createNewFile();
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }
    
    public static void makeRootDirectory(String filePath)
    {
        File file = null;
        try
        {
            file = new File(filePath);
            if(!file.exists())
            {
                file.mkdir();
            }
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * ��ֵд���������ļ�
     * 
     * @param c
     * @param fileName
     * @param key
     * @param value
     */
    public static void WriteLocal(Context c, String fileName, String key, String value)
    {
        SharedPreferences pref = c.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key,value);
        editor.commit();
    }
    
    /**
     * ��ȡ��Ӧ��ֵ
     * 
     * @param c
     * @param fileName
     * @param key
     * @return
     */
    public static String ReadLocal(Context c, String fileName, String key)
    {
        SharedPreferences pref = c.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return pref.getString(key,null);
    }
    
    /**
     * ɾ����Ӧ�ļ�
     * 
     * @param c
     * @param fileName
     * @param key
     */
    public static void RemoveLocal(Context c, String fileName, String key)
    {
        SharedPreferences pref = c.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }
    
    /**
     * ���û��������뼰������������һ����һ�޶����ļ�����
     * 
     * @param server
     *            ��������
     * @param userName
     *            �û���
     * @param password
     *            ����
     * @return �����ļ���
     */
    public static String ToFileName(String server, String userName, String password)
    {
        String ser = server.replace(":","").replace(".","").replace("//","");// ��.��:ȥ��
        // return ser+userName+password;
        return ser + userName;
    }
    
    /**
     * �ڴ濨�Ƿ����ʹ��
     * 
     * @return ������Ϊtrue,��������Ϊfalse
     */
    public static boolean isSDCardAvaible()
    {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED))
        {
            return false;
        }
        return true;
    }
    
    /**
     * �õ��汾��VersionCode
     * 
     * @param c
     * @return
     */
    public static int GetVersionCode(Context c)
    {
        String pName = c.getPackageName();
        try
        {
            PackageInfo pinfo = c.getPackageManager().getPackageInfo(pName,PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static void OpenAPKFile(String fileName, Context context)
    {
        File file = new File(fileName);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    
    public static String getJasonString(String url)
    {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Accept","application/json");
            InputStream is = conn.getInputStream();
            
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is),1024);
            for (String line = r.readLine(); line != null; line = r.readLine())
            {
                sb.append(line);
            }
            try
            {
                is.close();
            }
            catch (IOException e)
            {
            }
            
            String response = sb.toString();
            
            return response;
            
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}

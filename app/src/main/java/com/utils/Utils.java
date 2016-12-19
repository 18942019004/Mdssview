

package com.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.Player.Source.TLoginParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.gdmss.R;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.UserInfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


/**
 * 工具类 1：sharedpreferences存储普通配置 2：fastjson格式写入文件方式存储实体类、列表等数据
 *
 * @author Lee
 */
public class Utils
{
    public static final String TAG = "FileUtils";

    /**
     * 判断系统语言 TODO
     *
     * @param context 上下文
     *
     * @return 中文true
     */
    public static boolean isZh(Context context)
    {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 网络是否可用 TODO
     *
     * @param context
     *
     * @return
     */
    public static boolean netState(Context context)
    {
        boolean netOK = false;

        // 获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo state = connManager.getActiveNetworkInfo();
        if (state != null)
        {
            netOK = state.isAvailable();
        }
        else
        {
            netOK = false;
        }
        return netOK;
    }

    /**
     * 保存bool值 TODO
     *
     * @param con   当前context
     * @param key   键
     * @param value 要保存的值
     */
    public static void saveBoolean(Context con,String key,boolean value)
    {
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    /**
     * 保存String值 TODO
     *
     * @param con   当前context
     * @param key   键
     * @param value 要保存的值
     */
    public static void saveString(Context con,String key,String value)
    {
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 保存int值 TODO
     *
     * @param con   当前context
     * @param key   键
     * @param value 要保存的值
     */
    public static void saveInt(Context con,String key,int value)
    {
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    /**
     * 保存float值 TODO
     *
     * @param con   当前context
     * @param key   键
     * @param value 要保存的值
     */
    public static void saveFloat(Context con,String key,float value)
    {
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key,value);
        editor.commit();
    }

    // ------------------------------------以下为读数据-------------------------------------------------//
    // ------------------------------------以下为读数据-------------------------------------------------//
    // ------------------------------------以下为读数据-------------------------------------------------//

    /**
     * 读取boolean值 TODO
     *
     * @param con 当前context
     * @param key 键
     *
     * @return 跟key相对应的boolean值
     */
    public static boolean ReadBoolean(Context con,String key)
    {
        if (null == key)
        {
            return false;
        }
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        boolean result = sp.getBoolean(key,false);
        return result;
    }

    /**
     * 读取String值 TODO
     *
     * @param con 当前context
     * @param key 键
     *
     * @return 跟key相对应的String值
     */
    public static String ReadString(Context con,String key)
    {
        if (null == key)
        {
            return "";
        }
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        String result = sp.getString(key,"");
        return result;
    }

    /**
     * 读取int值 TODO
     *
     * @param con 当前context
     * @param key 键
     *
     * @return 跟key相对应的flaot值 缺省值为-1
     */
    public static int ReadInt(Context con,String key)
    {
        if (null == key)
        {
            return -1;
        }
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        int result = sp.getInt(key,-1);
        return result;
    }

    /**
     * 读取flaot值 TODO
     *
     * @param con 当前context
     * @param key 键
     *
     * @return 跟key相对应的flaot值 缺省值为-1
     */
    public static float ReadFloat(Context con,String key)
    {
        if (null == key)
        {
            return -1;
        }
        SharedPreferences sp = con.getSharedPreferences("parameters",Activity.MODE_PRIVATE);
        float result = sp.getFloat(key,-1);
        return result;
    }

    // ------------------------------------以下为文件操作-------------------------------------------------//
    // ------------------------------------以下为文件操作-------------------------------------------------//
    // ------------------------------------以下为文件操作-------------------------------------------------//

    /**
     * 判断指定路径文件是否存在 不存在则创建该文件 返回该路径的File对象 TODO
     *
     * @param path 文件完整路径（非文件名）
     *
     * @return fileName为路径的File实例
     */
    public static File fileJudgement(String path)
    {
        File file = new File(path);
        File parentFile = new File(file.getParent());
        if (!parentFile.exists())
        {
            parentFile.mkdirs();
        }
        try
        {
            if (!file.exists())
            {

                file.createNewFile();

            }
            else if (file.isDirectory())
            {
                file.delete();
                file.createNewFile();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    public static void pathCompletion(String dirPath)
    {
        File f = new File(dirPath);
        if (!f.exists())
        {
            f.mkdirs();
        }
        else if (f.isFile())
        {
            f.delete();
            f.mkdirs();
        }
    }

    /**
     * 读取指定文件的内容 返回字节数组 TODO
     *
     * @param f 文件实例
     *
     * @return byte[]
     */
    public static byte[] read(File f)
    {
        byte[] bt = new byte[(int) f.length()];
        try
        {
            FileInputStream is = new FileInputStream(f);
            BufferedInputStream brs = new BufferedInputStream(is);
            brs.read(bt);
            brs.close();
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bt;
    }

    /**
     * 字节流保存内容 TODO
     *
     * @param file 文件实例
     * @param bt   文件内容
     */
    public static void save(File file,byte[] bt)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(bt);
            bos.close();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 保存单个对象 TODO
     *
     * @param obj  要保存的对象
     * @param path 完整路径
     */
    public static void saveJsonObject(Object clazz,String path)
    {
        String jsonStr = JSON.toJSONString(clazz,false);
        File file = fileJudgement(path);
        save(file,jsonStr.getBytes());
    }

    /**
     * 读取单个对象 TODO
     *
     * @param clazz 对象类型
     * @param path  文件路径
     *
     * @return 以object类型返回读取到的对象 读取失败返回null
     */
    public static <T> T readJsonObject(String path,Class<?> clazz)
    {
        File f = fileJudgement(path);
        byte[] bt = read(f);
        String result = new String(bt);
        if (isEmpty(result))
        {
            return null;
        }
        JSON json = JSON.parseObject(result);
        return (T) JSON.toJavaObject(json,clazz);
    }

    /**
     * 保存集合 TODO
     *
     * @param list 对象集合
     * @param path 完整路径
     */
    public static void saveList(List<?> list,String path)
    {
        String jsonStr = JSON.toJSONString(list,true);
        File file = fileJudgement(path);
        save(file,jsonStr.getBytes());
    }

    /**
     * 保存集合 TODO
     *
     * @param map  对象HashMap
     * @param path 完整路径
     */
    public static void saveMap(Map<String, List<PlayNode>> map,String path)
    {
        String jsonStr = JSON.toJSONString(map,true);
        File file = fileJudgement(path);
        save(file,jsonStr.getBytes());
    }

    /**
     * 读取本地的集合数据 TODO
     *
     * @param path  文件路径
     * @param clazz 读取对象的类型(类.class)
     *
     * @return 读取到的对象集合
     */
    public static List<?> readList(Class<?> clazz,String path)
    {
        List<?> result = null;
        File file = new File(path);
        if (!file.exists())
        {
            return result;
        }
        String jsonStr = new String(read(file));
        // 判断读取到的是否为空
        if (isEmpty(jsonStr))
        {
            return result;
        }
        // 将jsonStr转换成clazz类型的集合
        result = JSON.parseArray(jsonStr,clazz);
        return result;
    }

    /**
     * 读取本地的集合数据 TODO
     *
     * @param path  文件路径
     * @param clazz 读取对象的类型(类.class)
     *
     * @return 读取到的对象集合
     */
    public static Map<String, List<PlayNode>> readMap(String path)
    {
        // HashMap<String, List<PlayNode>> result = null;
        File file = new File(path);
        if (!file.exists())
        {
            return new HashMap<String, List<PlayNode>>();
        }
        String jsonStr = new String(read(file));
        // 判断读取到的是否为空
        if (isEmpty(jsonStr))
        {
            return new HashMap<String, List<PlayNode>>();
        }
        // 将jsonStr转换成clazz类型的集合
        Map<String, List<PlayNode>> readInfo = (Map<String, List<PlayNode>>) JSON.parse(jsonStr);
        return readInfo;

        // HashMap<String, List<PlayNode>> result = new HashMap<String,
        // List<PlayNode>>(readInfo);
        // HashMap<String, List<PlayNode>> result1 = new HashMap<String,
        // List<PlayNode>>();
        // Iterator<?> iterator = result.entrySet().iterator();
        // while (iterator.hasNext())
        // {
        // Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
        // List<JSON> item = (List<JSON>) entry.getValue();
        // List<PlayNode> nodeList = new ArrayList<>();
        // for (int i = 0; i < item.size(); i++)
        // {
        // PlayNode tempNode = JSON.toJavaObject(item.get(i), PlayNode.class);
        // nodeList.add(tempNode);
        // }
        // result1.put(entry.getKey().toString(), nodeList);
        // }
        // return result1;
    }

    public static HashMap<String, List<PlayNode>> readMap1(String path)
    {
        HashMap<String, List<PlayNode>> result = new HashMap<>();
        File file = new File(path);
        if (!file.exists())
        {
            return new HashMap<String, List<PlayNode>>();
        }
        String jsonStr = new String(read(file));
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (jsonObject instanceof Map)
        {
            Map map = (Map) jsonObject;
            Iterator<List<PlayNode>> iterator = map.entrySet().iterator();
            while (iterator.hasNext())
            {
                Entry entry = (Entry) iterator.next();
                JSONArray item = (JSONArray) entry.getValue();
                String key = (String) entry.getKey();


                List<PlayNode> list = new ArrayList<>();
                list = JSON.parseArray(item.toJSONString(),PlayNode.class);
                result.put(key,list);

                // for (int i = 0; i < item.size(); i++)
                // {
                // // list.add(JSON.toJavaObject(item.get(i), PlayNode.class));
                // list.add(item.getObject(i, PlayNode.class));
                // }
                // result.put(key, list);
            }
        }
        return result;
    }

    /**
     * TODO 传入文件名 自动填充文件路径（包名目录下）
     *
     * @param con      Context
     * @param fileName 文件名
     *
     * @return 处理后的完整路径
     */
    public static String getCompletePath(Context con,String fileName)
    {
        String path = con.getFilesDir().getParentFile().getPath() + "/" + fileName;
        Log.e(TAG,fileName + "的保存路径为" + path);
        return path;
    }

    /**
     * 判断String是否为空 （String类的isEmpty方法是判断了长度但为判断字符串本身为空的条件） TODO
     *
     * @param str
     *
     * @return
     */
    public static boolean isEmpty(String str)
    {
        if (null == str)
        {
            return true;
        }
        if (str.length() <= 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断list是否为空 TODO
     *
     * @param str
     *
     * @return
     */
    public static boolean isEmpty(List<?> list)
    {
        if (null == list)
        {
            return true;
        }
        if (list.size() <= 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 获取手机唯一码 TODO
     *
     * @param con
     *
     * @return
     */
    public static String getImsi(Context con)
    {
        return android.provider.Settings.Secure.getString(con.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getFileName(File f)
    {
        String name = f.getName();
        String parentName = f.getParent();
        return name.replace(parentName,"");
    }

    public static String getFolderName(String path)
    {
        File f = new File(path);
        if (!f.exists())
        {
            return "";
        }
        String name = f.getName();
        String parentName = f.getParent();
        return name.replace(parentName,"");
    }

    public static <E> boolean isExist(List<E> dataSource,E obj)
    {
        Iterator<E> it = dataSource.iterator();
        while (it.hasNext())
        {
            if (it.next().equals(obj))
            {
                return true;
            }
        }
        return false;
    }

    public static Bitmap decodeFile(File f)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 8;
        return BitmapFactory.decodeFile(f.getPath(),opt).copy(Config.RGB_565,true);
    }

    public static Options getBmpOption(File f)
    {
        Options opt = new BitmapFactory.Options();
        BitmapFactory.decodeFile(f.getPath(),opt);
        return opt;
    }

    public static Bitmap decodeFile(File f,Rect rec)
    {
        if (null == rec)
        {

        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 4;
        return BitmapFactory.decodeFile(f.getPath(),opt).copy(Config.RGB_565,true);
    }

    public static BitmapFactory.Options getBitmapInfo(File f)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getPath(),opt);
        return opt;
    }

    public static void decodeFileInLimitSize(Rect size)
    {

    }

    /**
     * 获取分辨率信息 TODO
     *
     * @param con
     *
     * @return
     */
    public static DisplayMetrics getDefaultDisplay(Context con)
    {
        DisplayMetrics dm = new DisplayMetrics();

        WindowManager wm = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getMetrics(dm);

        return dm;
    }

    /**
     * 获取客户端版本名 TODO
     *
     * @param context
     *
     * @return
     */
    public static String getVersionName(Context context)
    {
        String version = "";
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            version = "v" + info.versionName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return version;
    }

    public static Calendar parseToCalendar(String str)
    {
        Calendar calendar = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date tempDate = format.parse(str);
            calendar = Calendar.getInstance();
            calendar.setTime(tempDate);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calendar;
    }

    public static String timeConvertion(int timeMillis)
    {
        int seconds = timeMillis / 1000;
        int hour = seconds / 3600;
        int minute = seconds % 3600 / 60;
        int second = seconds % 3600 % 60;

        String sHour = String.format("%02d",hour);
        String sMinute = String.format("%02d",minute);
        String sSecond = String.format("%02d",second);

//        if (hour > 0)
//        {
        return sHour + ":" + sMinute + ":" + sSecond;
//        }
//        else
//        {
//            return sMinute + ":" + sSecond;
//        }
    }
}

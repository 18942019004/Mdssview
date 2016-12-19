package com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;

import java.io.File;


/**
 * Created by James on 2016/11/21.
 * some helper mehod for bitmap
 */

public class BmpUtil
{
    public static Bitmap decodeBitmap(String path)
    {
        Bitmap tempBmp;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,opt);
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 2;
        opt.inPreferredConfig = Bitmap.Config.ALPHA_8;
        tempBmp = BitmapFactory.decodeFile(path,opt);
        return tempBmp;
    }

    public static Bitmap getThumbnail(String path)
    {
        File f = new File(path);
        if (!f.exists())
        {
            return null;
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap,150,150);
        return bitmap;
    }

    public static String getVideoTime(String path)
    {
        String result = "";
        File f = new File(path);
        if (!f.exists())
        {
            return result;
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        result = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mmr.release();
        int millis = Integer.valueOf(result);
        return Utils.timeConvertion(millis);

    }
}

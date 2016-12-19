

package com.utils;


import com.utils.Utils;
import android.content.Context;
import android.os.Environment;


public class Path
{
	public static String userinfo = "user.xml";

	public static String thumbnils = "";

	public static String favorite_group = "favorite_group";

	public static String favorites = "favorites";

	public static String optionInfo = "option";

	public static String SNAPSHOT = "GDMSS_SNAP";

	public static String VIDEORECORD = "GDMSS_VIDEORECORD";

	public static void initPath(Context context)
	{
		userinfo = Utils.getCompletePath(context, userinfo);

		thumbnils = Utils.getCompletePath(context, thumbnils);

		favorite_group = Utils.getCompletePath(context, favorite_group);

		favorites = Utils.getCompletePath(context, favorites);

		optionInfo = Utils.getCompletePath(context, optionInfo);

		if (isSDCardAvaliable())
		{
			SNAPSHOT = Environment.getExternalStorageDirectory() +"//"+ SNAPSHOT + "//";

			VIDEORECORD = Environment.getExternalStorageDirectory() +"//"+ VIDEORECORD + "//";
		}
		else
		{
			SNAPSHOT = Utils.getCompletePath(context, SNAPSHOT);

			VIDEORECORD = Utils.getCompletePath(context, VIDEORECORD);
		}

	}

	public static boolean isSDCardAvaliable()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		return false;
	}
}

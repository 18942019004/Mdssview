

package com.gdmss.base;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Player.Source.TLoginParam;
import com.Player.web.response.DevItemInfo;
import com.Player.web.response.ResponseDevList;
import com.Player.web.websocket.ClientCore;
import com.gdmss.fragment.FgMore;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.UserInfo;
import com.gdmss.fragment.FgAlarmMessage;
import com.gdmss.fragment.FgDeviceManage;
import com.gdmss.fragment.FgFavorite;
import com.gdmss.fragment.FgFileManage;
import com.gdmss.fragment.FgPlay;
import com.gdmss.fragment.FgPlayBack;
import com.utils.CommonData;
import com.utils.DataUtils;
import com.utils.L;
import com.utils.Path;
import com.utils.T;
import com.utils.Utils;
import com.widget.SlidingMenu;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;


public class APP extends Application
{
    // 登陆信息==========================
    public TLoginParam loginParam;

    // public PlayerClient client;

    public ClientCore client;

    public String SERVER = "app.umeye.cn";

    public int PORT = 8300;

    public String IMSI;

    public UserInfo currentUser;
    // ====================================

    public List<Fragment> fragments_live, fragments_talk;

    public List<PlayNode> nodeList = new ArrayList<>();

    // 设备列表(一级列表)
    public List<PlayNode> parentList = new ArrayList<PlayNode>();

    public Map<String, PlayNode> parentMap = new HashMap<>();

    // 所有镜头
    public Map<String, List<PlayNode>> cameraMap = new HashMap<>();

    // 收藏列表(一级列表)
    public List<String> favorite_group = new ArrayList<String>();

    // 收藏的镜头
    public Map<String, List<PlayNode>> favorites = new HashMap<String, List<PlayNode>>();

    public static APP app;

    public static APP getInstance(Context con)
    {
        if (null == app)
        {
            app = (APP) con.getApplicationContext();
        }
        return app;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        initClient();
        initParameters();
    }

    private void initParameters()
    {
        T.init(this);

        L.init("fuck!");

        Path.initPath(this);

        cameraMap = new HashMap<>();

        parentList = new ArrayList<>();

        currentUser = new UserInfo();

        // favorite_group = new ArrayList<String>();
        //
        // favorites = new HashMap<String, List<PlayNode>>();

        favorite_group = (List<String>) Utils.readList(String.class,Path.favorite_group);
        if (null == favorite_group)
        {
            favorite_group = new ArrayList<>();
        }

        favorites = Utils.readMap1(Path.favorites);
    }


    /**
     * 初始化客户端
     */
    void initClient()
    {
        client = ClientCore.getInstance();
        int language = Utils.isZh(this) ? 2 : 1;
        IMSI = Utils.getImsi(this);
        client.setupHost(this,CommonData.ServerAddress,0,IMSI,language,CommonData.ClientID,Utils.getVersionName(this),"");
    }


    /**
     * 初始化所有fragment
     *
     * @param menu
     */
    public void initAllFragment(SlidingMenu menu)
    {
        fragments_live = new ArrayList<>();
        fragments_live.add(FgPlay.getInstance(menu));
        fragments_live.add(FgPlayBack.getInstance(menu));
        fragments_live.add(FgDeviceManage.getInstance(menu));
        fragments_live.add(FgAlarmMessage.getInstance(menu));
        fragments_live.add(FgFileManage.getInstance(menu));
        fragments_live.add(FgFavorite.getInstance(menu));
        fragments_live.add(FgMore.getInstance(menu));
//        fragments_live.add(new FgLocalSetting(menu));
    }


    /**
     * 获取服务器上的列表数据
     */
    public void getListFromServer()
    {
//        boolean isLocal = currentUser.getsUserName().equals(CommonData.LOCAL_USER);
        client.getNodeList(0,0,0,loadListHandler);
    }

    private Handler loadListHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            ResponseDevList response = (ResponseDevList) msg.obj;
            if (null != response && null != response.h)
            {
                int error = response.h.e;
                if (error == 200)
                {
                    List<DevItemInfo> items = response.b.nodes;
                    List<PlayNode> list = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++)
                    {
                        DevItemInfo item = items.get(i);
                        if (null != item)
                        {
                            PlayNode tempNode = PlayNode.DataConversion(items.get(i));
                            list.add(tempNode);
                        }
                    }
                    DataUtils.sortList(app,list);
                    Intent it = new Intent();
                    it.setAction("refresh");
                    sendBroadcast(it);
                }
            }
            return false;
        }
    });


    /**
     * 清空数据
     */
    public void clearData()
    {
        currentUser.setsPassWord("");
        currentUser.setbAutoLogin(false);
        // 设备列表(一级列表)
        parentList.clear();// = new ArrayList<>();

        parentMap.clear();// = new HashMap<>();

        // 所有镜头
        cameraMap.clear();// = new HashMap<>();

        // 收藏列表(一级列表)
        favorite_group.clear();// = new ArrayList<>();

        // 收藏的镜头
        favorites.clear();// = new HashMap<>();

        System.gc();
    }
}

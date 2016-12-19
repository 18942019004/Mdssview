package com.gdmss.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.Player.Source.TDateTime;
import com.gdmss.activities.AcChoosePlayBackDevice;
import com.gdmss.R;
import com.gdmss.base.BaseFragment;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.VideoListResult;
import com.widget.PlayBackLayout;
import com.widget.PlayLayout;
import com.widget.SeekTimeBar;
import com.widget.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */
public class FgPlayBack extends BaseFragment implements View.OnClickListener
{
    @BindView(R.id.ibtn_device)
    Button ibtnDevice;
    @BindView(R.id.btn_snap)
    Button btnSnap;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.btn_replay)
    Button btnReplay;
    @BindView(R.id.cbtn_sound)
    CheckBox cbtnSound;
    @BindView(R.id.bottom_menu)
    HorizontalScrollView bottomMenu;
    @BindView(R.id.player)
    PlayLayout player;
    public List<PlayNode> nodeList;
    @BindView(R.id.player1)
    PlayBackLayout player1;
    @BindView(R.id.seekBar)
    SeekTimeBar seekBar;
    @BindView(R.id.btn_menu)
    Button btnMenu;
    @BindView(R.id.titlebar)
    RelativeLayout titlebar;
    @BindView(R.id.layout_functions)
    LinearLayout layoutFunctions;

    public static FgPlayBack getInstance(SlidingMenu menu)
    {
        FgPlayBack instance = new FgPlayBack();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fg_playback,container,false);
            ButterKnife.bind(this,view);
            initParam();
            initViews();
            initPlayer();
        }
        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        player.stopAll();
    }

    void initParam()
    {
        nodeList = new ArrayList<>();
    }

    void initViews()
    {
        ibtnDevice.setOnClickListener(this);
    }

    void initPlayer()
    {
        player.initeView(getActivity(),false);
        player.initeCanvas(4);
        player.playCanvas(4);
        player.seekTimeBar = seekBar;
        player.isPlayBack = true;
        player.setOnAddToPlayListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(context,AcChoosePlayBackDevice.class).putExtra("isSelectMode",true),666);
            }
        });
        player.hideFps();
    }

    void initPlayer1()
    {
//        player1 = (PlayBackLayout) view.findViewById(R.id.player1);
//        player1.setTitleView(tvTitle);
//        player1.setStateChangeListener(new StateChange());
        player1.initeCanvas(4);
        player1.playCanvas(4);
        player1.initeData(getActivity(),nodeList,false);

        int OnePageNum = 4;//sp.getInt(ROW_TOTAL, 4);

        player1.setOnePageNum(OnePageNum);// 锟斤拷取锟较次憋拷锟斤拷幕锟斤拷锟�
//        configuration(getResources().getConfiguration());
    }

    @OnClick({R.id.btn_snap,R.id.btn_record,R.id.btn_play,R.id.btn_replay,R.id.cbtn_sound})
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibtn_device:
                Intent it = new Intent(getActivity(),AcChoosePlayBackDevice.class);
                it.putExtra("isSelectMode",true);
                startActivityForResult(it,666);
                break;
            case R.id.btn_snap:
                player.snap();
                break;
            case R.id.btn_record:
                try
                {
                    player.record();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_play:
                if (player.getPlayState() == PlayLayout.PLAYING)
                {
                    player.pause();
                }
                else
                {
                    player.resume();
                }
                break;
            case R.id.btn_replay:
                player.replay();
//                player.stopAll();
                break;
            case R.id.cbtn_sound:
                player.setAudio();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == 666 && resultCode == Activity.RESULT_OK)
        {
            PlayNode tempNode = (PlayNode) data.getSerializableExtra("node");
            List<VideoListResult> multiData = (List<VideoListResult>) data.getSerializableExtra("vedioList");
            if (null == tempNode)
            {
                for (int i = 0; i < nodeList.size(); i++)
                {
                    tempNode = nodeList.get(i);
                    player.canvas[i].Prepare(tempNode.getName(),tempNode.getDeviceId(),true,"");
                    player.canvas[i].initePlayBackTime(AcChoosePlayBackDevice.startTime,AcChoosePlayBackDevice.endTime);
//                    player.canvas[i].PlayBack();
                    player.playback(i);

                    seekBar.setDate(TDateTimeToData(AcChoosePlayBackDevice.startTime));
                    seekBar.setPlayCoreAndParameters(player.canvas[i].player,AcChoosePlayBackDevice.startTime,AcChoosePlayBackDevice.endTime,0);
                    seekBar.setTimeArea(multiData.get(i).multiData);
                }
            }
            else
            {
                player.getCanvas().Prepare(tempNode.getName(),tempNode.getDeviceId(),true,"");
                player.getCanvas().initePlayBackTime(AcChoosePlayBackDevice.startTime,AcChoosePlayBackDevice.endTime);
                player.getCanvas().PlayBack();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public String TDateTimeToData(TDateTime tstart)
    {
        return tstart.iYear + "-" + tstart.iMonth + "-" + tstart.iDay;
    }
}

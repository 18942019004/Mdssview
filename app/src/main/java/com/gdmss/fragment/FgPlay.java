package com.gdmss.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdmss.R;
import com.gdmss.activities.AcChooseDevice;
import com.gdmss.adapter.PlayerPageAdapter;
import com.gdmss.base.BaseFragment;
import com.gdmss.dialog.AcAddFavorite;
import com.gdmss.entity.PlayNode;
import com.utils.CloudControl;
import com.utils.L;
import com.widget.MyHorizontalScrollView;
import com.widget.MyPlayer;
import com.widget.PlayLayout;
import com.widget.SlidingMenu;
import com.widget.VideoCanvas;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FgPlay extends BaseFragment implements OnClickListener, ViewPager.OnPageChangeListener
{
    @BindView(R.id.ibtn_device)
    Button ibtnDevice;
    //分屏
    @BindView(R.id.btn_division)
    Button btnDivision;
    @BindView(R.id.btn_snap)
    Button btnSnap;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_stream)
    Button btnStream;
    @BindView(R.id.btn_replay)
    Button btnReplay;
    @BindView(R.id.cbtn_sound)
    CheckBox cbtnSound;
    @BindView(R.id.btn_showptz)
    Button btnShowptz;
    @BindView(R.id.btn_zoom)
    Button btnZoom;
    /**
     * 云台按钮
     */
    @BindView(R.id.btn_left)
    Button btnLeft;
    @BindView(R.id.btn_aperture_add)
    Button btnApertureAdd;
    @BindView(R.id.btn_aperture_reduce)
    Button btnApertureSub;
    @BindView(R.id.btn_focus_add)
    Button btnFocusAdd;
    @BindView(R.id.btn_focus_reduce)
    Button btnFocusreduce;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.btn_left_up)
    Button btnLeftUp;
    @BindView(R.id.btn_left_down)
    Button btnLeftDown;
    @BindView(R.id.btn_right_up)
    Button btnRightUp;
    @BindView(R.id.btn_right_down)
    Button btnRightDown;
    @BindView(R.id.rl_controlBtns)
    RelativeLayout rlControlBtns;
    /**
     * 播放器
     */
//    @BindView(R.id.player)
//    PlayLayout player;
    int width = 0;
    int height = 0;
    ImageButton ibtn_4, ibtn_9, ibtn_16;
    @BindView(R.id.bottom_menu)
    MyHorizontalScrollView bottomMenu;
    @BindView(R.id.titlebar)
    RelativeLayout titlebar;
    @BindView(R.id.pagerPlayer)
    MyPlayer myPlayer;
    @BindView(R.id.tv_indicator)
    TextView indicator;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_indicatorLeft)
    TextView btnIndicatorLeft;
    @BindView(R.id.btn_indicatorRight)
    TextView btnIndicatorRight;

    private LayoutInflater inflater;

    CloudControl cloudListener;

    private RelativeLayout container;

    private CheckBox cb_playlast;

    public List<PlayNode> nodeList;


    public static FgPlay getInstance(SlidingMenu menu)
    {
        FgPlay instance = new FgPlay();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (null == view)
        {
            view = inflater.inflate(R.layout.fg_play, container, false);
            this.TAG = "FgPlay";
            initData();
            ButterKnife.bind(this, view);
            initViews();
            initFunctionPanel();
            initPlayer();
            initPtzControl();
        }
        return view;
    }

    @Override
    public void onPause()
    {
        myPlayer.stopAll();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        cloudListener.setPtzLength(OptionInfo.getInstance().getPtzLength());
//        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        myPlayer.playAll();
//        myPlayer.onPageChange();
    }

    private void initData()
    {
        nodeList = new ArrayList<>();
    }

    private void initViews()
    {
        container = (RelativeLayout) view.findViewById(R.id.layout_functions);

        ibtnDevice.setOnClickListener(this);

        btnSnap.setOnClickListener(this);

        btnRecord.setOnClickListener(this);

        btnDivision.setOnClickListener(this);

        btnStream.setOnClickListener(this);

        btnReplay.setOnClickListener(this);

        btnShowptz.setOnClickListener(this);

        btnZoom.setOnClickListener(this);

//        cbtnSound.setOnCheckedChangeListener(this);

        cbtnSound.setOnClickListener(this);
    }


    PlayerPageAdapter adapter;

    /**
     * 初始化播放器
     */
    void initPlayer()
    {
        myPlayer.initPlayer(nodeList, 4);

        adapter = new PlayerPageAdapter(myPlayer.pages);

        myPlayer.setAdapter(adapter);

        myPlayer.setOnAddToPlayListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(context, AcChooseDevice.class), 999);
            }
        });

        myPlayer.addOnPageChangeListener(this);

        myPlayer.tvPage = indicator;

        myPlayer.ptzMenu = rlControlBtns;

        myPlayer.tvTitle = tvTitle;
    }


    /**
     * 底部功能按钮
     */
    private void initFunctionPanel()
    {
        inflater = LayoutInflater.from(context);

        LinearLayout layout_main = (LinearLayout) inflater.inflate(R.layout.function_layout_main, container, false);

        Button btn_talk = (Button) layout_main.findViewById(R.id.btn_talk);

        btn_talk.setOnTouchListener(new TalkListener());

        Button btn_addFavorite = (Button) layout_main.findViewById(R.id.btn_addfavorite);

        btn_addFavorite.setOnClickListener(this);

        cb_playlast = (CheckBox) layout_main.findViewById(R.id.btn_closeall);

//        cb_playlast.setOnCheckedChangeListener(this);

        cb_playlast.setOnClickListener(this);

        container.addView(layout_main);

        bottomMenu.setOnscrollListener(new MyHorizontalScrollView.OnScrollListener()
        {
            @Override
            public void scrollInStart()
            {
                L.e("scrollInStart");
                btnIndicatorRight.setVisibility(View.VISIBLE);
                btnIndicatorLeft.setVisibility(View.GONE);
            }

            @Override
            public void scrollInEnd()
            {
                L.e("scrollInEnd");
                btnIndicatorRight.setVisibility(View.GONE);
                btnIndicatorLeft.setVisibility(View.VISIBLE);
            }

            @Override
            public void inScroll()
            {
                L.e("inScroll");
                btnIndicatorRight.setVisibility(View.VISIBLE);
                btnIndicatorLeft.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 画面数切换对话框
     */
    private void showScreenModeDialog()
    {
        View popupView;

        if (null == screenModeDialog)
        {
            popupView = inflater.inflate(R.layout.layout_menu, null, false);

            screenModeDialog = new Dialog(getActivity(), R.style.myProgress);
            screenModeDialog.setContentView(popupView);

            Window dialogWindow = screenModeDialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams params = dialogWindow.getAttributes();
            params.x = 0;//btnDivision.getLeft();
            params.y = btnDivision.getHeight() * 2;
            dialogWindow.setAttributes(params);


            width = btnDivision.getWidth();
            height = popupView.getMeasuredHeight();//popupView.getHeight();

            ibtn_4 = (ImageButton) popupView.findViewById(R.id.ibtn_4);
            ibtn_9 = (ImageButton) popupView.findViewById(R.id.ibtn_9);
            ibtn_16 = (ImageButton) popupView.findViewById(R.id.ibtn_16);

            ibtn_4.setOnClickListener(this);
            ibtn_9.setOnClickListener(this);
            ibtn_16.setOnClickListener(this);
        }
        if (!screenModeDialog.isShowing())
        {
            screenModeDialog.show();
        }
        else
        {
            screenModeDialog.dismiss();
        }
    }

    private Dialog screenModeDialog, streamTypeDialog;

    RadioButton rdo_main, rdo_sub;

    /**
     * 码流切换对话框
     */
    private void showStreamTypeDialog()
    {
        if (null == streamTypeDialog)
        {
            View v = inflater.inflate(R.layout.layout_stream, null, false);

            streamTypeDialog = new Dialog(getActivity(), R.style.myProgress);
            streamTypeDialog.setContentView(v);

            Window dialogWindow = streamTypeDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            WindowManager.LayoutParams params = dialogWindow.getAttributes();
            params.x = 0;//btnDivision.getLeft();
            params.y = bottomMenu.getHeight();

            params.width = container.getWidth();
            params.height = container.getHeight();

            dialogWindow.setAttributes(params);


            rdo_main = (RadioButton) v.findViewById(R.id.rdo_main);
            rdo_sub = (RadioButton) v.findViewById(R.id.rdo_sub);
            rdo_main.setOnClickListener(this);
            rdo_sub.setOnClickListener(this);
        }
        if (!streamTypeDialog.isShowing())
        {
            if (myPlayer.getSelectedCanvas().getState() == VideoCanvas.PLAYING)
            {
                int stream = myPlayer.getSelectedCanvas().getMediaStreamType();
                rdo_main.setChecked(stream == 0);
                rdo_sub.setChecked(stream == 1);
                streamTypeDialog.show();
            }
        }
        else
        {
            streamTypeDialog.dismiss();
        }
    }

    /**
     * 云台控制按钮
     */
    private void initPtzControl()
    {
        cloudListener = new CloudControl(getActivity(), myPlayer);
        btnLeft.setOnTouchListener(cloudListener);
        btnRight.setOnTouchListener(cloudListener);
        btnUp.setOnTouchListener(cloudListener);
        btnDown.setOnTouchListener(cloudListener);
        btnLeftUp.setOnTouchListener(cloudListener);
        btnLeftDown.setOnTouchListener(cloudListener);
        btnRightUp.setOnTouchListener(cloudListener);
        btnRightDown.setOnTouchListener(cloudListener);
        btnApertureAdd.setOnTouchListener(cloudListener);
        btnApertureSub.setOnTouchListener(cloudListener);
        btnFocusAdd.setOnTouchListener(cloudListener);
        btnFocusreduce.setOnTouchListener(cloudListener);
    }

    @Override
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.ibtn_device:
                Intent it1 = new Intent(context, AcChooseDevice.class);
                it1.putExtra("isSelectMode", true);
                startActivityForResult(it1, 888);
                break;
            case R.id.btn_addfavorite:
//                Intent it = new Intent(context,AcAddFavorite.class);
//                PlayNode tempNode = player.getCurrentNode();
//                it.putExtra("node",tempNode);
//                startActivity(it);
                break;
            case R.id.btn_snap:
                myPlayer.snap();
                break;
            case R.id.btn_record:
                myPlayer.record();
                break;
            case R.id.btn_division:
                showScreenModeDialog();
                break;
            case R.id.ibtn_4:
                screenModeDialog.dismiss();
                myPlayer.resetDisplayMode(4);
                break;
            case R.id.ibtn_9:
                screenModeDialog.dismiss();
                myPlayer.resetDisplayMode(9);
                break;
            case R.id.ibtn_16:
                screenModeDialog.dismiss();
                myPlayer.resetDisplayMode(16);
                break;
            case R.id.btn_replay:
                myPlayer.replay();
                break;
            case R.id.btn_stream:
                showStreamTypeDialog();
                break;
            case R.id.btn_showptz:
                if (myPlayer.displayMode != 1)
                {
                    myPlayer.showPtzControl();
                }
                rlControlBtns.setVisibility(rlControlBtns.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_zoom:

                break;
            case R.id.cbtn_sound:
                myPlayer.setAudio();
                break;
            case R.id.btn_closeall:
                if (myPlayer.isAllClosed())
                {
                    myPlayer.playAll();
                    cb_playlast.setChecked(false);
                }
                else
                {
                    myPlayer.stopAll(true);
                    cb_playlast.setChecked(true);
                }
                break;
            case R.id.rdo_main:
                myPlayer.setMediaPlayType(0);
                streamTypeDialog.dismiss();
                break;
            case R.id.rdo_sub:
                myPlayer.setMediaPlayType(1);
                streamTypeDialog.dismiss();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
//            menu.setScrollEnabled(true);
//            player.setLand(false);
            titlebar.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
        }
        else
        {
//            menu.setScrollEnabled(false);
//            player.setLand(true);
            titlebar.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            bottomMenu.setVisibility(View.GONE);
        }
        int width = menu.getMeasuredWidth();
        int height = menu.getMeasuredHeight();
        L.e("menuWidth:" + width + " menuHeight:" + height);
    }

    class TalkListener implements OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            v.performClick();
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN)
            {
                myPlayer.setPPT(true);
            }
            else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
            {
                myPlayer.setPPT(false);
            }
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 999 && resultCode == Activity.RESULT_OK)
        {
            PlayNode node = (PlayNode) data.getSerializableExtra("node");
            myPlayer.initParam(node);
        }
        else if (requestCode == 888 && resultCode == Activity.RESULT_OK)
        {
//            player.initDevice(nodeList);
//            adapter.setNodeList(nodeList);
//            adapter.setDisplayMode(4);
//            adapter.notifyDataSetChanged();
//            adapter.PlayAll();
            myPlayer.setNodeList(nodeList);
            adapter = new PlayerPageAdapter(myPlayer.pages);
            myPlayer.setAdapter(adapter);
//            adapter.refresh(myPlayer.pages);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        myPlayer.onPageChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
}

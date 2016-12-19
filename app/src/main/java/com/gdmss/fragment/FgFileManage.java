

package com.gdmss.fragment;


import java.io.File;
import java.util.LinkedList;

import com.gdmss.R;
import com.gdmss.base.BaseFragment;
import com.utils.L;
import com.utils.Path;
import com.utils.T;
import com.utils.Utils;
import com.widget.SlidingMenu;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class FgFileManage extends BaseFragment implements OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener
{
    private CheckBox btn_select;

    private RelativeLayout rl_container;

    private ImageView singleImage;

    private FgLocalPhoto fgPhoto;

    private FgLocalVideo fgVideo;

    private Button btn_menu, btn_selectAll;

    private LinearLayout ll_bottom;

    private ImageButton ibtn_share, ibtn_delete;

    private RadioGroup radioGroup;

    public static LinkedList<File> file_photos, file_videos;

//    public static File[] file_photos;

//    public static File[] file_videos;

//    private RadioButton rdo_video, rdo_picture;

    public static FgFileManage getInstance(SlidingMenu menu)
    {
        FgFileManage instance = new FgFileManage();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        if (null == view)
        {
            view = inflater.inflate(R.layout.fg_filemanager,container,false);
            initViews();
        }
        if (null != view.getParent())
        {

        }
        return view;
    }

    @Override
    public void onResume()
    {
        file_photos = traversalFiles(Path.SNAPSHOT);
        file_videos = traversalFiles(Path.VIDEORECORD);
        super.onResume();
    }

    private void initViews()
    {
        btn_select = (CheckBox) view.findViewById(R.id.btn_select);

        rl_container = (RelativeLayout) view.findViewById(R.id.container);

        singleImage = (ImageView) view.findViewById(R.id.singleImage);

        btn_selectAll = (Button) view.findViewById(R.id.btn_selectAll);

        btn_menu = (Button) view.findViewById(R.id.btn_menu);

        btn_selectAll.setOnClickListener(this);

        btn_menu.setOnClickListener(this);

        btn_select.setOnCheckedChangeListener(this);

        ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);

        ibtn_share = (ImageButton) view.findViewById(R.id.ibtn_share);

        ibtn_delete = (ImageButton) view.findViewById(R.id.ibtn_delete);

        ibtn_share.setOnClickListener(this);

        ibtn_delete.setOnClickListener(this);

        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);

//        rdo_video = (RadioButton) view.findViewById(R.id.rdo_video);

//        rdo_picture = (RadioButton) view.findViewById(R.id.rdo_picture);

        radioGroup.setOnCheckedChangeListener(this);

        fgPhoto = FgLocalPhoto.getInstance(menu);//new FgLocalPhoto(menu);
        fgPhoto.singleImage = singleImage;
        fgVideo = FgLocalVideo.getInstance(menu);//new FgLocalVideo(menu);
        onCheckedChanged(radioGroup,R.id.rdo_video);
    }

    private LinkedList<File> traversalFiles(String path)
    {
        Utils.pathCompletion(path);
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        LinkedList<File> result = new LinkedList<>();
        if (null == files)
        {
            files = new File[0];
        }
        for (File f : files)
        {
            result.add(f);
        }
        L.e("fileSize:" + files.length);
        return result;
    }

    //    private boolean

    @Override
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.btn_selectAll:
                if (radioGroup.getCheckedRadioButtonId() == R.id.rdo_picture)
                {
                    fgPhoto.adapter.selectAll();
                }
                else
                {
                    fgVideo.adapter.selectAll();
                }
                break;
            case R.id.ibtn_share:
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setAction(Intent.ACTION_VIEW);
                break;
            case R.id.ibtn_delete:
                if (radioGroup.getCheckedRadioButtonId() == R.id.rdo_picture)
                {
                    fgPhoto.adapter.deleteSelectedFile();
                }
                else
                {
                    fgVideo.adapter.deleteSelectedFile();
                }
                break;
        }
    }

    public static final int DELETE_FINISH = -111;

    private Handler deleteHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (msg.what == -1)
            {
                progress.dismiss();
                return false;
            }
            if (msg.what == DELETE_FINISH)
            {
//                file_photos = traversalFiles(Path.SNAPSHOT);
//                fgPhoto.adapter.refresh(file_photos);
                T.showS(R.string.msg_delete_success);
//                progress.dismiss();
                btn_select.setChecked(false);
            }
            return false;
        }
    });

    @Override
    public void onCheckedChanged(CompoundButton compoundButton,boolean b)
    {
        if (radioGroup.getCheckedRadioButtonId() == R.id.rdo_picture)
        {
            if (null != fgPhoto.adapter)
            {
                fgPhoto.adapter.setSelectMode(b);
            }
        }
        else
        {
            if (null != fgVideo.adapter)
            {
                fgVideo.adapter.setSelectMode(b);
            }
        }
        btn_selectAll.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        btn_menu.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
        ll_bottom.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (checkedId)
        {
            case R.id.rdo_video:
                ft.replace(R.id.rl_container,fgVideo);
                break;
            case R.id.rdo_picture:
                ft.replace(R.id.rl_container,fgPhoto);
                break;
        }
        btn_select.setChecked(false);
        ft.commit();
    }
}

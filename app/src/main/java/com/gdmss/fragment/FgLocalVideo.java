package com.gdmss.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdmss.R;
import com.gdmss.adapter.DividerItemDecoration;
import com.gdmss.adapter.LocalVideoAdapter;
import com.gdmss.base.BaseFragment;
import com.widget.SlidingMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 本地视频
 */
public class FgLocalVideo extends BaseFragment
{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LocalVideoAdapter adapter;

    public static FgLocalVideo getInstance(SlidingMenu menu)
    {
        FgLocalVideo instance = new FgLocalVideo();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        if (null == view)
        {
            view = inflater.inflate(R.layout.fg_localvideo,container,false);
            ButterKnife.bind(this,view);
            initViews();
        }
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.setSelectMode(false);
        adapter.refresh(FgFileManage.file_videos);
    }

    void initViews()
    {
        adapter = new LocalVideoAdapter(getActivity(),FgFileManage.file_videos);

        GridLayoutManager manager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST));

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);
    }
}

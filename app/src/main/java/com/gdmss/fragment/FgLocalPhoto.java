

package com.gdmss.fragment;


import com.gdmss.R;
import com.gdmss.adapter.DividerItemDecoration;
import com.gdmss.adapter.LocalPhotoAdapter;
import com.gdmss.base.BaseFragment;
import com.widget.SlidingMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FgLocalPhoto extends BaseFragment implements View.OnClickListener
{
    private RecyclerView recyclerView;

    public LocalPhotoAdapter adapter;

    public ImageView singleImage;

    public static FgLocalPhoto getInstance(SlidingMenu menu)
    {
        FgLocalPhoto instance = new FgLocalPhoto();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        if (null == view)
        {
            view = inflater.inflate(R.layout.fg_localphoto,container,false);
            initRecyclerView();
            initViews();
        }
        return view;
    }

    void initRecyclerView()
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        adapter = new LocalPhotoAdapter(getActivity(),FgFileManage.file_photos);

        adapter.rightPadding = menu.mMenuWidth;

        adapter.img = singleImage;

        GridLayoutManager manager = new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false);

        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.HORIZONTAL_LIST));

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);
    }

    void initViews()
    {
    }

    @Override
    public void onResume()
    {
        singleImage.setVisibility(View.INVISIBLE);
        adapter.setSelectMode(false);
        adapter.refresh(FgFileManage.file_photos);
        super.onResume();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibtn_share:
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setAction(Intent.ACTION_VIEW);
                break;
            case R.id.ibtn_delete:

                break;
        }
    }
}
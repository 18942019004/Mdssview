

package com.gdmss.adapter;


import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.gdmss.activities.AcImageViewer;
import com.gdmss.R;
import com.gdmss.adapter.LocalPhotoAdapter.MyHolder;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.utils.L;
import com.utils.ScreenUtils;
import com.utils.T;


public class LocalPhotoAdapter extends RecyclerView.Adapter<MyHolder> //implements OnClickListener// AnimatorListener,
{
//    private File[] files;

    private LinkedList<File> files;

    private boolean[] selectedArray;

    private LayoutInflater inflater;

    private Context context;

    public ImageView img;

    public float xOffset;

    public float yOffset;

    private View currentItem;

    private boolean isSelectMode = false;


    public LocalPhotoAdapter(Context context,LinkedList<File> files)
    {
        this.inflater = LayoutInflater.from(context);
        this.files = files;
        this.selectedArray = new boolean[files.size()];
        this.context = context;
        width = ScreenUtils.getScreenWidth(context);
        height = ScreenUtils.getScreenHeight(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup container,final int paramInt)
    {
        final MyHolder holder = new MyHolder(inflater.inflate(R.layout.item_photo1,container,false));
        //        if (!isSelectMode)
        //        {
        //            holder.cb_select.setSelected(isSelectMode);
        //            holder.cb_select.setVisibility(View.INVISIBLE);
        //        }
        //        else
        //        {
        //            holder.cb_select.setVisibility(View.VISIBLE);
        //        }
        holder.img.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION)
                {
                    return;
                }
                if (pos >= getItemCount())
                {
                    return;
                }
                if (!isSelectMode)
                {
                    openImg(v,pos);
                }
                else
                {
                    selectedArray[pos] = !selectedArray[pos];
                    notifyItemChanged(pos);
                    //                    holder.cb_select.setSelected(!holder.cb_select.isSelected());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder view,final int position)
    {
        view.img.setDrawingCacheEnabled(true);
        //        view.img.setImageURI(Uri.parse(file_photos[position].getPath()));
//        view.img.setImageBitmap(BmpUtil.decodeBitmap(file_photos[position].getPath()));
//        Picasso.with(context).load(file_photos[position]).into(view.img);
        Glide.with(context).load(files.get(position)).animate(R.anim.anim_loadimg).into(view.img);
        //根据选择模式设置控件显隐性
        view.cb_select.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
        view.view_overlay.setVisibility(isSelectMode ? View.VISIBLE : View.INVISIBLE);

        //设置控件状态
        view.cb_select.setChecked(selectedArray[position]);
        view.view_overlay.setAlpha(selectedArray[position] ? 0.5f : 0.0f);
    }

    public static ExecutorService pool = Executors.newFixedThreadPool(3);

    @Override
    public int getItemCount()
    {
        return files.size();
    }

    class MyHolder extends ViewHolder
    {
        ImageView img;

        CheckBox cb_select;

        View container;//CardView

        View view_overlay;

        public MyHolder(View itemView)
        {
            super(itemView);
//            if (itemView instanceof CardView)
//            {
            container = itemView;//(CardView)
//            }
            img = (ImageView) itemView.findViewById(R.id.img);

            cb_select = (CheckBox) itemView.findViewById(R.id.cb_select);

            view_overlay = itemView.findViewById(R.id.v_overlay);
        }
    }

    public void selectAll()
    {
        for (int i = 0; i < selectedArray.length; i++)
            selectedArray[i] = true;
        notifyDataSetChanged();
    }

    public void deSelectAll()
    {
        selectedArray = new boolean[files.size()];
        notifyDataSetChanged();
    }


    private long duration = 1;

    public int rightPadding;

    private float width;

    private float height;

    void startScale(View holder,int position)
    {
        currentItem = holder;

        img.bringToFront();
        img.setImageURI(Uri.parse(files.get(position).getPath()));
        //img.setOnClickListener(this);

        int[] location = new int[2];
        holder.getLocationOnScreen(location);

        ViewHelper.setX(img,location[0]);
        ViewHelper.setY(img,location[1] - ScreenUtils.getStatusHeight(context));

        img.setScaleType(ScaleType.FIT_CENTER);


        float scaleX = (float) width / (float) img.getWidth();
        float scaleY = (float) height / (float) img.getHeight();

        float x = (width - holder.getWidth()) / 2;
        float y = (height - holder.getHeight()) / 2;
        ViewPropertyAnimator.animate(img).x(x).y(y).start();
        ViewPropertyAnimator.animate(img).scaleX(scaleX).scaleY(scaleY).setStartDelay(duration);
    }


    private void openImg(View v,int index)
    {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        ViewHelper.setX(img,location[0]);
        ViewHelper.setY(img,location[1] - ScreenUtils.getStatusHeight(context));

        img.setImageURI(Uri.parse(files.get(index).getPath()));

        img.setVisibility(View.VISIBLE);

        Intent it = new Intent(context,AcImageViewer.class);

        it.putExtra("index",index);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            context.startActivity(it,ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,v,"img").toBundle());
        }
        else
        {
            context.startActivity(it);
        }
        //        context.startActivity(it, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }

    public void setSelectMode(boolean selectMode)
    {
        isSelectMode = selectMode;
        selectedArray = new boolean[files.size()];
        notifyDataSetChanged();
    }

    public boolean isSelectMode()
    {
        return isSelectMode;
    }


    public void refresh(LinkedList<File> files)
    {
        this.files = files;
        selectedArray = new boolean[files.size()];
        notifyDataSetChanged();
    }

    private int getSelectedCount()
    {
        int count = 0;
        for (boolean b : selectedArray)
        {
            if (b)
            {
                count++;
            }
        }
        return count;
    }

    public void deleteSelectedFile()
    {
        int count = getSelectedCount();
        if (count <= 0)
        {
            T.showS(R.string.msg_please_select_file);
            return;
        }

        int deletedCount = 0;

        for (int i = 0; i < selectedArray.length; i++)
        {
            if (selectedArray[i])
            {
                L.e("before size:" + files.size());
                boolean success = files.get(i - deletedCount).delete();
                if (success)
                {
                    files.remove(i - deletedCount);
                    L.e("after size:" + files.size());
                    deleteHandler.sendEmptyMessage(i - deletedCount);
                    deletedCount++;
                }
            }
            if (i == selectedArray.length - 1)
            {
                deleteHandler.sendEmptyMessage(FINISH);
            }
        }
    }

    public final int FINISH = -111;

    private Handler deleteHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            if (message.what == FINISH)
            {
                selectedArray = new boolean[files.size()];
//                notifyDataSetChanged();
            }
            else
            {
                notifyItemRemoved(message.what);
            }
            return false;
        }
    });
}
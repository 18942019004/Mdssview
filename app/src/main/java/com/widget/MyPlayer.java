package com.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdmss.R;
import com.gdmss.adapter.PlayerPageAdapter;
import com.gdmss.entity.PlayNode;
import com.utils.L;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */

public class MyPlayer extends ViewPager
{
    Context context;

    public MyPlayer(Context context)
    {
        super(context);
        this.context = context;
    }

    public MyPlayer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    List<PlayNode> nodeList;

    public PlayPiece[] pages;

    VideoCanvas[] canvasArray;

    public RelativeLayout ptzMenu;

    public TextView tvPage;


    public int displayMode = 4;

    public void initPlayer(List<PlayNode> nodeList, int displayMode)
    {
        context = getContext();
        this.displayMode = displayMode;
        if (Utils.isEmpty(nodeList))
        {
            nodeList = new ArrayList<>();
            for (int i = 0; i < 16; i++)
            {
                nodeList.add(PlayNode.CreateSimplePlayNode(""));
            }
        }
        this.nodeList = nodeList;
        initPageInfo(nodeList);
        //初始化canvas个数
        initCanvas(nodeList);
        //初始化page数
        initPieces(pageSize);
    }

    int pageCount = 0;

    void initPageInfo(List<PlayNode> nodeList)
    {
        pageCount = getPageCount(nodeList.size());
        int pageNum = getPageCount(nodeList.size());
        //node不够显示
//        int offSet = displayMode * pageNum - nodeList.size();
//        while (offSet > 0)
//        {
//            nodeList.add(PlayNode.CreateSimplePlayNode(""));
//            --offSet;
//        }
    }

    private void initCanvas(List<PlayNode> nodeList)
    {
        if (null != canvasArray && null != nodeList)
        {
            if (canvasArray.length < displayMode * pageSize)
            {
                VideoCanvas[] tempArray = new VideoCanvas[displayMode * pageSize];
                for (int i = 0; i < canvasArray.length; i++)
                {
                    tempArray[i] = canvasArray[i];
                }
                canvasArray = tempArray;
            }
            for (int i = 0; i < canvasArray.length; i++)
            {
                if (null == canvasArray[i])
                {
                    canvasArray[i] = new VideoCanvas(context);
                }
                PlayNode tempNode = null;
                if (i < nodeList.size())
                {
                    tempNode = nodeList.get(i);
                }
                if (null == tempNode)
                {
                    tempNode = PlayNode.CreateSimplePlayNode("");
                }
                canvasArray[i].Prepare(tempNode.getName(), tempNode.getDeviceId(), true, "");
            }
        }
        else
        {
            canvasArray = new VideoCanvas[nodeList.size()];
            for (int i = 0; i < canvasArray.length; i++)
            {
                PlayNode tempNode = nodeList.get(i);
                VideoCanvas tempCanvas = new VideoCanvas(context);
                tempCanvas.Prepare(tempNode.getName(), tempNode.getDeviceId(), true, "");
                canvasArray[i] = tempCanvas;
            }
        }
    }

    private void initPieces(int pageSize)
    {
        if (null != pages)
        {
            for (int i = 0; i < pages.length; i++)
            {
                pages[i].removeAllViews();
            }
        }
        pages = new PlayPiece[pageSize];
        for (int i = 0; i < pages.length; i++)
        {
            PlayPiece tempPiece = new PlayPiece(context);
            pages[i] = tempPiece;
            tempPiece.setCanvas(Arrays.copyOfRange(canvasArray, i * displayMode, (i + 1) * displayMode));
            tempPiece.layoutCanvas();
        }
    }

    public void setNodeList(List<PlayNode> nodeList)
    {
        this.nodeList = nodeList;
        initPageInfo(nodeList);
        //初始化canvas个数
        initCanvas(nodeList);
        //初始化page数
        initPieces(pageSize);
    }

    int pageSize = 0;

    //根据传入的数组计算需要显示的页数
    private int getPageCount(int count)
    {
        pageSize = 1;
        if (count / displayMode > 0)
        {
            pageSize = count / displayMode;
            if (count % displayMode > 0)
            {
                pageSize = count / displayMode + 1;
            }
        }
        return pageSize;
    }

    int pageIndex = 0;

    int lastPageIndex = 0;

    /**
     * 滑动页面后进行的播放、停止操作
     */
    public void onPageChange(int currentItem)
    {
        refreshIndicator();
        pageIndex = currentItem;
        if (displayMode == 1)
        {
            canvasIndex = currentItem;
            selectCanvas();
            canvasArray[lastPageIndex].imgVideo.setScaleType(ImageView.ScaleType.FIT_XY);
            canvasArray[pageIndex].imgVideo.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        L.e("onPageChange------pageIndex:" + pageIndex + " lastPageIndex:" + lastPageIndex);
        if (lastPageIndex != pageIndex)
        {
            pages[lastPageIndex].stopAll();
            pages[pageIndex].PlayAll();
            lastPageIndex = pageIndex;
        }
        else
        {
            pages[pageIndex].PlayAll();
            lastPageIndex = pageIndex;
        }
    }

    public void resetDisplayMode(int displayMode)
    {
        if (displayMode != 1 && null != ptzMenu)
            ptzMenu.setVisibility(INVISIBLE);
        lastPageIndex = 0;
        this.displayMode = displayMode;
        getPageCount(nodeList.size());
        setNodeList(nodeList);
        setAdapter(new PlayerPageAdapter(pages));
//        ((PlayerPageAdapter) getAdapter()).refresh(pages);
        pages[getCurrentItem()].PlayAll();
        refreshIndicator();
    }

    public void playAll()
    {
        pages[pageIndex].PlayAll();
    }

    public void stopAll()
    {
        pages[pageIndex].stopAll();
    }

    public void stopAll(boolean cleanDisplay)
    {
        pages[pageIndex].stopAll(cleanDisplay);
        for (VideoCanvas canvas :
                canvasArray)
        {
            canvas.stop(true);
        }
    }

    int canvasIndex = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                pageIndex = getCurrentItem();
                int touchIndex = getIndex(ev);
                doubleClick(touchIndex);
//                if (displayMode != 1)
//                {
//                    this.canvasIndex = pageIndex * displayMode + touchIndex;
//                }
//                else
//                {
//                    this.canvasIndex = touchIndex;
//                }
                //设置选中画面高亮
//                setItemHighlight(touchIndex);
                selectCanvas();
                L.e("touchIndex:" + touchIndex + "  canvasIndex:" + canvasIndex);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                canvasArray[canvasIndex].imgVideo.setScaleType(ImageView.ScaleType.MATRIX);
                break;
        }
        canvasArray[canvasIndex].deal.set(canvasArray[canvasIndex].imgVideo, ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取当前点击画面
     *
     * @param event 手势
     * @return 画面下表，获取失败返回-1
     */
    public int getIndex(MotionEvent event)
    {
//        Rect canvasRec = new Rect();
        for (int i = 0; i < canvasArray.length; i++)
        {
//            canvasArray[i].getView().getGlobalVisibleRect(canvasRec);
//
//            if (canvasRec.contains((int) event.getRawX(),(int) event.getRawY()))
//            {
//                return i;
//            }
            if (canvasArray[i].contains(event))
            {
                L.e("displayMode:" + displayMode + " pageIndex:" + pageIndex + " touch:" + i);
                int index = displayMode == 1 ? 0 : i;
                return displayMode * pageIndex + index;
            }
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return super.onTouchEvent(ev);
    }

    long timemilis = 0;
    int lastDisplayMode = 1;

    void doubleClick(int touchIndex)
    {
        if (touchIndex == canvasIndex)
        {
            if (System.currentTimeMillis() - timemilis < 300)
            {
                L.e("双击------------");
                if (displayMode == 1)
                {
                    resetDisplayMode(lastDisplayMode);
                }
                else
                {
                    lastDisplayMode = displayMode;
                    resetDisplayMode(1);
                }
                int currentPage = canvasIndex / displayMode;
                L.e("page:" + currentPage);
                setCurrentItem(currentPage, false);
                timemilis = 0;
                return;
            }
        }
        timemilis = System.currentTimeMillis();
        canvasIndex = touchIndex;
    }

    private void setItemHighlight(int canvasIndex)
    {
        for (int i = 0; i < canvasArray.length; i++)
        {
            canvasArray[i].setHighLight(i == canvasIndex);
        }
    }

    public TextView tvTitle;

    private void setTitle()
    {
        if (null == tvTitle)
        {
            return;
        }
        if (nodeList.size() > canvasIndex && !nodeList.get(canvasIndex).getName().equals(""))
        {
            tvTitle.setText(nodeList.get(canvasIndex).getName());
        }
        else
        {
            tvTitle.setText(context.getString(R.string.title_livepreview));
        }
    }

    private void selectCanvas()
    {
        setTitle();
        setItemHighlight(canvasIndex);
    }

    public VideoCanvas getSelectedCanvas()
    {
        return canvasArray[canvasIndex];
    }

    public void snap()
    {
        getSelectedCanvas().snap();
    }

    public void record()
    {
        getSelectedCanvas().setVideo();
    }

    public void setMediaPlayType(int type)
    {
        getSelectedCanvas().setMeadiaPlayType(type);
    }

    public void setPPT(boolean isOpen)
    {
        getSelectedCanvas().setPPT(isOpen);
    }

    /**
     * 单个画面填充播放参数
     *
     * @param node 播放节点
     */
    public void initParam(PlayNode node)
    {
        nodeList.set(canvasIndex, node);
        canvasArray[canvasIndex].Prepare(node.getName(), node.getDeviceId(), true, "");
    }

    /**
     * 点击跳转监听
     *
     * @param listener 跳转监听
     */
    public void setOnAddToPlayListener(OnClickListener listener)
    {
        if (null == listener)
        {
            return;
        }
        for (int i = 0; i < canvasArray.length; i++)
        {
            canvasArray[i].btnChoose.setOnClickListener(listener);
        }
    }

    /**
     * 先前显示的所有画面重连
     */
    public void replay()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < displayMode; i++)
                {
                    canvasArray[pageIndex * displayMode + i].stop();
                    rePlayHan.sendEmptyMessage(i);
                }
            }
        }).start();
    }

    public boolean isAllClosed()
    {
        for (int i = 0; i < displayMode; i++)
        {
            if (canvasArray[pageIndex * displayMode + i].getState() == VideoCanvas.PLAYING)
            {
                return false;
            }
        }
        return true;
    }

    private Handler rePlayHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            int i = msg.what;
            canvasArray[pageIndex * displayMode + i].Play();
            canvasArray[pageIndex * displayMode + i].tvFps.setText("0fps | 0kbps");
            return false;
        }
    });

    private int audioIndex = -1;

    public void setAudio()
    {
        if (audioIndex == -1)
        {
            canvasArray[canvasIndex].setIsAudio(true);
            audioIndex = canvasIndex;
            return;
        }
        if (audioIndex == canvasIndex)
        {
            canvasArray[canvasIndex].setIsAudio(false);
            audioIndex = -1;
        }
        else
        {
            canvasArray[audioIndex].setIsAudio(false);
            canvasArray[canvasIndex].setIsAudio(true);
            audioIndex = canvasIndex;
        }
    }

    /**
     * 开启云台控制按钮
     */
    public void showPtzControl()
    {
//        if (null != ptzMenu)
//            if (ptzMenu.getVisibility() != VISIBLE)
//                ptzMenu.setVisibility(VISIBLE);
        lastDisplayMode = displayMode;
        resetDisplayMode(1);
        int currentPage = canvasIndex / displayMode;
        L.e("page:" + currentPage);
        setCurrentItem(currentPage, false);
    }

    /**
     * 云台控制
     *
     * @param command 云台指令
     * @param length  云台步长
     */
    public void setPTZ(int command, int length)
    {
        if (canvasArray[canvasIndex].isPlayed())
        {
            canvasArray[canvasIndex].player.SetPtz(command, length);
        }
    }

    public void refreshIndicator()
    {
        resetPageInfo();
        if (null != tvPage)
            tvPage.setText((pageIndex + 1) + "/" + pageSize);
    }

    public void resetPageInfo()
    {
        pageSize = getPageCount(canvasArray.length);
        pageIndex = getCurrentItem();
    }
}
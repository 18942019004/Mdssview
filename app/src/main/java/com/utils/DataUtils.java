

package com.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Player.Core.PlayerClient;
import com.Player.Source.TFileListNode;
import com.gdmss.base.APP;
import com.gdmss.entity.NodeType;
import com.gdmss.entity.PlayNode;


public class DataUtils
{
    /**
     * 获取服务器上面的设备列表
     *
     * @param playerclient
     * @param app
     *
     * @return
     */
    public static synchronized boolean getDataFromServer(PlayerClient playerclient,APP app)
    {
        boolean HaveData = false;
        if (playerclient != null)
        {
            List<PlayNode> tempNodeList = new ArrayList<PlayNode>();
            if (Utils.isEmpty(app.parentList))
            {
                app.parentList = new ArrayList<PlayNode>();
            }
            if (app.cameraMap == null)
            {
                app.cameraMap = new HashMap<>();
            }
            // 获取列表
            HaveData = GetDevList(playerclient,0,tempNodeList);
            sortList(app,tempNodeList);
        }
        return HaveData;
    }

    /**
     * 遍历获取节点 并添加到参数列表 NodeList 中去
     *
     * @param playerclient
     * @param dwQueryNodeId
     * @param NodeList
     *
     * @return
     */
    public static boolean GetDevList(PlayerClient playerclient,int dwQueryNodeId,List<PlayNode> NodeList)
    {
        int hDevList = playerclient.DevListQuery(dwQueryNodeId);
        if (hDevList != 0)
        {
            playerclient.DevListMoveFirst(hDevList);// 移动到子节点中的第一个节点
            while (true)
            {
                TFileListNode tmpFileListNode = playerclient.DevListGetNextNode(hDevList);// 移动到兄弟结点
                if (null == tmpFileListNode)
                {
                    break;
                }
                if (dwQueryNodeId == 0)
                {
                    tmpFileListNode.dwParentNodeId = 0;
                }

                if (tmpFileListNode.iNodeType == NodeType.DIRECTORY || tmpFileListNode.iNodeType == NodeType.DVR)
                {
                    System.out.println("目录名:" + tmpFileListNode.sNodeName + "  ID号" + tmpFileListNode.dwNodeId);
                    PlayNode node = new PlayNode(tmpFileListNode);
                    NodeList.add(node);

                    GetDevList(playerclient,tmpFileListNode.dwNodeId,NodeList); // 循环，移动到兄弟节点
                }
                else if (tmpFileListNode.iNodeType == NodeType.CAMERA)
                {
                    PlayNode node = new PlayNode(tmpFileListNode);
                    NodeList.add(node);
                }
            }
        }
        playerclient.DevListRelease(hDevList);
        return NodeList.size() > 0;
    }

    /**
     * 将从服务器获取到的节点列表分组，一组是根节点（设备，文件夹）,一组为子节点（镜头）
     *
     * @param nodelist 服务器获取到的节点列表
     */
    public synchronized static void sortList(APP app,List<PlayNode> nodelist)
    {
        List<PlayNode> parentList = app.parentList;
        Map<String, List<PlayNode>> cameraMap = app.cameraMap;
        Map<String, PlayNode> parentMap = app.parentMap;
        if (null == nodelist)
        {
            return;
        }

        parentList.clear();
        cameraMap.clear();
        parentMap.clear();

        int size = nodelist.size();
        // 将根节点分为一组
        for (int i = 0; i < size; i++)
        {
            PlayNode playNode = nodelist.get(i);
            // 根节点
            if (playNode.node.dwParentNodeId == 0 && playNode.IsDvr())//&& !playNode.IsDirectory())
            {
                // 根节点添加设备
                parentList.add(playNode);
                cameraMap.put(playNode.node.dwNodeId + "",new ArrayList<PlayNode>());
            }
            //
            parentMap.put(playNode.node.sDevId,playNode);
        }
        // 子节点根据key(父节点ID)分组
        for (int i = 0; i < size; i++)
        {
            PlayNode node = nodelist.get(i);
            if (node.node.dwParentNodeId != 0 && node.isCamera())
            {
                if (cameraMap.containsKey(node.node.dwParentNodeId + ""))
                {
                    cameraMap.get(node.node.dwParentNodeId + "").add(node);
                }
            }
        }

        for (int i = 0; i < parentList.size(); i++)
        {
            String key = parentList.get(i).getNodeId() + "";
            parentList.get(i).channels = cameraMap.get(key);
        }
    }

    public static void test()
    {
        LinkedList<String> list = new LinkedList<>();

    }


    public static PlayNode getCamera(int devID,APP app)
    {
        for (int i = 0; i < app.parentList.size(); i++)
        {
            PlayNode tempNode = app.parentList.get(i);
            L.e("dev:" + devID + "  node:" + tempNode.getNodeId());
            if (devID == tempNode.getNodeId())
            {
                return tempNode;
            }
        }
        return null;
    }

    public static List<PlayNode> getChannels(APP app,String devID)
    {
        List<PlayNode> result = app.cameraMap.get(devID);
        if (null != result)
        {
            return app.cameraMap.get(devID);
        }
        return new ArrayList<>();
    }

    public static PlayNode getNode(APP app,int nodeID,boolean isParent)
    {
        if (isParent)
        {
            for (int i = 0; i < app.parentList.size(); i++)
            {
                PlayNode tempNode = app.parentList.get(i);
                if (tempNode.getNodeId() == nodeID)
                {
                    return tempNode;
                }
            }
        }
        else
        {
            for (String key : app.cameraMap.keySet())
            {
                List<PlayNode> childList = app.cameraMap.get(key);
                for (int i = 0; i < childList.size(); i++)
                {
                    PlayNode tempNode = childList.get(i);
                    if (tempNode.getNodeId() == nodeID)
                    {
                        return tempNode;
                    }
                }
            }
        }
        return new PlayNode();
    }
}

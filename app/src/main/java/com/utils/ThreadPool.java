package com.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ThreadPool
{
    public static ExecutorService pool = Executors.newFixedThreadPool(3);

    public static ExecutorService getInstance()
    {
        if (null == pool)
        {
            pool = Executors.newFixedThreadPool(3);
        }
        return pool;
    }

    public static ExecutorService getInstance(int threadnum)
    {
        if (null == pool)
        {
            pool = Executors.newFixedThreadPool(threadnum);
        }
        return pool;
    }

    //关闭线程池
    public static void shutdown()
    {
        if (null == pool)
        {
            return;
        }
        if (!pool.isShutdown())
        {
            pool.shutdown();
        }
    }
}

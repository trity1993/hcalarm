package cc.trity.library.net;

import android.util.Log;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cc.trity.library.utils.LogUtils;

/**
 * 线程池 、缓冲队列
 * 将缓存队列交给线程池去执行
 * 注意需要设为静态
 * 现在使用ArrayBlockingQueue，以后可优化
 * Created by TryIT on 2016/2/4.
 */
public class DefaultThreadPool {
    private static final String TAG="DefaultThreadPool";
    // 阻塞队列最大任务数量
    static final int BLOCKING_QUEUE_SIZE = 20;
    static final int THREAD_POOL_MAX_SIZE = 10;

    static final int THREAD_POOL_SIZE = 6;
    /**
     * 缓冲BaseRequest任务队列
     */
    static ArrayBlockingQueue<Runnable> blockingQueue=new ArrayBlockingQueue<Runnable>
            (DefaultThreadPool.BLOCKING_QUEUE_SIZE);

    private static DefaultThreadPool instance=null;
    /**
     * 线程池，目前是十个线程
     */
    static AbstractExecutorService pool=new ThreadPoolExecutor(
            DefaultThreadPool.THREAD_POOL_SIZE,
            DefaultThreadPool.THREAD_POOL_MAX_SIZE,15L, TimeUnit.SECONDS,
            DefaultThreadPool.blockingQueue,
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public static DefaultThreadPool getInstance() {
        if (DefaultThreadPool.instance == null) {
            synchronized (DefaultThreadPool.class){
                DefaultThreadPool.instance = new DefaultThreadPool();
            }
        }
        return DefaultThreadPool.instance;
    }

    public static void removeAllTask(){
        DefaultThreadPool.blockingQueue.clear();
    }

    public static void removeTaskFromQueue(final Object obj){
        DefaultThreadPool.blockingQueue.remove(obj);
    }
    /**
     * 关闭，并等待任务执行完成，不接受新任务
     */
    public static void shutdown() {
        if (DefaultThreadPool.pool != null) {
            DefaultThreadPool.pool.shutdown();
        }
    }

    public static void shutdownRightnow(){
        if(DefaultThreadPool.pool!=null){
            DefaultThreadPool.pool.shutdownNow();
            try {
                // 设置超时极短，强制关闭所有任务
                DefaultThreadPool.pool.awaitTermination(1,
                        TimeUnit.MICROSECONDS);
            }catch (Exception e){
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    /**
     * 执行任务
     *
     * @param r
     */
    public void execute(final Runnable r) {
        if (r != null) {
            try {
                DefaultThreadPool.pool.execute(r);
            } catch (Exception e) {
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        }
    }
}

package cn.hclab.alarm.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.io.OutputStream;

import cc.trity.domain.rest.GetDataSource;

/**
 * Created by TryIT on 2015/6/12.
 */
public class NetGetTask<I,P,O> extends AsyncTask<I,P,O> {
    private ProgressBar progressBar = null;//用以显示进度
    private Context context=null;
    private DiskLruCache mDiskLruCache=null;
    private String path=null;
    private String pathUrl=null;
    private View view=null;
    /**
     *
     * @param progressBar 使用进度条进行更新进度
     * @param context 进行吐司操作
     * @param mDiskLruCache 进行硬盘缓存
     * @param pathUrl 表示不同的路径，形成不同的Url
     * @param path 表示不同的路径，形成不同的Url
     * @param view 后面进行强制转化，显示textview，还是imageview
     */
    public NetGetTask(ProgressBar progressBar, Context context, DiskLruCache mDiskLruCache, String pathUrl,String path, View view) {
        this.progressBar = progressBar;
        this.context=context;
        this.mDiskLruCache=mDiskLruCache;
        this.pathUrl=pathUrl;
        this.path=path;
        this.view =view;
    }
    //onPreExecute方法用于在执行后台任务前做一些UI操作
    @Override
    protected void onPreExecute() {
        if (progressBar != null)//显示进度条
            progressBar.setVisibility(View.VISIBLE);
    }
    //doInBackground方法内部执行后台任务,不可在此方法内修改UI
    @Override
    protected O doInBackground(I... params) {
        try {
            GetDataSource<I,O> getDataSource=new GetDataSource<I,O>(pathUrl);//

            return getDataSource.getData(params[0],path);
        } catch (Exception e) {
            Log.e("WeiBo ERROR", e.getMessage());
            return null;
        }
    }
    //onProgressUpdate方法用于更新进度信息
    @Override
    protected void onProgressUpdate(P... values) {
        if (progressBar != null)
            progressBar.setProgress((Integer) values[0]);
    }
    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
    @Override
    protected void onPostExecute(O outPut) {
        progressBar.setVisibility(View.GONE);
        if (outPut != null) {//防止因网络错误而返回空指针的问题
            //输出json信息，以后再进行转换
            if(TextView.class.isInstance(view)){
                ((TextView)view).setText(outPut.toString());
            }
            String strJson = outPut.toString();

            try {
                String key = Tools.hashKeyForDisk(System.currentTimeMillis()+"");//使用当前时间保持唯一性
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);//这里设置为0，是因为初始化mDiskLruCache时的open参数为valueCount=1。从0开始取
                    if (Tools.writeStream(strJson, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();//这个方法并不是每次写入都必须要调用的，但在这里却不可缺少
            } catch (IOException e) {
                Log.e("cacheResource", e.getMessage());
            }

        } else {
            Tools.ToastUtils(context, "网络状况不好，请求失败");
        }
    }

    //onCancelled方法用于在取消执行中的任务时更改UI
    @Override
    protected void onCancelled() {
        progressBar.setProgress(0);
    }
}

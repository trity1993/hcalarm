package cc.trity.library.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 文件操作类
 * Created by TryIT on 2016/1/11.
 */
public class FileUtils {
    private static final String TAG="FileUtils";
    private static final String ERROR_RESPONSE="";

    public static String getAssetsData(Context context,String fileName){
        if(context==null)
            return ERROR_RESPONSE;
        try( InputStream is=context.getAssets().open(fileName);
             BufferedReader bufReader=new BufferedReader(new InputStreamReader(is))) {
            String line=null;
            StringBuffer strBuf=new StringBuffer();
            while ((line=bufReader.readLine())!=null){
                strBuf.append(line);
            }

            return strBuf.toString();
        } catch (IOException e) {
           LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return ERROR_RESPONSE;
    }

    /**
     * 通过getAssets来获取资源
     * @param context
     * @param fileName assets文件夹中的文件名
     * @return XmlPullParser
     */
    public static XmlPullParser getAssetsXmlPullParser(Context context,String fileName){
        try {
//            String xmlData=getAssetsData(context, fileName);//不这么用，因为转成string再转成inputstream耗费资源
            InputStream inputStream=context.getAssets().open(fileName);
            if(inputStream!=null){
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser=factory.newPullParser();
                xmlPullParser.setInput(inputStream,"UTF-8");
                return xmlPullParser;
            }

        } catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public static XmlPullParser getXmlPullParser(String xmlData){
        try {
            if(xmlData!=null){
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser=factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                return xmlPullParser;
            }

        } catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public static String getFileInput(Context context,String fileName){
        if(context==null)
            return ERROR_RESPONSE;
        try {
            FileInputStream fis=context.openFileInput(fileName);
            FileChannel fileChannel=fis.getChannel();
            MappedByteBuffer mbbuf=fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            //使用gbk编码创建解码器
            Charset charset=Charset.forName("UTF-8");
            //创建解码器
            CharsetDecoder decoder=charset.newDecoder();

            //使用解码器将byteBuffer转成charBuffer
            CharBuffer charBuffer=decoder.decode(mbbuf);
            return charBuffer.toString();

        } catch (Exception e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return ERROR_RESPONSE;
    }

    public static boolean getFileOutput(Context context,String writeMsg,String fileName){
        if(context==null)
            return false;
        try {
            FileOutputStream fileOutputStream=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(writeMsg);
            writer.close();
            return true;
        }catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return false;
    }

    /**
     * 通过得到arrays.xml中arrayResure的子项
     * 返回对应的资源数组
     * @param context
     * @param arrayResure
     * @return
     */
    public static int[] getResourseArray(Context context,int arrayResure){
        TypedArray ar = context.getResources().obtainTypedArray(arrayResure);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);

        ar.recycle();

        return resIds;
    }

}

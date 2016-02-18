package cc.trity.library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 一些通用的方法
 * Created by TryIT on 2016/1/12.
 */
public final class CommonUtils {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resInt) {
        Toast.makeText(context, resInt, Toast.LENGTH_SHORT).show();
    }

}

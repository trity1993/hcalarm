package cn.hclab.alarm.ui.dialog.base;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import cn.hclab.alarm.R;


/**
 * A simple {@link android.app.Fragment} subclass.
 * 通过R.fraction.dialog_min_width_minor的比分比
 * 具有自适应宽度的对话框
 */
public class MinimumDialogFragment extends DialogFragment {


    public MinimumDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams lp =createLayoutParams(getDialog());
        getDialog().getWindow().setAttributes(lp);
    }
    public static WindowManager.LayoutParams createLayoutParams(Dialog dialog) {
        Activity context = dialog.getOwnerActivity();
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final boolean isPortrait = metrics.widthPixels < metrics.heightPixels;//测试横屏还是竖屏，选择对应的百分比

        float percent;
        if (isPortrait) {//选择对应的百分比
            percent = context.getResources().getFraction(R.fraction.dialog_min_width_minor, 1, 1);
        } else {
            percent = context.getResources().getFraction(R.fraction.dialog_min_width_major, 1, 1);
        }
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * percent);//宽度*百分比得到相对应的宽度

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return lp;
    }

}

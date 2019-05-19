package com.bobo.shoppingmall.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bobo.shoppingmall.R;

/**
 * Created by 求知自学网 on 2019/5/19. Copyright © Leon. All rights reserved.
 * Functions: 用来处理 （例如）HomeFragmnet 沉浸式导航栏的问题
 */
public class StBarUtil {

    public final static void setTransparentToolbar(Activity activity,View itemView){

        if(Build.VERSION.SDK_INT >= 21){

            //想要设置沉浸式状态栏的activity中都创建一个view 高度20dp 设置成自己想要的颜色
            View view = itemView.findViewById(R.id.Occupation);
            if (view != null){//避免空指针异常
                //动态的设置view的高度==状态栏的高度
                view.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
                params.height = getStatusBarHeight(activity);
                view.setLayoutParams(params);
            }
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            //低版本不适配沉浸式状态栏所以要隐藏
            View view = itemView.findViewById(R.id.Occupation);
            if (view != null){
                view.setVisibility(View.GONE);
            }
        }
    }

    public final static void setOccupationHeight(Activity activity,View itemView){

        if(Build.VERSION.SDK_INT >= 21){

            //想要设置沉浸式状态栏的activity中都创建一个view 高度20dp 设置成自己想要的颜色
            View view = itemView.findViewById(R.id.Occupation);
            if (view != null){//避免空指针异常
                //动态的设置view的高度==状态栏的高度
                view.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
                //Toast.makeText(activity,">"+getStatusBarHeight(activity),Toast.LENGTH_SHORT).show();
                if (getStatusBarHeight(activity) > 50){//发现在老手机上 状态栏最佳 50 是最佳高度
                    params.height = getStatusBarHeight(activity) - 50;
                }
                view.setLayoutParams(params);
            }
        }else {
            //低版本不用所以要隐藏
            View view = itemView.findViewById(R.id.Occupation);
            if (view != null){
                view.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获得状态栏的高度
     */
    private static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

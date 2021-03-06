package com.bobo.shoppingmall.app;

import android.app.Application;
import android.content.Context;


import com.bobo.shoppingmall.httpsUtils.OkHttpUtils;
import com.bobo.shoppingmall.utils.CacheUtils;
import com.bobo.shoppingmall.utils.UpdateUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by 求知自学网 on 2019/5/11. Copyright © Leon. All rights reserved.
 * Functions: 应用程序类 系统默认单例
 * 强制更新极光命令 ： leon_orders_mandatory_updates
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //配置 OkHttpUtils
        //initOkhttpClient();

        mContext = this;

        //初始化极光推送
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);

        //程序重新开始的时候允许校验更新状态
        CacheUtils.setBoolean(this, UpdateUtils.DOWNLOADING,false);
    }

    //配置 OkHttpUtils https://github.com/hongyangAndroid/okhttputils
    private void initOkhttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)//连接超时
                .readTimeout(10000L, TimeUnit.MILLISECONDS)//读取超时
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    //app被杀了，你监听application的onDestroy方法就行@Override
    public void onTerminate() {
        // 程序终止的时候执行
        //下载成功和失败 是否下载过都要变为false（用户结束程序进程也要改为false）
        CacheUtils.setBoolean(this, UpdateUtils.DOWNLOADING,false);
        super.onTerminate();
    }

    /**获取静态的全局上下文*/
    public static Context getContext() {
        return mContext;
    }

}

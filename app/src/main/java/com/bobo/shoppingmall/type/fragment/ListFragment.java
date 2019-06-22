package com.bobo.shoppingmall.type.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bobo.shoppingmall.R;
import com.bobo.shoppingmall.base.BaseFragment;
import com.bobo.shoppingmall.httpsUtils.OkHttpUtils;
import com.bobo.shoppingmall.httpsUtils.callback.StringCallback;
import com.bobo.shoppingmall.type.adapter.TypeLeftAdapter;
import com.bobo.shoppingmall.type.adapter.TypeRightAdapter;
import com.bobo.shoppingmall.type.bean.TypeBean;
import com.bobo.shoppingmall.utils.Constants;
import com.bobo.shoppingmall.base.BaseFragment;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 分类页面
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment {
    private FrameLayout fl_list_container;
    private ListView lv_left;
    private RecyclerView rv_right;
    private List<TypeBean.ResultBean> result;

    //接收用户切换了页面（fragment）的广播- 注意接收一定onDestroy() 关闭
    protected LocalBroadcastManager mLBM;

    //接收到切换了页面（fragment）广播的处理
    private BroadcastReceiver WantUpdateData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //再次请求网络
            getDataFromNet(urls[0]);
        }
    };

    private String[] urls = new String[]{Constants.SKIRT_URL, Constants.JACKET_URL, Constants.PANTS_URL, Constants.OVERCOAT_URL,
            Constants.ACCESSORY_URL, Constants.BAG_URL, Constants.DRESS_UP_URL, Constants.HOME_PRODUCTS_URL, Constants.STATIONERY_URL,
            Constants.DIGIT_URL, Constants.GAME_URL};
//
//

    private TypeLeftAdapter leftAdapter;
    private boolean isFirst = true;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_list, null);
        lv_left = (ListView) view.findViewById(R.id.lv_left);
        rv_right = (RecyclerView) view.findViewById(R.id.rv_right);

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(getActivity());

        //定义接收广播的方法
        mLBM.registerReceiver(WantUpdateData,new IntentFilter(Constants.UPDATE_TYPE_DATA));

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        //Leon增加在重新开始的时候起到一个刷新数据的效果、
        getDataFromNet(urls[0]);
    }


    @Override
    public void initData() {
        super.initData();
        //联网请求
        //getDataFromNet(urls[0]);
    }

    /**
     * 具体的联网请求代码
     * @param url
     */
    public void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {


        @Override
        public void onBefore(Request request, int id) {
        }

        @Override
        public void onAfter(int id) {
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Log.e("TAG", "联网失败" + e.getMessage());
            Toast.makeText(mContext, "请求失败请检查网络", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            //两位请求成功

            switch (id) {
                case 100:
//                    Toast.makeText(mContext, "http", Toast.LENGTH_SHORT).show();
                    if (response != null) {
                        //解析数据
                        processData(response);
                        if (isFirst) {
                            leftAdapter = new TypeLeftAdapter(mContext);
                            lv_left.setAdapter(leftAdapter);
                        }


                        initListener(leftAdapter);

                        //解析右边数据
                        TypeRightAdapter rightAdapter = new TypeRightAdapter(mContext, result);
                        rv_right.setAdapter(rightAdapter);

                        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);

                        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                if (position == 0) {
                                    return 3;
                                } else {
                                    return 1;
                                }
                            }
                        });
                        rv_right.setLayoutManager(manager);
                    }

                    break;
                case 101:
                    Toast.makeText(mContext, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    private void initListener(final TypeLeftAdapter adapter) {
        //点击监听
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelected(position);//刷新
                if (position != 0) {
                    isFirst = false;
                }
                getDataFromNet(urls[position]);
                leftAdapter.notifyDataSetChanged();
            }
        });

        //选中监听
        lv_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelected(position);//刷新

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void processData(String json) {
        Gson gson = new Gson();
        TypeBean typeBean = gson.fromJson(json, TypeBean.class);
        result = typeBean.getResult();
    }

    @Override
    public void onDestroy() {

        //注册的广播一定要关掉
        mLBM.unregisterReceiver(WantUpdateData);

        super.onDestroy();
    }
}
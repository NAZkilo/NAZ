package com.test.demo.lifenotebook1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.test.demo.lifenotebook1.R;
import com.test.demo.lifenotebook1.base.NoActionBarActivity;
import com.test.demo.lifenotebook1.bean.Event;
import com.test.demo.lifenotebook1.bean.Record;
import com.test.demo.lifenotebook1.utils.BigDecimalUtils;
import com.test.demo.lifenotebook1.utils.Constant;
import com.test.demo.lifenotebook1.utils.LifeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class DaySpendActivity extends NoActionBarActivity implements View.OnClickListener {

    private RecyclerView rv;
    private RelativeLayout back_rl;
    private List<Event> eList=new ArrayList<>();
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_spend);
        //找到控件
        initView();
        //初始化rv
        initRv();
        //初始化数据
        initData();
        //设置监听
        setListener();
    }

    /**
     * 找到控件
     */
    private void initView() {
        //返回
        back_rl = findViewById(R.id.common_bar_back);
        //标题
        title_tv = findViewById(R.id.common_bar_title);
        //列表rv
        rv = findViewById(R.id.day_spend_rv);
    }

    /**
     * 初始化rv
     */
    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter adapter=new CommonAdapter<Event>(this,R.layout.day_spend_item_layout,eList) {
            @Override
            protected void convert(ViewHolder holder, final Event event, final int position) {
                //收支类型对应logo
                ImageView image=holder.getView(R.id.day_spend_item_image);
                image.setImageResource(LifeUtils.getIeTypeImage(event.getIe_type()));
                //收支类型
                holder.setText(R.id.day_spend_item_ie_type,LifeUtils.getIeTypeName(event.getIe_type()));
                //金额
                holder.setText(R.id.day_spend_item_amount, BigDecimalUtils.round(event.getAmonut(),2));
                //备注
                TextView remark_tv=holder.getView(R.id.day_spend_item_remark);
                remark_tv.setSelected(true);
                remark_tv.setText(event.getRemark());

                if (position==eList.size()-1){
                    holder.getView(R.id.day_spend_item_view_1).setVisibility(View.INVISIBLE);
                }

                //点击
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),AddActivity.class);
                        intent.putExtra(Constant.INTENT_PASS,Constant.EDITOR_RECORD);
                        intent.putExtra(Constant.EVENT_PASS,event);
                        intent.putExtra(Constant.EVENT_INDEX,position);//索引
                        startActivity(intent);
                    }
                });
            }

        };
        rv.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        Record record= (Record) getIntent().getSerializableExtra(Constant.RECORD_PASS);
        if (null==record||record.getDate()==null||record.getArray_text()==null){
            return;//空判断
        }
        title_tv.setText(record.getDate());//设置标题为日期
        Gson gson=new Gson();
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        ArrayList<Event> eventList = gson.fromJson(record.getArray_text(), type);//拿到当前日期的事件记录集合
        eList.addAll(eventList);
        rv.getAdapter().notifyDataSetChanged();
    }

    /**
     * 监听
     */
    private void setListener() {
        back_rl.setOnClickListener(this);//返回

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_bar_back:{
                //返回
                finish();
            }break;
        }
    }
}

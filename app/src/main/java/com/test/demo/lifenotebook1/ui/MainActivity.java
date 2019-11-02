package com.test.demo.lifenotebook1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.demo.lifenotebook1.R;
import com.test.demo.lifenotebook1.base.NoActionBarActivity;
import com.test.demo.lifenotebook1.bean.Event;
import com.test.demo.lifenotebook1.bean.Record;
import com.test.demo.lifenotebook1.db.LifeOperate;
import com.test.demo.lifenotebook1.utils.BigDecimalUtils;
import com.test.demo.lifenotebook1.utils.Constant;
import com.test.demo.lifenotebook1.utils.LifeUtils;
import com.test.demo.lifenotebook1.utils.SystemUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NoActionBarActivity implements View.OnClickListener {

    private RecyclerView rv;
    private ImageView add_image;
    private List<Record> rList=new ArrayList<>();//数据源
    private TextView record_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到控件
        initView();
        //初始化rv
        initRv();
        //数据加载
        loadData();
        //设置监听
        setListener();
    }

    /*
     * 找到控件
     */
    private void initView() {
        //添加image
        add_image = findViewById(R.id.main_add);
        //记录总计数
        record_num = findViewById(R.id.main_record_num);
        //数据列表rv
        rv = findViewById(R.id.main_rv);
    }

    /*
     * 初始化rv
     */
    private void initRv(){
        //记账记录列表
        rv.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter adapter=new CommonAdapter<Record>(this,R.layout.main_item_layout,rList) {
            @Override
            protected void convert(ViewHolder holder, final Record record, int position) {
                //标题
                long time= SystemUtils.dateToLong(record.getDate(),new SimpleDateFormat("yyyy年MM月dd日"));
                String date=SystemUtils.getSimpleDateTime(time,new SimpleDateFormat("yyyy/MM/dd"));
                holder.setText(R.id.main_item_title,date);
                //星期
                holder.setText(R.id.main_item_week,SystemUtils.getWeek(record.getDate(),new SimpleDateFormat("yyyy年MM月dd日")));
                //合计
                Gson gson=new Gson();
                Type type = new TypeToken<ArrayList<Event>>() {}.getType();
                ArrayList<Event> eventList = gson.fromJson(record.getArray_text(), type);//转换成当前日期的事件记录集合
                int size=eventList.size();
                String income="0.00";
                String expand="0.00";
                for (int i=0;i<size;i++){
                    if (eventList.get(i).getIe_type()==0){
                        //收入
                        income= BigDecimalUtils.add(income,eventList.get(i).getAmonut(),2);//小数精确计算  保留两位小数
                    }else {
                        //支出
                        expand= BigDecimalUtils.add(expand,eventList.get(i).getAmonut(),2);//小数精确计算  保留两位小数
                    }
                }
                holder.setText(R.id.main_item_total,"收入"+income+"  支出"+expand);

                //item点击
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),DaySpendActivity.class);
                        intent.putExtra(Constant.RECORD_PASS,record);//传递当前点击item对应的Record数据
                        startActivity(intent);
                    }
                });
            }

        };
        rv.setAdapter(adapter);

    }

    /*
     * 加载数据
     */
    private void loadData() {
        //从数据库拿到记录集合
        LifeOperate lifeOperate=new LifeOperate(this);
        ArrayList<Record> dataList = lifeOperate.getDataList();
        record_num.setText(dataList.size()+"天记账记录");
        rList.addAll(LifeUtils.quickSort(dataList,false));//排序后的
        rv.getAdapter().notifyDataSetChanged();//刷新列表

    }


    /*
     * 监听
     */
    private void setListener() {
        add_image.setOnClickListener(this);//添加
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_add:{
                //添加
                Intent intent=new Intent(this,AddActivity.class);
                intent.putExtra(Constant.INTENT_PASS,Constant.ADD_RECORD);
                startActivityForResult(intent, Constant.REQUEST_CODE_ADD_RECORD);
            }break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Constant.REQUEST_CODE_ADD_RECORD){
            switch (resultCode){
                case Constant.RESULT_CODE_ADD_RECORD_NEW_ADD:{
                    //新增数据
                    Record record = (Record) data.getSerializableExtra(Constant.RECORD_NEW_ADD);
                    rList.add(record);
                    LifeUtils.quickSort(rList,false);//排序
                    rv.getAdapter().notifyDataSetChanged();
                    record_num.setText(rList.size()+"天记账记录");
                }break;
                case Constant.RESULT_CODE_ADD_RECORD_UPDATE:{
                    //更新数据
                    Record record = (Record) data.getSerializableExtra(Constant.RECORD_UPDATE);

                    int size=rList.size();
                    for (int i=0;i<size;i++){
                        if (rList.get(i).getSn()==record.getSn()){
                            rList.set(i,record);
                            rv.getAdapter().notifyItemChanged(i);//局部更新
                        }
                    }

                }break;
            }
        }
    }
}

package com.test.demo.lifenotebook1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.test.demo.lifenotebook1.R;
import com.test.demo.lifenotebook1.base.NoActionBarActivity;
import com.test.demo.lifenotebook1.bean.Event;
import com.test.demo.lifenotebook1.bean.IeTypeBean;
import com.test.demo.lifenotebook1.bean.Record;
import com.test.demo.lifenotebook1.db.LifeOperate;
import com.test.demo.lifenotebook1.utils.BigDecimalUtils;
import com.test.demo.lifenotebook1.utils.Constant;
import com.test.demo.lifenotebook1.utils.LifeUtils;
import com.test.demo.lifenotebook1.utils.SystemUtils;
import com.test.demo.lifenotebook1.widget.CashierInputFilter;
import com.test.demo.lifenotebook1.widget.CommonPopupWindow;
import com.test.demo.lifenotebook1.widget.CustomDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends NoActionBarActivity implements View.OnClickListener {

    private RelativeLayout back_rl;
    private Button save_btn;
    private RelativeLayout date_rl;
    private TextView date_tv;
    private RelativeLayout ie_type_rl;
    private TextView ie_type_tv;
    private EditText amount_edit;
    private EditText remark_edit;
    private TextView title_tv;
    private int purpose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //找到控件
        initView();
        //初始化
        init();
        //设置监听
        setListener();
    }

    /*
     * 找到控件
     */
    private void initView() {
        //返回
        back_rl = findViewById(R.id.common_bar_back);
        //标题
        title_tv = findViewById(R.id.common_bar_title);
        //保存
        save_btn = findViewById(R.id.common_bar_button);
        //日期选择rl
        date_rl = findViewById(R.id.add_date_rl);
        //日期tv
        date_tv = findViewById(R.id.add_date_tv);
        date_tv.setText(SystemUtils.getSimpleDateTime(System.currentTimeMillis(),new SimpleDateFormat("yyyy年MM月dd日")));//默认当天
        //收支类型选择rl
        ie_type_rl = findViewById(R.id.add_ie_type_rl);
        //收支类型tv
        ie_type_tv = findViewById(R.id.add_ie_type_tv);
        //金额edit
        amount_edit = findViewById(R.id.add_amount_edit);
        InputFilter[] filters={new CashierInputFilter()};//过滤器
        amount_edit.setFilters(filters);
        //备注edit
        remark_edit = findViewById(R.id.add_remark_edit);
    }

    /*
     * 初始化
     */
    private void init(){
        purpose = getIntent().getIntExtra(Constant.INTENT_PASS,-1);
        if (purpose ==Constant.ADD_RECORD){
            //添加记录
            save_btn.setText(R.string.save);
            title_tv.setText(R.string.add_record);
        }else if (purpose ==Constant.EDITOR_RECORD){
            //编辑记录
            Event event= (Event) getIntent().getSerializableExtra(Constant.EVENT_PASS);
            save_btn.setText(R.string.save);
            title_tv.setText(R.string.editor_record);
            date_rl.setEnabled(false);//编辑时日期不可更改
            if(event!=null){
                date_tv.setText(event.getDate());
                amount_edit.setText(event.getAmonut());
                ie_type_tv.setText(LifeUtils.getIeTypeName(event.getIe_type()));
                remark_edit.setText(event.getRemark());
            }
        }
    }

    /*
     * 判断当前页面是否已有输入
     */
    private boolean isHaveInput(){
        if (!amount_edit.getText().toString().replace(" ","").isEmpty()){
            return true;
        }
        if (!remark_edit.getText().toString().replace(" ","").isEmpty()){
            return true;
        }

        return false;

    }

    /*
     * 弹窗询问用户是否保存
     */
    private void showIsSave(){
        CustomDialog customDialog = new CustomDialog(AddActivity.this,R.style.MyDialog);
        customDialog.setTitle("有记录尚未保存");
        customDialog.setMessage("是否保存？");
        customDialog.setCancel("不了", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
                dialog.dismiss();
                finish();
            }
        });
        customDialog.setConfirm("保存", new CustomDialog.IOnConfirmListener(){
            @Override
            public void onConfirm(CustomDialog dialog) {
                //保存本条记录
                if (purpose==Constant.ADD_RECORD){
                    saveRecord();
                }
                if (purpose==Constant.EDITOR_RECORD){
                    editorRecord();
                }
                dialog.dismiss();

            }
        });
        customDialog.show();
    }

    /*
     * 选择收支类型
     */
    private void showSelectIeType(){
        //弹窗布局
        View contentView= LayoutInflater.from(this).inflate(R.layout.popup_list_select_layout,null);

        final CommonPopupWindow popupWindow=new CommonPopupWindow(this,contentView,R.id.popup_list_select_root);
        popupWindow.setAnimationStyle(R.style.mypopwindow_scale_anim_style);//设置动画为缩放动画

        //列表
        RecyclerView popRv=contentView.findViewById(R.id.popup_list_select_rv);
        popRv.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter popAdapter=new CommonAdapter<IeTypeBean>(this,R.layout.popup_list_select_item_layout, LifeUtils.getIeTypeList()) {
            @Override
            protected void convert(ViewHolder holder, final IeTypeBean ieType, int position) {
                TextView tv=holder.getView(R.id.popup_list_select_item_tv);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                params.width= SystemUtils.getScreenWidth()/3;// 1/3屏幕宽
                tv.setLayoutParams(params);
                tv.setText(ieType.getType_name());

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ie_type_tv.setText(ieType.getType_name());//设置选择参数名称
                        popupWindow.dismiss();
                    }
                });
            }
        };
        popRv.setAdapter(popAdapter);


        popupWindow.showAsDropDown(ie_type_tv,0,0);//显示
    }


    /*
     * 弹出日期选择器
     */
    private void showTimePicker(){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //设置
        startDate.set(2000,0,1);
        endDate.set(2050,11,31);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                long time = date.getTime();
                date_tv.setText(SystemUtils.getSimpleDateTime(time,new SimpleDateFormat("yyyy年MM月dd日")));//设置日期为选中日期
            }
        })
                .setType(new boolean[]{true, true, true,false,false,false})// (年月日时分秒)只显示年月日
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setSubCalSize(18)//取消 确定按钮文字大小
                .setTitleText("")//标题文字
                .setDividerType(WheelView.DividerType.FILL)//分割线类型
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(ContextCompat.getColor(AddActivity.this,R.color.color_333333))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(AddActivity.this,R.color.colorPrimary))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(AddActivity.this,R.color.color_333333))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(AddActivity.this,R.color.color_white))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(AddActivity.this,R.color.color_white))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("","","","","","")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setLineSpacingMultiplier(2.5f)//设置间距倍数
                .build();
        pvTime.show();

    }

    /*
     * 保存本条收支记录
     */
    private void saveRecord(){
        if (!amount_edit.getText().toString().replace(" ","").isEmpty()&&!ie_type_tv.getText().toString().isEmpty()){
            //将数据插入数据库
            //先查询数据库中是否存在当前日期的记录
            LifeOperate lifeOperate=new LifeOperate(this);

            String date=date_tv.getText().toString();//日期
            String ieTypeName=ie_type_tv.getText().toString();//收支类型
            String amount= BigDecimalUtils.round(amount_edit.getText().toString().replace(" ",""),2);//金额  两位小数 没有则+0 例: 9==》9.00
            String remark=remark_edit.getText().toString().isEmpty()?"":remark_edit.getText().toString();//备注
            Event event=new Event(LifeUtils.getIeType(ieTypeName),date,remark,amount);

            Record record=lifeOperate.getRecordByDate(date);//查询数据库获取到的记录
            if (record==null){
                //不存在  新创一个
                ArrayList<Event> arrayList=new ArrayList();
                arrayList.add(event);
                Gson gson=new Gson();
                String array_text=gson.toJson(arrayList);//将收支事件集合(集合中包含某一天所有的收支事件)转换为json字符串
                Record saveRecord=new Record((int)lifeOperate.allCaseNum(),date,array_text);
                Log.e("123456", "不存在saveRecord: "+ lifeOperate.allCaseNum());
                int id=lifeOperate.insert(saveRecord);//存入数据库
                if (id>0){
                    setResult(Constant.RESULT_CODE_ADD_RECORD_NEW_ADD,new Intent().putExtra(Constant.RECORD_NEW_ADD,saveRecord));
                }
            }else {
                //存在  把事件存入原来的数据中 并重新存入 更新数据
                Log.e("123456", "存在saveRecord: "+ lifeOperate.allCaseNum());
                String array_text = record.getArray_text();
                Gson gson=new Gson();
                Type type = new TypeToken<ArrayList<Event>>() {}.getType();
                ArrayList<Event> eventList = gson.fromJson(array_text, type);//拿到数据库中当前日期的事件记录集合
                eventList.add(event);//添加新的记录
                Log.e("123456", "saveRecord: "+gson.toJson(eventList) );
                record.setArray_text(gson.toJson(eventList));//将添加了新事件的的集合转换为json字符串设置给Record
                lifeOperate.update(record);//重新录入对应日期的Record，更新数据库
                setResult(Constant.RESULT_CODE_ADD_RECORD_UPDATE,new Intent().putExtra(Constant.RECORD_UPDATE,record));
            }
            ToastUtils.showShort(R.string.add_success);
            finish();
        }else {
            ToastUtils.showShort(R.string.ie_typr_and_amount_not_null);
        }
    }

    /*
     * 编辑本条收支记录
     */
    private void editorRecord(){
        if (!amount_edit.getText().toString().replace(" ","").isEmpty()&&!ie_type_tv.getText().toString().isEmpty()){
            String date=date_tv.getText().toString();//日期
            String ieTypeName=ie_type_tv.getText().toString();//收支类型
            String amount= BigDecimalUtils.round(amount_edit.getText().toString().replace(" ",""),2);//金额  两位小数 没有则+0 例: 9==》9.00
            String remark=remark_edit.getText().toString().isEmpty()?"":remark_edit.getText().toString();//备注
            Event event=new Event(LifeUtils.getIeType(ieTypeName),date,remark,amount);

            LifeOperate lifeOperate=new LifeOperate(this);
            Record record=lifeOperate.getRecordByDate(date);//查询数据库获取到的记录
            String array_text = record.getArray_text();
            Gson gson=new Gson();
            Type type = new TypeToken<ArrayList<Event>>() {}.getType();
            ArrayList<Event> eventList = gson.fromJson(array_text, type);//拿到数据库中当前日期的事件记录集合
            int index=getIntent().getIntExtra(Constant.EVENT_INDEX,-1);
            if (index!=-1){
                eventList.set(index,event);
                record.setArray_text(gson.toJson(eventList));
                lifeOperate.update(record);//重新录入对应日期的Record，更新数据库
            }
            finish();
        }else {
            ToastUtils.showShort(R.string.ie_typr_and_amount_not_null);
        }
    }

    /*
     * 监听
     */
    private void setListener() {
        back_rl.setOnClickListener(this);//返回
        save_btn.setOnClickListener(this);//保存
        ie_type_rl.setOnClickListener(this);//选择收支类型
        date_rl.setOnClickListener(this);//选择日期
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_bar_back:{
                //返回
                if (isHaveInput()){
                    //有输入 询问用户是否保存
                    showIsSave();
                }else {
                    finish();
                }
            }break;
            case R.id.common_bar_button:{
                if (purpose==Constant.ADD_RECORD){
                    //添加保存
                    saveRecord();
                }
                if (purpose==Constant.EDITOR_RECORD){
                    //编辑保存
                    editorRecord();
                }
            }break;
            case R.id.add_date_rl:{
                //选择日期
                showTimePicker();
            }break;
            case R.id.add_ie_type_rl:{
                //选择收支类型
                showSelectIeType();
            }break;
        }
    }
}

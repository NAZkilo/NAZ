package com.test.demo.lifenotebook1.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.test.demo.lifenotebook1.R;



public class CustomDialog extends Dialog implements View.OnClickListener {
    //声明xml文件里的组件
    private TextView tv_title,tv_message;
    private Button bt_cancel,bt_confirm;

    //声明xml文件中组件中的text变量，为string类，方便之后改
    private String title,message;
    private String cancel,confirm;

    //声明两个点击事件，等会一定要为取消和确定这两个按钮也点击事件
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;

    //设置四个组件的内容
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setCancel(String cancel, IOnCancelListener cancelListener) {
        this.cancel = cancel;
        this.cancelListener=cancelListener;
    }
    public void setConfirm(String confirm, IOnConfirmListener confirmListener){
        this.confirm=confirm;
        this.confirmListener=confirmListener;
    }

    //CustomDialog类的构造方法
    public CustomDialog(@NonNull Context context) {
        super(context);

    }

    /**
     * @param context
     * @param themeResId style样式 自定义
     */
    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    //在app上以对象的形式把xml里面的东西呈现出来的方法！
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了锁定app界面的东西是来自哪个xml文件
        setContentView(R.layout.my_alert_dialog_layout);
        //点击外部区域不可消失
        setCanceledOnTouchOutside(false);
        //设置弹窗的宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p =getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int)(size.x * 0.8);//是dialog的宽度为app界面的80%
        getWindow().setAttributes(p);

        //找到组件
        tv_title=findViewById(R.id.my_alert_dialog_title);
        tv_message=findViewById(R.id.my_alert_dialog_text);
        bt_cancel=findViewById(R.id.my_alert_dialog_left_button);
        bt_confirm=findViewById(R.id.my_alert_dialog_right_button);

        //设置组件对象的text参数
        if (!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(message)){
            tv_message.setText(message);
        }else {
            tv_message.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(cancel)){
            bt_cancel.setText(cancel);
        }
        if (!TextUtils.isEmpty(confirm)){
            bt_confirm.setText(confirm);
        }

        //为两个按钮添加点击事件
        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
    }

    //重写onClick方法
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_alert_dialog_left_button:
                if(cancelListener!=null){
                    cancelListener.onCancel(this);
                }
                break;
            case R.id.my_alert_dialog_right_button:
                if(confirmListener!=null){
                    confirmListener.onConfirm(this);
                }
                break;
        }
    }

    //写两个接口，当要创建一个CustomDialog对象的时候，必须要实现这两个接口
    //也就是说，当要弹出一个自定义dialog的时候，取消和确定这两个按钮的点击事件，一定要重写！
    public interface IOnCancelListener{
        void onCancel(CustomDialog dialog);
    }
    public interface IOnConfirmListener{
        void onConfirm(CustomDialog dialog);
    }
}
package com.test.demo.lifenotebook1.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.test.demo.lifenotebook1.R;




public class CommonPopupWindow extends PopupWindow {

    private View contentView;
    private int rootResId;

    public CommonPopupWindow(Context context, View contentView, int rootResId){
        super(context);
        this.contentView=contentView;
        this.rootResId=rootResId;
        setPopupWindow();
    }


    /*
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(contentView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);
        this.setAnimationStyle(R.style.mypopwindow_translation_anim_style);// 设置动画(平移)
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        contentView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int top = contentView.findViewById(rootResId).getTop();
                int bottom = contentView.findViewById(rootResId).getBottom();
                int left = contentView.findViewById(rootResId).getLeft();
                int right = contentView.findViewById(rootResId).getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top||y>bottom||x<left||x>right) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}

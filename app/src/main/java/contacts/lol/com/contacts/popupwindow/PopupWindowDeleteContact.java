package contacts.lol.com.contacts.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import contacts.lol.com.contacts.R;

/**
 * Created by MSI on 2016/4/20.
 */
public class PopupWindowDeleteContact extends PopupWindow {
    private Context mContext;

    private View view;

    private Button btn_take_photo, btn_pick_photo, btn_cancel;


    public PopupWindowDeleteContact(Context mContext) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.inflate_popoup_delete, null);
        final Button btn_confrim = (Button) view.findViewById(R.id.btn_confrim);
        final Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);



        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });


      // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.rl_delete_contact).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);



        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popup_delete);
        //添加pop窗口关闭事件

    }



}

package com.soapp.asoda.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soapp.asoda.Market;
import com.soapp.asoda.R;
import com.soapp.asoda.SelectMarketFinish;
import com.soapp.asoda.Util.SharedPreferencesFactory;
import com.soapp.asoda.Util.ToastUtil;

import java.util.List;


/**
 * Created by macpro001 on 30/5/15.
 */
public class MyDialogStyle {
    protected Dialog dialog;
    protected Context context;
    SelectMarketFinish selectMarketFinish;

    public void setSelectMarketFinish(SelectMarketFinish selectMarketFinish) {
        this.selectMarketFinish = selectMarketFinish;
    }

    public MyDialogStyle(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.MyDialogPop);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void reShowDialog() {
        if (!((Activity) context).isFinishing()) {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public void showDialog() {
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    public void showCheckboxDialog(List<Market> markets) {
        showCheckboxDialog(markets, context.getString(R.string.pleaseselectmarket), context.getString(R.string.ok), false);
    }

    public void showCheckboxDialog(List<Market> markets, boolean b) {
        showCheckboxDialog(markets, context.getString(R.string.pleaseselectmarket), context.getString(R.string.ok), b);
    }

    public void showCheckboxDialog(final List<Market> markets, CharSequence Content, String btntext1, boolean cancelable) {
        dialog.setCancelable(cancelable);
        View detail_layout = View.inflate(
                context,
                R.layout.dialog_one_button, null);
        dialog.setContentView(detail_layout);
        final TextView content = (TextView) detail_layout.findViewById(R.id.content);
        final LinearLayout checkLay = (LinearLayout) detail_layout.findViewById(R.id.checklay);
        content.setText(Content);
        addCheckBoxList(markets, checkLay);
        Button button1 = (Button) detail_layout.findViewById(R.id.button1);
        button1.setText(btntext1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasAtLeastOneChecked(checkLay)) {
                    dismissDialog();
                    saveShared(markets, checkLay);
                    if (selectMarketFinish != null)
                        selectMarketFinish.SelectMarketFinishListener();
                } else {
                    ToastUtil.show(context, context.getString(R.string.pleaseselectatleastonemarket));
                }
            }
        });
        showDialog();
    }

    private boolean hasAtLeastOneChecked(LinearLayout checkLay) {
        for (int i = 0; i < checkLay.getChildCount(); i++) {
            if (checkLay.getChildAt(i) instanceof CheckBox) {
                if (((CheckBox) checkLay.getChildAt(i)).isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void saveShared(List<Market> markets, LinearLayout checkLay) {
        for (int i = 0; i < checkLay.getChildCount(); i++) {
            if (checkLay.getChildAt(i) instanceof CheckBox) {
                SharedPreferencesFactory.saveBoolean(context, markets.get(i).getName(), ((CheckBox) checkLay.getChildAt(i)).isChecked());
                markets.get(i).setEnable(((CheckBox) checkLay.getChildAt(i)).isChecked());
            }
        }
    }

    private void addCheckBoxList(List<Market> markets, LinearLayout viewById) {

        for (int i = 0; i < markets.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(markets.get(i).getName());
            checkBox.setChecked(markets.get(i).isEnable());
            viewById.addView(checkBox);
        }
    }


    public void showTwoButtonDialog(CharSequence Content, String btntext1, String btntext2, View.OnClickListener listener1, View.OnClickListener listener2, boolean cancelable) {
        dialog.setCancelable(cancelable);
        View detail_layout = View.inflate(
                context,
                R.layout.dialog_two_button, null);
        dialog.setContentView(detail_layout);
        TextView content = (TextView) detail_layout.findViewById(R.id.content);
        content.setText(Content);
        Button button1 = (Button) detail_layout.findViewById(R.id.button1);
        Button button2 = (Button) detail_layout.findViewById(R.id.button2);
        button1.setText(btntext1);
        button2.setText(btntext2);
        button1.setOnClickListener(listener1);
        button2.setOnClickListener(listener2);
        showDialog();
    }
}

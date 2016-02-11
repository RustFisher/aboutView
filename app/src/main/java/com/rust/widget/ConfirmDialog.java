package com.rust.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rust.aboutview.R;

/**
 * Extend from AlertDialog not Dialog
 * In API 19, extend from Dialog would have a blank frame on the top
 * API 23 is OK with Dialog
 */
public class ConfirmDialog extends AlertDialog {

    private Context context;
    private String title;
    private String confirmButtonText;
    private String cancelButtonText;
    private OnClickListenerInterface onClickListenerInterface;

    public ConfirmDialog(Context context) {
        super(context);
        this.context = context;
        this.title = context.getResources().getString(R.string.delete_message);
        this.confirmButtonText = context.getResources().getString(R.string.yes);
        this.cancelButtonText = context.getResources().getString(R.string.cancel);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setConfirmButtonText(String confirmButtonText) {
        this.confirmButtonText = confirmButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public interface OnClickListenerInterface {

        void doConfirm();

        void doCancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_confirm_delete_data, null);
        setContentView(view);

        TextView titleView = (TextView) view.findViewById(R.id.dialog_message);
        TextView confirmButton = (TextView) view.findViewById(R.id.confirm_delete_btn);
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_delete_btn);

        if (TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        if (TextUtils.isEmpty(confirmButtonText)) {
            confirmButton.setText(confirmButtonText);
        }
        if (TextUtils.isEmpty(cancelButtonText)) {
            cancelButton.setText(cancelButtonText);
        }
        confirmButton.setOnClickListener(new clickListener());
        cancelButton.setOnClickListener(new clickListener());
    }

    public void setClickListener(OnClickListenerInterface clickListenerInterface) {
        this.onClickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.confirm_delete_btn: {
                    onClickListenerInterface.doConfirm();
                    break;
                }
                case R.id.cancel_delete_btn: {
                    onClickListenerInterface.doCancel();
                    break;
                }
            }
        }
    }
}

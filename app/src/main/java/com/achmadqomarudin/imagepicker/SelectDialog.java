package com.achmadqomarudin.imagepicker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SelectDialog extends Dialog implements OnClickListener, OnItemClickListener {
    private SelectDialogListener mListener;
    private Activity mActivity;
    private Button mMBtn_Cancel;
    private TextView mTv_Title;
    private List<String> mName;
    private String mTitle;
    private boolean mUseCustomColor = false;
    private int mFirstItemColor;
    private int mOtherItemColor;

    public interface SelectDialogListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     * Cancel event monitoring interface
     */
    private SelectDialogCancelListener mCancelListener;

    public interface SelectDialogCancelListener {
        public void onCancelClick(View v);
    }

    public SelectDialog(Activity activity, int theme,
                        SelectDialogListener listener, List<String> names) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        this.mName=names;

        setCanceledOnTouchOutside(true);
    }

    /**
     * @param activity Call the pop-up menu activity
     * @param theme theme
     * @param listener Menu item click event
     * @param cancelListener Cancel event
     * @param names Menu item name
     *
     */
    public SelectDialog(Activity activity, int theme, SelectDialogListener listener, SelectDialogCancelListener cancelListener , List<String> names) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        mCancelListener = cancelListener;
        this.mName=names;

        // Set whether to click the periphery to not disband
        setCanceledOnTouchOutside(false);
    }

    /**
     * @param activity Call the pop-up menu activity
     * @param theme theme
     * @param listener Menu item click event
     * @param names Menu item name
     * @param title Menu title text
     *
     */
    public SelectDialog(Activity activity, int theme, SelectDialogListener listener, List<String> names, String title) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        this.mName=names;
        mTitle = title;

        // Set whether to click the periphery to dissolve
        setCanceledOnTouchOutside(true);
    }

    public SelectDialog(Activity activity, int theme, SelectDialogListener listener, SelectDialogCancelListener cancelListener, List<String> names, String title) {
        super(activity, theme);
        mActivity = activity;
        mListener = listener;
        mCancelListener = cancelListener;
        this.mName=names;
        mTitle = title;

        // Set whether to click the periphery to dissolve
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.view_dialog_select,
                null);
        setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        // Set display animation
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        // The following two sentences are to ensure that the button can fill the screen horizontally
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // Set display location
        onWindowAttributesChanged(wl);

        initViews();
    }

    private void initViews() {
        DialogAdapter dialogAdapter=new DialogAdapter(mName);
        ListView dialogList= findViewById(R.id.dialog_list);
        dialogList.setOnItemClickListener(this);
        dialogList.setAdapter(dialogAdapter);
        mMBtn_Cancel = findViewById(R.id.mBtn_Cancel);
        mTv_Title = findViewById(R.id.mTv_Title);

        mMBtn_Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mCancelListener != null){
                    mCancelListener.onCancelClick(v);
                }
                dismiss();
            }
        });

        if(!TextUtils.isEmpty(mTitle) && mTv_Title != null){
            mTv_Title.setVisibility(View.VISIBLE);
            mTv_Title.setText(mTitle);
        }else{
            mTv_Title.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onItemClick(parent, view, position, id);
        dismiss();
    }
    private class DialogAdapter extends BaseAdapter {
        private List<String> mStrings;
        private Viewholder viewholder;
        private LayoutInflater layoutInflater;
        public DialogAdapter(List<String> strings) {
            this.mStrings = strings;
            this.layoutInflater=mActivity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                viewholder=new Viewholder();
                convertView=layoutInflater.inflate(R.layout.view_dialog_item, null);
                viewholder.dialogItemButton = convertView.findViewById(R.id.dialog_item_bt);
                convertView.setTag(viewholder);
            }else{
                viewholder=(Viewholder) convertView.getTag();
            }
            viewholder.dialogItemButton.setText(mStrings.get(position));
            if (!mUseCustomColor) {
                mFirstItemColor = mActivity.getResources().getColor(R.color.blue);
                mOtherItemColor = mActivity.getResources().getColor(R.color.blue);
            }
            if (1 == mStrings.size()) {
                viewholder.dialogItemButton.setTextColor(mFirstItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.dialog_item_bg_only);
            } else if (position == 0) {
                viewholder.dialogItemButton.setTextColor(mFirstItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_top);
            } else if (position == mStrings.size() - 1) {
                viewholder.dialogItemButton.setTextColor(mOtherItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_buttom);
            } else {
                viewholder.dialogItemButton.setTextColor(mOtherItemColor);
                viewholder.dialogItemButton.setBackgroundResource(R.drawable.select_dialog_item_bg_center);
            }
            return convertView;
        }

    }

    public static class Viewholder {
        public TextView dialogItemButton;
    }

    /**
     * Set the text color of list items
     */
    public void setItemColor(int firstItemColor, int otherItemColor) {
        mFirstItemColor = firstItemColor;
        mOtherItemColor = otherItemColor;
        mUseCustomColor = true;
    }
}

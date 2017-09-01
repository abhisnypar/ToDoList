package com.cerner.a2do.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cerner.a2do.R;

import java.util.ArrayList;

/**
 * Created by c15333 on 8/23/17.
 */

public class CustomListAdapter extends BaseAdapter {
    private Context mContext;
    private String listItemText;
    private ArrayList<String> arrayList;

    public CustomListAdapter(Context mContext, String listItemText, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.listItemText = listItemText;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderClass holder = null;
        View rootView = view;
        if (rootView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.list_item, viewGroup, false);
            holder = new HolderClass(rootView);
            rootView.setTag(holder);
        } else {
            holder = (HolderClass) rootView.getTag();
            if (holder.editText != null) {
                holder.editText.setText(listItemText);
            }
        }
        return rootView;
    }

    public class HolderClass extends RecyclerView.ViewHolder {
        private TextView editText;

        public HolderClass(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.list_item_textView);
        }
    }
}

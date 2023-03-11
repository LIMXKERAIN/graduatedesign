package com.example.graduatedesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TableAdapter extends BaseAdapter {

    private List<MyFile> list;

    private LayoutInflater inflater;

    public TableAdapter(Context context, List<MyFile> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MyFile myFile = (MyFile) this.getItem(i);
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_file_item, null);
            viewHolder.fileName = (TextView) view.findViewById(R.id.file_name);
            viewHolder.fileGenerateTime = (TextView) view.findViewById(R.id.file_generate_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.fileName.setText(myFile.getFileName());
        viewHolder.fileGenerateTime.setText(myFile.getFileGenerateTime());

        return view;
    }

    public static class ViewHolder {

        // 文件名
        public TextView fileName;

        // 文件生成时间
        public TextView fileGenerateTime;

    }
}

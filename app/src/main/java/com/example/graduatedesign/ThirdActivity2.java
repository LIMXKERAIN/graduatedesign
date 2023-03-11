package com.example.graduatedesign;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThirdActivity2 extends AppCompatActivity {

    private static final String LOG_TAG = "SHOW_IMAGE";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third2);

        TextView filePathLabel = this.findViewById(R.id.file_path_label);
        filePathLabel.setText("文件路径: ");
        TextView filePathText = this.findViewById(R.id.file_path_text);
        filePathText.setText("/DCIM/100MEDIA");

        List<MyFile> list = new ArrayList<>();

        ViewGroup tableTitle = this.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));

        File sdRoot = Environment.getExternalStorageDirectory();
        String filePath = sdRoot.getAbsolutePath() + "/DCIM/100MEDIA/";
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File fileItem : files) {
                list.add(new MyFile(fileItem.getName(), "2023-03-11 21:00:11"));
            }
        }

        ListView tableListView = this.findViewById(R.id.list_view);
        TableAdapter tableAdapter = new TableAdapter(this, list);
        tableListView.setOnItemClickListener((adapterView, view, i, l) -> {
            MyFile myFile = (MyFile) tableAdapter.getItem(i);
            String fileName = myFile.getFileName();
            // 图片的 src
            String fullPath = filePath + fileName;
            Log.i(LOG_TAG, "fullPath: " + fullPath);

            Context context = ThirdActivity2.this;
            dialog = new Dialog(context, R.style.dialog_style);
            dialog.setContentView(R.layout.layout_show_image);
            ImageView imageView = dialog.findViewById(R.id.image_view);
            // 设置要展示哪张图片
            Bitmap bitmap = BitmapFactory.decodeFile(fullPath);
            imageView.setImageBitmap(bitmap);
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            dialog.onWindowAttributesChanged(params);
            imageView.setOnClickListener(view1 -> dialog.cancel());
            dialog.show();
        });


        tableListView.setAdapter(tableAdapter);
    }
}

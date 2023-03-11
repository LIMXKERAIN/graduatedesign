package com.example.graduatedesign;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ThirdActivity1 extends AppCompatActivity {

    // 存储车的宽度，录入框可以录入小数，单位为米，最后存储乘以10转换成整数
    private int carWidth = 20;

    // 车的幅宽的一半，方便计算
    private int halfCarWidth = 15;

    // 存储长度
    private int areaLength = 100;

    // 存储宽度
    private int areaWidth =78;

    private ImageView imageView = null;

    private Canvas canvas;

    private Bitmap bitmap;

    // Logcat 日志统一前缀
    private static final String LOG_PREFIX = "LOG_TAG_MY_DRAW";

    // 保存按钮
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third1);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {
            canvas.save(); //保存
            canvas.restore(); // 存储

            // 使用当前时间生成文件名
            Date now = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String time = dateFormat.format(now);
            String fileName = "路径规划_" + time;
            String fileSuffix = ".jpg";
            File sdRoot = Environment.getExternalStorageDirectory();
            String savePath = sdRoot.getAbsolutePath() + "/DCIM/100MEDIA/" + fileName + fileSuffix;
            try {
                File file = new File(savePath);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(savePath));
                Toast.makeText(this, "文件保存成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "文件保存失败！", Toast.LENGTH_SHORT).show();
                Log.e(LOG_PREFIX, "文件保存失败");
                e.printStackTrace();
            }
        });

        // 根据 xml 布局文件中的 id 获取到确定按钮
        Button confirmButton = this.findViewById(R.id.confirm_button);
        // 给确定按钮添加点击事件，告诉程序点击这个按钮时要做什么事情
        confirmButton.setOnClickListener(view -> {

            // 获取车的幅宽所在的录入框
            EditText carWidthText = this.findViewById(R.id.car_width);
            // 获取车的宽度所在录入框中的值扩大10倍并转化为整形
            carWidth = (int) Double.parseDouble(carWidthText.getText().toString().trim());
            halfCarWidth = carWidth / 2;

            // 获取长度输入框
            EditText areaLengthText = this.findViewById(R.id.area_length);
            // 同车的幅宽
            areaLength = Integer.parseInt(areaLengthText.getText().toString().trim());
            // 获取宽度输入框
            EditText areaWidthText = this.findViewById(R.id.area_width);
            // 获取宽度输入框的值并转成整形，简单起见，假设输入的都是整数
            areaWidth = (int) Double.parseDouble(areaWidthText.getText().toString().trim());

            // 在 Logcat 中打印出获取到的值
            Log.i(LOG_PREFIX, "获取到输入的车的幅宽是: " + carWidth);
            Log.i(LOG_PREFIX, "获取到输入的地的长度是: " + areaLength);
            Log.i(LOG_PREFIX, "获取到输入的地的宽度是: " + areaWidth);

            // 获取画图的布局
            imageView = this.findViewById(R.id.canvas_view);

            List<Line> lineList = this.calculateRoute(carWidth, areaLength, areaWidth);
            bitmap = Bitmap.createBitmap(1080, 2400, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            this.myDraw(imageView, canvas, bitmap, lineList, areaLength, areaWidth);
        });

        // 点击保存时

    }

    /**
     * 单独写一个方法根据车宽、地长、地宽计算路线，计算出来的所有路线存到列表里返回
     *
     * @param carWidth   车的宽度
     * @param areaLength 地的长度
     * @param areaWidth  地的宽度
     * @return 返回存放计算出来的所有路线的列表
     */
    private List<Line> calculateRoute(int carWidth, int areaLength, int areaWidth) {
        // 存放结果的列表
        List<Line> routeList = new ArrayList<>();
        // 参数校验，如果车的宽度大于地的宽度，直接返回空列表，1条路线也不给
        if (areaWidth < carWidth) {
            Toast.makeText(this, "地的宽度必须大于车的宽度", Toast.LENGTH_SHORT).show();
            return routeList;
        }

        // 参数合法，则正常计算路线
        // count 是需要跑的趟数，如果地的宽度正好是车的宽度的整数倍时，直接相除，否则剩下的宽度还需要额外跑 1 趟
        int count = 0;
        if (areaWidth % carWidth == 0) {
            count = areaWidth / carWidth;
        } else {
            count = areaWidth / carWidth + 1;
        }

        // 循环计算路线
        // 路线起点的横坐标
        int startPointX;
        // 路线起点的纵坐标
        int startPointY;
        // 路线终点的横坐标
        int endPointX;
        // 路线终点的纵坐标
        int endPointY;

        for (int i = 1; i <= count; i++) {
            // 奇数趟，都是从左往右运动，起点横坐标都是0，终点横坐标等于地的长度，
            // 偶数趟，都是从右往左运动，起点的横坐标都等于地的长度，终点的横坐标都是0
            String direction;
            if (i % 2 != 0) {
                direction = "RIGHT";
                startPointX = 0;
                endPointX = areaLength;
            } else {
                direction = "LEFT";
                startPointX = areaLength;
                endPointX = 0;
            }

            // 根据趟数计算纵坐标，每条直线的起点和终点纵坐标都是一样的 (2n - 1) * halfCarWidth
            startPointY = (2 * i - 1) * halfCarWidth;
            endPointY = (2 * i - 1) * halfCarWidth;
            if (startPointY >= areaWidth) {
                startPointY = areaWidth - halfCarWidth;
            }
            if (endPointY >= areaWidth) {
                endPointY = areaWidth - halfCarWidth;
            }


            // 根据坐标生成起点和终点对象，存起来
            Point startPoint = new Point(startPointX, startPointY);
            Point endPoint = new Point(endPointX, endPointY);

            // 根据起点和终点生成直线
            Line line = new Line(startPoint, endPoint, direction);
            // 把直线存到列表里
            routeList.add(line);
        }

        // 返回所有的路线列表
        return routeList;
    }

    /**
     * 画出路线的方向
     *
     * @param x         箭头所在位置的横坐标
     * @param y         箭头所在位置的纵坐标
     * @param direction 箭头的方向
     */
    private List<Line> arrowLineList(int x, int y, String direction) {
        int x2;
        int y2;
        int x3;
        int y3;
        if ("LEFT".equals(direction)) {
            x2 = x + 5;
            x3 = x + 5;
        } else {
            x2 = x - 5;
            x3 = x - 5;
        }
        y2 = y + 5;
        y3 = y - 5;

        List<Line> lineList = new ArrayList<>(2);
        Point point1 = new Point(x, y);
        Point point2 = new Point(x2, y2);
        Point point3 = new Point(x3, y3);
        Line line1 = new Line(point1, point2, "");
        Line line2 = new Line(point1, point3, "");
        lineList.add(line1);
        lineList.add(line2);

        return lineList;
    }

    /**
     * 把画直线的功能写到一个独立函数中，避免重复代码
     *
     * @param imageView  画图的背景布局
     * @param lineList   路线的集合
     * @param areaLength 地的长度
     * @param areaWidth  地的宽度
     */
    private void myDraw(ImageView imageView, Canvas canvas, Bitmap bitmap, List<Line> lineList,
                        int areaLength, int areaWidth) {

        // 参数校验通过则正常绘图
        // 定义画布的宽和高
        canvas.drawColor(Color.GRAY);

        // 定义画笔
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        // 画的图是空心的
        paint.setStyle(Paint.Style.STROKE);
        // 文字居左对齐
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(32f);
        paint.setStrokeWidth(4);
        paint.setColor(Color.RED);

        canvas.drawRect(new Rect(0, 0, areaLength, areaWidth), paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        for (Line line : lineList) {
            canvas.drawLine(line.getStartPoint().getX(), line.getStartPoint().getY(),
                    line.getEndPoint().getX(), line.getEndPoint().getY(), paint);
            // 画箭头
            List<Line> arrowLineList;
            if ("RIGHT".equals(line.getDirection())) {
                arrowLineList = this.arrowLineList(line.getEndPoint().getX() / 2, line.getEndPoint().getY(), line.getDirection());
            } else {
                arrowLineList = this.arrowLineList(line.getStartPoint().getX() / 2, line.getStartPoint().getY(), line.getDirection());
            }
            for (Line line1 : arrowLineList) {
                canvas.drawLine(line1.getStartPoint().getX(), line1.getStartPoint().getY(),
                        line1.getEndPoint().getX(), line1.getEndPoint().getY(), paint);
            }
        }
        imageView.setImageBitmap(bitmap);
    }
}

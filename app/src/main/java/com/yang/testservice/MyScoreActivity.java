package com.yang.testservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yang.data.Data;
import com.yang.service.getUsePhoneTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by krm on 2017/6/8.
 */
public class MyScoreActivity extends AppCompatActivity {

    private Intent backIntent;
    public Button years,months,dayChar;
    private LineChartView lineChart;
    String[] date = {"6:00-9:00","9:00-12:00","12:00-15:00","15:00-18:00", "18:00-21:00","21:00-24:00", "24:00-3:00","3:00-6:00"};//X轴的标注
//    int[] score = {2,1,1,2,3,2,2,0};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    int[] timeDay = new int[8];
    int[] timeMonth = new int[6];
    int[] timeYear = new int[12];
    int j = 0;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscore);

        final Button dayChar = (Button) findViewById(R.id.dayChar);
        final Button monthChar = (Button) findViewById(R.id.monthChar);
        final Button yearChar = (Button) findViewById(R.id.yearChar);
        lineChart = (LineChartView) findViewById(R.id.line_chart);

        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbarplan);
        setSupportActionBar(mToolbarTb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**获取用户名*/
        final String userName = Data.getUserName();
        /**获取使用时长*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
                List<Map<String,Object>> listDay = new ArrayList<Map<String,Object>>();
                List<Map<String,Object>> listMonth = new ArrayList<Map<String,Object>>();
                List<Map<String,Object>> listYear = new ArrayList<Map<String,Object>>();

                getUsePhoneTime getUserPhoneTime = new getUsePhoneTime();
                lists = getUserPhoneTime.HttpPost(userName);
                JSONArray jsonArray = new JSONArray(lists);

                for(int i =0 ;i < 8;i++)
                {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        timeDay[i] = Integer.parseInt(jsonObject.getString("usephonetime"));
                        // 默认显示日统计
                        getAxisXLables(date);//获取x轴的标注
                        getAxisPoints(timeDay);//获取坐标点
                        initLineChart();//初始化
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                for(int i =8 ;i < 14;i++)
                {
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = jsonArray.getJSONObject(i);
                        timeMonth[j] = Integer.parseInt(jsonObject.getString("usephonetime"));
                        ++j;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                for(int i =14 ;i < 26;i++)
                {
                    JSONObject jsonObject = null;

                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        timeYear[k] = Integer.parseInt(jsonObject.getString("usephonetime"));
                        ++k;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();



        // 日统计
        dayChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lineChart.invalidate();
                dayChar.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                monthChar.setBackgroundColor(getResources().getColor(R.color.white));
                yearChar.setBackgroundColor(getResources().getColor(R.color.white));
                String[] dateOfDay = {"6:00-9:00","9:00-12:00","12:00-15:00","15:00-18:00", "18:00-21:00","21:00-24:00", "24:00-3:00","3:00-6:00"};//X轴的标注
//                int[] scoreOfDay = {2,1,1,2,1,2,2,0};//图表的数据点
                getAxisXLables(dateOfDay);//获取x轴的标注
                getAxisPoints(timeDay);//获取坐标点
                initLineChart();//初始化
            }
        });

        // 月统计
        monthChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthChar.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                dayChar.setBackgroundColor(getResources().getColor(R.color.white));
                yearChar.setBackgroundColor(getResources().getColor(R.color.white));
                String[] dateOfMonth = {"1-5","6-10","11-15","16-20", "21-25","26-30/31"};//X轴的标注
                int[] scoreOfMonth = {20,50,30,20,10,50};//图表的数据点
                getAxisXLables(dateOfMonth);//获取x轴的标注
                getAxisPoints(timeMonth);//获取坐标点
                initLineChart();//初始化
            }
        });

        // 年统计
        yearChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearChar.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                dayChar.setBackgroundColor(getResources().getColor(R.color.white));
                monthChar.setBackgroundColor(getResources().getColor(R.color.white));
                String[] dateOfYear = {"1","2","3","4", "5","6","7","8","9","10", "11","12"};//X轴的标注
//                int[] scoreOfYear = {80,80,100,860,120,110,80,80,100,860,120,110};//图表的数据点
                getAxisXLables(dateOfYear);//获取x轴的标注
                getAxisPoints(timeYear);//获取坐标点
                initLineChart();//初始化

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backIntent = new Intent(MyScoreActivity.this, SlidingMenuActivity.class);
                startActivity(backIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables(String[] date) {
        mAxisXValues.clear();
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(int[] score) {
        mPointValues.clear();
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#61aa82"));  //折线的颜色（绿色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("时间段");  //表格名称
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setTextColor(Color.BLACK);  //设置字体颜色
        axisY.setName("时间（小时）");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }
    public void click(View v) {
        int key = v.getId();
        switch (key) {

            case R.id.index1:
                // 首页
                Intent intent = new Intent(MyScoreActivity.this,SlidingMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.index2:
                // 我的勋章
                Intent myGainIntent = new Intent(MyScoreActivity.this, MyGain.class);
                startActivity(myGainIntent);
                break;
            case R.id.index3:
                // 我的成绩
               /* Intent myScoreIntent = new Intent(MyScoreActivity.this, MyScoreActivity.class);
                startActivity(myScoreIntent);*/
                break;
            case R.id.index4:
                // 视频教程
                Intent AboutOurIntent = new Intent(MyScoreActivity.this, AboutOurActivity.class);
                startActivity(AboutOurIntent);
                break;
            default:
                break;
        }
        }
}
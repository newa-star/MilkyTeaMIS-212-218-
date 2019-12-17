package com.example.milkyteamis.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.milkyteamis.AAChartCoreLib.AAChartConfiger.AAChartModel;
import com.example.milkyteamis.AAChartCoreLib.AAChartConfiger.AAChartView;
import com.example.milkyteamis.AAChartCoreLib.AAChartConfiger.AASeriesElement;
import com.example.milkyteamis.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.milkyteamis.AAChartCoreLib.AAOptionsModel.AADataLabels;
import com.example.milkyteamis.R;
import com.example.milkyteamis.bean.ResultBean_Bar;
import com.example.milkyteamis.bean.ResultBean_Linchart;
import com.example.milkyteamis.bean.ResultBean_PieData;
import com.example.milkyteamis.server.ServerAddress;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

public class ReportActivity extends BaseActivity{
    private int[] milkyTea;
    private int[] fruitTea;
    private int[] freshTea;
    private int[] cheeseTea;
    private int[] total;
    private int[] pie_data;
    private static final String[] years = {"2018","2019","2020","2021","2022"};
    private String selectedYear="2019";
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Button bt_report_update;
    private AAChartView aaChartView,aaChartView2,aaChartView3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        super.setToolbarAndTitle("统计报表",true);
        milkyTea = new int[12];
        fruitTea = new int[12];
        freshTea = new int[12];
        cheeseTea = new int[12];
        total = new int[12];
        pie_data = new int[4];
        bt_report_update = findViewById(R.id.bt_report_update);
        spinner = findViewById(R.id.spinner_report_year);
        adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,years);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        getLineChartData(selectedYear);
        getBarChartData(selectedYear);
       getPieData(selectedYear);
        initListener();
       }

    private void initListener(){
        bt_report_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLineChartData(selectedYear);
                getBarChartData(selectedYear);
                getPieData(selectedYear);
            }
        });
    }
    /**
     * 返回指定年份，四类商品的销量数据
     * @param year
     */
   private void getLineChartData(String year){
       RequestParams params = new RequestParams();
       JsonObject jsonObject = new JsonObject();
       final AAChartView aaChartView = findViewById(R.id.AAChartView);
       try{
           jsonObject.addProperty("createYear",year);
           Gson gson = new Gson();
           params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
           params.setContentType("application/json");
       }
       catch (Exception e){
           e.printStackTrace();
       }
       HttpUtils httpUtils = new HttpUtils();
       httpUtils.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.LINE_CHART, params, new RequestCallBack<String>() {
           @Override
           public void onSuccess(ResponseInfo<String> responseInfo) {
               Log.i("TAG","------"+responseInfo.result);
               Gson gson = new Gson();
               ResultBean_Linchart resultBean_linchart = gson.fromJson(responseInfo.result,ResultBean_Linchart.class);
               System.arraycopy(resultBean_linchart.getData().getMilkyTea(),0,milkyTea,0,12);
               System.arraycopy(resultBean_linchart.getData().getFruitTea(),0,fruitTea,0,12);
               System.arraycopy(resultBean_linchart.getData().getFreshTea(),0,freshTea,0,12);
               System.arraycopy(resultBean_linchart.getData().getCheeseTea(),0,cheeseTea,0,12);
               AAChartModel aaChartModel = new AAChartModel()
                       .chartType(AAChartType.Spline)
                       .title("4种类别商品的月销量统计")
                       .titleFontSize(20f)
                       //.subtitle("Virtual Data")
                       .backgroundColor("#FFFFFF")
                       .categories(new String[]{"一月","二月","三月","四月", "五月","六月","七月","八月","九月","十月","十一月","十二月"})
                       .dataLabelsEnabled(false)
                       .yAxisGridLineWidth(0f)
                       .yAxisTitle("数量/单位：杯")
                       .series(new AASeriesElement[]{
                               new AASeriesElement()
                                       .name("奶茶")
                                       .data(new Object[]{milkyTea[0],milkyTea[1],milkyTea[2],milkyTea[3],milkyTea[4],milkyTea[5],milkyTea[6],milkyTea[7],milkyTea[8],milkyTea[9],milkyTea[10],milkyTea[11]}),
                               new AASeriesElement()
                                       .name("果茶")
                                       .data(new Object[]{fruitTea[0],fruitTea[1],fruitTea[2],fruitTea[3],fruitTea[4],fruitTea[5],fruitTea[6],fruitTea[7],fruitTea[8],fruitTea[9],fruitTea[10],fruitTea[11]}),
                               new AASeriesElement()
                                       .name("鲜茶")
                                       .data(new Object[]{freshTea[0],freshTea[1],freshTea[2],freshTea[3],freshTea[4],freshTea[5],freshTea[6],freshTea[7],freshTea[8],freshTea[9],freshTea[10],freshTea[11]}),
                               new AASeriesElement()
                                       .name("芝士")
                                       .data(new Object[]{cheeseTea[0],cheeseTea[1],cheeseTea[2],cheeseTea[3],cheeseTea[4],cheeseTea[5],cheeseTea[6],cheeseTea[7],cheeseTea[8],cheeseTea[9],cheeseTea[10],cheeseTea[11]})
                       });
               aaChartView.aa_drawChartWithChartModel(aaChartModel);
           }

           @Override
           public void onFailure(HttpException e, String s) {
               Log.i("失败","-------"+s);
               Toast.makeText(ReportActivity.this,"网络错误，获取数据失败",Toast.LENGTH_SHORT).show();

           }
       });
   }

    /**
     * 返回指定年份奶茶店总销量数据
     * @param year 年份
     */
   private void getBarChartData(String year){
       RequestParams params = new RequestParams();
       JsonObject jsonObject = new JsonObject();
       final AAChartView aaChartView2 = findViewById(R.id.AAChartView2);
       try{
           jsonObject.addProperty("createYear",year);
           Gson gson = new Gson();
           params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
           params.setContentType("application/json");
       }
       catch (Exception e){
           e.printStackTrace();
       }
       HttpUtils httpUtils = new HttpUtils();
       httpUtils.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.BAR_CHART, params, new RequestCallBack<String>() {
           @Override
           public void onSuccess(ResponseInfo<String> responseInfo) {
               Log.i("TAG","--------"+responseInfo.result);
               Gson gson = new Gson();
               ResultBean_Bar resultBean_bar = gson.fromJson(responseInfo.result,ResultBean_Bar.class);
               total = resultBean_bar.getData();
               AAChartModel aaChartMode2 = new AAChartModel()
                       .chartType(AAChartType.Column)
                       .title("本店每个月总销量柱状图")
                       .titleFontSize(20f)
                       //.subtitle("Virtual Data")
                       .backgroundColor("#FFFFFF")
                       .yAxisTitle("数量/单位：杯")
                       .categories(new String[]{"一月","二月","三月","四月", "五月","六月","七月","八月","九月","十月","十一月","十二月"})
                       .dataLabelsEnabled(false)
                       .yAxisGridLineWidth(0f)
                       .series(new AASeriesElement[]{
                               new AASeriesElement()
                                       .name("月销量")
                                       .data(new Object[]{total[0],total[1],total[2],total[3],total[4],total[5],total[6],total[7],total[8],total[9],total[10],total[11]}),
                       });
               aaChartView2.aa_drawChartWithChartModel(aaChartMode2);
           }

           @Override
           public void onFailure(HttpException e, String s) {
               Log.i("失败","---------"+s);
               Toast.makeText(ReportActivity.this,"网络错误，获取数据失败",Toast.LENGTH_SHORT).show();
           }
       });
   }

   private void getPieData(String year){
       RequestParams params = new RequestParams();
       JsonObject jsonObject = new JsonObject();
       final AAChartView aaChartView3 = findViewById(R.id.AAChartView3);
       try{
           jsonObject.addProperty("createYear",year);
           Gson gson = new Gson();
           params.setBodyEntity(new StringEntity(gson.toJson(jsonObject),"UTF-8"));
           params.setContentType("application/json");
       }
       catch (Exception e){
           e.printStackTrace();
       }
       HttpUtils httpUtils = new HttpUtils();
       httpUtils.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.PIE_CHART, params, new RequestCallBack<String>() {
           @Override
           public void onSuccess(ResponseInfo<String> responseInfo) {
               Log.i("TAG","-------"+responseInfo.result);
               Gson gson = new Gson();
               ResultBean_PieData resultBean_pieData = gson.fromJson(responseInfo.result,ResultBean_PieData.class);
               pie_data = resultBean_pieData.getData();
               AAChartModel aaChartMode3 = new AAChartModel()
                       .chartType(AAChartType.Pie)
                       .title("奶茶、水果茶、鲜茶、芝士茶四类商品销售占比")
                       .titleFontSize(16f)
                       .legendEnabled(true)
                       .subtitle("红色—奶茶，黄色—水果茶，蓝色—鲜茶，绿色—芝士茶")
                       .subtitleFontColor("#FFA500")
                       .subtitleFontSize(12f)
                       .backgroundColor("#FFFFFF")
                       .categories(new String[]{"Java","Swift","Python","Ruby"})
                       .dataLabelsEnabled(true)
                       .yAxisGridLineWidth(0f)
                       .series(new AASeriesElement[]{
                               new AASeriesElement()
                                       .name("Tokyo")
                                       .data(new Object[]{pie_data[0],pie_data[1],pie_data[2],pie_data[3]})
                       });


               aaChartView3.aa_drawChartWithChartModel(aaChartMode3);           }

           @Override
           public void onFailure(HttpException e, String s) {
               Log.i("失败","---------"+s);
               Toast.makeText(ReportActivity.this,"网络错误，获取数据失败",Toast.LENGTH_SHORT).show();

           }
       });
   }

   class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
       @Override
       public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
           selectedYear = years[i];
       }

       @Override
       public void onNothingSelected(AdapterView<?> adapterView) {

       }
   }
}

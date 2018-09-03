package hmapp.hm.com.hmandroidapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.net.Uri;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;


import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hmapp.hm.com.hmandroidapp.OKhttp.ReqCallBack;
import hmapp.hm.com.hmandroidapp.OKhttp.RequestManager;
import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.model.MeterBoxInfo;
import hmapp.hm.com.hmandroidapp.model.MeterInfo;
import hmapp.hm.com.hmandroidapp.zxing.android.CaptureActivity;

public class ElectricityMeterBoxDateCollectionActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private EditText address;
    private String boxType;
    private ArrayAdapter<String> boxTypeAdapter;
    private Spinner boxTypeSpinner;
    long endTime;
    private static Uri imageUri;
    private GridView gridView;
    private int columnNum = 0;
    private int rowNum = 0;
    private Button preStepButton;
    private Button saveButton;

    private BaseAdapter adapter;
    private List<Map<String, Object>> dataList;
    //OkHttpClient okHttpClient = new OkHttpClient();
    String url = "http://47.97.6.36:9000/";//线上地址
    //String url = "http://10.98.4.62:9000/";//家本地地址

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        rowNum = Integer.parseInt(extras.getString("rowNum"));
        columnNum = Integer.parseInt(extras.getString("columnNum"));
        preStepButton = (Button) findViewById(R.id.meter_pre_step);
        saveButton = (Button) findViewById(R.id.ammeter_sub_data_collection_output);
        preStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(columnNum);
        adapter = new MyAdapter(this);
        gridView.setAdapter(adapter);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setEnabled(false);
                saveInfo(v);
                findInfo(v);
            }
        });



        /*dataList=new ArrayList<>();
        adapter=new SimpleAdapter(this,getData(),R.layout.gridlayout,new String[]{"image","text"},
                new int[]{R.id.image,R.id.text});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);*/
    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < rowNum * columnNum; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", R.drawable.launcher_icon);
            map.put("text", "表" + i);
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 在任何线程当中都可以调用弹出吐司方法
     *
     * @param result
     */
    private void showToastInAnyThread(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "我是" + position, Toast.LENGTH_SHORT).show();
        Log.i("tag", "我是" + position);
    }

    //自定义适配器
    class MyAdapter extends BaseAdapter {

        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        private int[] images = new int[rowNum * columnNum];

        //获取图片有多少个
        @Override
        public int getCount() {
            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < columnNum; j++) {
                    Toast.makeText(ElectricityMeterBoxDateCollectionActivity.this, "i=" + String.valueOf(i) + " j=" + String.valueOf(j) + "String.valueOf(i*columnNum+j=" + String.valueOf(i * columnNum + j), Toast.LENGTH_SHORT);
                    //每一张图片
                    images[i * columnNum + j] = R.drawable.launcher_icon;
                }
            }
            return rowNum * columnNum;
        }

        //每一个图片
        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //给每一个item填充图片
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grid_item, parent, false);
            ImageView iv = view.findViewById(R.id.itemImage);
            iv.setImageResource(images[position]);
            EditText et = view.findViewById(R.id.itemCode);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanCode(position);
                }
            });
            if(null != retCodes && !TextUtils.isEmpty(retCodes[position])){
                et.setText(retCodes[position]);
            }else {
                et.setText(null);
            }
            return view;
        }
    }


    private void scanCode(int position) {
        startActivityForResult(new Intent(ElectricityMeterBoxDateCollectionActivity.this,
                CaptureActivity.class), position);

    }

    String[] retCodes;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null == retCodes)
            retCodes = new String[adapter.getCount()];
        if(null != data){
            Log.e("s", "sssssssssssssssssss " + requestCode +
                    " " + resultCode + " " + data.getStringExtra("codedContent"));
            retCodes[requestCode] = data.getStringExtra("codedContent");
            adapter.notifyDataSetChanged();
        }
    }

    protected void onStop() {
        super.onStop();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 0x4) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    public static void main(String[] args) {
        int rowNum = 3;
        int columnNum = 4;
        int[] images1 = new int[rowNum * columnNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum; j++) {
                System.out.println("i=" + i + "j=" + j + "line=" + images1[i * columnNum + j]);
                images1[i * columnNum + j] = R.drawable.launcher_icon;
            }
        }
    }

    //带參数的Get请求
    public void findInfo(View view) {

        RequestManager instance = RequestManager.getInstance(getApplicationContext());
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = dateFormat.format(new Date());
        String endTime = dateFormat.format(new Date());
        paramsMap.put("type", "1");
        paramsMap.put("startTime", startTime);
        paramsMap.put("endTime", endTime);
        instance.requestAsyn("findDataByType", instance.TYPE_GET, paramsMap, new ReqCallBack<Object>() {

            @Override
            public void onReqSuccess(Object result) {
                System.out.println(222222222);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                System.out.println(666666666);
            }
        });

    }

    //带參数的Get请求
    public void saveInfo(View view) {
        RequestManager instance = RequestManager.getInstance(getApplicationContext());
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = dateFormat.format(new Date());
        String endTime = dateFormat.format(new Date());

        Intent intent = getIntent();
        //Integer rowNum = Integer.valueOf(intent.getStringExtra("rowNum"));
        Integer columnNum = Integer.valueOf(intent.getStringExtra("columnNum"));
        paramsMap.put("tgname", intent.getStringExtra("sstg"));
        paramsMap.put("meterBoxTgno", intent.getStringExtra("scanText"));
        paramsMap.put("assetNo", intent.getStringExtra("scanText"));
        paramsMap.put("installAddress", intent.getStringExtra("anzhuangdizhi"));
        paramsMap.put("detailAddress", intent.getStringExtra("accuracy_edit"));
        paramsMap.put("posX", intent.getStringExtra("longitude"));
        paramsMap.put("posY", intent.getStringExtra("latitude"));
        paramsMap.put("rowNum", intent.getStringExtra("rowNum"));
        paramsMap.put("colNum", intent.getStringExtra("columnNum"));
        paramsMap.put("collector", "");
        paramsMap.put("collDate", startTime);
        paramsMap.put("meterBoxStatusCode", "1");

        paramsMap.put("boxMeterRela", intent.getStringExtra("scanText"));
        //组装电能表数据为json字符串
        List<MeterInfo> meterInfoList = new ArrayList<MeterInfo>();
        if(retCodes!=null){
            for (int i=0;i<retCodes.length;i++){
                if(retCodes[i]!=null){
                    MeterInfo meterInfo = new MeterInfo();
                    meterInfo.setAssetNo(retCodes[i]);
                    meterInfo.setRowNo((i/columnNum+1));
                    meterInfo.setColNo((i%columnNum+1));
                    meterInfoList.add(meterInfo);
                }

            }
        }
        Gson json  = new Gson();
        String meters = json.toJson(meterInfoList);
        paramsMap.put("meterListsJsonStr", meters);//电能表数据集合
        instance.requestAsyn("saveAll", instance.TYPE_POST_FORM, paramsMap, new ReqCallBack<Object>() {

            @Override
            public void onReqSuccess(Object result) {
                System.out.println(222222222);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                System.out.println(666666666);
            }
        });

    }

    /**
     * 登录按钮的点击事件
     *
     * @param view
     */
    public void save(View view) {
        new Thread(new Runnable() {//创建子线程
            @Override
            public void run() {
                try {
                    String path = "http://47.97.6.36:9000/meter/find?id=1";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000);

                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 设置发送的数据为表单类型，会被添加到http body当中
                    String id = "1";
                    // String data = "?id=" + URLEncoder.encode(id, "utf-8") ;

                    //conn.setRequestProperty("Content-Length", String.valueOf(data.length()));

                    // post的请求是把数据以流的方式写给了服务器
                    // 指定请求输出模式
                    conn.setDoOutput(true);// 运行当前的应用程序给服务器写数据
                    //conn.getOutputStream().write(data.getBytes());

                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtils.readStream(is);

                        showToastInAnyThread(result);
                    } else {
                        showToastInAnyThread("请求失败");
                    }
                    /*//1,找水源--创建URL
                    URL url = new URL("https://www.baidu.com/");//放网站
                    //2,开水闸--openConnection
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //3，建管道--InputStream
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //4，建蓄水池蓄水-InputStreamReader
                    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                    //5，水桶盛水--BufferedReader
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    StringBuffer buffer = new StringBuffer();
                    String temp = null;

                    while ((temp = bufferedReader.readLine()) != null) {
                        //取水--如果不为空就一直取
                        buffer.append(temp);
                    }
                    bufferedReader.close();//记得关闭
                    reader.close();
                    inputStream.close();
                    Log.e("MAIN",buffer.toString());//打印结果*/

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();//启动子线程





        /*new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    String path = "http://127.0.0.1:9000/meter/find";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);

                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 设置发送的数据为表单类型，会被添加到http body当中
                    String id = "1";
                    String data = "username=" + URLEncoder.encode(id, "utf-8") ;

                    conn.setRequestProperty("Content-Length", String.valueOf(data.length()));

                    // post的请求是把数据以流的方式写给了服务器
                    // 指定请求输出模式
                    conn.setDoOutput(true);// 运行当前的应用程序给服务器写数据
                    conn.getOutputStream().write(data.getBytes());

                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtils.readStream(is);

                        showToastInAnyThread(result);
                    } else {
                        showToastInAnyThread("请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToastInAnyThread("请求失败");
                }
            }
        }.start();*/
    }

}




package hmapp.hm.com.hmandroidapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;


import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hmapp.hm.com.hmandroidapp.OKhttp.ReqCallBack;
import hmapp.hm.com.hmandroidapp.OKhttp.RequestManager;
import hmapp.hm.com.hmandroidapp.PermissionsActivity;
import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.model.MeterBoxInfo;
import hmapp.hm.com.hmandroidapp.model.MeterInfo;
import hmapp.hm.com.hmandroidapp.model.Tginfo;
import hmapp.hm.com.hmandroidapp.util.AMapUtil;
import hmapp.hm.com.hmandroidapp.util.SysApplication;
import hmapp.hm.com.hmandroidapp.util.Utils;


public class MainActivity extends PermissionsActivity
        implements AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener,
        View.OnClickListener,
        RouteSearch.OnRouteSearchListener{
    private Button scanBtn;
    private TextView resultTv;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private Button naviButton;
    private Button search;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView;
    private String[] strMsg;
    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private static LatLonPoint latLonPoint;
    String assetType;
    double posX;
    double posY;
    String navAddress;

    EditText assetNo;
    RouteSearch routeSearch;
    Button searchButton;
    MyLocationStyle myLocationStyle;
    private static final String[] m_Countries = { "计量箱资产号", "电能表资产号", "台区名称" }; //定义数组
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        initMainPage();
        textView = (TextView) findViewById(R.id.text_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        spinner = (Spinner) findViewById(R.id.selectcondition);
        assetNo = (EditText)findViewById(R.id.editText2);
        //将可选内容与ArrayAdapter连接，
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_Countries);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        //添加Spinner事件监听
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                assetType = m_Countries[arg2];
                //设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        Location();
        //导航按钮
        naviButton = (Button) findViewById(R.id.button6);
        naviButton.setOnClickListener(this);
        naviButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navAddress==null ){
                    Toast.makeText(MainActivity.this, "请先搜索位置后在导航", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("position",navAddress);
                    intent.putExtra("jingdu",posX);
                    intent.putExtra("weidu",posY);
                    intent.setClass(MainActivity.this,NaviActivity.class);
                    startActivity(intent);
                }

            }
        });

        //搜索按钮
        searchButton = (Button) findViewById(R.id.button3);
        searchButton.setOnClickListener(this);
        aMap = mapView.getMap();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAssetNoRemote();
            }
        });


        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                Location myLocation = aMap.getMyLocation();
                String s = myLocation.toString();

                //2、获取终点经纬度
                LatLng position = marker.getPosition();
                LatLonPoint from = new LatLonPoint(myLocation.getLatitude(),myLocation.getLongitude());
                Log.e("TAG",myLocation.getLatitude()+"    "+myLocation.getLongitude());
                LatLonPoint to = new LatLonPoint(position.latitude,position.longitude);
                RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(from,to);
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fat,RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST_AVOID_CONGESTION,null,null,"");
                routeSearch = new RouteSearch(MainActivity.this);
                routeSearch.setRouteSearchListener(MainActivity.this);
                routeSearch.calculateDriveRouteAsyn(query);
                return false;
            }
        };
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);

    }

    private void searchAssetNoRemote() {
        //1、根据资产类型从App服务器查询电表经纬度和地址("计量箱资产号", "电能表资产号", "台区名称" )
        //带參数的Get请求
        RequestManager instance = RequestManager.getInstance(getApplicationContext());
        HashMap<String, String> paramsMap = new HashMap<String, String>();
         if(assetType!=null){
             if ("计量箱资产号".equals(assetType)){
                 paramsMap.put("assetNo", assetNo.getText().toString());
                 instance.requestAsyn("meterbox/findMeterBoxByAssetNo", instance.TYPE_GET, paramsMap, new ReqCallBack<Object>() {
                     @Override
                     public void onReqSuccess(Object result) {
                         MeterBoxInfo meterBoxInfo = new MeterBoxInfo();
                         String resultStr = String.valueOf(result);
                         Gson gson = new Gson();
                         if(result!=null){
                             meterBoxInfo= (MeterBoxInfo) gson.fromJson(result.toString(),MeterBoxInfo.class);
                             if(meterBoxInfo!=null){
                                 navAddress = meterBoxInfo.getDetailAddress();
                                 posX = meterBoxInfo.getPosX();
                                 posY = meterBoxInfo.getPosY();
                                 //2、获取终点经纬度
                                 LatLng position = new LatLng(posX, posY);
                                 latLonPoint= new LatLonPoint(position.latitude,position.longitude);
                                 MarkerOptions markerOption = new MarkerOptions();
                                 markerOption.position(position);
                                 markerOption.title(navAddress);
                                 markerOption.draggable(true);//设置Marker可拖动
                                 markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                         .decodeResource(getResources(), R.mipmap.icon_gcoding)));
                                 // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                                 markerOption.setFlat(true);//设置marker平贴地图效果
                                 aMap.addMarker(markerOption);
                                 getAddress(latLonPoint);
                             }else{
                                 Toast.makeText(MainActivity.this, "没有搜索到该计量箱地理位置", Toast.LENGTH_LONG).show();

                             }

                         }else{
                             Toast.makeText(MainActivity.this, "没有搜索到该计量箱地理位置", Toast.LENGTH_LONG).show();

                         }

                     }

                     @Override
                     public void onReqFailed(String errorMsg) {
                         System.out.println("查询计量箱失败");
                         Toast.makeText(MainActivity.this, "没有搜索到该计量箱地理位置", Toast.LENGTH_LONG).show();

                     }
                 });
             }else if ("电能表资产号".equals(assetType)){
                 paramsMap.put("assetNo", assetNo.getText().toString());
                     instance.requestAsyn("meter/findByAssetNo", instance.TYPE_GET, paramsMap, new ReqCallBack<Object>() {
                     @Override
                     public void onReqSuccess(Object result) {
                         MeterInfo meterInfo = new MeterInfo();
                         String resultStr = String.valueOf(result);
                         Gson gson = new Gson();
                         if(result!=null){
                             meterInfo= (MeterInfo) gson.fromJson(result.toString(),MeterInfo.class);
                             if(meterInfo!=null){
                                 navAddress = meterInfo.getDetailAddress();
                                 posX = meterInfo.getPosX();
                                 posY = meterInfo.getPosY();
                                 //2、获取终点经纬度
                                 LatLng position = new LatLng(posX, posY);
                                 latLonPoint= new LatLonPoint(position.latitude,position.longitude);
                                 MarkerOptions markerOption = new MarkerOptions();
                                 markerOption.position(position);
                                 markerOption.title(navAddress);
                                 markerOption.draggable(true);//设置Marker可拖动
                                 markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                         .decodeResource(getResources(), R.mipmap.icon_gcoding)));
                                 // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                                 markerOption.setFlat(true);//设置marker平贴地图效果
                                 aMap.addMarker(markerOption);
                                 getAddress(latLonPoint);
                             }else{
                                 Toast.makeText(MainActivity.this, "没有搜索到该电能表地理位置", Toast.LENGTH_LONG).show();

                             }

                         }else{
                             Toast.makeText(MainActivity.this, "没有搜索到该电能表地理位置", Toast.LENGTH_LONG).show();

                         }

                     }

                     @Override
                     public void onReqFailed(String errorMsg) {
                         System.out.println("查询电能表失败");
                         Toast.makeText(MainActivity.this, "没有搜索到该电能表地理位置", Toast.LENGTH_LONG).show();

                     }
                 });
             }else if ("台区名称".equals(assetType)){
                 paramsMap.put("assetNo", assetNo.getText().toString());
                 instance.requestAsyn("tg/findTgByName", instance.TYPE_GET, paramsMap, new ReqCallBack<Object>() {
                     @Override
                     public void onReqSuccess(Object result) {
                         Tginfo tginfo = new Tginfo();
                         String resultStr = String.valueOf(result);
                         Gson gson = new Gson();
                         if(result!=null){
                             tginfo= (Tginfo) gson.fromJson(result.toString(),Tginfo.class);

                             /*navAddress = tginfo.getDetailAddress();
                             posX = tginfo.getPosX();
                             posY = tginfo.getPosY();*/
                         }
                         Toast.makeText(MainActivity.this, "没有搜索到该台区地理位置", Toast.LENGTH_LONG).show();

                     }

                     @Override
                     public void onReqFailed(String errorMsg) {
                         System.out.println("查询台区失败");
                         Toast.makeText(MainActivity.this, "没有搜索到该台区地理位置", Toast.LENGTH_LONG).show();

                     }
                 });
             }
         }


    }

    private void initMap() {

        geoMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_gcoding)))
                .title("电表")
                .setFlat(true));
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        getAddress(latLonPoint);
    }


    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    String result = "";
                    try {
                        AMapLocation loc = (AMapLocation) msg.obj;
                        result = Utils.getLocationStr(loc, 1);
                        strMsg = result.split(",");
                        Toast.makeText(MainActivity.this, "定位成功", Toast.LENGTH_LONG).show();
                        //textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[2]);
                        latLonPoint = new LatLonPoint(Double.valueOf(strMsg[2]), Double.valueOf(strMsg[1]));
                        initMap();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }

        ;

    };

    public void Location() {
        // TODO Auto-generated method stub
        try {
            locationClient = new AMapLocationClient(this);
            locationOption = new AMapLocationClientOption();
            // 设置定位模式为低功耗模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationOption.setOnceLocation(true);//设置为单次定位
            locationClient.setLocationOption(locationOption);// 设置定位参数
            // 启动定位
            locationClient.startLocation();
            mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

                Toast.makeText(MainActivity.this, result.getRegeocodeAddress().getFormatAddress()
                        + "附近", Toast.LENGTH_LONG).show();
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
            } else {

            }
        } else {
        }
    }


    private void initMainPage() {
        Button meterBoxButton = (Button) findViewById(R.id.ammeterBox_collection_data_button);
        //scanBtn = (Button) findViewById(R.id.scanBtn);
        //resultTv = (TextView) findViewById(R.id.resultTv);

        //scanBtn.setOnClickListener(this);

    }

    public void initMeterBox(View view) {
        Intent intent = new Intent();
        intent.setClass(this, meterboxdatacollection.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        //运行时权限
        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }else {
            startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),0);
        }

        switch (v.getId()) {
            case R.id.scanBtn:

                Intent intent = new Intent(MainActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);

                break;
        }*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                resultTv.setText("解码结果： \n" + content);

            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    
}

package hmapp.hm.com.hmandroidapp.activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import hmapp.hm.com.hmandroidapp.PermissionsActivity;
import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.util.AMapUtil;
import hmapp.hm.com.hmandroidapp.util.SysApplication;
import hmapp.hm.com.hmandroidapp.util.Utils;
import hmapp.hm.com.hmandroidapp.zxing.android.CaptureActivity;


public class MainActivity extends PermissionsActivity implements AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener,View.OnClickListener {
    private Button scanBtn;
    private TextView resultTv;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView;
    private String[] strMsg;
    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private static LatLonPoint latLonPoint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        initMainPage();
        textView = (TextView) findViewById(R.id.text_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        Location();
    }
    private void initMap(){

        if (aMap == null) {
            aMap = mapView.getMap();

            //用高德默认图标
            //geoMarker= aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //自定义图标
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.punch_dw))));
        }
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
                        textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[2]);
                        latLonPoint= new LatLonPoint(Double.valueOf(strMsg[2]), Double.valueOf(strMsg[1]));
                        initMap();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        };

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

                Toast.makeText(MainActivity.this,result.getRegeocodeAddress().getFormatAddress()
                        + "附近",Toast.LENGTH_LONG).show();
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
            } else {

            }
        } else {
        }
    }



    private void initMainPage() {
        Button meterBoxButton = (Button)findViewById(R.id.ammeterBox_collection_data_button);
        //scanBtn = (Button) findViewById(R.id.scanBtn);
        resultTv = (TextView) findViewById(R.id.resultTv);

        //scanBtn.setOnClickListener(this);

    }
    public void initMeterBox(View view){
        Intent intent = new Intent();
        intent.setClass(this,MeterBoxActivity.class);
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


}

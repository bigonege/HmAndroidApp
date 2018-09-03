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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

import hmapp.hm.com.hmandroidapp.PermissionsActivity;
import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.util.AMapUtil;
import hmapp.hm.com.hmandroidapp.util.MathUtil;
import hmapp.hm.com.hmandroidapp.util.Utils;
import hmapp.hm.com.hmandroidapp.util.windowsUtil;
import hmapp.hm.com.hmandroidapp.zxing.android.CaptureActivity;

public class meterboxdatacollection extends PermissionsActivity implements AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener,View.OnClickListener{

    private Button nextStepButton;
    private EditText meterDataCollectionRowNumEdit;
    private EditText meterDataCollectionColumnNumEdit;
    private Button scanBtn;
    private EditText scanText;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private Button callGpsBtn;
    private EditText longitude;
    private EditText sstg;

    private EditText latitude;

    private EditText accuracy_edit;
    private EditText anzhuangdizhi;
    //定位代码
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView;
    private String[] strMsg;
    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private static LatLonPoint latLonPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_meterboxdatacollection);
        nextStepButton = (Button)findViewById(R.id.caijiqi_ammeter_sub_data_collection_next_step);
        init();
        nextStepButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toNextStep();
            }
        });
        mapView = (MapView) findViewById(R.id.mapdw);
        //mapView.onCreate(savedInstanceState);// 此方法必须重写
        Location();
    }


    private void init() {
         meterDataCollectionRowNumEdit = (EditText) findViewById(R.id.meterBoxCollectionRowNum);
         meterDataCollectionColumnNumEdit = (EditText) findViewById(R.id.meterBoxCollectionColumnNum);
         scanBtn = (Button)findViewById(R.id.ammeter_data_collection_call_zxing);
         scanBtn.setOnClickListener(this);
        scanText = (EditText)findViewById(R.id.caijiqi_ammeterDateCollection_ScanCode_edit);//计量箱条码
        sstg = (EditText)findViewById(R.id.caijiqi_accourt_name_edit_2);//所属台区
        callGpsBtn = (Button)findViewById(R.id.ammeter_data_collection_call_gps);
        longitude = (EditText)findViewById(R.id.caijiqi_ammeter_sub_type_edit);//经度
        latitude = (EditText)findViewById(R.id.caijiqi_ammeter_scan_edit);//纬度
        accuracy_edit = (EditText)findViewById(R.id.caijiqi_detaildizhi_edit);//详细地址
        anzhuangdizhi = (EditText)findViewById(R.id.caijiqi_anzhuangdizhi_edit);//安装位置
        scanText.setInputType(InputType.TYPE_NULL);//禁止软键盘
        longitude.setInputType(InputType.TYPE_NULL);//禁止软键盘
        latitude.setInputType(InputType.TYPE_NULL);//禁止软键盘
        accuracy_edit.setInputType(InputType.TYPE_NULL);//禁止软键盘
        anzhuangdizhi.setInputType(InputType.TYPE_NULL);//禁止软键盘
        accuracy_edit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        accuracy_edit.setSingleLine(false);
        accuracy_edit.setHorizontallyScrolling(false);
    }

    public void clickDw(View v){
        Location();


    }

    private void toNextStep() {

        //数据校验
        //scanText = (EditText)findViewById(R.id.caijiqi_ammeterDateCollection_ScanCode_edit);//计量箱条码
        //sstg = (EditText)findViewById(R.id.caijiqi_accourt_name_edit_2);//所属台区
        //longitude = (EditText)findViewById(R.id.caijiqi_ammeter_sub_type_edit);//经度
        //latitude = (EditText)findViewById(R.id.caijiqi_ammeter_scan_edit);//纬度
        //accuracy_edit = (EditText)findViewById(R.id.caijiqi_detaildizhi_edit);//详细地址
        //anzhuangdizhi = (EditText)findViewById(R.id.caijiqi_anzhuangdizhi_edit);//安装位置
        if((scanText.getText().toString() == null) || (scanText.getText().toString().trim().equals(""))) {
            windowsUtil.alertInfo_ok("计量箱条码不能为空", this);
            return;
        }
        if((sstg.getText().toString() == null) || (sstg.getText().toString().trim().equals(""))) {
            windowsUtil.alertInfo_ok("所属不能为空", this);
            return;
        }
        if((anzhuangdizhi.getText().toString() == null) || (anzhuangdizhi.getText().toString().trim().equals(""))) {
            windowsUtil.alertInfo_ok("安装位置不能为空", this);
            return;
        }
        if((accuracy_edit.getText().toString() == null) || (accuracy_edit.getText().toString().trim().equals(""))) {
            windowsUtil.alertInfo_ok("详细地址不能为空", this);
            return;
        }
        if((longitude.getText().toString() == null) || (longitude.getText().toString().trim().equals(""))) {
            windowsUtil.alertInfo_ok("经度不能为空", this);
            return;
        }
        if((latitude.getText().toString() == null) || (latitude.getText().toString().trim().equals(""))) {
            windowsUtil.alertInfo_ok("纬度不能为空", this);
            return;
        }


        String RowNum = meterDataCollectionRowNumEdit.getText().toString();
        if((RowNum == null) || (RowNum.trim().equals(""))) {
            windowsUtil.alertInfo_ok("\u884c\u6570\u9700\u8981\u8f93\u5165\uff01", this);
            return;
        }
        if((!MathUtil.isInt(RowNum)) || (Integer.parseInt(RowNum) < 0)) {
            windowsUtil.alertInfo_ok("\u884c\u6570\u9700\u4e3a\u975e\u8d1f\u6574\u6570\uff01", this);
            return;
        }
        String ColumnNum = meterDataCollectionColumnNumEdit.getText().toString();
        if((ColumnNum == null) || (ColumnNum.trim().equals(""))) {
            windowsUtil.alertInfo_ok("\u5217\u6570\u9700\u8981\u8f93\u5165\uff01", this);
            return;
        }
        if((!MathUtil.isInt(ColumnNum)) || (Integer.parseInt(ColumnNum) < 0)) {
            windowsUtil.alertInfo_ok("\u5217\u6570\u9700\u4e3a\u975e\u8d1f\u6574\u6570\uff01", this);
            return;
        }

        if((RowNum.trim().equals("0")) || (ColumnNum.trim().equals("0"))) {
            windowsUtil.alertInfo_ok("行数或者列数为0",this);
            return;
        }
        if(Integer.valueOf(RowNum.trim())>10 ) {
            windowsUtil.alertInfo_ok("行数不能超过十行",this);
            return;
        }
        if(Integer.valueOf(ColumnNum.trim())>10 ) {
            windowsUtil.alertInfo_ok("列数不能超过十行",this);
            return;
        }

        Intent intent = new Intent();

        intent.putExtra("scanText",scanText.getText().toString());
        intent.putExtra("sstg",sstg.getText().toString());
        intent.putExtra("anzhuangdizhi",anzhuangdizhi.getText().toString());
        intent.putExtra("accuracy_edit",accuracy_edit.getText().toString());
        intent.putExtra("longitude",longitude.getText().toString());
        intent.putExtra("latitude",latitude.getText().toString());
        intent.putExtra("rowNum",RowNum);
        intent.putExtra("columnNum",ColumnNum);
        intent.setClass(meterboxdatacollection.this,ElectricityMeterBoxDateCollectionActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        //运行时权限
        if (ContextCompat.checkSelfPermission(meterboxdatacollection.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(meterboxdatacollection.this,new String[]{Manifest.permission.CAMERA},1);
        }else {
            startActivityForResult(new Intent(meterboxdatacollection.this, CaptureActivity.class),0);
        }

        /*switch (v.getId()) {
            case R.id.scanBtn:

                Intent intent = new Intent(meterboxdatacollection.this,
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
                scanText.setText(content);

            }
        }
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
public void onLocationChanged(AMapLocation amapLocation) {
    if (amapLocation != null) {
        if (amapLocation.getErrorCode() == 0) {
            //可在其中解析amapLocation获取相应内容。
            longitude.setText(String.valueOf(amapLocation.getLongitude()));
            latitude.setText(String.valueOf(amapLocation.getLatitude()));
            accuracy_edit.setText(amapLocation.getAddress());
            anzhuangdizhi.setText(amapLocation.getProvince()+amapLocation.getCity());

        }else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError","location Error, ErrCode:"
                    + amapLocation.getErrorCode() + ", errInfo:"
                    + amapLocation.getErrorInfo());
        }
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
                        Toast.makeText(meterboxdatacollection.this, "定位成功", Toast.LENGTH_LONG).show();
                        textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[2]);
                        latLonPoint= new LatLonPoint(Double.valueOf(strMsg[2]), Double.valueOf(strMsg[1]));
                        initMap();
                    } catch (Exception e) {
                    Toast.makeText(meterboxdatacollection.this, "定位失败", Toast.LENGTH_LONG).show();
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
        Toast.makeText(meterboxdatacollection.this, "定位失败", Toast.LENGTH_LONG).show();
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

        Toast.makeText(meterboxdatacollection.this,result.getRegeocodeAddress().getFormatAddress()
        + "附近",Toast.LENGTH_LONG).show();
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
        AMapUtil.convertToLatLng(latLonPoint), 15));
        geoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
        } else {

        }
        } else {
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

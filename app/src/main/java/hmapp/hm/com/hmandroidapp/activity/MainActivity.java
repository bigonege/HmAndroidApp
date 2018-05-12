package hmapp.hm.com.hmandroidapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/*import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;*/

import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.util.SysApplication;
import hmapp.hm.com.hmandroidapp.zxing.android.CaptureActivity;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button scanBtn;
    private TextView resultTv;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
/*    private MapView mapView;
    private AMap aMap;*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        /*mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();*/
        initMainPage();
    }


    private void initMainPage() {
        Button meterBoxButton = (Button)findViewById(R.id.ammeterBox_collection_data_button);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        resultTv = (TextView) findViewById(R.id.resultTv);

        scanBtn.setOnClickListener(this);

    }
    public void initMeterBox(View view){
        Intent intent = new Intent();
        intent.setClass(this,MeterBoxActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        //运行时权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
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
        }
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
        //mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        //mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }


}

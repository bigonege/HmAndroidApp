package hmapp.hm.com.hmandroidapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.util.MathUtil;
import hmapp.hm.com.hmandroidapp.util.windowsUtil;
import hmapp.hm.com.hmandroidapp.zxing.android.CaptureActivity;

public class meterboxdatacollection extends AppCompatActivity  implements View.OnClickListener{

    private Button nextStepButton;
    private EditText meterDataCollectionRowNumEdit;
    private EditText meterDataCollectionColumnNumEdit;
    private Button scanBtn;
    private EditText scanText;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
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
    }


    private void init() {
         meterDataCollectionRowNumEdit = (EditText) findViewById(R.id.meterBoxCollectionRowNum);
         meterDataCollectionColumnNumEdit = (EditText) findViewById(R.id.meterBoxCollectionColumnNum);
         scanBtn = (Button)findViewById(R.id.ammeter_data_collection_call_zxing);
         scanText = (EditText)findViewById(R.id.caijiqi_ammeterDateCollection_ScanCode_edit);
         scanBtn.setOnClickListener(this);

    }

    private void toNextStep() {

        //数据校验


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

        intent.putExtra("RowNum",RowNum);
        intent.putExtra("ColumnNum",ColumnNum);
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

        switch (v.getId()) {
            case R.id.scanBtn:

                Intent intent = new Intent(meterboxdatacollection.this,
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
                scanText.setText(content);

            }
        }
    }
}

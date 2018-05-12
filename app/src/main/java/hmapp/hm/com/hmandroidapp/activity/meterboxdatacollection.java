package hmapp.hm.com.hmandroidapp.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class meterboxdatacollection extends AppCompatActivity {

    private Button nextStepButton;
    private EditText meterDataCollectionRowNumEdit;
    private EditText meterDataCollectionColumnNumEdit;

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
    }

    private void toNextStep() {

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
        /*if((ColumnNum == null) || (ColumnNum.trim().equals(""))) {
            windowsUtil.alertInfo_ok("\u5217\u6570\u9700\u8981\u8f93\u5165\uff01", this);
            return;
        }
        if((!MathUtil.isInt(ColumnNum)) || (Integer.parseInt(ColumnNum) < 0)) {
            windowsUtil.alertInfo_ok("\u5217\u6570\u9700\u4e3a\u975e\u8d1f\u6574\u6570\uff01", this);
            return;
        }*/

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


}

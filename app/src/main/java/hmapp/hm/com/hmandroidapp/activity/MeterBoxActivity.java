package hmapp.hm.com.hmandroidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import hmapp.hm.com.hmandroidapp.R;

public class MeterBoxActivity extends AppCompatActivity {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private Button addMBButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_box);
        spinner = (Spinner)findViewById(R.id.selectcondition);
        //数据
        data_list = new ArrayList<String>();
        data_list.add("计量箱资产号");
        data_list.add("电能表资产号");
        data_list.add("台区名称");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        init();
    }
    private void  init(){
        addMBButton = (Button)findViewById(R.id.addMeterBox);
    }
    public void initMeterBox(View view){
        Intent intent = new Intent();
        intent.setClass(this,meterboxdatacollection.class);
        startActivity(intent);
    }

}

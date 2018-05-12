package hmapp.hm.com.hmandroidapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.net.Uri;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import hmapp.hm.com.hmandroidapp.R;

public class ElectricityMeterBoxDateCollectionActivity extends AppCompatActivity {

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        rowNum = Integer.parseInt(extras.getString("RowNum"));
        columnNum = Integer.parseInt(extras.getString("ColumnNum"));
        preStepButton = (Button)findViewById(R.id.meter_pre_step);
        preStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setNumColumns(columnNum);
        gridView.setAdapter(new MyAdapter(this));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //自定义适配器
    class MyAdapter extends BaseAdapter {

        private Context context;
        public MyAdapter(Context context){
            this.context = context;
        }
        private int[] images = new int[rowNum*columnNum];

        //获取图片有多少个
        @Override
        public int getCount() {
            for (int i=0;i<rowNum;i++){
                for(int j=0;j<columnNum;j++){
                    Toast.makeText(ElectricityMeterBoxDateCollectionActivity.this,"i="+String.valueOf(i)+" j="+String.valueOf(j)+"String.valueOf(i*columnNum+j="+String.valueOf(i*columnNum+j),Toast.LENGTH_SHORT);
                    //每一张图片
                    images[i*columnNum+j]=R.drawable.launcher_icon;
                }
            }
            return rowNum*columnNum;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup ;
            ImageView iv = new ImageView(context);
            iv.setImageResource(images[position]);
            return iv;
        }
    }

    protected void onStop() {
        super.onStop();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 0x4) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    public static void main(String[] args) {
        int rowNum=3;
        int columnNum=4;
         int[] images1 = new int[rowNum*columnNum];
        for (int i=0;i<rowNum;i++){
            for(int j=0;j<columnNum;j++){
                System.out.println("i="+i+"j="+j+"line="+images1[i*columnNum+j]);
                images1[i*columnNum+j]=R.drawable.launcher_icon;
            }
        }
    }


}

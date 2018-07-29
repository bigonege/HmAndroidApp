package hmapp.hm.com.hmandroidapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;

import com.amap.api.navi.AmapNaviType;

import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;


import hmapp.hm.com.hmandroidapp.R;
import hmapp.hm.com.hmandroidapp.view.FeatureView;

/**
 * Created by shixin on 16/8/23.
 * bug反馈QQ:1438734562
 */
public class NaviActivity extends CheckPermissionsActivity implements INaviInfoCallback {

    private static class DemoDetails {
        private final int titleId;
        private final int descriptionId;
        private final Class<? extends android.app.Activity> activityClass;

        public DemoDetails(int titleId, int descriptionId,
                           Class<? extends android.app.Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            featureView.setTitleId(demo.titleId, demo.activityClass!=null);
            return featureView;
        }
    }

    private static final DemoDetails[] demos = {
		    // 组件起终点算路-驾驶
            new DemoDetails(R.string.navi_end_poi_calculate_title, R.string.navi_end_poi_calculate_desc, NaviActivity.class),
           // 组件起终点算路-步行
            new DemoDetails(R.string.navi_end_poi_calculate_buxing_title, R.string.navi_end_poi_calculate_buxing_desc, NaviActivity.class),
           // 组件起终点算路-骑行
            new DemoDetails(R.string.navi_end_poi_calculate_qixing_title, R.string.navi_end_poi_calculate_qixing_desc, NaviActivity.class)
    };

    LatLng p1 = new LatLng(39.993266, 116.473193);//首开广场
    LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
    LatLng p3 = new LatLng(39.904556, 116.427231);//北京站
    LatLng p4 = new LatLng(39.773801, 116.368984);//新三余公园(南5环)
    LatLng p5 = new LatLng(40.041986, 116.414496);//立水桥(北5环)

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(),
                        new AmapNaviParams(null, null, new Poi("电能表1地址", p3, ""),
                                AmapNaviType.DRIVER), NaviActivity.this);
            } else if (position == 1) {
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(),
                        new AmapNaviParams(null, null, new Poi("电能表2地址", p3, ""),
                                AmapNaviType.WALK), NaviActivity.this);
            } else if (position == 2) {
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(),
                        new AmapNaviParams(null, null, new Poi("电能表3地址", p3, ""),
                                AmapNaviType.RIDE), NaviActivity.this);
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
    }

    ListAdapter adapter;
    private void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        setTitle("导航SDK " + AMapNavi.getVersion());

        adapter = new CustomArrayAdapter(
                this.getApplicationContext(), demos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(mItemClickListener);
    }

    /**
     * 返回键处理事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);// 退出程序
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }
}

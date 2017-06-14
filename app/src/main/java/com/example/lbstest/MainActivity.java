package com.example.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;
    List<String> permissionList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        authority();








    }

    private void initView(){
        positionText = (TextView)findViewById(R.id.position_text_view);
    }

    private void initEvent(){
        permissionList = new ArrayList<>();
    }

    // 申请权限

    private void authority(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else{
            requestLocation();
        }


    }

    private void requestLocation(){
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
    }





    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            final StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度： ").append(location.getLatitude()).append("\n");
            currentPosition.append("经度： ").append(location.getLongitude()).append("\n");
            currentPosition.append("定位方式： ");
            if(location.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络： ");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    positionText.setText(currentPosition);
                }
            });

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
    }


}

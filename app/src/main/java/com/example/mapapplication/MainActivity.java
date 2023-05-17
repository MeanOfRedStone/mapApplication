package com.example.mapapplication;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    private GoogleMap map;

    /*공영주차장 파싱해서 리스트에 담는 부분 */
    TestApiData apiData = new TestApiData();
    ArrayList<TestData> dataArr = apiData.getData();
    /*공영주차장 파싱해서 리스트에 담는 부분 */


    /*현재 위치 담을 배열 */
    ArrayList<Double> LatLng = new ArrayList<Double>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("배열 크기");
        System.out.println(dataArr.size());
        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* 버튼을 클릭하면 위치정보 불러옴 */
        Button btn_findMarker = (Button) findViewById(R.id.btn_findMarker);
        btn_findMarker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*더미 데이터로 현재 위치 대신*/
//                 startLocationService
//                startLocationService();
                /*더미 데이터로 현재 위치 대신*/
//                final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                System.out.println(lm);
                LatLng.add(37.541);
                LatLng.add(126.986);

                findParkingLot();
            }


        });
        /* 버튼을 클릭하면 위치정보 불러옴 */

        /* 버튼을 클릭하면 해당 관할구역에서 가장 가까운 마커 불러옴 */
        Button btn_findAddress = (Button) findViewById(R.id.btn_findAddress);

        btn_findAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*더미 데이터로 현재 위치 대신*/

                LatLng.add(37.541);
                LatLng.add(126.986);

                String adr = "신사동";

                findAddressParkinglot(adr);
            }


        });
        /* 버튼을 클릭하면 해당 관할구역에서 가장 가까운 마커 불러옴 */
    }




    // Get a handle to the GoogleMap object and display marker.
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(new LatLng(37.541, 126.986))
                .title("더미 위치"); // 타이틀.

        // 2. 마커 생성 (마커를 나타냄)
        map.addMarker(makerOptions);



        /*마커 찍는 loop 밑에 가까운 마커 찾는 기능 구현되서 주석처리*/
//        for (int i = 0; i < dataArr.size(); i++) {
//            // 1. 마커 옵션 설정 (만드는 과정)
////            MarkerOptions makerOptions = new MarkerOptions();
//            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
//                    .position(new LatLng(dataArr.get(i).latitude, dataArr.get(i).longitude))
//                    .title(dataArr.get(i).name); // 타이틀.
//
//            // 2. 마커 생성 (마커를 나타냄)
//            map.addMarker(makerOptions);
//        }
        /*마커 찍는 loop 밑에 가까운 마커 찾는 기능 구현되서 주석처리*/



        // 카메라를 위치로 옮긴다.
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.52487, 126.92723)));
        /*현재 위치 구하는 부분*/
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);


    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /* 현재위치 위도 경도 구하기 */


    private void startLocationService() {

        // get manager instance
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // set listener
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000000;  //기존값 10000
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        Toast.makeText(getApplicationContext(), "Location Service started.\nyou can test using DDMS.", 2000).show();
    }


    private class GPSListener implements LocationListener {

        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            LatLng.add(latitude);
            LatLng.add(longitude);
//            System.out.println(LatLng);

//            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
//            Log.i("GPSLocationService", msg);
//            Toast.makeText(getApplicationContext(), msg, 2000).show();

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }
    /* 버튼을 클릭하면 위치정보 불러옴 */

    private void findParkingLot() {
        Location locationA = new Location("pointA");
        locationA.setLatitude(LatLng.get(0));
        locationA.setLongitude(LatLng.get(1));
        float min = 99999;
        int index = 0;

        for (int i = 0; i < dataArr.size(); i++) {
            Location locationB = new Location("pointB");
            locationB.setLatitude(dataArr.get(i).latitude);
            locationB.setLongitude(dataArr.get(i).longitude);
            float d = locationA.distanceTo(locationB);

            if(d < min)
            {
                min = d;
                index = i;
            }

        }

        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(new LatLng(dataArr.get(index).latitude, dataArr.get(index).longitude))
                .title(dataArr.get(index).pname); // 타이틀.

        // 2. 마커 생성 (마커를 나타냄)
        map.addMarker(makerOptions);
    }

    private void  findAddressParkinglot(String address) {
        Location locationA = new Location("pointA");
        locationA.setLatitude(LatLng.get(0));
        locationA.setLongitude(LatLng.get(1));

        float min = 99999;
        int index = 0;
        for (int i = 0; i < dataArr.size(); i++) {

            if(dataArr.get(i).name.contains(address) )
            {
                Location locationB = new Location("pointB");
                locationB.setLatitude(dataArr.get(i).latitude);
                locationB.setLongitude(dataArr.get(i).longitude);
                float d = locationA.distanceTo(locationB);

                if (d < min) {
                    min = d;
                    index = i;
                }
            }
        }

        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(new LatLng(dataArr.get(index).latitude, dataArr.get(index).longitude))
                .title(dataArr.get(index).pname); // 타이틀.

        // 2. 마커 생성 (마커를 나타냄)
        map.addMarker(makerOptions);

    }
}


/*API에서 호출한 데이터를 저장하는 클래스 */
class TestData {
    String name;

    String pname;
    Double latitude;
    Double longitude;



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPname() {
        return pname;
    }


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "name='" + name + '\'' +
                ", pname=" + pname + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

/*서울 공공데이터 포털에서 데이터를 불러들이는 부분 */
class TestApiData {
    String apiUrl = "http://openapi.seoul.go.kr:8088/";
    String apiKey = "55514777426768643538504e776b77";
    public ArrayList<TestData> getData() {
        //return과 관련된 부분
        ArrayList<TestData> dataArr = new ArrayList<TestData>();

        //네트워킹 작업은 메인스레드에서 처리하면 안된다. 따로 스레드를 만들어 처리하자

        Thread t = new Thread() {
            @Override
            public void run() {

                try {
                    //url과 관련된 부분
                    String fullurl = apiUrl + apiKey + "/xml" + "/GetParkingInfo/1/1000";
                    URL url = new URL(fullurl);
                    InputStream is = url.openStream();

                    //xmlParser 생성
                    XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlFactory.newPullParser();
                    parser.setInput(is,"utf-8");

                    //xml과 관련된 변수들
                    boolean bName = false, bLat = false, bLong = false, bPname = false;
                    String name = "", latitude = "", longitude = "", pname = "";

                    //본격적으로 파싱
                    while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        int type = parser.getEventType();
                        TestData data = new TestData();

                        //태그 확인
                        if(type == XmlPullParser.START_TAG) { // 시작 태그부터 xml 모든 데이터 파싱
                            if (parser.getName().equals("ADDR")) {// 위치명 태그
                                bName = true; // true 값 변수 지정
                            } else if (parser.getName().equals("LAT")) {// 위도 태그
                                bLat = true;
                            }else if (parser.getName().equals("LNG")) {// 경도 태그
                                bLong = true;
                            }else if (parser.getName().equals("PARKING_NAME")){
                                bPname = true;
                            }
                        }
                        //내용(텍스트) 확인
                        else if(type == XmlPullParser.TEXT) {
                            if (bName) {
                                name = parser.getText();
                                bName = false;
                            } else if (bLat) {
                                latitude = parser.getText();
                                bLat = false;
                            } else if (bLong) {
                                longitude = parser.getText();
                                bLong = false;
                            } else if (bPname) {
                                pname = parser.getText();
                                bPname = false;
                            }
                        }
                        //내용 다 읽었으면 데이터 추가
                        else if (type == XmlPullParser.END_TAG && parser.getName().equals("row")) { // 엔드 태그의 이름 일치 확인

                            data.setName(name); // 데이터 네임 확인
                            data.setLatitude(Double.valueOf(latitude));
                            data.setLongitude(Double.valueOf(longitude));
                            data.setPname(pname);
                            dataArr.add(data);
                        }

                        type = parser.next();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return dataArr;
    }



}


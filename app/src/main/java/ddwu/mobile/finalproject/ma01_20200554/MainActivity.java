package ddwu.mobile.finalproject.ma01_20200554;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ddwu.mobile.place.placebasic.OnPlaceBasicResult;
import ddwu.mobile.place.placebasic.PlaceBasicManager;
import ddwu.mobile.place.placebasic.pojo.PlaceBasic;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "MainActivity";
    final static int PERMISSION_REQ_CODE = 100;

    private BottomNavigationView bottomNavigationView;
    private EditText etKeyword;

    //    Map & Place
    private GoogleMap mGoogleMap;
    private PlaceBasicManager placeBasicManager;
    private PlacesClient placesClient;
    MarkerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKeyword = findViewById(R.id.etKeyword);

        /* 1. PlaceBasicManager 생성 */
        placeBasicManager = new PlaceBasicManager(getString(R.string.api_key));
        /* 2. placeBasicManager.setOnPlaceBasicResult() 구현
         * 확인한  위도/경도, 장소명, PlaceID 를 사용하여 지도에 새로운 마커 추가
         * placeID 는 Marker.setTag(placeID)를 사용하여 각 마커에 저장*/
        placeBasicManager.setOnPlaceBasicResult(onPlaceBasicResult);

        if (checkPermission()) mapLoad();

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(this);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start에 지정된 Drawer 열기
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_map:
                        Intent map = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(map);
                        return true;
                    case R.id.action_review:
                        Intent review = new Intent(MainActivity.this, ReviewListActivity.class);
                        startActivity(review);
                        return true;
                    case R.id.action_alarm:
                        Intent alarm = new Intent(MainActivity.this, AlarmActivity.class);
                        startActivity(alarm);
                        return true;
                    case R.id.action_scrap:
                        Intent scrap = new Intent(MainActivity.this,ScrapActivity.class);
                        startActivity(scrap);
                        return true;
                    case R.id.action_search:
                        Intent search = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(search);
                        return true;
                }
                return false;
            }
        });
        navigationView.bringToFront();
    }

    OnPlaceBasicResult onPlaceBasicResult = new OnPlaceBasicResult() {
        @Override
        public void onPlaceBasicResult(List<PlaceBasic> list) {
            for (PlaceBasic place : list) {
                options = new MarkerOptions()
                        .title(place.getName())
                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                Marker marker = mGoogleMap.addMarker(options);
                /*현재 장소의 place_id 를 각각의 마커에 보관*/
                marker.setTag(place.getPlaceId());
            }
        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                String searchText = etKeyword.getText().toString();
                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;
                Log.e(TAG, searchText);

                try {
                    addresses = geocoder.getFromLocationName(searchText, 3);
                    if (addresses != null && !addresses.equals(" ")) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        mGoogleMap.clear();
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        searchStart(address.getLatitude(),
                                address.getLongitude(),
                                100, PlaceTypes.RESTAURANT);
                        Log.e(TAG, address.getLatitude() +" 경도: "+address.getLongitude());

                    }
                } catch(Exception e) {

                }

                break;
            case R.id.cafe:
                    searchStart(Double.parseDouble(getString(R.string.init_lat)),
                            Double.parseDouble(getString(R.string.init_lng)),
                            100, PlaceTypes.CAFE);
                    break;
            case R.id.restaurant:
                searchStart(Double.parseDouble(getString(R.string.init_lat)),
                            Double.parseDouble(getString(R.string.init_lng)),
                            100, PlaceTypes.RESTAURANT);
                break;
            case R.id.bakery:
                searchStart(Double.parseDouble(getString(R.string.init_lat)),
                        Double.parseDouble(getString(R.string.init_lng)),
                        100, PlaceTypes.BAKERY);

        }
    }


    /*입력된 유형의 주변 정보를 검색
     * PlaceBasicManager 를 사용해 type 의 정보로 PlaceBasic 을 사용하여 현재위치 주변의 관심장소 확인 */
    private void searchStart(double lat, double lng, int radius, String type) {
        placeBasicManager.searchPlaceBasic(lat, lng, radius, type);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);


        /*마커의 InfoWindow 클릭 시 marker에 Tag 로 보관한 placeID 로
        * Google PlacesAPI 를 이용하여 장소의 상세정보*/
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
//                1. 마커에서 Marker.getTag() 를 사용하여 placeID 확인
                  String placeID = marker.getTag().toString();
//                2. getPlaceDetail() 을 호출하여 Place 검색
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER,Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.RATING);
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeID, placeFields).build();

                placesClient.fetchPlace(request)
                        .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                            @Override
                            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                                Place place = fetchPlaceResponse.getPlace();
                                callDetailActivity(place);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(e instanceof ApiException){
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    Log.e(TAG, "Place not foound: "+ e.getMessage());

                                }
                            }
                        });
                 // Place place = getPlaceDetail(placeID);

//                3. callDetailActivity() 에 Place 정보를 전달하고 DetailActivity 호출 (callDetailActivity() 사용)
              //  callDetailActivity(place);
            }
        });
    }


    /*Place ID 의 장소에 대한 세부정보 획득하여 반환
    * 마커의 InfoWindow 클릭 시 호출*/
    private Place getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER,Place.Field.ADDRESS, Place.Field.ICON_URL, Place.Field.RATING);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        final Place[] place = {null};

        placesClient.fetchPlace(request)
                .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        place[0] = fetchPlaceResponse.getPlace();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof ApiException){
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e(TAG, "Place not foound: "+ e.getMessage());

                        }
                    }
                });
        return place[0];
    }


//    Google PlacesAPI 의 place 객체를 전달 받아 DetailActivity 에 전달하며 액티비티 실행
    private void callDetailActivity(Place place) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("name",place.getName());
        intent.putExtra("phone",place.getPhoneNumber());
        intent.putExtra("address",place.getAddress());
        intent.putExtra("rating", place.getRating());
        startActivity(intent);
    }



    /*구글맵을 멤버변수로 로딩*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // 매배변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }


    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




    /*구글이 현재 위치의 주변 정보를 확인하는 기본 방법 사용 메소드*/

/*
    private void startCPSearch() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ID);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).

        if (checkPermission()) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            ((Task<?>) placeResponse).addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                public void onComplete() {
                    onComplete();
                }

                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                            Log.i(TAG, String.format("Place ID: %s", placeLikelihood.getPlace().getId()));
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getLikelihood()));
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        }
    }

*/
}

package humaneer.org.wearablerunning._Deprecated;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import humaneer.org.wearablerunning.R;

import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static GoogleMap GlobalGoogleMap = null;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
                mapFragment.getMapAsync(MapFragment.this);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> arrayList) {

                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(getActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("GPS를 동작시키세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

//        if(ServiceGPS.IsServiceRunning) {
//            GlobalGoogleMap = ServiceGPS.getGoogleMap();
//        } else {
//            GlobalGoogleMap = googleMap;
//        }
////        ServiceGpsInstance.setGoogleMap(this, googleMap);
//        MainActivity.ServiceGpsInstance.setmContext(getActivity());
//        MainActivity.ServiceGpsInstance.setGoogleMap(GlobalGoogleMap);
//        MainActivity.ServiceGpsInstance.startMap();
//
//        if (!MainActivity.ServiceGpsInstance.isGetLocation()) {
//            if (!MainActivity.ServiceGpsInstance.showSettingsAlert())
//                return;
//        }
//
//        double latitude = MainActivity.ServiceGpsInstance.getLatitude();
//        double longitude = MainActivity.ServiceGpsInstance.getLongitude();


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // GPS 정보 가져오기
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // 현재 네트워크 상태 값 알아오기
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        double longitude = 0.0;
        double latitude = 0.0;

        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(isGPSEnabled) {
            longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        } else if (isNetworkEnabled) {
            longitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
            latitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
        } else {
            Toast.makeText(getActivity(), "GPS가 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
            longitude = 126.97467286;
            latitude = 37.2934204446;   // 성균관대학교 자연과학캠퍼스
        }

        // Add a marker in Sydney and move the camera
        LatLng myPosition = new LatLng(latitude, longitude);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18));

        GlobalGoogleMap = googleMap;
//        MainActivity.ServiceGpsInstance.setmContext(getActivity());
//        MainActivity.ServiceGpsInstance.setGoogleMap(GlobalGoogleMap);
//        MainActivity.ServiceGpsInstance.startMap();
    }

}

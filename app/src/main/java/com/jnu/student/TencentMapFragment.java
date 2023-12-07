package com.jnu.student;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.student.data.DataDownload;
import com.jnu.student.data.ShopLocation;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TencentMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TencentMapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TencentMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TencentMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TencentMapFragment newInstance(String param1, String param2) {
        TencentMapFragment fragment = new TencentMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private com.tencent.tencentmap.mapsdk.maps.TextureMapView mapView = null;

    public class DataDownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return new ShopDownLoader().download(urls[0]);
        }
        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            if (responseData != null) {
                ArrayList<ShopLocation> shopLocations= new ShopDownLoader() .parseJsonObjects(responseData);
                TencentMap tencentMap = mapView.getMap();
                for (ShopLocation shopLocation : shopLocations) {
                    LatLng point1 = new LatLng(shopLocation.getLatitude(), shopLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions(point1)
                            .title(shopLocation.getName());
                    Marker marker = tencentMap.addMarker(markerOptions);


                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tencent_map, container, false);
        mapView = rootView.findViewById(R.id.mapView);

        new DataDownloadTask().execute("http://file.nidama.net/class/mobile_develop/data/bookstore2023.json");


        TencentMap tencentMap = mapView.getMap();
        tencentMap.setBuildingEnable(false);

        LatLng jinanUniversityLatLng = new LatLng(22.249942,113.534341);

        tencentMap.moveCamera(CameraUpdateFactory.newLatLng(jinanUniversityLatLng));

        MarkerOptions markerOptions = new MarkerOptions(jinanUniversityLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        Marker iconMarker = tencentMap.addMarker(markerOptions);

        iconMarker.setTitle("珠海暨南大学");

        tencentMap.setOnMarkerClickListener(marker -> {
            // 处理标记点击事件
            if (marker.getTitle() != null && marker.getTitle().equals("珠海暨南大学")) {
                // 在这里执行相应的操作
            }
            return false;
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
    }

}
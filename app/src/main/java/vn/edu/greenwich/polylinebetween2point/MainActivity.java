package vn.edu.greenwich.polylinebetween2point;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import vn.edu.greenwich.polylinebetween2point.DirectionHelpers.FetchURL;
import vn.edu.greenwich.polylinebetween2point.DirectionHelpers.TaskLoadedCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;

    List<MarkerOptions> markerOptionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(mContext: MainActivity.this).execute(getUrl(place1.getPosition(),
                        place2.getPosition(), directionMode "driving"), "driving");
            }
        });

        place1 = new MarkerOptions().position(new LatLng(v:27.658143, v1: 85.3199503)).title("Location");
        place2 = new MarkerOptions().position(new LatLng(v:27.667491, v1: 85.3208583)).title("location");

        markerOptionsList.add(place1);
        markerOptionsList.add(place2);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(onMapReadyCallback: this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        mMap.addMarker(place1);
        mMap.addMarker(place2);

        showAllMarkers();

        private void showAllMarkers(){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (MarkerOptions m : markerOptionsList){
                builder.include(m.getPosition());

            }

            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.30);

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
            mMap.animateCamera(cu);
        }
    }

    private String getUrl(LatLng origin, LatLng destination, String directionMode){
        String str_origin;
        str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest;
        str_dest = "destination=" + destination.latitude + "," + destination.longitude;

        String mode;
        mode = "mode=" + directionMode;

        String parameter;
        parameter = str_origin + "&" + str_dest + "&" + mode;

        String format;
        format = "json";

        String url;
        url = "https://maps.googleapis.com/maps/api/directions/" + format + "?"
                + parameter + "&key=AIzaSyDo9Q1DEk9x_88IrmJj3s8aymaP0MArI-U";

        return url;
    }

    @Override
    public void onTaskDone(Object... values){
        if (currentPolyline != null)
            currentPolyline.remove();

        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
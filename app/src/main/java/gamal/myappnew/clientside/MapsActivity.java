package gamal.myappnew.clientside;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Moduel.Request;
import gamal.myappnew.clientside.Moduel.ShippingInformation;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private GoogleMap mMap;
    public static final PatternItem Dot=new Dot();
    public static final PatternItem DASH=new Dash(20);
    public static final PatternItem GAP=new Gap(20);
    public static final List<PatternItem> styleline= Arrays.asList(GAP,DASH);
    DatabaseReference request,shipperorder;
    Request current_request;
    LatLng locationrequest;
    Marker shippingmaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        request= FirebaseDatabase.getInstance().getReference(Common.REQUEST);
        shipperorder=FirebaseDatabase.getInstance().getReference("ShippingOrders");

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean issuccess=mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style));
        if(!issuccess)
            Log.i("ERROR","Error in stylr");

        // Add a marker in Sydney and move the camera
  trakinglocation();
    }

    private void trakinglocation() {
        request.child(Common.CurrentKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         current_request=snapshot.getValue(Request.class);
                        Geocoder geocoder=new Geocoder(getApplicationContext());
                        try {

                            List<Address> addressList=  geocoder.getFromLocationName(current_request.getAdress(), 1);
                            if (addressList!=null&&addressList.size()>0) {
                                Address address = addressList.get(0);
                                locationrequest=new LatLng(address.getLatitude(),address.getLongitude());
                                MarkerOptions marker=new MarkerOptions()
                                        .title("your location")
                                        .snippet("Name is :"+Common.CURRENT_USER.getUsername())
                                        .position(locationrequest)
                                        .icon(BitmapDescriptorFactory.defaultMarker());
                                mMap.addMarker(marker);
                                shipperorder.child(Common.CurrentKey)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    ShippingInformation shippingInformation = snapshot.getValue(ShippingInformation.class);
                                                    LatLng locationofshipper = new LatLng(shippingInformation.getLat(), shippingInformation.getLng());
                                                    if (shippingmaker == null) {
                                                        shippingmaker = mMap.addMarker(new MarkerOptions()
                                                                .title("Shipper")
                                                                .snippet("Name is :" + shippingInformation.getName())
                                                                .position(locationofshipper)
                                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                                        ;

                                                    } else {
                                                        shippingmaker.setPosition(locationofshipper);
                                                    }
                                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                                            .target(locationofshipper)
                                                            .zoom(8)
                                                            .bearing(0)
                                                            .tilt(45).build();
                                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                    Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                                            .add(locationofshipper, locationrequest)
                                                            .width(5)
                                                            .color(Color.YELLOW));


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                Log.i("jfkewjbkew","Done");
                            }else {
                                Log.i("jfkewjbkew","null");

                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.i("jfkewjbkew","ERROR");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}

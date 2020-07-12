package gamal.myappnew.clientside;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import gamal.myappnew.clientside.Adapter.CartAdapter;
import gamal.myappnew.clientside.Cart.DealwWthDateBase;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Common.Config;
import gamal.myappnew.clientside.Common.Token;
import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.Moduel.MyResponse;
import gamal.myappnew.clientside.Moduel.Notification;
import gamal.myappnew.clientside.Moduel.Order;
import gamal.myappnew.clientside.Moduel.Request;
import gamal.myappnew.clientside.Moduel.Sender;
import gamal.myappnew.clientside.Remote.APIService;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LayoutAnimationController layoutAnimationController;
    public static CartAdapter adapter;
    TextView total,empty;
    Button btnplaceholder;
    CardView cardview;
   // List<Cart> cartList;
    List<Cart> taskList;
    DatabaseReference requests;
    FirebaseDatabase firebaseDatabase;
    APIService mservec;
String myaddress,mycomment;
LocationRequest locationRequest;
LocationCallback locationCallback;
FusedLocationProviderClient fusedLocationProviderClient;
Location currentlocation;
    Place shipingaddress;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public static final int PAYPAL_REQUEST_CODE=1;
    //paypal
static PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAUPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/cf.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_cart);
        //
        Intent intent=new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);
        //

        //
        mservec=Common.getFCMServec();
        empty=findViewById(R.id.empty);
         empty.setVisibility(View.GONE);
        cardview=findViewById(R.id.cardview);
        cardview.setVisibility(View.VISIBLE);
        recyclerView=findViewById(R.id.recycle_cart);
        total=findViewById(R.id.total);

        btnplaceholder=findViewById(R.id.btnplaceholder);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getApplicationContext(),R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskList=new ArrayList<>();
        adapter=new CartAdapter(taskList,getApplicationContext());
        recyclerView.setAdapter(adapter);
        getTasks();
        btnplaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLocationSettingsRequest(CartActivity.this);
                ShowAlertDialoge(taskList);
            }
        });

    }

    private void ShowAlertDialoge( final List<Cart> list) {
        Toast.makeText(this, "mdmmddke", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("One More Step!");
        builder.setMessage("Enter your address: ");
        builder.setIcon(R.drawable.nav_cart);
       View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_addres_comment,null);
        final  EditText adress=view.findViewById(R.id.edit_addressuser);
        RadioButton rid_home=view.findViewById(R.id.rdi_home_address);
        RadioButton rid_shipthisaddress=view.findViewById(R.id.rdi_ship_this_address);
        adress.setText(Common.CURRENT_USER.getAddress());
        rid_home.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                adress.setText(Common.CURRENT_USER.getAddress());
                myaddress=adress.getText().toString();
            }
        });
        rid_shipthisaddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                Dexter.withActivity(CartActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                displayLocationSettingsRequest(CartActivity.this);
                                intilocation();
                                 fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(CartActivity.this, "Failed Get location", Toast.LENGTH_SHORT).show();
                                     }
                                 }).addOnCompleteListener(new OnCompleteListener<Location>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Location> task) {
                                        if(task!=null) {
                                            String coordinete = new StringBuilder()
                                                    .append(task.getResult().getLatitude())
                                                    .append("/")
                                                    .append(task.getResult().getLongitude()).toString();
                                           // adress.setText(coordinete);
                                            Single<String> singleaddress=Single.just(getAddressFromLatlng(task.getResult().getLatitude(),
                                                    task.getResult().getLongitude()));
                                            Disposable disposable=singleaddress.subscribeWith(new DisposableSingleObserver<String>() {

                                                @Override
                                                public void onSuccess(String s) {
                                                    adress.setText(s);
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Toast.makeText(CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            myaddress=adress.getText().toString();


                                        }else {
                                            Toast.makeText(CartActivity.this, "Please open location...", Toast.LENGTH_SHORT).show();
                                        }
                                     }
                                 });
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(CartActivity.this, "Can't Complete ,Please Accept Permission", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        }).check();

            }
        });

        final EditText comment=view.findViewById(R.id.edcomment);
        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //show paypal to payment
                //frist,getaddress,and comment;
                mycomment=comment.getText().toString();
                final String formataccount=total.getText().toString()
                        .replace("$","")
                        .replace(",","");
               // float amount=Float.parseFloat(formataccount);
                myaddress=adress.getText().toString();
                if (myaddress.isEmpty())
                {
                    Toast.makeText(CartActivity.this,"this, Failed ,adress is empty!", Toast.LENGTH_SHORT).show();
                }else {
                    //  cartList=new ArrayList<>();
                    if (mycomment.isEmpty())
                        mycomment.equals("");

                            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formataccount),
                                    "USD", "Eat It App Order",
                                    PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                            startActivityForResult(intent, PAYPAL_REQUEST_CODE);


                    //remove fragement;
                }
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });

        builder.show();
    }

    private String getAddressFromLatlng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String resulat = "";
        try {
               List<Address> addresses=geocoder.getFromLocation(latitude,longitude,1);
               if (addresses!=null&&addresses.size()>0)
               {
                   Address address=addresses.get(0);
                   StringBuilder sb=new StringBuilder(address.getAddressLine(0));
                   resulat=sb.toString();
               }else {
                   resulat="Address Not Found...";
               }
        } catch (Exception e)
        {
            e.printStackTrace();
            resulat=e.getMessage();
        }
        return resulat;
    }

    private void intilocation() {
        buildlocationRequest();
        bulidLocationCallbacks();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
    }

    private void bulidLocationCallbacks() {
        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentlocation=locationResult.getLastLocation();

            }
        };
    }
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(CartActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    private void buildlocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }
    /*    if (address.getText().toString().isEmpty())
                {
                    Toast.makeText(CartActivity.this,"this, Failed ,adress is empty!", Toast.LENGTH_SHORT).show();
                }else {
                 //  cartList=new ArrayList<>();
                  if(comment.getText().toString().isEmpty())
                      comment.setText("");
                    Request request=new Request(
                            Common.CURRENT_USER.getPhone(),
                            Common.CURRENT_USER.getUsername(),
                            address.getText().toString(),
                            total.getText().toString(),
                            list
                            ,Common.CURRENT_USER.getImageurl(),
                            comment.getText().toString()
                    );
                    // submit to firebase
                    firebaseDatabase=FirebaseDatabase.getInstance();
                          String order_number=String.valueOf(System.currentTimeMillis());
                    requests=firebaseDatabase.getReference(Common.REQUEST);
                         requests
                            .child(order_number)
                            .setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(CartActivity.this, "Sucessfull", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.i("onComplete",task.getException().getMessage());
                            }
                        }
                    });
                         SendNotificationOrder(order_number);

                }*/



//    private void SendNotificationOrder(final String order_number) {
//        Log.e("nqwjidwqei","Start");
//        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference(Common.TOKENS);
//        final Query data=tokens.orderByChild("serverToken").equalTo(true);
//        data.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    Token token=snapshot.getValue(Token.class);
//                    Notification notification=new Notification("you have new order"+order_number,"Food App");
//                    Sender content=new Sender(token.getToken(),notification);
//                     mservec.sendNotification(content).enqueue(new Callback<MyResponse>() {
//                         @Override
//                         public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                             if (response.code() == 200) {
//                                 if (response.body().success == 1) {
//                                     Toast.makeText(CartActivity.this, "Thank you,Submit order is Done !", Toast.LENGTH_SHORT).show();
//                                     Log.e("nqwjidwqei","Done");
//
//
//                                 } else {
//                                     Log.e("nqwjidwqei","Failed");
//
//                                     Toast.makeText(CartActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
//                                 }
//                             }
//                         }
//
//                         @Override
//                         public void onFailure(Call<MyResponse> call, Throwable t) {
//                                            Log.e("ERROR",t.getMessage());
//                             Log.e("nqwjidwqei","ERRor");
//
//                         }
//                     });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void DeletAllCart() {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
         MyRoomDateBase.getInstance(getApplicationContext()).cartDao().cleancart();
                //adding to database

                return null;
            }


        }
        SaveTask st = new SaveTask();
        st.execute();

    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Cart>> {

            @Override
            protected List<Cart> doInBackground(Void... voids) {
                 taskList = MyRoomDateBase
                        .getInstance(getApplicationContext())
                        .cartDao()
                        .getall();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Cart> tasks) {
                super.onPostExecute(tasks);
                double totalprice=0;
                for (Cart cart:tasks)
                {
                    totalprice+=(Double.parseDouble(cart.getPrice().trim()))*(Double.parseDouble(cart.getQuantity().trim()));
                    total.setText(totalprice+"");
                }
                adapter.notifyDataSetChanged();
                adapter = new CartAdapter( tasks,getApplicationContext());
                recyclerView.setAdapter(adapter);
                  if (tasks.isEmpty())
                  {
                      empty.setVisibility(View.VISIBLE);
                      cardview.setVisibility(View.GONE);
                  }else {
                      empty.setVisibility(View.GONE);
                      cardview.setVisibility(View.VISIBLE);
                  }

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("uiuyhvcexes","y");
        if (requestCode==PAYPAL_REQUEST_CODE)
        {
            Log.i("uiuyhvcexes","z");
            if (resultCode==RESULT_OK)
            {
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null)
                {
                    Log.i("uiuyhvcexes","o");

                    try {
                        double lat,lng;
                        if (currentlocation!=null)
                        {
                            Log.i("uiuyhvcexes","u");


                            lat =currentlocation.getLatitude();
                           lng= currentlocation.getLongitude();
                        }else {
                            Log.i("uiuyhvcexes","p");

                            lat=0;
                            lng=0;
                        }

                        String paypaldeatials=confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject=new JSONObject(paypaldeatials);
                              Log.i("uiuyhvcexes","w");
                            Request request=new Request(
                                         Common.CURRENT_USER.getPhone(),
                                           Common.CURRENT_USER.getUsername(),
                                            myaddress,
                                             total.getText().toString(),
                                             taskList
                                             ,Common.CURRENT_USER.getImageurl(),
                                              mycomment,
                                              jsonObject.getJSONObject("response").getString("state"),
                                               String.valueOf(lat),
                                               String.valueOf(lng)
                            );
                        Log.i("uiuyhvcexes","e");

                        // submit to firebase
                            firebaseDatabase=FirebaseDatabase.getInstance();
                            String order_number=String.valueOf(System.currentTimeMillis());
                            requests=firebaseDatabase.getReference(Common.REQUEST);
                            requests
                                    .child(order_number)
                                    .setValue(request).addOnCompleteListener(task -> {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(CartActivity.this, "Sucessfull", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(CartActivity.this, MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            DeletAllCart();
                                        }else {
                                            Log.i("uiuyhvcexes","Done");
                                            Log.i("onComplete",task.getException().getMessage());
                                        }
                                    });
                            //SendNotificationOrder(order_number);



                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Log.i("uiuyhvcexes","Done");

                }

            }else if (requestCode== Activity.RESULT_CANCELED)
                Toast.makeText(this, "PayMent Cancle", Toast.LENGTH_SHORT).show();
            else if (requestCode==PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(this, "Invalid Payment..", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient!=null)
        {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayLocationSettingsRequest(getApplicationContext());

        if (fusedLocationProviderClient!=null)
        {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayLocationSettingsRequest(getApplicationContext());
    }
}





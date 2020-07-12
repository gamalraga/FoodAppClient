package gamal.myappnew.clientside;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.Moduel.User;
import gamal.myappnew.clientside.Server.Lisiner;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ProgressDialog alertDialog;
    NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    public static CounterFab counterFab;
    public static MainActivity activity;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog=new ProgressDialog(getApplicationContext());
        alertDialog.setTitle("Open location");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        counterFab=findViewById(R.id.fab);
       getcountindatebase();
       counterFab.setCount(count);
       activity=this;
       PrintKeyHash();
        counterFab.setOnClickListener(view -> {
          displayLocationSettingsRequest(MainActivity.this);
//                startActivity(new Intent(getApplicationContext(),FoodList.class)
//             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });
        getcountindatebase();
        MainActivity.counterFab.setCount(count);
         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categories, R.id.nav_myacount,R.id.nav_myorder,
                R.id.nav_wishlist, R.id.nav_food_list
        )
                .setDrawerLayout(drawer)
                .build();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        //.........
        Bundle bundle=getIntent().getExtras();

         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerLayout = navigationView.getHeaderView(0);
        final ImageView imageView=headerLayout.findViewById(R.id.imageview_header);
        final TextView username=headerLayout.findViewById(R.id.username_header);
         if (isNetworkConnected()==false)
         {
             getSupportActionBar().setTitle("Wait,to connect with network");

         }else {
             getSupportActionBar().setTitle("Home");
         }
        final TextView bio=headerLayout.findViewById(R.id.bio_header);
        FirebaseDatabase.getInstance().getReference(Common.USERS_REF)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        if (user.getisStaff()==false) {
                            Common.CURRENT_USER = user;
                            Glide.with(getApplicationContext()).load(Common.CURRENT_USER.getImageurl())
                                    .into(imageView);
                            username.setText(Common.CURRENT_USER.getUsername());
                            bio.setText(Common.CURRENT_USER.getBio());
                            getSupportActionBar().setTitle("Home");

                        }else {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(MainActivity.this, "you not user", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,StartActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        getcountindatebase();
        counterFab.setCount(count);
        if (bundle!=null)
        {
            String menuid= bundle.getString("menuid");
            String menuname=bundle.getString("menuname");
            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("menuid", menuid);
            editor.apply();
            navController.navigate(R.id.nav_showfood);
            getSupportActionBar().setTitle(menuname);

        }
        // just register server;
//        Intent intent=new Intent(MainActivity.this, ListentOrder.class);
//        startService(intent);

    }

    private void PrintKeyHash() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("gamal.myappnew.clientside", PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures)
            {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("are you sure to log out ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        (dialog, which) -> {
                            Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this,StartActivity.class));
                            finish();
                        });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayLocationSettingsRequest(Context context) {

        //alertDialog.show();
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
                        startActivity(new Intent(getApplicationContext(),CartActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                      //  alertDialog.dismiss();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1);
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
        Intent intent=new Intent(MainActivity.this, Lisiner.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                getSupportActionBar().setTitle("Home");
                break;
            case R.id.nav_categories:
                navController.navigate(R.id.nav_categories);
                getSupportActionBar().setTitle("Category");
                break;

            case R.id.nav_myacount:
                navController.navigate(R.id.nav_myacount);
                getSupportActionBar().setTitle("My Acount");
                break;

            case R.id.nav_myorder:
                navController.navigate(R.id.nav_myorder);
                getSupportActionBar().setTitle("Orders");
                break;


            case R.id.nav_food_list:
                navController.navigate(R.id.nav_food_list);
                getSupportActionBar().setTitle("All Foods");
                break;

            case R.id.nav_wishlist:
                navController.navigate(R.id.nav_wishlist);
                getSupportActionBar().setTitle("Wishlist");
                break;




        }
            return true;
    }

    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);

        }
        else {
            super.onBackPressed();
        }
    }
  private void getcountindatebase() {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {


                count=MyRoomDateBase.getInstance(getApplicationContext())
               .cartDao().getCount();

                return null;
            }


        }
        SaveTask st = new SaveTask();
        st.execute();

    }

    @Override
    protected void onStop() {
        super.onStop();
        getcountindatebase();
        counterFab.setCount(count);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //alertDialog.show();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i(TAG, "All location settings are satisfied.");
                    //  alertDialog.dismiss();

                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(MainActivity.this, 1);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i(TAG, "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
   if (FirebaseAuth.getInstance().getCurrentUser()==null)
   {
       startActivity(new Intent(MainActivity.this,StartActivity.class)
       .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
   }
   getcountindatebase();
   counterFab.setCount(count);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getcountindatebase();
        counterFab.setCount(count);

    }

    @Override
    protected void onResume() {
        super.onResume();
       getcountindatebase();
        counterFab.setCount(count);

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}

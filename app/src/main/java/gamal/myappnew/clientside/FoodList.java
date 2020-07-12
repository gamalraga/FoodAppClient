package gamal.myappnew.clientside;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gamal.myappnew.clientside.Adapter.MyFoodListAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Moduel.FoodModuel;

public class FoodList extends AppCompatActivity {
//    PlacesClient placesClient;
//    List<Place.Field> placeField= Arrays.asList(Place.Field.ID,
//            Place.Field.NAME,
//            Place.Field.ADDRESS);
//    AutocompleteSupportFragment places_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
//        Places.initialize(this,getString(R.string.PLEACE_API_KEY));
//        placesClient=Places.createClient(this);
//        places_fragment=(AutocompleteSupportFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.place_autocomplute_fragment);
////        ((EditText)places_fragment.getView().findViewById(R.id.places_autocomplete_search_input))
////                .setHint("Enter your Address");
////        ((EditText)places_fragment.getView().findViewById(R.id.places_autocomplete_search_input))
////                .setTextSize(14);
//        places_fragment.setPlaceFields(placeField);
//        places_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                Toast.makeText(FoodList.this, place.getAddress(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//Log.i("ERROR404",status.getStatusMessage());
//            }
//        });

    }


    }


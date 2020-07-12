package gamal.myappnew.clientside.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.Adapter.MyFoodListAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.FoodList;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.Moduel.FoodModuel;
import gamal.myappnew.clientside.R;

public class show_foodFragment extends Fragment {
    RecyclerView recyclerView;
    List<FoodModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    String menuid;
    MyFoodListAdapter adapter;
    public show_foodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

          View view=inflater.inflate(R.layout.fragment_show_food, container, false);
        EditText search=view.findViewById(R.id.search_food);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
       menuid= sharedPreferences.getString("menuid","none");
        recyclerView=view.findViewById(R.id.recycle_food_list);
        categoryModuels=new ArrayList<>();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                     SearchFood(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new MyFoodListAdapter(categoryModuels,getContext());
        recyclerView.setAdapter(adapter);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        if (menuid!=null && !menuid.isEmpty())
        {
            LoadMenu(menuid);
        }else {
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
        }

        return  view;
    }
    private void SearchFood(String s) {
        FirebaseDatabase.getInstance().getReference(Common.FOODS)
                .orderByChild("Food")
                .startAt(s)
                .endAt(s+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoryModuels.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            FoodModuel foodModuel=snapshot.getValue(FoodModuel.class);
                               if (foodModuel.getMenuId().equals(menuid)) {
                                   foodModuel.setFoodId(snapshot.getKey());
                                   categoryModuels.add(foodModuel);
                               }
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LoadMenu(final String menuid) {

        if (isAdded())
        {
            FirebaseDatabase.getInstance().getReference(Common.FOODS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {

                                FoodModuel foodModuel=snapshot.getValue(FoodModuel.class);
                                if (foodModuel.getMenuId().equals(menuid)) {
                                    foodModuel.setFoodId(snapshot.getKey());
                                    Log.i("GamalRagab", snapshot.getKey());
                                    categoryModuels.add(foodModuel);
                                }

                            }

                            adapter.notifyDataSetChanged();
                            recyclerView.scheduleLayoutAnimation();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    }

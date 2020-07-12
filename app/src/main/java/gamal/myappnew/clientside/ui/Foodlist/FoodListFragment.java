package gamal.myappnew.clientside.ui.Foodlist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.Adapter.MyFoodListAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.Moduel.FoodModuel;
import gamal.myappnew.clientside.R;

public class FoodListFragment extends Fragment {
    RecyclerView recyclerView;
    List<FoodModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    MyFoodListAdapter adapter;
SwipeRefreshLayout swipeRefreshLayout;
EditText materialSearchBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_show_food, container, false);

        recyclerView = root.findViewById(R.id.recycle_food_list);
        categoryModuels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyFoodListAdapter(categoryModuels, getContext());
        recyclerView.setAdapter(adapter);
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        swipeRefreshLayout=root.findViewById(R.id.swip_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        materialSearchBar=root.findViewById(R.id.search_food);
       materialSearchBar.addTextChangedListener(new TextWatcher() {
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
        swipeRefreshLayout.setOnRefreshListener(() -> LoadMenu());
        swipeRefreshLayout.post(() -> LoadMenu());

        LoadMenu();
return root;
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

                            foodModuel.setFoodId(snapshot.getKey());
                            categoryModuels.add(foodModuel);
                        }

                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void LoadMenu() {

        if (isAdded())
        {
            FirebaseDatabase.getInstance().getReference(Common.FOODS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            categoryModuels.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                FoodModuel foodModuel=snapshot.getValue(FoodModuel.class);

                                    foodModuel.setFoodId(snapshot.getKey());
                                    categoryModuels.add(foodModuel);
                            }

                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}
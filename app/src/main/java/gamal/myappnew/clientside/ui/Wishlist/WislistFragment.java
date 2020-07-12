package gamal.myappnew.clientside.ui.Wishlist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.Adapter.MyFoodListAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Moduel.FoodModuel;
import gamal.myappnew.clientside.R;

public class WislistFragment extends Fragment {
    RecyclerView recyclerView;
    List<FoodModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    MyFoodListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    public WislistFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View root= inflater.inflate(R.layout.fragment_wislist, container, false);
        recyclerView = root.findViewById(R.id.recycler_wishlist);
        categoryModuels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyFoodListAdapter(categoryModuels, getContext());
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout=root.findViewById(R.id.swip_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isAdded()) {
                    LoadMenu();
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    LoadMenu();
                }
            }
        });
        return  root;
    }

    private void LoadMenu() {
        if (isAdded())
        {
            FirebaseDatabase.getInstance().getReference(Common.WISHLIST)
                    .child(Common.CURRENT_USER.getId())
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }


}

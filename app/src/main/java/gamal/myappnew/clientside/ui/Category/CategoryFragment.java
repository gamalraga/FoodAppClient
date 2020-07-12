package gamal.myappnew.clientside.ui.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.Adapter.CategoryAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.Moduel.CategoryModuel;
import gamal.myappnew.clientside.R;

public class CategoryFragment extends Fragment {
RecyclerView recyclerView;
    List<CategoryModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    CategoryAdapter adapter;
SwipeRefreshLayout swipeRefreshLayout;
  DatabaseReference reference;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        reference= FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF);
        recyclerView=root.findViewById(R.id.recycler_category);
        categoryModuels=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter=new CategoryAdapter(getContext(),categoryModuels);
        recyclerView.setAdapter(adapter);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_fal_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        //view
        swipeRefreshLayout=root.findViewById(R.id.swip_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
                );
        swipeRefreshLayout.setOnRefreshListener(() -> LoadMenu());
        swipeRefreshLayout.post(() -> LoadMenu());

        return root;
    }

    private void LoadMenu() {

   reference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoryModuels.clear();
                        if (isAdded()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CategoryModuel moduel = snapshot.getValue(CategoryModuel.class);
                                moduel.setMenuid(snapshot.getKey());
                                categoryModuels.add(moduel);
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            recyclerView.scheduleLayoutAnimation();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
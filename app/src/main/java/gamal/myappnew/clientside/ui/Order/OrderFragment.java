package gamal.myappnew.clientside.ui.Order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.Adapter.OrderAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Moduel.Request;
import gamal.myappnew.clientside.R;

public class OrderFragment extends Fragment {
    RecyclerView recyclerView;
    List<Request> requestList;
    LayoutAnimationController layoutAnimationController;
   OrderAdapter adapter;
   TextView empty;
   SwipeRefreshLayout swipeRefreshLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order_, container, false);
        recyclerView=root.findViewById(R.id.recycle_order);
        requestList=new ArrayList<>();
        empty=root.findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutAnimation(layoutAnimationController);
        adapter=new OrderAdapter(requestList,getContext());
        recyclerView.setAdapter(adapter);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
               swipeRefreshLayout=root.findViewById(R.id.swip_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isAdded())
                {
                    LoodAllorder();

                }

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded())
                {
                    LoodAllorder();
                    if (requestList.isEmpty())
                    {
                        empty.setVisibility(View.VISIBLE);
                    }else {
                        empty.setVisibility(View.GONE);
                    }
                }

            }
        });


        return root;
    }

    private void LoodAllorder() {
        FirebaseDatabase.getInstance().getReference(Common.REQUEST)
       .orderByChild("phone").equalTo(Common.CURRENT_USER.getPhone())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                  for (DataSnapshot snapshot:dataSnapshot.getChildren())
                  {

                       Request request=snapshot.getValue(Request.class);
                       request.setRequest_id(snapshot.getKey());
                           requestList.add(request);

                  }
                  adapter.notifyDataSetChanged();
                if (requestList.isEmpty())
                {
                    empty.setVisibility(View.VISIBLE);
                }else {
                    empty.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
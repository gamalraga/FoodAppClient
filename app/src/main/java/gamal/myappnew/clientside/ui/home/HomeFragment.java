package gamal.myappnew.clientside.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.Adapter.MostaPopularAdapter;
import gamal.myappnew.clientside.Adapter.MyBestDealAdapter;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Common.Token;
import gamal.myappnew.clientside.Moduel.BestDealModuel;
import gamal.myappnew.clientside.Moduel.MostPopular;
import gamal.myappnew.clientside.R;

public class HomeFragment extends Fragment {

    RecyclerView recycle_popular;
    LoopingViewPager viewpager;
    List<MostPopular> mostPopularList;
    List<BestDealModuel> bestDealModuelList;
    MostaPopularAdapter popularadapter;
   MyBestDealAdapter bestdealadapter;
SwipeRefreshLayout swipeRefreshLayout;
    LayoutAnimationController layoutAnimationController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recycle_popular=root.findViewById(R.id.recycle_categories);
        mostPopularList=new ArrayList<>();
        recycle_popular.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        popularadapter=new MostaPopularAdapter(mostPopularList,getContext());
        recycle_popular.setAdapter(popularadapter);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recycle_popular.setLayoutAnimation(layoutAnimationController);

        swipeRefreshLayout=root.findViewById(R.id.swip_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (isAdded()) {
                LoodPopularfood();
                LoodBestDeal();
            }
        });
        swipeRefreshLayout.post(() -> {
            if (isAdded()) {
                LoodPopularfood();
                LoodBestDeal();
            }
        });

        if (isAdded()) {
            LoodPopularfood();
            viewpager = root.findViewById(R.id.viewpager);
            bestDealModuelList = new ArrayList<>();
            bestdealadapter = new MyBestDealAdapter(getContext(), bestDealModuelList, true);
            viewpager.setAdapter(bestdealadapter);
            if (isAdded())
            {
                LoodBestDeal();
        }


        }

        return root;
    }


    private void LoodBestDeal() {
        FirebaseDatabase.getInstance().getReference(Common.DESTDEAL)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            bestDealModuelList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                BestDealModuel moduel = snapshot.getValue(BestDealModuel.class);
                                bestDealModuelList.add(moduel);
                            }
                            bestdealadapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                                 viewpager.scheduleLayoutAnimation();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void LoodPopularfood() {
        FirebaseDatabase.getInstance().getReference(Common.MOSTPOPULAR)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isAdded()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MostPopular popular = snapshot.getValue(MostPopular.class);
                                mostPopularList.add(popular);
                            }
                            popularadapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            recycle_popular.scheduleLayoutAnimation();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    @Override
    public void onPause() {
        super.onPause();
        viewpager.pauseAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewpager.resumeAutoScroll();
    }

}
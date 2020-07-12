package gamal.myappnew.clientside.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DeatailsFood;
import gamal.myappnew.clientside.Moduel.BestDealModuel;
import gamal.myappnew.clientside.Moduel.FoodModuel;
import gamal.myappnew.clientside.R;

public class MyBestDealAdapter extends LoopingPagerAdapter<BestDealModuel> {
 List<FoodModuel> foodModuelList;
    public MyBestDealAdapter(Context context, List<BestDealModuel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
        foodModuelList=new ArrayList<>();
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.best_deal_itrem,container,false);
    }

    @Override
    protected void bindView(View convertView, final int listPosition, int viewType) {
        ImageView imageView=convertView.findViewById(R.id.image_besat_deal);
        TextView textView=convertView.findViewById(R.id.tex_best_deal);
       // Glide.with(context).load(R.drawable.register).into(imageView);
        Log.i("addtobestdeal","before");

       Glide.with(context).load(itemList.get(listPosition).getImage()).into(imageView);
        textView.setText(itemList.get(listPosition).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+itemList.get(listPosition).getFood_id(), Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, DeatailsFood.class)
                        .putExtra("foodid",itemList.get(listPosition).getFood_id())
                        .addFlags(Intent. FLAG_ACTIVITY_NEW_TASK));

            }
        });
    }


}

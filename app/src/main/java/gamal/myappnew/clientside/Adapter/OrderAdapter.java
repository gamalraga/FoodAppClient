package gamal.myappnew.clientside.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.MapsActivity;
import gamal.myappnew.clientside.Moduel.Request;
import gamal.myappnew.clientside.R;
import info.hoang8f.widget.FButton;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<Request> list;
    Context context;
    Recycleroforder adapter;

    public OrderAdapter(List<Request> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
       adapter=new Recycleroforder(list.get(position).getFoods(),context);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter);
   holder.fButton.setOnClickListener(v -> {
       context.startActivity(new Intent(context, MapsActivity.class));
       Common.CurrentKey=list.get(position).getRequest_id();
   });
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layoutitem_from_left);
       holder.recyclerView.setLayoutAnimation(layoutAnimationController);
        adapter.notifyDataSetChanged();
        holder.address.setText(list.get(position).getAdress());
        holder.phone.setText(list.get(position).getPhone());
        holder.status.setText(Common.ConvertStatusts(list.get(position).getStatus()));
        holder.total.setText(list.get(position).getTotal());

        ColorGenerator generator = ColorGenerator.MATERIAL;
                                int randomcolor = generator.getRandomColor();
                            TextDrawable.IBuilder builder = TextDrawable.builder()
                                    .beginConfig()
                                    .withBorder(4)
                                    .endConfig()
                                    .round();
                                TextDrawable drawable = builder.build(Common.CURRENT_USER.getUsername()
                                        .substring(0, 1).toUpperCase(), randomcolor);
                                      holder.imageprofile.setImageDrawable(drawable);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageprofile;
        TextView phone,address,status,total;
        RecyclerView recyclerView;
        FButton fButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.recycle_food_list);
            phone=itemView.findViewById(R.id.phone);
            address=itemView.findViewById(R.id.address);
            status=itemView.findViewById(R.id.status);
            imageprofile=itemView.findViewById(R.id.imageprofile);
            total=itemView.findViewById(R.id.total);
            fButton=itemView.findViewById(R.id.btnsingin);
        }
    }
}

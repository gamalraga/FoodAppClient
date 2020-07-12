package gamal.myappnew.clientside.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DeatailsFood;
import gamal.myappnew.clientside.R;

public class Recycleroforder extends RecyclerView.Adapter<Recycleroforder.ViewHolder> {
    List<Cart> list;
    Context context;

    public Recycleroforder(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_of_order_food,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImageFood()).into(holder.imageView);
        holder.textView.setText(list.get(position).getFoodName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DeatailsFood.class)
                .putExtra("foodid",list.get(position).getFoodId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.order_image);
            textView=itemView.findViewById(R.id.text_order_name);
        }
    }
}

package gamal.myappnew.clientside.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.Moduel.CategoryModuel;
import gamal.myappnew.clientside.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<CategoryModuel> categoryModuelList;
     Context context;
    public CategoryAdapter(Context context,List<CategoryModuel> categoryModuelList) {
        this.categoryModuelList = categoryModuelList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
           holder.textView.setText(categoryModuelList.get(position).getName());
        Glide.with(context).load(categoryModuelList.get(position).getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class)
                .putExtra("menuid",categoryModuelList.get(position).getMenuid())
                .putExtra("menuname",categoryModuelList.get(position).getName()));
                Toast.makeText(context, ""+categoryModuelList.get(position).getMenuid(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModuelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
           ImageView imageView;
           TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_category);
            textView=itemView.findViewById(R.id.tex_category);
        }
    }
}

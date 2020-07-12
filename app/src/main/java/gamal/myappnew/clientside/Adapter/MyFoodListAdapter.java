package gamal.myappnew.clientside.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.DeatailsFood;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.Moduel.BestDealModuel;
import gamal.myappnew.clientside.Moduel.FoodModuel;
import gamal.myappnew.clientside.Moduel.User;
import gamal.myappnew.clientside.R;


public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.ViewHolder>  {
    List<FoodModuel> list;
    Context context;
    List<FoodModuel> searchlist;
    int count;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    public MyFoodListAdapter(List<FoodModuel> list, Context context) {
        this.list = list;
        this.context = context;
        searchlist=new ArrayList<>(list);


    }

    @NonNull
    @Override
    public MyFoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFoodListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

//        SharedPreferences preferences=context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//        menuid=preferences.getString("menuid","none");

        iswishlist(list.get(position).getFoodId(),holder.fav);
             holder.name.setText(list.get(position).getFood());
             holder.price.setText(list.get(position).getPrice()+"$");
             Glide.with(context).load(list.get(position).getImage()).into(holder.imagefood);

             holder.itemView.setOnClickListener(v -> {
                 Toast.makeText(context, ""+list.get(position).getFoodId(), Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, DeatailsFood.class)
                 .putExtra("foodid",list.get(position).getFoodId())
                 .addFlags(Intent. FLAG_ACTIVITY_NEW_TASK));
             });
             holder.fav.setOnClickListener(v -> {
                 if (holder.fav.getTag().equals("Like"))
                 {
                     HashMap<String,Object> hashMap=new HashMap<>();
                     hashMap.put("Description",list.get(position).getDescription());
                     hashMap.put("Discount",list.get(position).getDiscount());
                     hashMap.put("Food",list.get(position).getFood());
                     hashMap.put("Image",list.get(position).getImage());
                     hashMap.put("MenuId",list.get(position).getMenuId());
                     hashMap.put("Price",list.get(position).getPrice());

                     FirebaseDatabase.getInstance().getReference().child(Common.WISHLIST)
                             .child(Common.CURRENT_USER.getId())
                             .child(list.get(position).getFoodId())
                             .setValue(hashMap);
//                         addnotification(post.getPublisher(),post.getPostid());
//                         addToken(post.getPublisher(),post.getPostid());
                 }else {
                     FirebaseDatabase.getInstance().getReference().child(Common.WISHLIST)
                             .child(Common.CURRENT_USER.getId())
                             .child(list.get(position).getFoodId())
                             .removeValue();
                 }
             });
             holder.auic_cart.setOnClickListener(v -> {

                 Cart cart=new Cart();
                 cart.setDisCount(list.get(position).getDiscount());
                 cart.setFoodId(list.get(position).getFoodId());
                 cart.setFoodName(list.get(position).getFood());
                 cart.setImageFood(list.get(position).getImage());
                 cart.setPrice(list.get(position).getPrice());
                 cart.setQuantity("1");
                 InsertAsyncTask insertAsyncTask=new InsertAsyncTask();
                 insertAsyncTask.execute(cart);
                 //new DealwWthDateBase().AddToCart(cart,getApplicationContext());
                 Toast.makeText(context, "Added to Cart Done", Toast.LENGTH_SHORT).show();
                 getcountindatebase();
                 MainActivity.counterFab.setCount(count);

             });
//             holder.sharetofacebook.setOnClickListener(v -> {
//                 Target target=new Target() {
//                     @Override
//                     public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                         SharePhoto photo=new SharePhoto.Builder()
//                                 .setBitmap(bitmap)
//                                 .build();
//                         if (ShareDialog.canShow(SharePhotoContent.class))
//                         {
//                             SharePhotoContent content=new SharePhotoContent.Builder()
//                                     .addPhoto(photo)
//                                     .build();
//                             shareDialog.show(content);
//                         }
//                     }
//
//                     @Override
//                     public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                     }
//
//
//                     @Override
//                     public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                     }
//                 };
//
//                 callbackManager=CallbackManager.Factory.create();
//                 shareDialog=new ShareDialog(MainActivity.activity);
//                 Picasso.get().load(list.get(position).getImage()).into(target);
//
//
//             });
        getcountindatebase();
        MainActivity.counterFab.setCount(count);
     /*   Double discount=(Double.parseDouble(list.get(position).getDiscount())/
                Double.parseDouble(list.get(position).getPrice()))*100;*/

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    private void iswishlist(final String foodid, final ImageView like)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(Common.WISHLIST)
                .child(Common.CURRENT_USER.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(foodid).exists())
                {
                    like.setImageResource(R.drawable.like);
                    like.setTag("Liked");
                }
                else {
                    like.setImageResource(R.drawable.dislike);
                    like.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
  public class ViewHolder extends RecyclerView.ViewHolder{
    TextView price,name;
    ImageView fav,auic_cart,imagefood,sharetofacebook;
      public ViewHolder(@NonNull View itemView) {
          super(itemView);

          price=itemView.findViewById(R.id.text_food_price);
          name=itemView.findViewById(R.id.text_food_name);
          fav=itemView.findViewById(R.id.image_fav);
          auic_cart=itemView.findViewById(R.id.image_quic_cart);
          imagefood=itemView.findViewById(R.id.image_food_list);
          sharetofacebook=itemView.findViewById(R.id.share_facebook);
      }
  }

    class InsertAsyncTask extends AsyncTask<Cart,Void,Void>
    {
        @Override
        protected Void doInBackground(Cart... carts) {
            MyRoomDateBase.getInstance(context).cartDao().insert(carts[0]);
            return null;
        }
    }
    private void getcountindatebase() {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {


                count=MyRoomDateBase.getInstance(context)
                        .cartDao().getCount();

                return null;
            }


        }
        SaveTask st = new SaveTask();
        st.execute();

    }





}

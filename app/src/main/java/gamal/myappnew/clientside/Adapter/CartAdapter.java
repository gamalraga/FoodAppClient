package gamal.myappnew.clientside.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


import gamal.myappnew.clientside.CartActivity;
import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.R;

import static android.content.Context.WINDOW_SERVICE;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHoldere> {
    List<Cart> cartList;
    Context context;

    public CartAdapter(List<Cart> ordeerModuels, Context context) {
        this.cartList = ordeerModuels;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHoldere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHoldere(LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHoldere holder, final int position) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + cartList.get(position).getQuantity(), Color.DKGRAY);
        holder.image_cart_count.setImageDrawable(drawable);
        double price = Double.parseDouble(cartList.get(position).getPrice().trim()) * Double.parseDouble(cartList.get(position).getQuantity().trim());
        Log.i("MainActivity", String.valueOf(price));
        holder.text_price.setText(String.valueOf(price)+"$");
        holder.txt_cart_name.setText(cartList.get(position).getFoodName());
        Glide.with(context).load(cartList.get(position).getImageFood()).into(holder.image_food_cart);
        holder.text_cart_discoun.setText(cartList.get(position).getDisCount());
        holder.move_vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  ShowPopo(v,position);
            }
        });
        holder.image_cart_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialoge  = new AlertDialog.Builder(context);
                alertdialoge.setTitle("Update Cart");
                alertdialoge.setMessage("Change Quantity of food");

                final EditText quantity = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                quantity.setLayoutParams(lp);
                alertdialoge.setView(quantity);
                alertdialoge.setIcon(R.drawable.nav_cart);
                quantity.setText(cartList.get(position).getQuantity());
                alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (quantity.getText().toString().isEmpty())
                        {
                            Toast.makeText(context, "No things is change", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Cart cart = new Cart();
                            cart.setQuantity(quantity.getText().toString());
                            updatecart(position, cart);
                            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, CartActivity.class));
                            dialog.dismiss();
                        }
                    }
                });
                alertdialoge.setNegativeButton("Cancle",null);
                alertdialoge.show();

            }
        });

    }

    private void ShowPopo(final View view, final int position) {
        final android.widget.PopupMenu popupMenu= new android.widget.PopupMenu(context,view);
        popupMenu.inflate(R.menu.popo_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.delete)
                {
                  deletecart(position);
                    Toast.makeText(context, "Delete Done!", Toast.LENGTH_SHORT).show();
                     context.startActivity(new Intent(context, CartActivity.class)
                     .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                return false;
            }
        });
        popupMenu.show();


    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public  class CartViewHoldere extends RecyclerView.ViewHolder
    {
        TextView txt_cart_name,text_price,text_cart_discoun;
        ImageView image_cart_count,image_food_cart,move_vet;
        ElegantNumberButton elegantNumberButton;
        public CartViewHoldere(@NonNull View itemView) {
            super(itemView);
            text_price=itemView.findViewById(R.id.cart_item_price);
            txt_cart_name=itemView.findViewById(R.id.cart_item_name);
            image_cart_count=itemView.findViewById(R.id.cart_item_count);
            image_food_cart=itemView.findViewById(R.id.image_food_cart);
            move_vet=itemView.findViewById(R.id.edit_cart);
            text_cart_discoun=itemView.findViewById(R.id.cart_item_discount);
        }
    }
    public void deletecart( final int position)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cart cart= MyRoomDateBase.getInstance(context)
                        .cartDao().findcartbyid(cartList.get(position).getUid());
                MyRoomDateBase.getInstance(context)
                        .cartDao().deletecart(cart);
            }
        }).start();
    }
    public void updatecart(final int position, final Cart cart)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cart cart1=cart;
                 cart1= MyRoomDateBase.getInstance(context)
                        .cartDao().findcartbyid(cartList.get(position).getUid());

                MyRoomDateBase.getInstance(context)
                        .cartDao().update(cart1);
            }
        }).start();
    }


}


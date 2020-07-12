package gamal.myappnew.clientside;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import gamal.myappnew.clientside.Cart.DealwWthDateBase;
import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;
import gamal.myappnew.clientside.Fragment.ShowCommentFragment;
import gamal.myappnew.clientside.Moduel.FoodModuel;
import gamal.myappnew.clientside.Moduel.Rating;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeatailsFood extends AppCompatActivity implements RatingDialogListener {
    String foodid;
    TextView food_name_selected,food_price_selected,food_description_selected;
    ImageView image_food_selected;
    CollapsingToolbarLayout collpsing;
    FloatingActionButton btncart,btn_rating;
    ElegantNumberButton numberButton;
    FoodModuel moduel;
    RatingBar rating;
    Button showcomments;
int countfb;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/cf.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_deatails_food);
        if (getIntent()!=null)
        {
            foodid=getIntent().getStringExtra("foodid");
        }
        btn_rating=findViewById(R.id.btn_rating);
        food_description_selected=findViewById(R.id.food_description_selected);
        food_name_selected=findViewById(R.id.food_name_selected);
        food_price_selected=findViewById(R.id.food_price_selected);
        image_food_selected=findViewById(R.id.image_food_selected);
        collpsing=findViewById(R.id.collpsing);
        btncart=findViewById(R.id.btncart);
        rating=findViewById(R.id.reatingbar);
        showcomments=findViewById(R.id.show_comments);
        showcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CommentActivity.class)
                .putExtra("FoodId",foodid));
            }
        });
        numberButton=findViewById(R.id.number_button);
        if (!foodid.equals("")&& !foodid.isEmpty())
        {
            Loadinfofoodselected(foodid);
            getRagingfood(foodid);
        }
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRatingDialoge();

            }
        });
btncart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Cart cart=new Cart();
        cart.setDisCount(moduel.getDiscount());
        cart.setFoodId(foodid);
        cart.setFoodName(moduel.getFood());
        cart.setImageFood(moduel.getImage());
        cart.setPrice(moduel.getPrice());
        cart.setQuantity(numberButton.getNumber());
        InsertAsyncTask insertAsyncTask=new InsertAsyncTask();
        insertAsyncTask.execute(cart);
        //new DealwWthDateBase().AddToCart(cart,getApplicationContext());
        Toast.makeText(DeatailsFood.this, "Added to Cart Done", Toast.LENGTH_SHORT).show();

        MainActivity.counterFab.setCount(countfb);

    }
});

    }

    private void getRagingfood(String foodid) {
        // i  will rating food with get all rating from users and sum average and ther sum finaly i wil get it for food;
        FirebaseDatabase.getInstance().getReference(Common.RATING)
                .orderByChild("foodid").equalTo(foodid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count=0;int sum=0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Rating rating=snapshot.getValue(Rating.class);
                            Log.i("jwfiuwenf",rating.getRatevalue());
                            sum+=Integer.parseInt(rating.getRatevalue());
                            count++;
                        }
                        if (count!=0) {
                            float average = sum / count;
                            rating.setRating(average);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void ShowRatingDialoge() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(1)
                .setTitle("Rate this Food")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("Good Food !")
                .setStarColor(R.color.colorPrimaryDark)
                .setNoteDescriptionTextColor(R.color.backgroundColor)
                .setTitleTextColor(R.color.colorBlank)
                .setDescriptionTextColor(R.color.colorPrimaryDark)
                .setHint("Please write your comment here ...")
                .setHintTextColor(android.R.color.white)
                .setCommentTextColor(R.color.red)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(DeatailsFood.this)
                .show();
    }

    private void Loadinfofoodselected(String foodid) {
        FirebaseDatabase.getInstance().getReference()
                .child(Common.FOODS).child(foodid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     moduel=dataSnapshot.getValue(FoodModuel.class);
                food_name_selected.setText(moduel.getFood());
                food_description_selected.setText(moduel.getDescription());
                food_price_selected.setText(moduel.getPrice()+"");
                Glide.with(getApplicationContext()).load(moduel.getImage()).into(image_food_selected);
                collpsing.setTitle(moduel.getFood());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String commont) {
        final Rating rating=new Rating(Common.CURRENT_USER.getId(),
                foodid,String.valueOf(value),commont);
        FirebaseDatabase.getInstance().getReference(Common.RATING)
                .push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(DeatailsFood.this, "Thanks for submit rating !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*private void ConvertAllFormDateBase() {
         class SaveTask extends AsyncTask<Void, Void, Void> {

             @SuppressLint("WrongThread")
             @Override
             protected Void doInBackground(Void... voids) {

                 //creating a task
                 OrderModuel task = new OrderModuel();
                 task.setProductName(moduel.getName());
                 task.setPrice(moduel.getPrice()+"");
                  task.setProductId(foodid);
                  task.setMenuId(menuid);
                 task.setQuantity(numberButton.getNumber());

                     APPDateBase.getInstance(getApplicationContext()).orderModuelDao().AddOrderToRoom(task);
                 //adding to database
                        countfb= APPDateBase.getInstance(getApplicationContext()).orderModuelDao()
                                 .getWordWithCode();

                 return null;
             }


         }
         SaveTask st = new SaveTask();
         st.execute();

     }*/
   class InsertAsyncTask extends AsyncTask<Cart,Void,Void>
   {
       @Override
       protected Void doInBackground(Cart... carts) {
           MyRoomDateBase.getInstance(getApplicationContext()).cartDao().insert(carts[0]);
           return null;
       }
   }

}

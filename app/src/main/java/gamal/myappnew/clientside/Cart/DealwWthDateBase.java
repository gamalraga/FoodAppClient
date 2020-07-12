package gamal.myappnew.clientside.Cart;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.clientside.DATEBASE.Cart;
import gamal.myappnew.clientside.DATEBASE.MyRoomDateBase;

public class DealwWthDateBase {

    public void AddToCart(final Cart cart, final Context context)
    {

        class InsertAsyncTask extends AsyncTask<Cart,Void,Void>
        {
            @Override
            protected Void doInBackground(Cart... carts) {
      MyRoomDateBase.getInstance(context).cartDao().insert(carts[0]);
                return null;
            }
        }
        new InsertAsyncTask().execute(cart);
    }
    public List<Cart> getAllCarts(final Context context)
    {

        return null;
    }
}

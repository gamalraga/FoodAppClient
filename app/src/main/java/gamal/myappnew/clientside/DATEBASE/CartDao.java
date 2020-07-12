package gamal.myappnew.clientside.DATEBASE;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao  {
    @Insert(entity = Cart.class)
    void insert(Cart cart);

    @Query("Select * from Order_table")
    List<Cart> getall();
    @Query("Select * from Order_table Where order_uid like :uid")
    Cart findcartbyid(int uid);
    @Delete
    Void deletecart(Cart cart);
    @Update
    Void update(Cart cart);
    @Query("Delete  from Order_table")
    void cleancart();
    @Query("SELECT COUNT(*) FROM Order_table ")
    int getCount();
}

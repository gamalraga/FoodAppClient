package gamal.myappnew.clientside.DATEBASE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Order_table")
public class Cart {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_uid")
    private int uid;
    @ColumnInfo(name = "FoodId")
    private String FoodId;
    @ColumnInfo(name = "ImageFood")
    private String ImageFood;
    @ColumnInfo(name = "FoodName")
    private String FoodName;
    @ColumnInfo(name = "DisCount")
    private String DisCount;
    @ColumnInfo(name = "Price")
    private String Price;
    @ColumnInfo(name = "Quantity")
    private   String Quantity;

    public Cart() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getImageFood() {
        return ImageFood;
    }

    public void setImageFood(String imageFood) {
        ImageFood = imageFood;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getDisCount() {
        return DisCount;
    }

    public void setDisCount(String disCount) {
        DisCount = disCount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}

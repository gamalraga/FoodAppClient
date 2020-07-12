package gamal.myappnew.clientside.DATEBASE;

import android.content.Context;
import android.support.v4.app.INotificationSideChannel;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Cart.class},version = 4, exportSchema = false)
public abstract class MyRoomDateBase extends RoomDatabase {
    public abstract CartDao cartDao();
    private static volatile MyRoomDateBase INSTANCE;
    public static MyRoomDateBase getInstance(Context context)
    {
        if (INSTANCE==null)
        {
            synchronized (MyRoomDateBase.class)
            {
                if (INSTANCE==null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext()
                                    , MyRoomDateBase.class, "DB_NAME")
                            //use it when you change in the coulam exists and increase verision
                            .fallbackToDestructiveMigration()
                            .build();
                }

            }
        }
        return INSTANCE;
    }
}

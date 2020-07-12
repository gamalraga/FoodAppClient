package gamal.myappnew.clientside.Server;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.Moduel.Request;
import gamal.myappnew.clientside.Notification.NotificationHelper;

public class Lisiner extends Service implements ChildEventListener {
    DatabaseReference reference;
    public Lisiner() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reference= FirebaseDatabase.getInstance().getReference(Common.REQUEST);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         reference.orderByChild("phone").equalTo(Common.CURRENT_USER.getPhone())
                .addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//        Request request=snapshot.getValue(Request.class);
//        NotificationHelper notificationHelper=new NotificationHelper(getBaseContext());
//        notificationHelper.sendhightproirityNotification("Order is updated",Common.ConvertStatusts(request.getStatus()), MainActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Request request=snapshot.getValue(Request.class);
        NotificationHelper notificationHelper=new NotificationHelper(getBaseContext());
        notificationHelper.sendhightproirityNotification("Order is updated",Common.ConvertStatusts(request.getStatus()), MainActivity.class);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}

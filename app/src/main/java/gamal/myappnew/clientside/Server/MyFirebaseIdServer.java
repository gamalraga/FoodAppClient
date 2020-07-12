package gamal.myappnew.clientside.Server;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;

import gamal.myappnew.clientside.Common.Common;
import gamal.myappnew.clientside.Common.Token;

public class MyFirebaseIdServer extends FirebaseMessagingService {



    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() !=null)
        UpdateTokenToFirebase(refreshedToken);
    }

    private void UpdateTokenToFirebase(String tokenRefershed) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference(Common.TOKENS);
        Token token=new Token(tokenRefershed,false);//false beacuse this token send to clientapp
        tokens.child(Common.CURRENT_USER.getId()).setValue(token);
    }
}

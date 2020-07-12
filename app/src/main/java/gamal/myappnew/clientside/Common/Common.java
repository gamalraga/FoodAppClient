package gamal.myappnew.clientside.Common;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

import gamal.myappnew.clientside.Moduel.User;
import gamal.myappnew.clientside.Remote.APIService;
import gamal.myappnew.clientside.Remote.RetrofitClient;
import retrofit2.Retrofit;

public class Common {
    public static final String GETAGORY_REF ="Category" ;
    public static final String FOODS ="Foods" ;
    public static final String REQUEST = "Request";
    public static final String MOSTPOPULAR ="MostPopular" ;
    public static final String DESTDEAL ="BestDeals";
    public static final String WISHLIST ="Wishlist" ;
    public static final String RATING ="Rating" ;
    public static final String TOKENS ="Tokens" ;
    public static  String USERS_REF="Users";
    public static User CURRENT_USER=null;
    public static String PHONE="";
    private static final String BASE_URL="https://fcm.googleapis.com/";
    public static APIService getFCMServec()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
    public static String CURRENT_ID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static  String IMAGE_URL="https://firebasestorage.googleapis.com/v0/b/gamaldox-15260.appspot.com/o/placeprofile.png?alt=media&token=555836cd-4ac5-4772-8cde-c53b240ac51a";
    public static String ConvertStatusts(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On My Way";
        else if (status.equals("2"))
            return "Shipping";
        else
            return "Shipped";
    }
public static String CurrentKey;

}

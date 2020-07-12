package gamal.myappnew.clientside.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import gamal.myappnew.clientside.MainActivity;
import gamal.myappnew.clientside.R;
import gamal.myappnew.clientside.StartActivity;


public class LogoutFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_logout, container, false);
        TextView logout=view.findViewById(R.id.logout_out);
         logout.setOnClickListener(v -> {
          LogOUT();
         });
                return view;

        }

    private void LogOUT() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setMessage("are you sure to log out ?");
       alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", (dialog, which) -> {
           Toast.makeText(getContext(), "Log Out", Toast.LENGTH_SHORT).show();
           FirebaseAuth.getInstance().signOut();
           startActivity(new Intent(getContext(), StartActivity.class));
           getActivity().finish();
           dialog.dismiss();
       });
       alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle", (dialog, which) -> dialog.cancel());
       alertDialog.show();
    }
}




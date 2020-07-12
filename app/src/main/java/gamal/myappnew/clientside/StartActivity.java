package gamal.myappnew.clientside;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StartActivity extends AppCompatActivity {

        Button login, register;
        FirebaseUser firebaseUser;

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
            setContentView(R.layout.activity_start);
            login = findViewById(R.id.start_login);
            register = findViewById(R.id.start_register);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        }
    }


package app.wallet.client.netux.sv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.wallet.client.netux.sv.auth.LoginActivity;
import app.wallet.client.netux.sv.user.TutorialActivity;

public class SplashActivity extends AppCompatActivity {

    int SPLASH_TIME = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);
        new Handler().postDelayed(() -> {
            if(isNetworkAvailable()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent i;
                if(user != null){
                    i = new Intent(SplashActivity.this, TutorialActivity.class);
                }else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
            }else{
                Intent i = new Intent(SplashActivity.this,NotConnectedActivity.class);
                startActivity(i);
            }
            finish();
        }, SPLASH_TIME);
    }
   private boolean isNetworkAvailable(){
       ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo ni = cm.getActiveNetworkInfo();
       if (ni != null) {
           NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
           NetworkInfo mMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
           if (mWifi.isConnected() || mMobile.isConnected()) {
               return true;
           }else{
               return false;
           }
       }
       else {
           return false;
       }
   }
}
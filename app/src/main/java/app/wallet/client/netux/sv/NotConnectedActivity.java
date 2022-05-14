package app.wallet.client.netux.sv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import app.wallet.client.netux.sv.auth.LoginActivity;

public class NotConnectedActivity extends AppCompatActivity {
    Button btnRestart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_connected);
        btnRestart = findViewById(R.id.btnIntentConnect);
        btnRestart.setOnClickListener(v -> {
            if(isNetworkAvailable()){
                Intent i = new Intent(NotConnectedActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(NotConnectedActivity.this,"Conexión no disponible, vuelva a intentarlo más tarde", Toast.LENGTH_LONG).show();
            }
        });
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
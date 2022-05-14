package app.wallet.client.netux.sv.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import app.wallet.client.netux.sv.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnRestorePassword;
    EditText edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btnRestorePassword = findViewById(R.id.btnForgot);
        edtEmail = findViewById(R.id.edtEmailF);
        btnRestorePassword.setOnClickListener(v -> {
            validate();
        });
    }
    private void validate(){
        String email = edtEmail.getText().toString().trim();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Correo inválido");
            return;
        }else{
            sendEmail(email);
        }
    }
    private void sendEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this,"Correo enviado exitosamente", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this,"Envío de correo fallido",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
package app.wallet.client.netux.sv.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.user.TutorialActivity;

public class RegisterActivity extends AppCompatActivity {

    Button  btnRegister;
    EditText edtEmail, edtPassword, edtConfirmPassword;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadView();
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(v -> {
            validate();
        });
    }
    private void validate(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Correo inválido");
            return;
        }else{
            edtEmail.setError(null);
        }
        if(password.isEmpty() || password.length() < 8){
            edtPassword.setError("Se necesitan más de 8 caracteres");
            return;
        }else if(!Pattern.compile("[0-9]").matcher(password).find()){
            edtPassword.setError("Al menos un número");
            return;
        }else{
            edtPassword.setError(null);
        }
        if (!confirmPassword.equals(password)){
            edtConfirmPassword.setError("Deben ser iguales las contraseñas");
            return;
        }else{
            signUp(email,password);
        }
    }
    public void signUp(String ema, String pass){
        mAuth.createUserWithEmailAndPassword(ema, pass)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(RegisterActivity.this, TutorialActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(RegisterActivity.this, "Falló su registro",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void loadView(){
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmailR);
        edtPassword = findViewById(R.id.edPassR);
        edtConfirmPassword = findViewById(R.id.edPassConfirmR);
    }
}
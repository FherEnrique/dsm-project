package app.wallet.client.netux.sv.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.user.TutorialActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnReg, btnRestart, btnLog;
    EditText edtEmail, edtPassword;
    private FirebaseAuth mAuth;

    //SignIn Google
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loadView();
        btnReg.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });
        btnRestart.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
        });
        btnLog.setOnClickListener(v -> {
            validate();
        });
        //Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(v -> {
            signInWithGoogle();
        });
    }
    private void validate(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Correo inválido");
            return;
        }else{
            edtEmail.setError(null);
        }
        if(password.isEmpty()){
            edtPassword.setError("Campo vacío");
            return;
        }else{
            signIn(email,password);
        }
    }
    public void signIn(String ema, String pass){
        mAuth.signInWithEmailAndPassword(ema,pass)
                .addOnCompleteListener(this, task -> {
                   if(task.isSuccessful()){
                       Intent i = new Intent(LoginActivity.this, TutorialActivity.class);
                       startActivity(i);
                       finish();
                   }else {
                       Toast.makeText(this,"Credenciales erróneas, intenta de nuevo",Toast.LENGTH_LONG).show();
                   }
                });
    }
    private void signInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Toast.makeText(LoginActivity.this,"Falló en el proveedor de Google",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent i = new Intent(LoginActivity.this,TutorialActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,"Falló en iniciar sesión con Google",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void loadView(){
        btnReg = findViewById(R.id.btnReg);
        btnRestart = findViewById(R.id.btnReset);
        btnLog = findViewById(R.id.btnLog);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPass);
        signInButton = findViewById(R.id.loginGoogle);
    }
}
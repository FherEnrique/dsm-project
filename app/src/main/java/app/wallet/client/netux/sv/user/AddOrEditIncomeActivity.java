package app.wallet.client.netux.sv.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app.wallet.client.netux.sv.R;

public class AddOrEditIncomeActivity extends AppCompatActivity {

    Button btnAddIncome;
    EditText edtAmount, edtDescription;
    RadioButton rbSave, rbNormal, rbLoan, rbOther;
    FirebaseFirestore mFirestore;
    private String action;
    private String articleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_income);
        loadView();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        action = getIntent().getStringExtra("action");
        if (action.equals("a")){
            btnAddIncome.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_add_24,0,0,0);
            btnAddIncome.setText("Agregar");
        }else {
            btnAddIncome.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_edit_24,0,0,0);
            btnAddIncome.setText("Actualizar");
            articleId = getIntent().getStringExtra("articleId");
            getDataDocument();
        }
        btnAddIncome.setOnClickListener(v -> {
            if (action.equals("a")){
                addIncome(user);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditIncomeActivity.this);
                builder.setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setTitle("Modificar Datos")
                        .setMessage("¿Estás seguro de modificar estos datos?")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            updateData();
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss()).show();
            }
        });
    }
    private void loadView(){
        btnAddIncome = findViewById(R.id.btnAddIncome);
        edtAmount = findViewById(R.id.edtAmountIncome);
        edtDescription = findViewById(R.id.edtDescriptionIncome);
        rbSave = findViewById(R.id.rb1I);
        rbNormal = findViewById(R.id.rb2I);
        rbLoan = findViewById(R.id.rb3I);
        rbOther = findViewById(R.id.rb4I);
    }
    private void getDataDocument(){
        mFirestore.collection("Incomes")
                .document(articleId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        double amount = documentSnapshot.getDouble("amount");
                        String description = documentSnapshot.getString("description");
                        String type = documentSnapshot.getString("type");
                        edtAmount.setText(String.valueOf(amount));
                        edtDescription.setText(description);
                        switch (Objects.requireNonNull(type)){
                            case "Cuenta de Ahorro":
                                rbSave.setChecked(true);
                                break;
                            case "Cuenta Corriente":
                                rbNormal.setChecked(true);
                                break;
                            case "Préstamo":
                                rbLoan.setChecked(true);
                                break;
                            case "Otro":
                                rbOther.setChecked(true);
                            default:
                                rbSave.setChecked(true);
                                break;
                        }
                    }
                });
    }
    private void addIncome(FirebaseUser user){

        String strDateFormat = "dd/MMM/yyyy hh:mm:ss a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        DecimalFormat df = new DecimalFormat("0.00");
        Date objdate = new Date();
        String type = null;
        Map<String, Object> map = new HashMap<>();
        String amount = edtAmount.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        if(amount.isEmpty()){
            edtAmount.setError("Campo vacío");
            return;
        }else{
            edtAmount.setError(null);
        }
        if(description.isEmpty()){
            edtDescription.setError("Campo vacío");
            return;
        }else{
            if (rbSave.isChecked()){
                type = rbSave.getText().toString();
            }else if (rbNormal.isChecked()){
                type = rbNormal.getText().toString();
            }else if (rbLoan.isChecked()){
                type = rbLoan.getText().toString();
            }else {
                type = rbOther.getText().toString();
            }
            double number = Double.parseDouble(amount);
            double decimals = Double.parseDouble(df.format(number));
            map.put("email", user.getEmail());
            map.put("amount", decimals);
            map.put("description", description);
            map.put("type", type);
            map.put("created", objSDF.format(objdate));
            map.put("modified", objSDF.format(objdate));
            mFirestore.collection("Incomes")
                    .add(map)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddOrEditIncomeActivity.this,"El ingreso se registró exitosamente.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddOrEditIncomeActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditIncomeActivity.this,"El ingreso no se puedo guardar en este momento.",Toast.LENGTH_LONG).show();
                    });
        }
    }
    private void updateData(){
        String strDateFormat = "dd/MMM/yyyy hh:mm:ss a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        DecimalFormat df = new DecimalFormat("0.00");
        Date objdate = new Date();
        Map<String, Object> map = new HashMap<>();
        String amount = edtAmount.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String type = null;
        if(amount.isEmpty()){
            edtAmount.setError("Campo vacío");
            return;
        }else{
            edtAmount.setError(null);
        }
        if(description.isEmpty()){
            edtDescription.setError("Campo vacío");
            return;
        }else{
            if (rbSave.isChecked()){
                type = rbSave.getText().toString();
            }else if (rbNormal.isChecked()){
                type = rbNormal.getText().toString();
            }else if (rbLoan.isChecked()){
                type = rbLoan.getText().toString();
            }else {
                type = rbOther.getText().toString();
            }
            double number = Double.parseDouble(amount);
            double decimals = Double.parseDouble(df.format(number));
            map.put("amount", decimals);
            map.put("description", description);
            map.put("type", type);
            map.put("modified", objSDF.format(objdate));
            mFirestore.collection("Incomes")
                    .document(articleId)
                    .update(map)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddOrEditIncomeActivity.this,"El ingreso se modificó exitosamente.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddOrEditIncomeActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditIncomeActivity.this,"El ingreso no se puedo modificar en este momento.",Toast.LENGTH_LONG).show();
                    });
        }
    }
}
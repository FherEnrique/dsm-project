package app.wallet.client.netux.sv.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.wallet.client.netux.sv.R;

import static app.wallet.client.netux.sv.Notifications.CHANNEL_1_ID;

public class AddOrEditBillActivity extends AppCompatActivity {
    Button btnAddBill;
    EditText edtAmount, edtDescription;
    RadioButton[] rbCartegory;
    RadioGroup rbGroup;
    FirebaseUser user;
    LinearLayout linearLayout;
    FirebaseFirestore mFirestore;
    private String action;
    private String articleId;
    NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_bill);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        notificationManager = NotificationManagerCompat.from(this);
        action = getIntent().getStringExtra("action");
        loadView();
        if (action.equals("a")){
            loadCategory(user);
            btnAddBill.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_add_24,0,0,0);
            btnAddBill.setText("Agregar");
        }else {
            btnAddBill.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_edit_24,0,0,0);
            btnAddBill.setText("Actualizar");
            articleId = getIntent().getStringExtra("articleId");
            getDataDocument();
        }
        btnAddBill.setOnClickListener(v -> {
            if (action.equals("a")){
                addBill(user);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditBillActivity.this);
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
        edtAmount = findViewById(R.id.edtAmountBill);
        edtDescription = findViewById(R.id.edtDescriptionBill);
        rbGroup = findViewById(R.id.radioGroupBill);
        btnAddBill = findViewById(R.id.btnAddBill);
        linearLayout = findViewById(R.id.linearradio);
    }
    private void loadCategory(FirebaseUser user){
       mFirestore.collection("Categories")
               .whereEqualTo("email",user.getEmail())
               .orderBy("created")
               .get()
               .addOnSuccessListener(queryDocumentSnapshots -> {
                   if (queryDocumentSnapshots.getDocuments().size() == 0){
                       AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditBillActivity.this);
                       builder.setIcon(R.drawable.ic_baseline_error_outline_24)
                               .setTitle("Advertencia")
                               .setMessage("No hay categorías de gastos registradas")
                               .setPositiveButton("Aceptar", (dialog, which) -> {
                                    Intent i = new Intent(AddOrEditBillActivity.this, DashboardActivity.class);
                                    startActivity(i);
                                    finish();
                               }).show();
                       btnAddBill.setEnabled(false);
                   }else {
                       List<String> list = new ArrayList<>();
                       for (int i= 0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                           String name = queryDocumentSnapshots.getDocuments().get(i).getString("name");
                           list.add(name);
                       }
                       rbCartegory = new RadioButton[list.size()];
                       for (int i = 0; i < list.size(); i++){
                           rbCartegory[i] = new RadioButton(this);
                           rbCartegory[i].setText(list.get(i));
                           if(i == 0){
                               rbCartegory[i].setChecked(true);
                           }
                           LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(
                                   RadioGroup.LayoutParams.WRAP_CONTENT,
                                   RadioGroup.LayoutParams.WRAP_CONTENT
                           );
                           rbCartegory[i].setLayoutParams(params);
                           rbCartegory[i].setTag(list.get(i));
                           rbCartegory[i].setId(i);
                           rbGroup.addView(rbCartegory[i]);
                       }
                   }
               });

    }
    private void addBill(FirebaseUser user){
        String strDateFormat = "dd/MMM/yyyy hh:mm:ss a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        DecimalFormat df = new DecimalFormat("0.00");
        Date objdate = new Date();
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
            double number = Double.parseDouble(amount);
            double decimals = Double.parseDouble(df.format(number));
            String category = "";
            for (int j =0; j<rbCartegory.length; j++){
                if(rbCartegory[j].isChecked()){
                    category = rbCartegory[j].getText().toString();
                }
            }
            map.put("email", user.getEmail());
            map.put("amount", decimals);
            map.put("description", description);
            map.put("category", category);
            map.put("created", objSDF.format(objdate));
            map.put("modified", objSDF.format(objdate));
            mFirestore.collection("Bills")
                    .add(map)
                    .addOnSuccessListener(documentReference -> {
                        executeNotification();
                        Toast.makeText(AddOrEditBillActivity.this,"El gasto se registró exitosamente.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddOrEditBillActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditBillActivity.this,"El gasto no se puedo guardar en este momento.",Toast.LENGTH_LONG).show();
                    });
        }

    }
    private  void getDataDocument(){
        mFirestore.collection("Bills")
                .document(articleId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        double amount = documentSnapshot.getDouble("amount");
                        String description = documentSnapshot.getString("description");
                        String category = documentSnapshot.getString("category");
                        edtAmount.setText(String.valueOf(amount));
                        edtDescription.setText(description);
                        mFirestore.collection("Categories")
                                .whereEqualTo("email",user.getEmail())
                                .orderBy("created")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    List<String> list = new ArrayList<>();
                                    for (int i= 0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                                        String name = queryDocumentSnapshots.getDocuments().get(i).getString("name");
                                        list.add(name);
                                    }
                                    rbCartegory = new RadioButton[list.size()];
                                    for (int i = 0; i < list.size(); i++){
                                        rbCartegory[i] = new RadioButton(this);
                                        rbCartegory[i].setText(list.get(i));
                                        if(rbCartegory[i].getText().equals(category)){
                                            rbCartegory[i].setChecked(true);
                                        }
                                        LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(
                                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                                RadioGroup.LayoutParams.WRAP_CONTENT
                                        );
                                        rbCartegory[i].setLayoutParams(params);
                                        rbCartegory[i].setTag(list.get(i));
                                        rbCartegory[i].setId(i);
                                        rbGroup.addView(rbCartegory[i]);
                                    }
                                });
                    }
                });
    }
    private void updateData(){
        String strDateFormat = "dd/MMM/yyyy hh:mm:ss a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        DecimalFormat df = new DecimalFormat("0.00");
        Date objdate = new Date();
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
            double number = Double.parseDouble(amount);
            double decimals = Double.parseDouble(df.format(number));
            String category = "";
            for (int j =0; j<rbCartegory.length; j++){
                if(rbCartegory[j].isChecked()){
                    category = rbCartegory[j].getText().toString();
                }
            }
            map.put("amount", decimals);
            map.put("description", description);
            map.put("category", category);
            map.put("modified", objSDF.format(objdate));
            mFirestore.collection("Bills")
                    .document(articleId)
                    .update(map)
                    .addOnSuccessListener(documentReference -> {
                        executeNotification();
                        Toast.makeText(AddOrEditBillActivity.this,"El gasto se modificó exitosamente.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddOrEditBillActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditBillActivity.this,"El gasto no se puedo modificar en este momento.",Toast.LENGTH_LONG).show();
                    });
        }

    }
    public void executeNotification(){
        DecimalFormat df = new  DecimalFormat("0.00");
        mFirestore.collection("Incomes")
                .whereEqualTo("email",user.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() != 0){
                        double amounts = 0.0;
                        for (int k = 0; k<queryDocumentSnapshots.getDocuments().size(); k++){
                            amounts += queryDocumentSnapshots.getDocuments().get(k).getDouble("amount");
                        }
                        double finalAmounts = amounts;
                        mFirestore.collection("Bills")
                                .whereEqualTo("email",user.getEmail())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    if (queryDocumentSnapshots1.getDocuments().size() != 0){
                                        double amountsB = 0.0;
                                        for (int k = 0; k<queryDocumentSnapshots1.getDocuments().size(); k++){
                                            amountsB += queryDocumentSnapshots1.getDocuments().get(k).getDouble("amount");
                                        }
                                        if (finalAmounts != 0){
                                            double summary = finalAmounts - amountsB;
                                            String message = "";
                                            if (summary < 0){
                                                message = "Su economía está en números rojos.";
                                            }else if (summary == 0){
                                                message = "Tú economía está en equilibrio";
                                            }else {
                                                double percent = (summary/finalAmounts) *100;
                                                message = "Ingresos disponibles en: " + df.format(percent) + "%";
                                            }
                                            Notification notification = new  NotificationCompat.Builder(this, CHANNEL_1_ID)
                                                    .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                                                    .setContentTitle("Balance General")
                                                    .setContentText(message)
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                    .build();
                                            notificationManager.notify(1, notification);
                                        }
                                    }
                                });
                    }
                });
    }
}

package app.wallet.client.netux.sv.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.wallet.client.netux.sv.R;

public class AddOrEditCategoryActivity extends AppCompatActivity {

    Button btnAddCategory;
    EditText edtName, edtDescription;
    FirebaseFirestore mFirestore;
    private String action;
    private String articleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        loadView();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        action = getIntent().getStringExtra("action");
        if (action.equals("a")){
            btnAddCategory.setText("Agregar");
            btnAddCategory.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_add_24,0,0,0);
        }else{
            articleId = getIntent().getStringExtra("articleId");
            getDataDocument(user);
            btnAddCategory.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_edit_24,0,0,0);
            btnAddCategory.setText("Actualizar");
        }
        btnAddCategory.setOnClickListener(v -> {
            if (action.equals("a")){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditCategoryActivity.this);
                builder.setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setTitle("Guardar Datos")
                        .setMessage("Recuerde que ya no podrá cambiar el nombre de la categoría cuando se asocie a un gasto.")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            addCategory(user);
                        }).show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditCategoryActivity.this);
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
        btnAddCategory = findViewById(R.id.btnAddCategory);
        edtName = findViewById(R.id.edtNameCategory);
        edtDescription = findViewById(R.id.edtDescriptionCategory);
    }
    private void addCategory(FirebaseUser user){
        String strDateFormat = "dd/MMM/yyyy hh:mm:ss a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        Date objdate = new Date();
        Map<String, Object> map = new HashMap<>();
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        if(name.isEmpty()){
            edtName.setError("Campo vacío");
            return;
        }else{
            edtName.setError(null);
        }
        if(description.isEmpty()){
            edtDescription.setError("Campo vacío");
            return;
        }else{
            map.put("email", user.getEmail());
            map.put("name", name);
            map.put("description", description);
            map.put("created", objSDF.format(objdate));
            map.put("modified", objSDF.format(objdate));
            mFirestore.collection("Categories")
                    .add(map)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddOrEditCategoryActivity.this,"La categoría se registró exitosamente.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddOrEditCategoryActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditCategoryActivity.this,"La categoría no se puedo guardar en este momento.",Toast.LENGTH_LONG).show();
                    });
        }

    }
    private void  getDataDocument(FirebaseUser user){
        mFirestore.collection("Categories")
                .document(articleId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String name = documentSnapshot.getString("name");
                        String description = documentSnapshot.getString("description");
                        mFirestore.collection("Bills")
                                .whereEqualTo("email", user.getEmail())
                                .whereEqualTo("category", name)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (queryDocumentSnapshots.getDocuments().size() == 0){
                                        edtName.setText(name);
                                    }else{
                                        edtName.setText(name);
                                        edtName.setEnabled(false);
                                    }
                                });
                        edtDescription.setText(description);
                    }
                });
    }
    private void updateData(){
        String strDateFormat = "dd/MMM/yyyy hh:mm:ss a";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        Date objdate = new Date();
        Map<String, Object> map = new HashMap<>();
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        if(name.isEmpty()){
            edtName.setError("Campo vacío");
            return;
        }else{
            edtName.setError(null);
        }
        if(description.isEmpty()){
            edtDescription.setError("Campo vacío");
            return;
        }else{
            map.put("name", name);
            map.put("description", description);
            map.put("modified", objSDF.format(objdate));
            mFirestore.collection("Categories")
                    .document(articleId)
                    .update(map)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddOrEditCategoryActivity.this,"La categoría se modificó exitosamente.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AddOrEditCategoryActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddOrEditCategoryActivity.this,"La categoría no se puedo modificar en este momento.",Toast.LENGTH_LONG).show();
                    });
        }
    }
}
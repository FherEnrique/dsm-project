package app.wallet.client.netux.sv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.models.Income;
import app.wallet.client.netux.sv.user.AddOrEditIncomeActivity;

public class IncomeAdapter extends FirestoreRecyclerAdapter<Income,IncomeAdapter.ViewHolder> {

    Context context;
    public IncomeAdapter(@NonNull FirestoreRecyclerOptions<Income> options, Context context){
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull IncomeAdapter.ViewHolder holder, int position, @NonNull Income income) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String id = documentSnapshot.getId();
        DecimalFormat df = new DecimalFormat("0.00");
        holder.tvType.setText(income.getType() + " - $" + df.format(income.getAmount()));
        holder.tvCreated.setText("Modificado: " + income.getCreated());
        holder.tvModified.setText("Creado: " + income.getModified());
        holder.tvDes.setText(income.getDescription());
        holder.btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(context, AddOrEditIncomeActivity.class);
            i.putExtra("action", "e");
            i.putExtra("articleId", id);
            context.startActivity(i);
        });
        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_baseline_delete_forever_24)
                    .setTitle("Eliminar Dato")
                    .setMessage("¿Estás seguro eliminar estos datos?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        deleteData(id);
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss()).show();
        });
    }

    @NonNull
    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_incomes,parent,false);
        return new IncomeAdapter.ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton btnEdit, btnDelete;
        TextView tvType, tvDes, tvCreated, tvModified;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            btnEdit = itemView.findViewById(R.id.editIncome);
            btnDelete = itemView.findViewById(R.id.deleteIncome);
            tvType = itemView.findViewById(R.id.tvTypeIncome);
            tvCreated = itemView.findViewById(R.id.tvCreatedIncome);
            tvModified = itemView.findViewById(R.id.tvModifiedIncome);
            tvDes = itemView.findViewById(R.id.tvDescriptionIncome);
        }
    }
    private void deleteData(String id){
        FirebaseFirestore mFirestore;
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Incomes")
                .document(id)
                .delete()
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context,"El ingreso se eliminó exitosamente.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context,"El ingreso no se puedo eliminar en este momento.",Toast.LENGTH_LONG).show();
                });
    }
}

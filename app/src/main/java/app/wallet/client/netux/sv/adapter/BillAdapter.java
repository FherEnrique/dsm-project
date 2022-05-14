package app.wallet.client.netux.sv.adapter;

import android.app.Notification;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.models.Bill;
import app.wallet.client.netux.sv.user.AddOrEditBillActivity;

import static app.wallet.client.netux.sv.Notifications.CHANNEL_1_ID;


public class BillAdapter extends FirestoreRecyclerAdapter<Bill,BillAdapter.ViewHolder> {
    Context context;
    public BillAdapter(@NonNull FirestoreRecyclerOptions<Bill> options, Context context){
        super(options);
        this.context = context;
    }
    @NonNull
    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_bills,parent,false);
        return new BillAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position, @NonNull Bill bill) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String id = documentSnapshot.getId();
        DecimalFormat df = new DecimalFormat("0.00");
        holder.tvCatBill.setText(bill.getCategory() + " - $" + df.format(bill.getAmount()));
        holder.tvDesBill.setText(bill.getDescription());
        holder.tvCreated.setText("Creado: " + bill.getCreated());
        holder.tvModified.setText("Modificado: " + bill.getModified());
        holder.btnEditBill.setOnClickListener(v -> {
            Intent i = new Intent(context, AddOrEditBillActivity.class);
            i.putExtra("action", "e");
            i.putExtra("articleId", id);
            context.startActivity(i);
        });
        holder.btnDeleteBill.setOnClickListener(v -> {
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCatBill, tvDesBill, tvCreated, tvModified;
        ImageButton btnEditBill, btnDeleteBill;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvCatBill = itemView.findViewById(R.id.tvCatBill);
            tvDesBill = itemView.findViewById(R.id.tvDescriptionBill);
            tvCreated = itemView.findViewById(R.id.tvCreatedBill);
            tvModified = itemView.findViewById(R.id.tvModifiedBill);
            btnEditBill = itemView.findViewById(R.id.editBill);
            btnDeleteBill = itemView.findViewById(R.id.deleteBill);
        }
    }
    private void deleteData(String id){
        FirebaseFirestore mFirestore;
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("Bills")
                .document(id)
                .delete()
                .addOnSuccessListener(documentReference -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    executeNotification(user);
                    Toast.makeText(context,"El gasto se eliminó exitosamente.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context,"El gasto no se puedo eliminar en este momento.",Toast.LENGTH_LONG).show();
                });
    }
    private void executeNotification(FirebaseUser user){
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
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
                                            Notification notification = new  NotificationCompat.Builder(context, CHANNEL_1_ID)
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

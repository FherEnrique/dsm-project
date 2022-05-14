package app.wallet.client.netux.sv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.models.Category;
import app.wallet.client.netux.sv.user.AddOrEditCategoryActivity;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.ViewHolder> {

    Context context;
    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options, Context context){
        super(options);
        this.context = context;
    }
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_categories,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position, @NonNull Category category) {
        DocumentSnapshot articleDocument = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = articleDocument.getId();
        holder.tvTitleC.setText(category.getName());
        holder.tvDes.setText(category.getDescription());
        holder.tvCreatedC.setText("Creado: "+ category.getCreated());
        holder.tvModifiedC.setText("Modificado: "+ category.getModified());
        holder.btnEditCategory.setOnClickListener(v -> {
            Intent i = new Intent(context, AddOrEditCategoryActivity.class);
            i.putExtra("action", "e");
            i.putExtra("articleId", id);
            context.startActivity(i);
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton btnEditCategory;
        TextView tvTitleC, tvDes, tvCreatedC, tvModifiedC;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitleC = itemView.findViewById(R.id.tvTitleCategory);
            tvDes = itemView.findViewById(R.id.tvDescriptionCategory);
            tvCreatedC = itemView.findViewById(R.id.tvCreatedCategory);
            tvModifiedC = itemView.findViewById(R.id.tvModifiedCategory);
            btnEditCategory = itemView.findViewById(R.id.editCategory);
        }
    }
}

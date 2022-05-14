package app.wallet.client.netux.sv.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.adapter.CategoryAdapter;
import app.wallet.client.netux.sv.models.Category;
import app.wallet.client.netux.sv.user.AddOrEditCategoryActivity;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerViewListCategories;
    FirebaseFirestore mFirestore;
    CategoryAdapter cAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton btnAddCategory;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        btnAddCategory = view.findViewById(R.id.floatingAddCategory);
        recyclerViewListCategories = view.findViewById(R.id.listCategories);
        recyclerViewListCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddCategory.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddOrEditCategoryActivity.class);
            i.putExtra("action","a");
            startActivity(i);
        });
        Query query = mFirestore.
                collection("Categories")
                .whereEqualTo("email",user.getEmail())
                .orderBy("created");
        FirestoreRecyclerOptions<Category> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Category>()
                .setQuery(query,Category.class)
                .build();
        cAdapter = new CategoryAdapter(firestoreRecyclerOptions,getContext());
        cAdapter.notifyDataSetChanged();
        recyclerViewListCategories.setAdapter(cAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        cAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cAdapter.stopListening();
    }
}
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
import app.wallet.client.netux.sv.adapter.IncomeAdapter;
import app.wallet.client.netux.sv.models.Income;
import app.wallet.client.netux.sv.user.AddOrEditCategoryActivity;
import app.wallet.client.netux.sv.user.AddOrEditIncomeActivity;

public class IncomeFragment extends Fragment {

    RecyclerView recyclerViewIncomes;
    IncomeAdapter iAdapter;
    FirebaseFirestore mFirestore;
    public IncomeFragment() {
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
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton btnAddIncome;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        btnAddIncome = view.findViewById(R.id.floatingAddIncome);
        recyclerViewIncomes = view.findViewById(R.id.listIncomes);
        recyclerViewIncomes.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddIncome.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddOrEditIncomeActivity.class);
            i.putExtra("action","a");
            startActivity(i);
        });
        Query query = mFirestore.
                collection("Incomes")
                .whereEqualTo("email",user.getEmail())
                .orderBy("created");
        FirestoreRecyclerOptions<Income> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Income>()
                .setQuery(query,Income.class)
                .build();
        iAdapter = new IncomeAdapter(firestoreRecyclerOptions,getContext());
        iAdapter.notifyDataSetChanged();
        recyclerViewIncomes.setAdapter(iAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        iAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        iAdapter.stopListening();
    }
}
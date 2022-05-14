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
import app.wallet.client.netux.sv.adapter.BillAdapter;
import app.wallet.client.netux.sv.models.Bill;
import app.wallet.client.netux.sv.user.AddOrEditBillActivity;



public class BillsFragment extends Fragment {


    RecyclerView recyclerViewBills;
    FirebaseFirestore mFirestore;
    BillAdapter bAdapter;

    public BillsFragment() {
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
        return inflater.inflate(R.layout.fragment_bills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton btnAddBill;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        btnAddBill = view.findViewById(R.id.floatingAddBill);
        recyclerViewBills = view.findViewById(R.id.listBills);
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddBill.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddOrEditBillActivity.class);
            i.putExtra("action","a");
            startActivity(i);
        });
        Query query = mFirestore.
                collection("Bills")
                .whereEqualTo("email",user.getEmail())
                .orderBy("created");
        FirestoreRecyclerOptions<Bill> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Bill>()
                .setQuery(query,Bill.class)
                .build();
        bAdapter = new BillAdapter(firestoreRecyclerOptions,getContext());
        bAdapter.notifyDataSetChanged();
        recyclerViewBills.setAdapter(bAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        bAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        bAdapter.stopListening();
    }
}
package app.wallet.client.netux.sv.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import app.wallet.client.netux.sv.R;

public class ChartsFragment extends Fragment {

    FirebaseFirestore mFirestore;
    public ChartsFragment() {
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
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        PieChart pieChart = view.findViewById(R.id.pieChart);
        mFirestore.collection("Incomes")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double income = 0;
                    for (int i= 0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                        double amount = queryDocumentSnapshots.getDocuments().get(i).getDouble("amount");
                        income += amount;
                    }
                    double finalIncome = income;
                    mFirestore.collection("Bills")
                            .whereEqualTo("email", user.getEmail())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                double bill = 0;
                                for (int i= 0; i<queryDocumentSnapshots1.getDocuments().size(); i++){
                                    double amount1 = queryDocumentSnapshots1.getDocuments().get(i).getDouble("amount");
                                    bill += amount1;
                                }
                                ArrayList<PieEntry> balance = new ArrayList<>();
                                balance.add(new PieEntry((float) finalIncome, "Ingresos"));
                                balance.add(new PieEntry((float) bill, "Gastos"));
                                PieDataSet pieDataSet = new PieDataSet(balance, "Leyenda");
                                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                pieDataSet.setValueTextColor(Color.WHITE);
                                pieDataSet.setValueTextSize(16f);

                                PieData pieData = new PieData(pieDataSet);
                                pieChart.setData(pieData);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setCenterText("Balance General USD($)");
                                pieChart.animate();
                            });
                });
    }


}
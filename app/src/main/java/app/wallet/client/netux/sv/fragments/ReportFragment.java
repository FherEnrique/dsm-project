package app.wallet.client.netux.sv.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import app.wallet.client.netux.sv.R;

public class ReportFragment extends Fragment {

    public ReportFragment() {
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
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    TextView incomeFinal, billFinal, balanceFinal, state;
    TextView[] tvIncomes;
    TextView[] tvBills;
    FirebaseFirestore mFirestore;
    LinearLayout vBill, vIncome, vBalance;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        vBill = view.findViewById(R.id.viewBill);
        vIncome = view.findViewById(R.id.viewIncome);
        vBalance = view.findViewById(R.id.viewBalance);
        loadIncomes(user);
        loadBill(user);
        loadBalance(user);

    }
    private void loadIncomes(FirebaseUser user){
        mFirestore.collection("Incomes")
                .whereEqualTo("email",user.getEmail())
                .orderBy("created")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    DecimalFormat df = new DecimalFormat("0.00");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    incomeFinal = new TextView(getContext());
                    if (queryDocumentSnapshots.getDocuments().size() == 0){
                        incomeFinal.setText("No hay ingresos registrados");
                    }else {
                        double summaryIncome = 0;
                        for (int i= 0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                            double amount = queryDocumentSnapshots.getDocuments().get(i).getDouble("amount");
                            summaryIncome += amount;
                        }
                        String[] types = {"Cuenta de Ahorro","Cuenta Corriente","Préstamo","Otro"};
                        tvIncomes = new TextView[4];
                        for (int k = 0; k<types.length; k++){
                            int finalK = k;
                            mFirestore.collection("Incomes")
                                    .whereEqualTo("email", user.getEmail())
                                    .whereEqualTo("type", types[k])
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        tvIncomes[finalK] = new TextView(getContext());
                                        if (queryDocumentSnapshots1.getDocuments().size() == 0){
                                            tvIncomes[finalK].setText(types[finalK] + ": $" + df.format(0.00));
                                        }else{
                                            double summary = 0.0;
                                            for (int h = 0; h<queryDocumentSnapshots1.getDocuments().size(); h++){
                                                double amount = queryDocumentSnapshots1.getDocuments().get(h).getDouble("amount");
                                                summary += amount;
                                            }
                                            tvIncomes[finalK].setText(types[finalK] + ": $" + df.format(summary));
                                        }
                                        tvIncomes[finalK].setTextColor(Color.BLACK);
                                        tvIncomes[finalK].setTextSize(16);
                                        tvIncomes[finalK].setLayoutParams(params);
                                        vIncome.addView(tvIncomes[finalK]);
                                    });
                        }
                        incomeFinal.setText("Total de Ingresos: $" + df.format(summaryIncome));
                    }
                    incomeFinal.setTextColor(Color.BLACK);
                    incomeFinal.setTextSize(16);
                    incomeFinal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    incomeFinal.setLayoutParams(params);
                    vIncome.addView(incomeFinal);
                });
    }
    private void loadBill(FirebaseUser user){
        mFirestore.collection("Categories")
                .whereEqualTo("email",user.getEmail())
                .orderBy("created")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    DecimalFormat df = new DecimalFormat("0.00");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    billFinal = new TextView(getContext());
                    if (queryDocumentSnapshots.getDocuments().size() == 0){
                        billFinal.setText("No hay categorías registradas");
                    }else {
                        List<String> list = new ArrayList<>();
                        for (int l= 0; l<queryDocumentSnapshots.getDocuments().size(); l++){
                            String name = queryDocumentSnapshots.getDocuments().get(l).getString("name");
                            list.add(name);
                        }
                        mFirestore.collection("Bills")
                                .whereEqualTo("email",user.getEmail())
                                .orderBy("created")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    if (queryDocumentSnapshots1.getDocuments().size() == 0){
                                        billFinal.setText("No hay gastos registrados");
                                    }else {
                                        double summaryBill = 0;
                                        for (int i= 0; i<queryDocumentSnapshots1.getDocuments().size(); i++){
                                            double amount = queryDocumentSnapshots1.getDocuments().get(i).getDouble("amount");
                                            summaryBill += amount;
                                        }
                                        tvBills = new TextView[list.size()];
                                        for (int k = 0; k<list.size(); k++){
                                            int finalK = k;
                                            mFirestore.collection("Bills")
                                                    .whereEqualTo("email", user.getEmail())
                                                    .whereEqualTo("category", list.get(k))
                                                    .get()
                                                    .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                                        tvBills[finalK] = new TextView(getContext());
                                                        if (queryDocumentSnapshots2.getDocuments().size() == 0){
                                                            tvBills[finalK].setText(list.get(finalK) + ": $" + df.format(0.00));
                                                        }else{
                                                            double summary = 0.0;
                                                            for (int h = 0; h<queryDocumentSnapshots2.getDocuments().size(); h++){
                                                                double amount = queryDocumentSnapshots2.getDocuments().get(h).getDouble("amount");
                                                                summary += amount;
                                                            }
                                                            tvBills[finalK].setText(list.get(finalK) + ": $" + df.format(summary));
                                                        }
                                                        tvBills[finalK].setTextColor(Color.BLACK);
                                                        tvBills[finalK].setTextSize(16);
                                                        tvBills[finalK].setLayoutParams(params);
                                                        vBill.addView(tvBills[finalK]);
                                                    });
                                        }
                                        billFinal.setText("Total de Gastos: $" + df.format(summaryBill));
                                    }
                                });
                    }
                    billFinal.setTextColor(Color.BLACK);
                    billFinal.setTextSize(16);
                    billFinal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    billFinal.setLayoutParams(params);
                    vBill.addView(billFinal);
                });
    }

    private void loadBalance(FirebaseUser user){
        final double[] summaryIncome = {0};
        final double[] summaryBill = {0.0};
        mFirestore.collection("Incomes")
                .whereEqualTo("email",user.getEmail())
                .orderBy("created")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    DecimalFormat df = new DecimalFormat("0.00");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    balanceFinal = new TextView(getContext());
                    if (queryDocumentSnapshots.getDocuments().size() == 0){
                        balanceFinal.setText("No hay ingresos registrados");
                    }else {
                        for (int i= 0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                            double amount = queryDocumentSnapshots.getDocuments().get(i).getDouble("amount");
                            summaryIncome[0] += amount;
                        }
                        mFirestore.collection("Bills")
                                .whereEqualTo("email",user.getEmail())
                                .orderBy("created")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    if (queryDocumentSnapshots1.getDocuments().size() == 0){
                                        balanceFinal.setText("No hay gastos registrados");
                                    }else {
                                        for (int i= 0; i<queryDocumentSnapshots1.getDocuments().size(); i++){
                                            double amount = queryDocumentSnapshots1.getDocuments().get(i).getDouble("amount");
                                            summaryBill[0] += amount;
                                        }
                                        double summaryFinal = summaryIncome[0] - summaryBill[0];
                                        balanceFinal.setText("Total: $" + df.format(summaryFinal));
                                        if (summaryFinal < 0){
                                            state = new TextView(getContext());
                                            state.setText("Su economía está en números rojos.");
                                            state.setTextColor(Color.RED);
                                            state.setTextSize(20);
                                            state.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            state.setLayoutParams(params);
                                            vBalance.addView(state);
                                        }
                                    }
                                });
                    }
                    balanceFinal.setTextColor(Color.BLACK);
                    balanceFinal.setTextSize(22);
                    balanceFinal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    balanceFinal.setLayoutParams(params);
                    vBalance.addView(balanceFinal);
                });
    }
}
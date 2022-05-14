package app.wallet.client.netux.sv.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.auth.LoginActivity;
import app.wallet.client.netux.sv.fragments.BillsFragment;
import app.wallet.client.netux.sv.fragments.CategoryFragment;
import app.wallet.client.netux.sv.fragments.ChartsFragment;
import app.wallet.client.netux.sv.fragments.IncomeFragment;
import app.wallet.client.netux.sv.fragments.ReportFragment;
import app.wallet.client.netux.sv.fragments.SettingsFragment;

public class DashboardActivity extends AppCompatActivity {

    //nos ayudara a cambiar de fragments
    FragmentTransaction transaction;
    Fragment fragmentBills, fragmentCategories, fragmentSettings, fragmentIncome, fragmentChart, fragmentReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fragmentIncome = new IncomeFragment();
        fragmentChart = new ChartsFragment();
        fragmentBills = new BillsFragment();
        fragmentCategories = new CategoryFragment();
        fragmentSettings = new SettingsFragment();
        fragmentReport = new ReportFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentsContainer, fragmentBills)
                .commit();

    }
    public void onClick(View view){
        transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId())
        {
            case R.id.btnBills:
                transaction.replace(R.id.fragmentsContainer,fragmentBills);
                break;
            case R.id.btnCategories:
                transaction.replace(R.id.fragmentsContainer,fragmentCategories);
                break;
            case R.id.btnSettings:
                transaction.replace(R.id.fragmentsContainer,fragmentSettings);
                break;
            case  R.id.btnCharts:
                transaction.replace(R.id.fragmentsContainer,fragmentChart);
                break;
            case R.id.btnIncomes:
                transaction.replace(R.id.fragmentsContainer,fragmentIncome);
                break;
            case R.id.btnReport:
                transaction.replace(R.id.fragmentsContainer,fragmentReport);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
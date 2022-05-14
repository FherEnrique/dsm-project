package app.wallet.client.netux.sv.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.auth.LoginActivity;
import app.wallet.client.netux.sv.user.TutorialActivity;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvEmailAccount = view.findViewById(R.id.tvAccountEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Button btnLogout, btnTutor;
        btnLogout  = view.findViewById(R.id.btnLogout);
        btnTutor = view.findViewById(R.id.btnTutorial);
        ImageView photo = view.findViewById(R.id.photoProfile);
        if(user != null){
            tvEmailAccount.setText(user.getEmail());
            if(user.getPhotoUrl() != null){
                Picasso.get().load(user.getPhotoUrl()).into(photo);
            }else {
                photo.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }
        }
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.ic_baseline_error_outline_24)
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro de cerrar tu sesión actual?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss()).show();
        });
        btnTutor.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), TutorialActivity.class);
            startActivity(i);
            getActivity().finish();
        });
    }
}
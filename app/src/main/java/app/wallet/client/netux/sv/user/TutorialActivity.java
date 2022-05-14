package app.wallet.client.netux.sv.user;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import app.wallet.client.netux.sv.R;
import app.wallet.client.netux.sv.adapter.MyViewPagerAdapter;
import app.wallet.client.netux.sv.fragments.SliderFragment;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private LinearLayout dotLayout;
    private Button btnBack, btnNext;
    private String[] titles = {
            "Bienvenid@",
            "Ingresos",
            "Gastos",
            "Categorías",
            "Informes",
            "Gráficos estadísticos"
    };
    private String[] contents = {
            "Muchas gracias por unirte a nuestra aplicación, te ayudaremos a encontrar tu equilibrio financiero.",
            "Es prácticamente el dinero activo que tienes en tus cuentas de ahorros, crédito y préstamos.",
            "Son todas los pasivos(deudas) que ya has pagado o posiblemente en un futuro.",
            "Podrás clasificar tus ingresos y gastos para tener un mayor control de tu dinero actual",
            "Tendrás acceso a revisar cada gasto o inversión realizado con todos los detalles",
            "Visualmente se mostrarán gráficos para observar tus movimientos financieros de manera general y específica."
    };
    private int[] images = {
            R.drawable.ic_baseline_emoji_emotions_24,
            R.drawable.ic_baseline_payment_24,
            R.drawable.ic_baseline_monetization_on_24,
            R.drawable.ic_baseline_local_offer_24,
            R.drawable.ic_baseline_description_24,
            R.drawable.ic_baseline_pie_chart_24
    };
    private int[] colorsBackground,colorDot;
    private TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        loadView();
        loadViewPager();
        btnBack.setOnClickListener(v -> {
            if(viewPager.getCurrentItem() == 0){
                //se coloca el intent de la otra actividad
                Intent i = new Intent(TutorialActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }else{
                int back = getItem(-1);
                viewPager.setCurrentItem(back);
            }
        });
        btnNext.setOnClickListener(v -> {
            int next = getItem(+1);
            if(next <titles.length){
                viewPager.setCurrentItem(next);
            }else{
                //se coloca el intent de la otra actividad
                Intent i = new Intent(TutorialActivity.this,DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }
    private  void  loadView(){
        viewPager = findViewById(R.id.ViewPager);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        dotLayout = findViewById(R.id.layoutDots);
        colorsBackground = getResources().getIntArray(R.array.array_background);
        colorDot = getResources().getIntArray(R.array.array_dot);
        addDots(0);
    }
    private void loadViewPager(){
        adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        for (int i=0; i<titles.length;i++){
            adapter.addFragment(newInstance(titles[i],contents[i],images[i],colorsBackground[i]));
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
                if(position == titles.length-1){
                    btnNext.setText("Finalizar");
                    btnBack.setText("Atrás");
                }else if(position == 0){
                    btnNext.setText("Siguiente");
                    btnBack.setText("Saltar");
                }else{
                    btnNext.setText("Siguiente");
                    btnBack.setText("Atrás");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private SliderFragment newInstance(String title, String content, int Image, int color){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("image", Image);
        bundle.putInt("color",color);

        SliderFragment fragment = new SliderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    private void addDots(int currentPage){
        dots = new TextView[titles.length];
        dotLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            if(i == currentPage){
                dots[i].setTextColor(colorDot[currentPage]);
            }else{
                dots[i].setTextColor(Color.LTGRAY);
            }
            dotLayout.addView(dots[i]);
        }
    }

}
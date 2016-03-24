package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;

import c.proyecto.R;
import c.proyecto.models.Anuncio;
import c.proyecto.pojo.Prestacion;

public class CrearAnuncio1Activity extends AppCompatActivity {

    private static final String INTENT_ANUNCIO = "intent anuncio1";
    private Anuncio mAnuncio;
    private ImageView imgSiguiente;
    private ImageView imgPrincipal;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;

    public static void start(Context context, @Nullable Anuncio anuncio){
        Intent intent = new Intent(context, CrearAnuncio1Activity.class);
        intent.putExtra(INTENT_ANUNCIO, anuncio);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio1);
        mAnuncio = getIntent().getParcelableExtra(INTENT_ANUNCIO);
        initViews();
    }

    private void initViews() {
        imgPrincipal = (ImageView) findViewById(R.id.imgPrincipal);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);


        imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearAnuncio2Activity.start(CrearAnuncio1Activity.this, mAnuncio);
            }
        });
    }

    private void showImageDialog(ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}

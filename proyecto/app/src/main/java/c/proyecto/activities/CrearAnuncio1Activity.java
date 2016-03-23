package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import c.proyecto.R;
import c.proyecto.models.Anuncio;
import c.proyecto.pojo.Prestacion;

public class CrearAnuncio1Activity extends AppCompatActivity {

    private static final String INTENT_ANUNCIO = "intent anuncio1";
    private Anuncio mAnuncio;
    private ImageView imgSiguiente;

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
        imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearAnuncio2Activity.start(CrearAnuncio1Activity.this, mAnuncio);
            }
        });
    }
}

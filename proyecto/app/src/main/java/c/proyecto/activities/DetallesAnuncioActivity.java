package c.proyecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import c.proyecto.R;
import c.proyecto.fragments.DetallesAnuncioFragment;
import c.proyecto.models.Anuncio;

public class DetallesAnuncioActivity extends AppCompatActivity {


    private static final String INTENT_ANUNCIO = "anuncio";

    public static void start(Context context, Anuncio anuncio){
        Intent intent = new Intent(context, DetallesAnuncioActivity.class);
        intent.putExtra(INTENT_ANUNCIO, anuncio);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_anuncio);
        Anuncio anuncio = getIntent().getParcelableExtra(INTENT_ANUNCIO);

        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, DetallesAnuncioFragment.newInstance(anuncio)).commit();
    }
}

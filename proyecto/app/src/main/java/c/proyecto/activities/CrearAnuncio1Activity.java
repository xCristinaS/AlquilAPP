package c.proyecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import c.proyecto.R;

public class CrearAnuncio1Activity extends AppCompatActivity {

    private ImageView imgSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio1);
        initViews();
    }

    private void initViews() {
        imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrearAnuncio1Activity.this, CrearAnuncio2Activity.class));
            }
        });
    }
}

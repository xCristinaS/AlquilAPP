package c.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import c.proyecto.R;
import c.proyecto.interfaces.InicioActivityOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.InicioPresenter;

public class InicioActivity extends AppCompatActivity implements InicioActivityOps {

    private InicioPresenter presentador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        initViews();
    }

    private void initViews() {
        presentador = InicioPresenter.getPresentador(this);
        findViewById(R.id.btnIniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.singInRequested("alejandro", "1234");
            }
        });

        findViewById(R.id.btnRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioActivity.this, RegistroActivity.class));
                //Usuario.createNewUser("cristina", "1234", "cristina", "sola");
                //Usuario.createNewUser("alejandro", "1234", "alejandro", "torres");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enter(Usuario u) {
/*
        for (int i = 0; i < 20; i++)
            Anuncio.createNewAnuncio("12052659", "titulo del anuncio " + i, "direccion de vivienda", "12", "poblacion", "provincia");
*/
        /*
        for (int i = 0; i < 8; i++)
            Anuncio.createNewAnuncio("-386798187", "titulo del anuncio " + i, "direccion de vivienda", "12", "poblacion", "provincia");
*/
        MainActivity.start(this, u);
    }
}

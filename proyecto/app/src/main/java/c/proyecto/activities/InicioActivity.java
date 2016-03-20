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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initViews() {
        presentador = InicioPresenter.getPresentador(this);
        findViewById(R.id.btnIniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.singInRequested("Pepe", "12245654");
            }
        });

        findViewById(R.id.btnRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presentador.createNewUser("Pepe", "12245654") != null)
                    Toast.makeText(InicioActivity.this,"Se ha creado", Toast.LENGTH_SHORT).show();
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
        MainActivity.start(this, u);
    }
}
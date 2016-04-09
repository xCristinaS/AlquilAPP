package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.github.florent37.materialtextfield.MaterialTextField;

import c.proyecto.Constantes;

import c.proyecto.R;
import c.proyecto.mvp_views_interfaces.InicioActivityOps;
import c.proyecto.mvp_models.Usuario;
import c.proyecto.mvp_presenters.InicioPresenter;

public class InicioActivity extends AppCompatActivity implements InicioActivityOps {

    private InicioPresenter presentador;
    private TextView txtUser;
    private TextView txtPass;
    private SwitchCompat swRememberMe;
    private SharedPreferences preferences;

    public static void start(Activity a){
        Intent intent = new Intent(a, InicioActivity.class);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        initViews();
        checkSavedUser();
        //Firebase f = new Firebase("https://proyectofinaldam.firebaseio.com").child("conversaciones").child("12052659").child("-386798187_Mi_anuncio").push();
        //f.setValue(new Message(new Date(), "Pos mu bien chico!"));
    }

    private void initViews() {
        preferences = getSharedPreferences(Constantes.NOMBRE_PREFERENCIAS, MODE_PRIVATE);
        presentador = InicioPresenter.getPresentador(this);
        swRememberMe = (SwitchCompat) findViewById(R.id.swRememberMe);
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtPass = (TextView) findViewById(R.id.txtPass);
        MaterialTextField mTUser = (MaterialTextField) findViewById(R.id.mTUser);
        MaterialTextField mTPass = (MaterialTextField) findViewById(R.id.mTPass);
        mTPass.expand();
        mTUser.expand();

        //Botón Iniciar sesión
        findViewById(R.id.btnIniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.singInRequested(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });
        //Botón Registrarse
        findViewById(R.id.btnRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioActivity.this, RegistroActivity.class));
            }
        });
    }

    private void checkSavedUser() {
        //Coloca el usuario guardado en el txtUser si el usuario activó el switch de recordar la última vez que inicio sesión.
        txtUser.setText(preferences.getString(Constantes.KEY_REMEMBER_ME, ""));
        //Si ha recordado el usuario dejará activado el swift
        if(!txtUser.getText().toString().isEmpty())
            swRememberMe.setChecked(true);
    }


    @Override
    public void enter(Usuario u) {
        SharedPreferences.Editor editor = preferences.edit();


        if(u != null){
            //Guardará en las preferencias el usuario para la próxima ves que entre.
            if(swRememberMe.isChecked())
                editor.putString(Constantes.KEY_REMEMBER_ME, txtUser.getText().toString());
            else
                editor.putString(Constantes.KEY_REMEMBER_ME, "");
            editor.apply();
            MainActivity.start(this, u);
            finish();
        }
        else
            Toast.makeText(this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

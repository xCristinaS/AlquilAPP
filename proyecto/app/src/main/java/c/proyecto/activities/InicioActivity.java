package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.github.florent37.materialtextfield.MaterialTextField;

import c.proyecto.Constantes;

import c.proyecto.R;
import c.proyecto.interfaces.MyInicio;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.InicioActivityOps;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.InicioPresenter;

public class InicioActivity extends AppCompatActivity implements InicioActivityOps, MyInicio {

    private TextView txtUser;
    private TextView txtPass;
    private SwitchCompat swRememberMe;
    private SharedPreferences preferences;
    private InicioPresenter presentador;
    private Button btnIniciar;
    private boolean loging;

    public static void start(Activity a) {
        Intent intent = new Intent(a, InicioActivity.class);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        loging = false;
        initViews();
        checkIfSavedUser();
    }

    private void initViews() {
        preferences = getSharedPreferences(Constantes.NOMBRE_PREFERENCIAS, MODE_PRIVATE);
        presentador = InicioPresenter.getPresentador(this);
        presentador.setUsersManager(new UsersFirebaseManager(presentador));
        swRememberMe = (SwitchCompat) findViewById(R.id.swRememberMe);
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtPass = (TextView) findViewById(R.id.txtPass);
        MaterialTextField mTUser = (MaterialTextField) findViewById(R.id.mTUser);
        MaterialTextField mTPass = (MaterialTextField) findViewById(R.id.mTPass);
        mTPass.expand();
        mTUser.expand();

        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        //Botón Iniciar sesión
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loging = true;
                presentador.signInRequested(txtUser.getText().toString(), txtPass.getText().toString());
                btnIniciar.setEnabled(false);
            }
        });
        //Botón Registrarse
        findViewById(R.id.btnRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loging)
                    startActivity(new Intent(InicioActivity.this, RegistroActivity.class));
            }
        });

    }

    private void checkIfSavedUser() {
        //Coloca el usuario guardado en el txtUser si el usuario activó el switch de recordar la última vez que inicio sesión.
        txtUser.setText(preferences.getString(Constantes.KEY_USER, ""));
        //Si ha recordado el usuario dejará activado el swift
        if (!preferences.getString(Constantes.KEY_PASS, "").isEmpty())
            swRememberMe.setChecked(true);
    }


    @Override
    public void enter(Object o) {
        SharedPreferences.Editor editor = preferences.edit();

        if (o instanceof Usuario) {
            //Guardará en las preferencias el usuario para la próxima ves que entre.
            editor.putString(Constantes.KEY_USER, txtUser.getText().toString());
            //Guardará la contraseña dependiendo si quiere que se conecte solo la próxima vez
            if (swRememberMe.isChecked())
                editor.putString(Constantes.KEY_PASS, txtPass.getText().toString());
            else
                editor.putString(Constantes.KEY_PASS, "");

            editor.apply();
            MainActivity.start(this, (Usuario) o);
            finish();
        } else {
            Toast.makeText(this, (String) o, Toast.LENGTH_SHORT).show();
            btnIniciar.setEnabled(true);
            loging = false;
        }
    }


    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.KEY_PASS, "");
        editor.apply();
        super.onBackPressed();
    }
}

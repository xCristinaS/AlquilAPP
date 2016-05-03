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
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.InicioActivityOps;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.InicioPresenter;

public class InicioActivity extends AppCompatActivity implements InicioActivityOps {

    private TextView txtUser;
    private TextView txtPass;
    private SwitchCompat swRememberMe;
    private SharedPreferences preferences;
    private InicioPresenter presentador;

    public static void start(Activity a){
        Intent intent = new Intent(a, InicioActivity.class);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
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

        //Botón Iniciar sesión
        findViewById(R.id.btnIniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.signInRequested(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });
        //Botón Registrarse
        findViewById(R.id.btnRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioActivity.this, RegistroActivity.class));
            }
        });
        //Iniciar sesión con google
        findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Iniciar sesión con twiter
        findViewById(R.id.btnTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.signInWithTwitterRequested(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });
        //Iniciar sesión con facebook
        findViewById(R.id.btnFace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.signInWithFacebookRequested(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });
    }

    private void checkIfSavedUser() {
        String user = preferences.getString(Constantes.KEY_USER, "");
        String pass = preferences.getString(Constantes.KEY_PASS, "");
        txtUser.setText(user);
        txtPass.setText(pass);
        
        if(!user.isEmpty()){
            swRememberMe.setChecked(true);
            presentador.signInRequested(user, pass);
        }
    }


    @Override
    public void enter(Usuario u) {
        SharedPreferences.Editor editor = preferences.edit();

        if(u != null){
            //Guardará en las preferencias el usuario para la próxima ves que entre.
            if(swRememberMe.isChecked()){
                editor.putString(Constantes.KEY_USER, txtUser.getText().toString());
                editor.putString(Constantes.KEY_PASS, txtPass.getText().toString());
            }
            else{
                editor.putString(Constantes.KEY_USER, "");
                editor.putString(Constantes.KEY_PASS, "");
            }
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

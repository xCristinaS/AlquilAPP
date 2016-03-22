package c.proyecto.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import c.proyecto.R;
import c.proyecto.interfaces.RegistroActivityOps;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.RegistroPresenter;

public class RegistroActivity extends AppCompatActivity implements RegistroActivityOps {

    private TextView lblCancelar;
    private TextView txtUser;
    private TextView txtPass;
    private TextView txtRepeatPass;
    private TextView txtNombre;
    private TextView txtApellidos;
    private Button btnRegistrarse;
    private RegistroPresenter presentador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        presentador = RegistroPresenter.getPresentador(this);
        initViews();
    }

    private void initViews() {
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtPass = (TextView) findViewById(R.id.txtPass);
        txtRepeatPass = (TextView) findViewById(R.id.txtRepeatPass);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtApellidos = (TextView) findViewById(R.id.txtApellidos);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        lblCancelar = (TextView) findViewById(R.id.lblCancelar);
        lblCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!areFieldsEmpty()){
                if (!existUser()) {
                    if(comprobarPass()){
                        Usuario u = presentador.register(txtUser.getText().toString(), txtPass.getText().toString(), txtNombre.getText().toString(), txtApellidos.getText().toString());
                        //Iniciar Sesion con el usuario creado
                        MainActivity.start(RegistroActivity.this, u);
                        finish();

                    }else
                        txtRepeatPass.setError("Las contraseñas no son iguales");
                } else
                    txtUser.setError("Este Usuario ya existe");
            }
            }
        });
    }

    private boolean areFieldsEmpty() {
        boolean empty = false;

        if(txtUser.getText().toString().isEmpty()){
            txtUser.setError("Rellene este campo");
            empty = true;
        }
        if(txtPass.getText().toString().isEmpty()){
            txtPass.setError("Rellene este campo");
            empty = true;
        }
        if(txtRepeatPass.getText().toString().isEmpty()){
            txtRepeatPass.setError("Rellene este campo");
            empty = true;
        }
        if(txtNombre.getText().toString().isEmpty()){
            txtNombre.setError("Rellene este campo");
            empty = true;
        }
        if(txtApellidos.getText().toString().isEmpty()){
            txtApellidos.setError("Rellene este campo");
            empty = true;
        }

        return empty;
    }
    private boolean existUser(){
        return presentador.checkUser(txtUser.getText().toString());
    }
    private boolean comprobarPass(){
        //Comprueba que la contraseña y la contraseña repetida sean iguales
        return !txtPass.getText().toString().isEmpty() && txtPass.getText().toString().equals(txtRepeatPass.getText().toString());
    }



}

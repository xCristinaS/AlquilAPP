package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import c.proyecto.R;
import c.proyecto.models.Usuario;

public class EditProfileActivity extends AppCompatActivity {

    private static final String ARG_USUARIO = "args_user";

    private Usuario user;
    private EditText lblNombre, lblApellidos;
    private ImageView imgFoto;

    public static void start(Activity a, Usuario u){
        Intent intent = new Intent(a, EditProfileActivity.class);
        intent.putExtra(ARG_USUARIO, u);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
        if (getIntent().hasExtra(ARG_USUARIO))
            user = getIntent().getParcelableExtra(ARG_USUARIO);
    }

    private void initViews() {

    }
}

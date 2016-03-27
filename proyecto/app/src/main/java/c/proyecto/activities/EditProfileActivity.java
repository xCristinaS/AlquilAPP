package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import c.proyecto.R;
import c.proyecto.fragments.CaracteristicasUsuarioDialogFragment;
import c.proyecto.models.Usuario;

public class EditProfileActivity extends AppCompatActivity {

    private static final String ARG_USUARIO = "args_user";
    private static final String TAG_DIALOG_HABITOS = "DialogHabitos";

    private EditText lblNombre, lblApellidos;
    private ImageView imgFoto;
    private TextView txtNombre;
    private TextView txtApellidos;
    private TextView txtFechaNac;
    private TextView txtNacionalidad;
    private TextView txtProfesion;
    private TextView txtComentDesc;
    private ImageView imgHabitos;
    private ImageView imgGenero;
    private Button btnConfirmar;

    private Usuario mUser;

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

        mUser = getIntent().getParcelableExtra(ARG_USUARIO);
        recuperarUser();

    }

    private void initViews() {
        imgFoto = (ImageView) findViewById(R.id.imgFoto);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtApellidos = (TextView) findViewById(R.id.txtApellidos);
        txtFechaNac = (TextView) findViewById(R.id.txtFechaNac);
        txtNacionalidad = (TextView) findViewById(R.id.txtNacionalidad);
        txtProfesion = (TextView) findViewById(R.id.txtProfesion);
        txtComentDesc =(TextView) findViewById(R.id.txtComentDesc);
        imgHabitos = (ImageView) findViewById(R.id.imgHabitos);
        imgGenero = (ImageView) findViewById(R.id.imgGenero);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        imgHabitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHabitosDialog();
            }
        });
    }

    private void recuperarUser() {
        Picasso.with(this).load(mUser.getFoto()).error(R.drawable.default_user).into(imgFoto);
        txtNombre.setText(mUser.getNombre());
        txtApellidos.setText(mUser.getApellidos());
        //MADRE MÍA esta cristina con los Date que no sabe programar -.-!!!  /////////////////////////////////////////////////////////////////////////////////
        //txtFechaNac.setText(mUser.getFecha_nacimiento());
        txtNacionalidad.setText(mUser.getNacionalidad());
        txtProfesion.setText(mUser.getProfesion());
        txtComentDesc.setText(mUser.getComentario_desc());

    }

    private void showHabitosDialog(){
        FragmentManager fm = getSupportFragmentManager();
        CaracteristicasUsuarioDialogFragment.newInstance(mUser).show(fm, TAG_DIALOG_HABITOS);
    }
}

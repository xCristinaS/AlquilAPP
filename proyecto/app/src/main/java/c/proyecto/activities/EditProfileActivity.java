package c.proyecto.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import c.proyecto.R;
import c.proyecto.fragments.CaracteristicasUsuarioDialogFragment;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.EditProfilePresenter;

public class EditProfileActivity extends AppCompatActivity {

    private static final String ARG_USUARIO = "args_user";
    private static final String TAG_DIALOG_HABITOS = "DialogHabitos";
    public static final String EXTRA_USER_RESULT = "extra_result_user";

    private EditText txtNombre, txtApellidos, txtFechaNac, txtNacionalidad, txtProfesion, txtComentDesc;
    private ImageView imgFoto, imgHabitos, imgGenero;
    private Button btnConfirmar;
    private Usuario mUser;
    private EditProfilePresenter mPresenter;

    public static void start(Activity a, Usuario u, int requestCode) {
        Intent intent = new Intent(a, EditProfileActivity.class);
        intent.putExtra(ARG_USUARIO, u);
        a.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
        mPresenter = EditProfilePresenter.getPresentador(this);
        mUser = getIntent().getParcelableExtra(ARG_USUARIO);
        recuperarUser();
    }

    private void initViews() {
        imgFoto = (ImageView) findViewById(R.id.imgFoto);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtFechaNac = (EditText) findViewById(R.id.txtFechaNac);
        txtNacionalidad = (EditText) findViewById(R.id.txtNacionalidad);
        txtProfesion = (EditText) findViewById(R.id.txtProfesion);
        txtComentDesc = (EditText) findViewById(R.id.txtComentDesc);
        imgHabitos = (ImageView) findViewById(R.id.imgHabitos);
        imgGenero = (ImageView) findViewById(R.id.imgGenero);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introducirDatosEnUser();
                mPresenter.updateUserProfile(mUser);
                finish();
            }
        });

        txtFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        imgHabitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHabitosDialog();
            }
        });
    }

    private void introducirDatosEnUser() {
        if (!TextUtils.isEmpty(txtNombre.getText()))
            mUser.setNombre(txtNombre.getText().toString());
        if (!TextUtils.isEmpty(txtApellidos.getText()))
            mUser.setApellidos(txtApellidos.getText().toString());
        if (!TextUtils.isEmpty(txtFechaNac.getText())){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                mUser.setFecha_nacimiento(format.parse(txtFechaNac.getText().toString()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void recuperarUser() {
        Picasso.with(this).load(mUser.getFoto()).error(R.drawable.default_user).into(imgFoto);
        txtNombre.setText(mUser.getNombre());
        txtApellidos.setText(mUser.getApellidos());
        txtNacionalidad.setText(mUser.getNacionalidad());
        txtProfesion.setText(mUser.getProfesion());
        txtComentDesc.setText(mUser.getComentario_desc());
        txtFechaNac.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(mUser.getFecha_nacimiento())));
    }

    private void showHabitosDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CaracteristicasUsuarioDialogFragment.newInstance(mUser).show(fm, TAG_DIALOG_HABITOS);
    }


    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        int year, mont, day;
        String[] fechaRecuperada;
        //Si el txt está vacio se mostrará por defecto la fecha actual.
        if (txtFechaNac.getText().toString().isEmpty()) {
            year = currentDate.get(Calendar.YEAR);
            mont = currentDate.get(Calendar.MONTH);
            day = currentDate.get(Calendar.DAY_OF_MONTH);
        } else {
            //Por el contrario, si ya contiene una fecha, mostrará esa por defecto para elegir a partir de esa.
            fechaRecuperada = txtFechaNac.getText().toString().split("/");
            year = Integer.valueOf(fechaRecuperada[2]);
            mont = Integer.valueOf(fechaRecuperada[1]) - 1;
            day = Integer.valueOf(fechaRecuperada[0]);
        }

        DatePickerDialog datePicker = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(selectedyear, selectedmonth, selectedday);
                Date date = cal.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtFechaNac.setText(String.format("%s", format.format(date)));
            }
        }, year, mont, day);
        datePicker.setTitle("Seleccione su fecha");
        datePicker.show();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_USER_RESULT, mUser));
        super.finish();
    }
}

package c.proyecto.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private ImageView imgFoto, imgCaracteristicas, imgGenero;
    private Button btnConfirmar;
    private Usuario mUser;
    private EditProfilePresenter mPresenter;
    private String[] mNationalities;

    public static void start(Activity a, Usuario u, int requestCode) {
        Intent intent = new Intent(a, EditProfileActivity.class);
        intent.putExtra(ARG_USUARIO, u);
        a.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mNationalities = new String[248];
        getNationalitiesFromFile();
        mPresenter = EditProfilePresenter.getPresentador(this);
        mUser = getIntent().getParcelableExtra(ARG_USUARIO);
        initViews();
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
        imgCaracteristicas = (ImageView) findViewById(R.id.imgHabitos);
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

        imgCaracteristicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaracteristicasDialog();
            }
        });

        txtNacionalidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNationalitiesDialog();
            }
        });

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void introducirDatosEnUser() {
        if (!TextUtils.isEmpty(txtNombre.getText()))
            mUser.setNombre(txtNombre.getText().toString());
        if (!TextUtils.isEmpty(txtApellidos.getText()))
            mUser.setApellidos(txtApellidos.getText().toString());
        if (!TextUtils.isEmpty(txtFechaNac.getText())) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                mUser.setFecha_nacimiento(format.parse(txtFechaNac.getText().toString()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(txtComentDesc.getText()))
            mUser.setComentario_desc(txtComentDesc.getText().toString());
        if (!TextUtils.isEmpty(txtProfesion.getText()))
            mUser.setProfesion(txtProfesion.getText().toString());
        if (!TextUtils.isEmpty(txtNacionalidad.getText()))
            mUser.setNacionalidad(txtNacionalidad.getText().toString());
    }

    private void recuperarUser() {
        Picasso.with(this).load(mUser.getFoto()).error(R.drawable.default_user).into(imgFoto);
        txtNombre.setText(mUser.getNombre());
        txtApellidos.setText(mUser.getApellidos());
        if (mUser.getNacionalidad() != null)
            txtNacionalidad.setText(mUser.getNacionalidad());
        if (mUser.getProfesion() != null)
            txtProfesion.setText(mUser.getProfesion());
        if (mUser.getComentario_desc() != null)
            txtComentDesc.setText(mUser.getComentario_desc());
        if (mUser.getFecha_nacimiento() > 0)
            txtFechaNac.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(mUser.getFecha_nacimiento())));
    }

    private void showCaracteristicasDialog() {
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

    private void getNationalitiesFromFile(){
        BufferedReader lector;
        String line, pais;
        int cont = 0;
        try {
            InputStream input = this.getResources().openRawResource(R.raw.countries);
            lector = new BufferedReader(new InputStreamReader(input));
            while ((line = lector.readLine()) != null) {
                pais = line.split(":")[1];
                mNationalities[cont] = pais;
                cont++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNationalitiesDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(EditProfileActivity.this);
        b.setTitle("Nacionalidades");
        b.setItems(mNationalities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtNacionalidad.setText(mNationalities[which]);
            }
        });
        b.setIcon(R.drawable.ic_flag);
        b.create().show();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_USER_RESULT, mUser));
        super.finish();
    }
}

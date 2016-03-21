package c.proyecto.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import c.proyecto.R;

public class RegistroActivity extends AppCompatActivity {

    private TextView lblCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initViews();
    }

    private void initViews() {
        lblCancelar = (TextView) findViewById(R.id.lblCancelar);
        lblCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import c.proyecto.R;
import c.proyecto.fragments.PrincipalFragment;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;

/**
 * Created by aleja on 19/03/2016.
 */
public class MainActivity extends AppCompatActivity {

    private static final String ARG_USUARIO = "usuario_extra";
    private MainPresenter mPresenter;
    private Usuario user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        initViews();
        if (getIntent().hasExtra(ARG_USUARIO))
            user = getIntent().getParcelableExtra(ARG_USUARIO);
    }

    private void initViews() {
        mPresenter = MainPresenter.getPresentador(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, new PrincipalFragment()).commit();
    }

    public static void start(Activity a, Usuario u){
        a.startActivity(new Intent(a, MainActivity.class).putExtra(ARG_USUARIO, u));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                break;
        }
        return true;
    }

    public MainPresenter getmPresenter(){
        return mPresenter;
    }

    public Usuario getUser() {
        return user;
    }
}

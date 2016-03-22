package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.fragments.DetallesAnuncioFragment;
import c.proyecto.fragments.PrincipalFragment;
import c.proyecto.interfaces.MainActivityOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;

/**
 * Created by aleja on 19/03/2016.
 */
public class MainActivity extends AppCompatActivity implements MainActivityOps{

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
        Intent intent = new Intent(a, MainActivity.class);
        //Cierra todas las actividades anteriores.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_USUARIO, u);

        a.startActivity(intent);
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

    public Usuario getUser() {
        return user;
    }

    public MainPresenter getmPresenter(){
        return mPresenter;
    }

    @Override
    public void advertHasBeenObtained(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addAdvertToAdapter(a);
    }

    @Override
    public void adverHasBeenModified(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceAdvertFromAdapter(a);
    }

    @Override
    public void subHasBeenObtained(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addSubToAdapter(a);
    }

    @Override
    public void subHasBeenModified(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceSubFromAdapter(a);
    }

    @Override
    public void userAdvertHasBeenObtained(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addUserAdvertToAdapter(a);
    }

    @Override
    public void userAdvertHasBeenModified(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceUserAdvertFromAdapter(a);
    }

    @Override
    public void removeSub(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).removeSub(a);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachListeners();
        super.onDestroy();
    }
}

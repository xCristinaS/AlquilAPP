package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import c.proyecto.R;
import c.proyecto.adapters.MyRecyclerViewAdapter;
import c.proyecto.fragments.PrincipalFragment;
import c.proyecto.interfaces.MainActivityOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;

/**
 * Created by aleja on 19/03/2016.
 */
public class MainActivity extends AppCompatActivity implements MainActivityOps, MyRecyclerViewAdapter.OnAdapterItemLongClick, MyRecyclerViewAdapter.OnAdapterItemClick{

    private static final String ARG_USUARIO = "usuario_extra";
    private MainPresenter mPresenter;
    private Usuario user;
    private Toolbar toolbar;
    private MyRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        initViews();
        setSupportActionBar(toolbar);
        if (getIntent().hasExtra(ARG_USUARIO))
            user = getIntent().getParcelableExtra(ARG_USUARIO);
    }

    private void initViews() {
        mPresenter = MainPresenter.getPresentador(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, new PrincipalFragment()).commit();
    }

    public static void start(Activity a, Usuario u){
        a.startActivity(new Intent(a, MainActivity.class).putExtra(ARG_USUARIO, u));
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

    @Override
    public void setAdapterAllowMultiDeletion(MyRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.limpiar:
                adapter.clearAllSelections();
                adapter.disableMultiDeletionMode();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
            case R.id.eliminar:
                adapter.clearAllSelections();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemLongClick() {
        toolbar.getMenu().findItem(R.id.eliminar).setVisible(true);
        toolbar.getMenu().findItem(R.id.limpiar).setVisible(true);
    }

    @Override
    public void desactivarMultiseleccion() {
        toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
        toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
    }

    public Usuario getUser() {
        return user;
    }

    public MainPresenter getmPresenter(){
        return mPresenter;
    }
}

package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import c.proyecto.R;

import c.proyecto.adapters.MessagesRecyclerViewAdapter;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.dialog_fragments.FilterDialogFramgent;
import c.proyecto.dialog_fragments.SeleccionPrestacionesDialogFragment;
import c.proyecto.fragments.MessagesFragment;
import c.proyecto.fragments.PrincipalFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.MainActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Prestacion;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.mvp_presenters.MainPresenter;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements MainActivityOps, AdvertsRecyclerViewAdapter.OnAdapterItemLongClick, AdvertsRecyclerViewAdapter.OnAdapterItemClick, NavigationView.OnNavigationItemSelectedListener, MessagesRecyclerViewAdapter.OnMessagesAdapterItemClick, PrincipalFragment.AllowFilters, FilterDialogFramgent.ApplyFilters, SeleccionPrestacionesDialogFragment.ICallBackOnDismiss {


    private static final String ARG_USUARIO = "usuario_extra";
    private static final String TAG_PRINCIPAL_FRAGMENT = "principal_fragment";
    private static final String TAG_MESSAGES_FRAGMENT = "messages_fragment";
    private static final String TAG_FILTER_DIALOG_FRAMGENT = "filtros_dialog_fragment";

    private static MainPresenter mPresenter;
    private Usuario mUser;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private AdvertsRecyclerViewAdapter adapter;
    private CircleImageView imgNavDrawer;
    private TextView txtUserNavDrawer;
    private CircleImageView navHeader;
    private FragmentManager mFragmentManager;

    public static void start(Activity a, Usuario u) {
        Intent intent = new Intent(a, MainActivity.class);
        //Cierra todas las actividades anteriores.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_USUARIO, u);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().hasExtra(ARG_USUARIO))
            mUser = getIntent().getParcelableExtra(ARG_USUARIO);
        initViews();
    }

    private void initViews() {
        mPresenter = MainPresenter.getPresentador(this);
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, mUser));
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, mUser));
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.frmContenido, new PrincipalFragment(), TAG_PRINCIPAL_FRAGMENT).commit();
        configNavDrawer();
    }

    private void configNavDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //Listener del menú del navigationDrawer
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        txtUserNavDrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtUserNavDrawer);
        txtUserNavDrawer.setText(String.format("%s %s", mUser.getNombre(), mUser.getApellidos()));
        imgNavDrawer = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imgNavDrawer);

        //Foto del usuario en el nav drawer.
        if (mUser.getFoto() != null)
            Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgNavDrawer);
        else
            Picasso.with(this).load(R.drawable.default_user).fit().centerCrop().into(imgNavDrawer);

        navHeader = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imgNavDrawer);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(MainActivity.this, mUser);
            }
        });
    }

    @Override
    public void userAdvertHasBeenModified(Usuario user) {
        mUser = user;
        txtUserNavDrawer.setText(String.format("%s %s", mUser.getNombre(), mUser.getApellidos()));
        if (mUser.getFoto() != null)
            Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgNavDrawer);
    }

    @Override
    public void advertHasBeenObtained(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addAdvertToAdapter(a);
    }

    @Override
    public void adverHasBeenModified(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceAdvertFromAdapter(a);
    }

    @Override
    public void subHasBeenObtained(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addSubToAdapter(a);
    }

    @Override
    public void subHasBeenModified(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceSubFromAdapter(a);
    }

    @Override
    public void userAdvertHasBeenObtained(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addUserAdvertToAdapter(a);
    }

    @Override
    public void userAdvertHasBeenModified(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceUserAdvertFromAdapter(a);
    }

    @Override
    public void removeSub(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).removeSub(a);
    }

    @Override
    public void removeAdvert(Anuncio a){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).removeAdvert(a);
    }

    @Override
    public void sendAdvertHasBeenRemovedBroadcast() {
        sendBroadcast(new Intent(DetallesAnuncioActivity.ACTION_CLOSE_ACTIVITY));
    }

    //MENU NAV DRAWER
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                //Si el principalFragment no está cargado en el FrameLayout
                if(!(mFragmentManager.findFragmentById(R.id.frmContenido) instanceof PrincipalFragment)){
                    //Si ya existe se carga
                    if(mFragmentManager.findFragmentByTag(TAG_PRINCIPAL_FRAGMENT) != null)
                        mFragmentManager.beginTransaction().replace(R.id.frmContenido, mFragmentManager.findFragmentByTag(TAG_PRINCIPAL_FRAGMENT)).commit();
                    else
                        mFragmentManager.beginTransaction().replace(R.id.frmContenido, new PrincipalFragment()).commit();
                }

                break;
            case R.id.nav_new_adv:
                //Null = nuevo Anuncio.
                CrearAnuncio1Activity.start(this, mUser);
                break;
            case R.id.nav_edit_profile:
                EditProfileActivity.start(this, mUser);
                break;
            case R.id.nav_messages:
                mPresenter.requestUserMessages(mUser);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, MessagesFragment.newInstance(false, null), TAG_MESSAGES_FRAGMENT).commit();
                break;
            case R.id.nav_preferences:
                break;
            case R.id.nav_sign_off:
                InicioActivity.start(this);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setAdapterAllowMultiDeletion(AdvertsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showFilterIcon();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.limpiar:
                adapter.clearAllSelections();
                adapter.disableMultiDeletionMode();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
            case R.id.eliminar:
                adapter.removeSelections();
                adapter.clearAllSelections();
                adapter.disableMultiDeletionMode();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
            case R.id.filters:
                new FilterDialogFramgent().show(getSupportFragmentManager(), TAG_FILTER_DIALOG_FRAMGENT);
                return true;
            case R.id.deshacerFiltro:
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
                if (f instanceof PrincipalFragment)
                    ((PrincipalFragment) f).removeFilter();

                mPresenter.detachAdvertsListener();
                mPresenter.attachAdvertsListeners();
                toolbar.getMenu().findItem(R.id.deshacerFiltro).setVisible(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFilterIcon() {
        toolbar.getMenu().findItem(R.id.filters).setVisible(true);
    }

    @Override
    public void hideFilterIcon() {
        toolbar.getMenu().findItem(R.id.filters).setVisible(false);
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

    @Override
    public void onItemClick(Anuncio anuncio, int advertType) {
        DetallesAnuncioActivity.start(this, anuncio, advertType, mUser);
    }

    @Override
    public void onBackPressed() {
        //Si el navDrawer está abierto lo cierra
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawers();
        else{
            if (getSupportFragmentManager().findFragmentById(R.id.frmContenido) instanceof MessagesFragment) {
                PrincipalFragment f = (PrincipalFragment) getSupportFragmentManager().findFragmentByTag(TAG_PRINCIPAL_FRAGMENT);
                if (f != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, f).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, new PrincipalFragment(), TAG_PRINCIPAL_FRAGMENT).commit();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void userMessageHasBeenObtained(MessagePojo m) {
        if (getSupportFragmentManager().findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).getmAdapter().addItem(m);
    }

    @Override
    public void filterRequest(String[] tipoVivienda, int minPrice, int maxPrice, int minSize, int maxSize, ArrayList<Prestacion> prestaciones) {
        mPresenter.filterRequest(tipoVivienda, minPrice, maxPrice, minSize, maxSize, prestaciones);
    }

    @Override
    public void filteredAdvertsObtained(ArrayList<Anuncio> filteredAdverts){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).loadFilteredAdverts(filteredAdverts);
        toolbar.getMenu().findItem(R.id.deshacerFiltro).setVisible(true);
    }

    public Usuario getmUser() {
        return mUser;
    }

    public static MainPresenter getmPresenter() {
        return mPresenter;
    }

    @Override
    public void onItemClick(MessagePojo mensaje) {
        ConversationActivity.start(this, mensaje, mUser);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachListeners();
        super.onDestroy();
    }

    @Override
    public void onDismiss() {
        ((FilterDialogFramgent)getSupportFragmentManager().findFragmentByTag(TAG_FILTER_DIALOG_FRAMGENT)).updatePrestaciones();
    }
}

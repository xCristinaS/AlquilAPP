package c.proyecto.activities;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.interfaces.MyInicio;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters.InicioPresenter;
import c.proyecto.mvp_views_interfaces.InicioActivityOps;
import c.proyecto.pojo.Usuario;

public class SplashActivity extends AppCompatActivity implements InicioActivityOps, MyInicio{

    private ImageView imgLogo;
    private InicioPresenter mPresenter;
    private boolean animationFinished;
    private Usuario mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPresenter = InicioPresenter.getPresentador(this);
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));
        initView();
    }



    private void initView() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        imgLogo.animate().scaleX(1.3f).scaleY(1.3f).setDuration(1400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                getStoredUser();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgLogo.animate().scaleX(1).scaleY(1).setDuration(1000).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        conectar();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void getStoredUser(){
        SharedPreferences preferences = getSharedPreferences(Constantes.NOMBRE_PREFERENCIAS, MODE_PRIVATE);
        String user = preferences.getString(Constantes.KEY_USER, "");
        String pass = preferences.getString(Constantes.KEY_PASS, "");

        if(!user.isEmpty())
            mPresenter.signInRequested(user, pass);
    }

    @Override
    public void enter(Usuario u) {
        mUser = u;
    }

    private void conectar(){
        if(mUser == null)
            InicioActivity.start(this);
        else
            MainActivity.start(this, mUser);
        finish();
    }
}

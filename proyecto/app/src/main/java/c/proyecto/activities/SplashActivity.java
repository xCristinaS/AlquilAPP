package c.proyecto.activities;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.interfaces.MyInicio;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters.InicioPresenter;
import c.proyecto.pojo.Usuario;

public class SplashActivity extends AppCompatActivity implements MyInicio {

    private ImageView imgLogo;
    private InicioPresenter mPresenter;
    private Usuario mUser;
    private Lanzador hiloLanzador;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPresenter = InicioPresenter.getPresentador(this);
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));
        hiloLanzador = new Lanzador();
        new Thread(hiloLanzador).start();
        getStoredUser();
        initView();
    }

    private void initView() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

        imgLogo.animate().scaleX(1.3f).scaleY(1.3f).setDuration(1400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                imgLogo.animate().scaleX(1).scaleY(1).setDuration(1000).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hiloLanzador.countDown.countDown();
                        new HiloProgresBar().execute();
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

    private void getStoredUser() {
        SharedPreferences preferences = getSharedPreferences(Constantes.NOMBRE_PREFERENCIAS, MODE_PRIVATE);
        String user = preferences.getString(Constantes.KEY_USER, "");
        String pass = preferences.getString(Constantes.KEY_PASS, "");

        if (!user.isEmpty() && !pass.isEmpty())
            mPresenter.signInRequested(user, pass);
        else
            hiloLanzador.countDown.countDown();
    }

    @Override
    public void enter(Object o) {
        if (o instanceof Usuario)
            mUser = (Usuario) o;
        hiloLanzador.countDown.countDown();
    }

    private void conectar() {
        if (mUser == null)
            InicioActivity.start(this);
        else
            MainActivity.start(this, mUser);
        finish();
    }

    class Lanzador implements Runnable {

        private final CountDownLatch countDown;

        public Lanzador() {
            countDown = new CountDownLatch(2);
        }

        @Override
        public void run() {
            try {
                countDown.await();
                conectar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class HiloProgresBar extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(4500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}

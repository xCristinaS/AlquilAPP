package c.proyecto.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import c.proyecto.presenters.MainPresenter;

/**
 * Created by aleja on 19/03/2016.
 */
public class MainActivity extends AppCompatActivity {

    private MainPresenter presentador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        presentador = MainPresenter.getPresentador(this);
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
}

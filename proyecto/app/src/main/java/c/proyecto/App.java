package c.proyecto;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Cristina on 19/03/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(this);
        super.onCreate();
    }
}

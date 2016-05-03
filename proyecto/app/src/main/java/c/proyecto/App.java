package c.proyecto;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.firebase.client.Firebase;

public class App extends Application {

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(this);
        Stetho.initializeWithDefaults(this);
        super.onCreate();
    }
}

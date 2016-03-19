package c.proyecto.models;

import com.firebase.client.Firebase;

/**
 * Created by Cristina on 19/03/2016.
 */
public class Usuario {

    private String email = "aaa", contra = "bbb";

    public Usuario(){

    }

    public static boolean createNewUser(){
        boolean r = false;
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/");
        mFirebase.child("usuarios").setValue("alex");
        return true;
    }
}

package c.proyecto.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import c.proyecto.R;
import c.proyecto.models.Usuario;


public class DescripcionDialogFragment extends AppCompatDialogFragment {

    private static final String ARG_USER = "usuario";
    private Usuario mUser;
    private ImageView imgMasculino, imgFemenino, imgEstudiante, imgTrabajador, imgFumador, imgNoFumador;


    public static DescripcionDialogFragment newInstance(Usuario user) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        DescripcionDialogFragment fragment = new DescripcionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle("Descripción");
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_fragment_descripcion, container, false);

        initViews(view);
        mUser = (Usuario) getArguments().get(ARG_USER);
        recuperarDatos();

        return view;
    }

    private void initViews(View view) {
        imgMasculino = (ImageView) view.findViewById(R.id.imgMasculino);
        imgFemenino = (ImageView) view.findViewById(R.id.imgFemenino);
        imgEstudiante = (ImageView) view.findViewById(R.id.imgEstudiante);
        imgTrabajador = (ImageView) view.findViewById(R.id.imgTrabajador);
        imgFumador = (ImageView) view.findViewById(R.id.imgFumador);
        imgNoFumador = (ImageView) view.findViewById(R.id.imgNoFumador);

        imgMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintImageView(imgMasculino);
                mUser.setIsMale(true);
            }
        });

        imgFemenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintImageView(imgFemenino);
                mUser.setIsMale(false);
            }
        });

        imgEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintImageView(imgEstudiante);
                mUser.setIsStudent(true);
            }
        });

        imgTrabajador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintImageView(imgTrabajador);
                mUser.setIsStudent(false);
            }
        });

        imgFumador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintImageView(imgFumador);
                mUser.setIsSmoker(true);
            }
        });

        imgNoFumador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintImageView(imgNoFumador);
                mUser.setIsSmoker(false);
            }
        });
    }

    private void recuperarDatos() {
        if(mUser.isMale())
            tintImageView(imgMasculino);
        else
            tintImageView(imgFemenino);

        if(mUser.isStudent())
            tintImageView(imgEstudiante);
        else
            tintImageView(imgTrabajador);

        if(mUser.isSmoker())
            tintImageView(imgFumador);
        else
            tintImageView(imgNoFumador);

    }
    private void tintImageView(ImageView img){
        img.setColorFilter(getResources().getColor(R.color.colorAccent));

        if(img.equals(imgMasculino))
            imgFemenino.clearColorFilter();
        else if( img.equals(imgFemenino))
            imgMasculino.clearColorFilter();
        else if(img.equals(imgEstudiante))
            imgTrabajador.clearColorFilter();
        else if(img.equals(imgTrabajador))
            imgEstudiante.clearColorFilter();
        else if(img.equals(imgFumador))
            imgNoFumador.clearColorFilter();
        else if(img.equals(imgNoFumador))
            imgFumador.clearColorFilter();

    }


}

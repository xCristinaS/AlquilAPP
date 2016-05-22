package c.proyecto.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.pojo.Usuario;


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
        View view = inflater.inflate(R.layout.dialog_fragment_descripcion, container, false);

        mUser = (Usuario) getArguments().get(ARG_USER);
        initViews(view);
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


        onClickImages(imgMasculino, R.drawable.genero_masculino);
        onClickImages(imgFemenino, R.drawable.genero_femenino);
        onClickImages(imgEstudiante, R.drawable.estudiante);
        onClickImages(imgTrabajador, R.drawable.trabajador);
        onClickImages(imgFumador, R.drawable.fumador);
        onClickImages(imgNoFumador, R.drawable.no_fumador);

    }

    private void recuperarDatos() {
        ArrayList<String> ids = mUser.getIdDrawItemsDescriptivos();

        if (ids.contains(getResources().getResourceEntryName(R.drawable.genero_masculino)))
            tintImageView(imgMasculino);
        else if (ids.contains(getResources().getResourceEntryName(R.drawable.genero_femenino)))
            tintImageView(imgFemenino);

        if (ids.contains(getResources().getResourceEntryName(R.drawable.estudiante)))
            tintImageView(imgEstudiante);
        else if (ids.contains(getResources().getResourceEntryName(R.drawable.trabajador)))
            tintImageView(imgTrabajador);

        if (ids.contains(getResources().getResourceEntryName(R.drawable.fumador)))
            tintImageView(imgFumador);
        else if (ids.contains(getResources().getResourceEntryName(R.drawable.no_fumador)))
            tintImageView(imgNoFumador);

    }

    private void tintImageView(ImageView img) {
        img.setColorFilter(getResources().getColor(R.color.colorAccent));

        if (img.equals(imgMasculino))
            imgFemenino.clearColorFilter();
        else if (img.equals(imgFemenino))
            imgMasculino.clearColorFilter();
        else if (img.equals(imgEstudiante))
            imgTrabajador.clearColorFilter();
        else if (img.equals(imgTrabajador))
            imgEstudiante.clearColorFilter();
        else if (img.equals(imgFumador))
            imgNoFumador.clearColorFilter();
        else if (img.equals(imgNoFumador))
            imgFumador.clearColorFilter();

    }

    private void onClickImages(final ImageView imgOnClick, final int drawToTint) {
        //Cuando haces CLICK en un Item ya seleccionado se raya en la base de datos.
        imgOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = 0;
                tintImageView(imgOnClick);
                //Reemplaza
                switch (drawToTint) {
                    case R.drawable.genero_masculino:
                    case R.drawable.genero_femenino:
                        pos = 0;
                        break;
                    case R.drawable.estudiante:
                    case R.drawable.trabajador:
                        pos = 1;
                        break;
                    case R.drawable.fumador:
                    case R.drawable.no_fumador:
                        pos = 2;
                        break;
                }
                if(pos+1 <= mUser.getIdDrawItemsDescriptivos().size())
                    mUser.getIdDrawItemsDescriptivos().set(pos, getResources().getResourceEntryName(drawToTint));
                else
                    mUser.getIdDrawItemsDescriptivos().add(getResources().getResourceEntryName(drawToTint));

            }
            
        });
    }


}

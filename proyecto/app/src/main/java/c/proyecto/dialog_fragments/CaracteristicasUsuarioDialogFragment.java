package c.proyecto.dialog_fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.pojo.Usuario;


public class CaracteristicasUsuarioDialogFragment extends AppCompatDialogFragment {

    private static final String ARG_USER = "user";
    private static final String ARG_IS_DIALOG = "isDialog";
    private SeekBar skOrdenado, skFiestero, skSociable, skActivo;
    private TextView lblNumOrdenado, lblNumFiestero, lblNumSociable, lblNumActivo;
    private Usuario mUser;

    public static CaracteristicasUsuarioDialogFragment newInstance(Usuario user, boolean isDialog) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        args.putBoolean(ARG_IS_DIALOG, isDialog);

        CaracteristicasUsuarioDialogFragment fragment = new CaracteristicasUsuarioDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_fragment_caracteristicas_usuario, container, false);

        initViews(view);
        mUser = (Usuario) getArguments().get(ARG_USER);
        recuperarDatos();
        if(getArguments().getBoolean(ARG_IS_DIALOG))
            view.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.widthDialogFragmentCaracterísticas), getResources().getDimensionPixelOffset(R.dimen.heightDialogFragmentCaracterísticas)));
        return view;
    }



    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle(R.string.title_CaracteristicasUsuarioDialogFragment);
        super.setupDialog(dialog, style);
    }

    private void initViews(View view) {
        skOrdenado = (SeekBar) view.findViewById(R.id.skOrdenado);
        lblNumOrdenado = (TextView) view.findViewById(R.id.lblNumOrdenado);
        skFiestero = (SeekBar) view.findViewById(R.id.skFiestero);
        lblNumFiestero = (TextView) view.findViewById(R.id.lblNumFiestero);
        skSociable = (SeekBar) view.findViewById(R.id.skSociable);
        lblNumSociable = (TextView) view.findViewById(R.id.lblNumSociable);
        skActivo = (SeekBar) view.findViewById(R.id.skActivo);
        lblNumActivo = (TextView) view.findViewById(R.id.lblNumActivo);

        confSeekBar(skOrdenado, lblNumOrdenado);
        confSeekBar(skFiestero, lblNumFiestero);
        confSeekBar(skSociable, lblNumSociable);
        confSeekBar(skActivo, lblNumActivo);
    }

    private void confSeekBar(SeekBar sk, final TextView txt){
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt.setText(String.valueOf(progress / Constantes.MULTIPLICADOR_SEEK_BAR));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Permite mover o no la SeekBar dependiendo de como se haya llamado a este fragmento.
        if(!getArguments().getBoolean(ARG_IS_DIALOG))
            sk.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
    }

    private void recuperarDatos() {
        lblNumOrdenado.setText(String.valueOf(mUser.getOrdenado()));
        lblNumFiestero.setText(String.valueOf(mUser.getFiestero()));
        lblNumSociable.setText(String.valueOf(mUser.getSociable()));
        lblNumActivo.setText(String.valueOf(mUser.getActivo()));

        skOrdenado.setProgress(mUser.getOrdenado() * Constantes.MULTIPLICADOR_SEEK_BAR);
        skFiestero.setProgress(mUser.getFiestero()* Constantes.MULTIPLICADOR_SEEK_BAR);
        skSociable.setProgress(mUser.getSociable()* Constantes.MULTIPLICADOR_SEEK_BAR);
        skActivo.setProgress(mUser.getActivo()* Constantes.MULTIPLICADOR_SEEK_BAR);
    }
    private void saveChanges() {
        mUser.setOrdenado(Integer.valueOf(lblNumOrdenado.getText().toString()));
        mUser.setFiestero(Integer.valueOf(lblNumFiestero.getText().toString()));
        mUser.setSociable(Integer.valueOf(lblNumSociable.getText().toString()));
        mUser.setActivo(Integer.valueOf(lblNumActivo.getText().toString()));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        saveChanges();
        super.onDismiss(dialog);
    }
}

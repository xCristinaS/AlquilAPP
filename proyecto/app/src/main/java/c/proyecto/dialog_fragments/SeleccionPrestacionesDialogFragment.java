package c.proyecto.dialog_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.pojo.Prestacion;


public class SeleccionPrestacionesDialogFragment extends AppCompatDialogFragment {

    public interface ICallBackOnDismiss {
        void onDismiss();
    }

    private static final String ARG_PRESTACIONES = "prestaciones_selected";

    private ICallBackOnDismiss mListener;
    private CheckBox cbAptoMascotas;
    private CheckBox cbAireAcondicionado;
    private CheckBox cbAscensor;
    private CheckBox cbCalefaccion;
    private CheckBox cbLavadora;
    private CheckBox cbParking;
    private CheckBox cbProhibidoFumar;
    private CheckBox cbSecadora;
    private CheckBox cbTren;
    private CheckBox cbTv;
    private CheckBox cbInternet;
    private List<Prestacion> mPrestaciones;

    public static SeleccionPrestacionesDialogFragment newInstance(ArrayList<Prestacion> prestacionesSelected) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PRESTACIONES, prestacionesSelected);
        SeleccionPrestacionesDialogFragment fragment = new SeleccionPrestacionesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_seleccion_prestaciones, container, false);
        mPrestaciones = getArguments().getParcelableArrayList(ARG_PRESTACIONES);
        initViews(view);

        return view;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle(R.string.title_SeleccionPrestacionesDialogFragment);
        super.setupDialog(dialog, style);
    }

    private void initViews(View view) {
        cbAptoMascotas = (CheckBox) view.findViewById(R.id.cbAptoMascotas);
        cbAireAcondicionado = (CheckBox) view.findViewById(R.id.cbAireAcondicionado);
        cbAscensor = (CheckBox) view.findViewById(R.id.cbAscensor);
        cbCalefaccion = (CheckBox) view.findViewById(R.id.cbCalefaccion);
        cbLavadora = (CheckBox) view.findViewById(R.id.cbLavadora);
        cbParking = (CheckBox) view.findViewById(R.id.cbParking);
        cbProhibidoFumar = (CheckBox) view.findViewById(R.id.cbProhibidoFumar);
        cbSecadora = (CheckBox) view.findViewById(R.id.cbSecadora);
        cbTren = (CheckBox) view.findViewById(R.id.cbTren);
        cbTv = (CheckBox) view.findViewById(R.id.cbTv);
        cbInternet = (CheckBox) view.findViewById(R.id.cbInternet);

        bindCheckBox();
    }

    //Marca como activados todos los checkboxes que contenga como prestaciones el anuncio.
    private void bindCheckBox() {
        Resources res = getResources();

        for (Prestacion p : mPrestaciones)
            if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_apto_mascotas)))
                cbAptoMascotas.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_aire_acondicionado)))
                cbAireAcondicionado.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_ascensor)))
                cbAscensor.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_calefaccion)))
                cbCalefaccion.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_lavadora)))
                cbLavadora.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_parking)))
                cbParking.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_prohibido_fumar)))
                cbProhibidoFumar.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_secadora)))
                cbSecadora.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_tren)))
                cbTren.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_tv)))
                cbTv.setChecked(true);
            else if (p.getNameDrawable().equals(res.getResourceEntryName(R.drawable.prest_internet)))
                cbInternet.setChecked(true);
    }

    @Override
    public void onDestroy() {
        guardarCambios();
        mListener.onDismiss();
        super.onDestroy();
    }

    private void guardarCambios() {
        Resources res = getResources();
        mPrestaciones.clear();

        if (cbAptoMascotas.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_apto_mascotas), getContext().getString(R.string.aptoMascotas)));
        if (cbAireAcondicionado.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_aire_acondicionado), getContext().getString(R.string.aireAcondicionado)));
        if (cbAscensor.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_ascensor), getContext().getString(R.string.ascensor)));
        if (cbCalefaccion.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_calefaccion), getContext().getString(R.string.calefaccion)));
        if (cbLavadora.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_lavadora), getContext().getString(R.string.lavadora)));
        if (cbParking.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_parking), getContext().getString(R.string.parking)));
        if (cbProhibidoFumar.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_prohibido_fumar), getContext().getString(R.string.prohibidoFumar)));
        if (cbSecadora.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_secadora), getContext().getString(R.string.secadora)));
        if (cbTren.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_tren), getContext().getString(R.string.trenCercano)));
        if (cbTv.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_tv), getContext().getString(R.string.television)));
        if (cbInternet.isChecked())
            mPrestaciones.add(new Prestacion(res.getResourceEntryName(R.drawable.prest_internet), getContext().getString(R.string.internet)));
    }

    @Override
    public void onAttach(Context context) {
        mListener = (ICallBackOnDismiss) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}

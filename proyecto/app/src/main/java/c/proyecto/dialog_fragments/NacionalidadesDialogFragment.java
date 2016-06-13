package c.proyecto.dialog_fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import c.proyecto.R;

public class NacionalidadesDialogFragment extends DialogFragment {

    public interface IonNacionalidadClicked{
        void onNacionalidadClicked(String nacionalidad);
    }

    private static final String ARG_NACIONALIDADES = "nacionalidadesArray";
    private ListView lvNacionalidades;
    private IonNacionalidadClicked mListener;

    public static NacionalidadesDialogFragment newInstance(String[] nacionalidades) {

        Bundle args = new Bundle();
        args.putStringArray(ARG_NACIONALIDADES, nacionalidades);

        NacionalidadesDialogFragment fragment = new NacionalidadesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_nacionalidades, container, false);
        lvNacionalidades = (ListView) view.findViewById(R.id.lvNacionalidades);
        lvNacionalidades.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, getArguments().getStringArray(ARG_NACIONALIDADES)));

        lvNacionalidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onNacionalidadClicked((String) lvNacionalidades.getAdapter().getItem(position));
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle(R.string.title_NacionalidadesDialogFragment);
        super.setupDialog(dialog, style);
    }

    @Override
    public void onAttach(Context context) {
        mListener = (IonNacionalidadClicked) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}

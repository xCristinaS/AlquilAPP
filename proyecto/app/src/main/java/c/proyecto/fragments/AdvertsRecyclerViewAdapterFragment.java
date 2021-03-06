package c.proyecto.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.HuespedesAdapter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.utils.UtilMethods;


public class AdvertsRecyclerViewAdapterFragment extends Fragment implements AdvertsRecyclerViewAdapter.IAdvertsRecyclerViewAdapter {

    private static final String ARG_ADAPTER_TYPE = "type_of_adapter";
    private AdvertsRecyclerViewAdapter mAdapter;
    //Variables
    private AdvertsRecyclerViewAdapter.OnAdapterItemLongClick listenerLongClick;
    private AdvertsRecyclerViewAdapter.OnAdapterItemClick listenerItemClick;
    private AdvertsRecyclerViewAdapter.OnSubsIconClick listenerSubClick;
    private HuespedesAdapter.IHuespedesAdapterListener listenerUserSubClick;
    private int adapter_type;
    private ImageView imgEmptyView;
    private TextView lblEmptyView;
    private LinearLayout emptyView;
    private boolean creado = false;

    public static AdvertsRecyclerViewAdapterFragment newInstance(int adapter_type) {
        Bundle args = new Bundle();
        args.putInt(ARG_ADAPTER_TYPE, adapter_type);
        AdvertsRecyclerViewAdapterFragment fragment = new AdvertsRecyclerViewAdapterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        creado = true;
        initViews();
        super.onActivityCreated(savedInstanceState);
        getActivity().sendBroadcast(new Intent(MainActivity.ACTION_ADVERT_RVADAP_CREADO));
    }

    private void initViews() {
        Bundle args = getArguments();
        int idDrawable = 0;
        String textEmptyView = "";
        emptyView = (LinearLayout) getView().findViewById(R.id.emptyView);
        imgEmptyView = (ImageView) getView().findViewById(R.id.imgEmptyView);
        lblEmptyView = (TextView) getView().findViewById(R.id.lblEmptyView);

        adapter_type = args.getInt(ARG_ADAPTER_TYPE);

        RecyclerView rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new AdvertsRecyclerViewAdapter(adapter_type, MainPresenter.getPresentador(getActivity()), ((PrincipalFragment) getParentFragment()).getUser());
        mAdapter.setmIAdvertsRecyclerViewAdapter(this);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setListenerItemClick(listenerItemClick);

        switch (adapter_type) {
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS:
                mAdapter.setListenerLongClick(listenerLongClick);
                idDrawable = R.drawable.tab_suscripciones;
                textEmptyView = getActivity().getString(R.string.text_emptyView_tab_suscritos);
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS:
                idDrawable = R.drawable.tab_anuncios;
                textEmptyView = getActivity().getString(R.string.text_emptyView_tab_anuncios);
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS:
                mAdapter.setListenerLongClick(listenerLongClick);
                mAdapter.setListenerSubsClick(listenerSubClick);
                mAdapter.setListenerUserSubClick(listenerUserSubClick);
                idDrawable = R.drawable.tab_mis_anuncios;
                textEmptyView = getActivity().getString(R.string.text_emptyView_tab_misAnuncios);
                break;
        }
        mAdapter.setEmptyView(emptyView);

        confEmptyView(idDrawable, textEmptyView);
        imgEmptyView.setColorFilter(getResources().getColor(R.color.colorAccent));

    }

    public void confEmptyView(int idDrawable, String textEmptyView) {
        //Si no tiene activado la localización
        //Default true --> Por si el dispositivo es menor a la API 23, no tendrá esta preferencia ya que no se le pedirá el permiso en ejecución
        if(creado){
            if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Constantes.KEY_LOCATION_ACTIVED, true)) {
                if (adapter_type == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS) {
                    imgEmptyView.setImageResource(R.drawable.logo);
                    lblEmptyView.setText(R.string.text_emptyView_anuncios_ubicacion_desactivada);
                    emptyView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UtilMethods.isUbicationPermissionGranted(getActivity());
                        }
                    });

                }else{
                    imgEmptyView.setImageResource(idDrawable);
                    lblEmptyView.setText(textEmptyView);
                }

            }else if(!UtilMethods.isNetworkAvailable(getContext())) {
                imgEmptyView.setImageResource(R.drawable.no_connection);
                lblEmptyView.setText(R.string.text_no_connection);
            } else {
                imgEmptyView.setImageResource(idDrawable);
                lblEmptyView.setText(textEmptyView);
            }
        }

        
    }

    public void confEmptyViewWithClick(int idDrawable, String textEmptyView, View.OnClickListener onClickListener){
        emptyView.setOnClickListener(onClickListener);
        confEmptyView(idDrawable, textEmptyView);
    }

    @Override
    public void onAttach(Context context) {
        listenerLongClick = (AdvertsRecyclerViewAdapter.OnAdapterItemLongClick) context;
        listenerItemClick = (AdvertsRecyclerViewAdapter.OnAdapterItemClick) context;
        listenerSubClick = (AdvertsRecyclerViewAdapter.OnSubsIconClick) context;
        listenerUserSubClick = (HuespedesAdapter.IHuespedesAdapterListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        listenerLongClick = null;
        listenerItemClick = null;
        listenerSubClick = null;
        listenerUserSubClick = null;
        super.onDetach();
    }

    public void disableMultideletion() {
        listenerLongClick.desactivarMultiseleccion();
        mAdapter.clearAllSelections();
        mAdapter.disableMultiDeletionMode();
    }

    public AdvertsRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public void itemAdded(int AdvertType) {
        switch (adapter_type){
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS:
                confEmptyView(R.drawable.tab_suscripciones, getString(R.string.text_emptyView_tab_suscritos));
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS:
                confEmptyView(R.drawable.tab_anuncios, getString(R.string.text_emptyView_tab_anuncios));
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS:
                confEmptyView(R.drawable.tab_mis_anuncios, getString(R.string.text_emptyView_tab_misAnuncios));
                break;
        }
    }
}

package c.proyecto.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.HuespedesAdapter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.utils.UtilMethods;


public class AdvertsRecyclerViewFragment extends Fragment {

    private static final String ARG_ADAPTER_TYPE = "type_of_adapter";
    //Views
    private RecyclerView rvLista;
    private AdvertsRecyclerViewAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    //Variables
    private AdvertsRecyclerViewAdapter.OnAdapterItemLongClick listenerLongClick;
    private AdvertsRecyclerViewAdapter.OnAdapterItemClick listenerItemClick;
    private AdvertsRecyclerViewAdapter.OnSubsIconClick listenerSubClick;
    private HuespedesAdapter.OnUserSubClick listenerUserSubClick;
    private int adapter_type;
    private ImageView imgEmptyView;
    private TextView lblEmptyView;
    private LinearLayout emptyView;

    public static AdvertsRecyclerViewFragment newInstance(int adapter_type) {
        Bundle args = new Bundle();
        args.putInt(ARG_ADAPTER_TYPE, adapter_type);
        AdvertsRecyclerViewFragment fragment = new AdvertsRecyclerViewFragment();
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
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        Bundle args = getArguments();
        int idDrawable = 0;
        String textEmptyView = "";
        emptyView = (LinearLayout) getView().findViewById(R.id.emptyView);
        imgEmptyView = (ImageView) getView().findViewById(R.id.imgEmptyView);
        lblEmptyView = (TextView) getView().findViewById(R.id.lblEmptyView);

        adapter_type = args.getInt(ARG_ADAPTER_TYPE);

        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new AdvertsRecyclerViewAdapter(adapter_type, MainPresenter.getPresentador(getActivity()), ((PrincipalFragment)getParentFragment()).getUser());
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setListenerItemClick(listenerItemClick);

        switch (adapter_type){
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS:
                mAdapter.setListenerLongClick(listenerLongClick);
                mAdapter.setListenerItemClick(listenerItemClick);
                idDrawable = R.drawable.tab_solicitudes;
                textEmptyView = "Sin suscribciones";
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS:
                mAdapter.setListenerSubsClick(listenerSubClick);
                mAdapter.setListenerUserSubClick(listenerUserSubClick);
                idDrawable = R.drawable.tab_anuncios;
                textEmptyView = "Sin anuncios";
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS:
                mAdapter.setListenerLongClick(listenerLongClick);
                mAdapter.setListenerItemClick(listenerItemClick);
                idDrawable = R.drawable.tab_mis_anuncios;
                textEmptyView = "Sin Mis anuncios";
                break;
        }
        mAdapter.setEmptyView(emptyView);

        confEmptyView(idDrawable,textEmptyView);
        imgEmptyView.setColorFilter(getResources().getColor(R.color.colorAccent));

    }

    public void confEmptyView(int idDrawable, String textEmptyView){
        //Si tiene activado la localización
        //Default true --> Por si el dispositivo es menor a la API 23, no tendrá esta preferencia ya que no se le pedirá el permiso en ejecución
        if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(Constantes.KEY_LOCATION_ACTIVED, true))
            imgEmptyView.setImageResource(idDrawable);
        else{
            imgEmptyView.setImageResource(R.drawable.logo);
            lblEmptyView.setText("Sin ubicación activada");
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UtilMethods.isUbicationPermissionGranted(getActivity());
                }
            });
        }
        lblEmptyView.setText(textEmptyView);
    }
    @Override
    public void onAttach(Context context) {
        listenerLongClick = (AdvertsRecyclerViewAdapter.OnAdapterItemLongClick) context;
        listenerItemClick = (AdvertsRecyclerViewAdapter.OnAdapterItemClick) context;
        listenerSubClick = (AdvertsRecyclerViewAdapter.OnSubsIconClick) context;
        listenerUserSubClick = (HuespedesAdapter.OnUserSubClick) context;
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

    public void disableMultideletion(){
        listenerLongClick.desactivarMultiseleccion();
        mAdapter.clearAllSelections();
        mAdapter.disableMultiDeletionMode();
    }

    public AdvertsRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }
}

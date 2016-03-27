package c.proyecto.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.presenters.MainPresenter;


public class AdvertsRecyclerViewFragment extends Fragment {

    private static final String ARG_ADAPTER_TYPE = "type_of_adapter";
    //Views
    private RecyclerView rvLista;
    private AdvertsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    //Variables
    private AdvertsRecyclerViewAdapter.OnAdapterItemLongClick listenerLongClick;
    private AdvertsRecyclerViewAdapter.OnAdapterItemClick listenerItemClick;
    private int adapter_type;

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
        adapter_type = args.getInt(ARG_ADAPTER_TYPE);

        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new AdvertsRecyclerViewAdapter(adapter_type, MainPresenter.getPresentador(getActivity()), ((PrincipalFragment)getParentFragment()).getUser());
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
        //rvLista.setHasFixedSize(true);
        mAdapter.setListenerItemClick(listenerItemClick);
        if (adapter_type == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS || adapter_type == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS) {
            mAdapter.setListenerLongClick(listenerLongClick);
            mAdapter.setListenerItemClick(listenerItemClick);
        }
    }

    @Override
    public void onAttach(Context context) {
        listenerLongClick = (AdvertsRecyclerViewAdapter.OnAdapterItemLongClick) context;
        listenerItemClick = (AdvertsRecyclerViewAdapter.OnAdapterItemClick) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        listenerLongClick = null;
        listenerItemClick = null;
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
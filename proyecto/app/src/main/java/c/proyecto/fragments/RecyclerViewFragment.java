package c.proyecto.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.adapters.RecyclerViewAdapter;
import c.proyecto.models.Anuncio;


public class RecyclerViewFragment extends Fragment {

    private static final String ARG_IS_MY_ADV = "ismyADv";
    //Views
    private RecyclerView rvLista;
    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    //Variables
    private ArrayList<Anuncio> mAnuncios;
    private boolean isMyAdv;

    public static RecyclerViewFragment newInstance(boolean isMyAdv) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_MY_ADV, isMyAdv);

        RecyclerViewFragment fragment = new RecyclerViewFragment();
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
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        isMyAdv = args.getBoolean(ARG_IS_MY_ADV);

        rvLista = (RecyclerView) getActivity().findViewById(R.id.rvLista);
        rvLista.setHasFixedSize(true);

        mAdapter = new RecyclerViewAdapter(isMyAdv);
        rvLista.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
    }

    public RecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }
}

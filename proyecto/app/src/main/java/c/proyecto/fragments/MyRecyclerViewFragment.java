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

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.adapters.MyRecyclerViewAdapter;
import c.proyecto.models.Anuncio;


public class MyRecyclerViewFragment extends Fragment {

    private static final String ARG_IS_MY_ADV = "ismyADv";
    //Views
    private RecyclerView rvLista;
    private MyRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    //Variables
    private ArrayList<Anuncio> mAnuncios;
    private boolean isMyAdv;

    public static MyRecyclerViewFragment newInstance(boolean isMyAdv) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_MY_ADV, isMyAdv);
        MyRecyclerViewFragment fragment = new MyRecyclerViewFragment();
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
        isMyAdv = args.getBoolean(ARG_IS_MY_ADV);

        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new MyRecyclerViewAdapter(isMyAdv);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
        //rvLista.setHasFixedSize(true);
    }

    public MyRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }
}

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
import c.proyecto.adapters.MessagesAdapter;
import c.proyecto.adapters.MyRecyclerViewAdapter;
import c.proyecto.presenters.MainPresenter;

/**
 * Created by Cristina on 23/03/2016.
 */
public class MessagesFragment extends Fragment {

    private RecyclerView rvMessages;
    private MessagesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        rvMessages = (RecyclerView) getView().findViewById(R.id.rvMessages);
        mAdapter = new MessagesAdapter();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(mLayoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        //rvMessages.setHasFixedSize(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public MessagesAdapter getmAdapter() {
        return mAdapter;
    }
}

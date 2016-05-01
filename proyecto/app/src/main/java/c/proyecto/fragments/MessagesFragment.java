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
import c.proyecto.adapters.MessagesRecyclerViewAdapter;
import c.proyecto.pojo.MessagePojo;

public class MessagesFragment extends Fragment {

    private static final String ARG_CONVER = "conver";
    private static final String ARG_CURRENT_USER = "user";

    private RecyclerView rvMessages;
    private MessagesRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MessagesRecyclerViewAdapter.OnMessagesAdapterItemClick listenerItemClick;
    private MessagesRecyclerViewAdapter.ConversationManager listenerConverManager;
    private boolean isAConversation;
    private String mKeyCurrentUser;

    public static MessagesFragment newInstance(boolean isAConversation, String keyCurrentUser) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_CONVER, isAConversation);
        args.putString(ARG_CURRENT_USER, keyCurrentUser);
        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        Bundle args = getArguments();
        isAConversation = args.getBoolean(ARG_CONVER, false);
        mKeyCurrentUser = args.getString(ARG_CURRENT_USER);
        rvMessages = (RecyclerView) getView().findViewById(R.id.rvMessages);
        mAdapter = new MessagesRecyclerViewAdapter(isAConversation, mKeyCurrentUser);
        if (listenerItemClick != null)
            mAdapter.setListenerItemClick(listenerItemClick);
        if (listenerConverManager != null)
            mAdapter.setListenerConverManager(listenerConverManager);
        if (isAConversation)
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        else
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(mLayoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
    }

    public void addItem(MessagePojo m) {
        mAdapter.addItem(m);
        // si no estoy en el chat, que se posicione en el último mensaje agregado
        if (!isAConversation)
            rvMessages.scrollToPosition(mAdapter.getmDatos().indexOf(m));
        else
            // si estoy en el chat, que se posicione abajo, en el último mensaje recibido
            rvMessages.scrollToPosition(0);
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof MessagesRecyclerViewAdapter.OnMessagesAdapterItemClick)
            listenerItemClick = (MessagesRecyclerViewAdapter.OnMessagesAdapterItemClick) context;
        if (context instanceof MessagesRecyclerViewAdapter.ConversationManager)
            listenerConverManager = (MessagesRecyclerViewAdapter.ConversationManager) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        if (listenerItemClick != null)
            listenerItemClick = null;
        if (listenerConverManager != null)
            listenerConverManager = null;
        super.onDetach();
    }

    public MessagesRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }
}

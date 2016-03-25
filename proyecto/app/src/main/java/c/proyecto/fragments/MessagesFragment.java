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
import c.proyecto.pojo.MessagePojo;

public class MessagesFragment extends Fragment {

    private static final String ARG_CONVER = "conver";

    private RecyclerView rvMessages;
    private MessagesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MessagesAdapter.OnMessagesAdapterItemClick listenerItemClick;
    private MessagesAdapter.ConversationManager listenerConverManager;
    private boolean isAConversation;

    public static MessagesFragment newInstance(boolean isAConversation) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_CONVER, isAConversation);
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
        rvMessages = (RecyclerView) getView().findViewById(R.id.rvMessages);
        mAdapter = new MessagesAdapter(isAConversation);
        if (listenerItemClick != null)
            mAdapter.setListenerItemClick(listenerItemClick);
        if (listenerConverManager != null)
            mAdapter.setListenerConverManager(listenerConverManager);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(mLayoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        //rvMessages.setHasFixedSize(true);
    }

    public void addItem(MessagePojo m) {
        mAdapter.addItem(m);
        rvMessages.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof MessagesAdapter.OnMessagesAdapterItemClick)
            listenerItemClick = (MessagesAdapter.OnMessagesAdapterItemClick) context;
        if (context instanceof MessagesAdapter.ConversationManager)
            listenerConverManager = (MessagesAdapter.ConversationManager) context;
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

    public MessagesAdapter getmAdapter() {
        return mAdapter;
    }
}

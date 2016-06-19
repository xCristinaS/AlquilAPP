package c.proyecto.fragments;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.CachedFragmentPagerAdapter;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.MainPresenter;


public class PrincipalFragment extends Fragment {

    public interface AllowFilters{
        void showFilterIcon();
        void hideFilterIcon();
        void hideMapIcon();
        void showMapIcon();
    }

    private MainPresenter mPresenter;
    private Usuario user;
    private SectionsPagerAdapter vpAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AllowFilters listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = MainPresenter.getPresentador(null);
        user = ((MainActivity) getActivity()).getmUser();
        confViewPager();
    }


    private void confViewPager() {
        vpAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        viewPager.setAdapter(vpAdapter);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        ImageView customImg = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.tab_custom, null);
        ImageView customImg2 = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.tab_custom, null);
        ImageView customImg3 = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.tab_custom, null);

        customImg.setImageResource(R.drawable.tab_suscripciones);
        tabLayout.getTabAt(0).setCustomView(customImg);
        customImg2.setImageResource(R.drawable.tab_anuncios);
        tabLayout.getTabAt(1).setCustomView(customImg2);
        customImg3.setImageResource(R.drawable.tab_mis_anuncios);
        tabLayout.getTabAt(2).setCustomView(customImg3);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1); // el fragmento principal será el de anuncios
        ((ImageView) tabLayout.getTabAt(1).getCustomView()).setColorFilter(getResources().getColor(R.color.colorAccent));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                AdvertsRecyclerViewAdapterFragment fragmento = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(viewPager.getCurrentItem());
                ((AdvertsRecyclerViewAdapter.OnAdapterItemLongClick) getActivity()).setAdapterAllowMultiDeletion(fragmento.getmAdapter());
                AdvertsRecyclerViewAdapter adapter = fragmento.getmAdapter();
                ImageView tab0 = (ImageView) tabLayout.getTabAt(0).getCustomView();
                ImageView tab1 = (ImageView) tabLayout.getTabAt(1).getCustomView();
                ImageView tab2 = (ImageView) tabLayout.getTabAt(2).getCustomView();

                switch (position){
                    case 0:
                        getActivity().setTitle(getString(R.string.tab_solicitudes));
                        tab1.clearColorFilter();
                        tab2.clearColorFilter();
                        ((ImageView) tabLayout.getTabAt(0).getCustomView()).setColorFilter(getResources().getColor(R.color.colorAccent));
                        break;
                    case 1:
                        getActivity().setTitle(getString(R.string.tab_anuncios));
                        tab0.clearColorFilter();
                        tab2.clearColorFilter();
                        ((ImageView) tabLayout.getTabAt(1).getCustomView()).setColorFilter(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        getActivity().setTitle(getString(R.string.tab_mis_anuncios));
                        tab0.clearColorFilter();
                        tab1.clearColorFilter();
                        ((ImageView) tabLayout.getTabAt(2).getCustomView()).setColorFilter(getResources().getColor(R.color.colorAccent));
                        break;
                }
                if (adapter != null && adapter.getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS) {
                    listener.showFilterIcon();
                    listener.showMapIcon();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(viewPager.getCurrentItem());
                if (f != null)
                    f.disableMultideletion();

                AdvertsRecyclerViewAdapter adapter = f.getmAdapter();
                if (adapter != null && adapter.getAdapter_type() != AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS) {
                    listener.hideFilterIcon();
                    listener.hideMapIcon();
                }
                confEmptyViewsNormales();
            }
        });
        mPresenter.initializeFirebaseListeners(user);
        mPresenter.detachGeoAdvertsLocationListener();
        ((MainActivity)getActivity()).getAdvertsNearUser();
    }

    public void addAdvertToAdapter(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().addItem(a);
    }

    public void replaceAdvertFromAdapter(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().replaceItem(a);
    }

    public void addSubToAdapter(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(0);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
            f.getmAdapter().addItem(a);
    }

    public void replaceSubFromAdapter(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(0);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
            f.getmAdapter().replaceItem(a);
    }

    public void addUserAdvertToAdapter(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(2);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            f.getmAdapter().addItem(a);
    }

    public void replaceUserAdvertFromAdapter(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(2);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            f.getmAdapter().replaceItem(a);
    }

    public void removeSub(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(0);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
            f.getmAdapter().removeItem(a);
    }

    public void removeAdvert(Anuncio a) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().removeItem(a);
    }

    public void loadFilteredAdverts(ArrayList<Anuncio> filteredAdverts) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS){
            //Si no ha encontrado ningún anuncio filtrando
            if(filteredAdverts.size() == 0)
                f.confEmptyView(R.drawable.ic_not_found, getString(R.string.emptyView_busqueda_sin_exito));
            f.getmAdapter().addItems(filteredAdverts);
        }
    }

    public void removeFilter() {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS){
            f.getmAdapter().removeFilter();
            if(!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(Constantes.KEY_LOCATION_ACTIVED, true))
                confEmptyViewsSinUbicacion();
            else
                f.confEmptyView(R.drawable.tab_anuncios, getString(R.string.text_emptyView_tab_anuncios));
        }
    }

    public void solicitantesObtained(View itemView, ArrayList<Usuario> listaSolicitantes, Anuncio anuncio) {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(2);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            f.getmAdapter().solicitantesObtained(itemView, listaSolicitantes, anuncio);
    }

    public void closeSolicitantesDialog() {
        AdvertsRecyclerViewAdapterFragment f = (AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(2);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            f.getmAdapter().closeSolicitantesDialog();
    }

    public Usuario getUser() {
        return user;
    }

    //Adaptader
    class SectionsPagerAdapter extends CachedFragmentPagerAdapter {
        AdvertsRecyclerViewAdapterFragment frgSolicitudes;
        AdvertsRecyclerViewAdapterFragment frgAnuncios;
        AdvertsRecyclerViewAdapterFragment frgMisAnuncios;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        //Especifica que fragmento irá en cada página del viewPager
        @Override
        public Fragment getItem(int position) {
            Fragment frg = null;
            switch (position) {
                case 0:
                    if (frgSolicitudes == null)
                        frgSolicitudes = AdvertsRecyclerViewAdapterFragment.newInstance(AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS);
                    frg = frgSolicitudes;
                    break;
                case 1:
                    if (frgAnuncios == null)
                        frgAnuncios = AdvertsRecyclerViewAdapterFragment.newInstance(AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS);
                    frg = frgAnuncios;
                    break;
                case 2:
                    if (frgMisAnuncios == null)
                        frgMisAnuncios = AdvertsRecyclerViewAdapterFragment.newInstance(AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS);
                    frg = frgMisAnuncios;
                    break;
            }
            return frg;
        }
    }

    @Override
    public void onAttach(Context context) {
        listener = (AllowFilters) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();

    }

    public void confEmptyViewsNormales(){
        ((AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(0)).confEmptyView(R.drawable.tab_suscripciones, getString(R.string.text_emptyView_tab_suscritos));
        ((AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1)).confEmptyView(R.drawable.tab_anuncios, getString(R.string.text_emptyView_tab_anuncios));
        ((AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(2)).confEmptyView(R.drawable.tab_mis_anuncios, getString(R.string.text_emptyView_tab_misAnuncios));
    }
    public void confEmptyViewsSinUbicacion(){
        ((AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1)).confEmptyView(R.drawable.location_disabled, getString(R.string.emptyView_location_disabled));
    }

    public void confEmptyViewsSinUbicacion(String textoEmptyView, View.OnClickListener onClickListener){
        ((AdvertsRecyclerViewAdapterFragment) vpAdapter.getItem(1)).confEmptyViewWithClick(R.drawable.location_disabled, textoEmptyView, onClickListener);
    }
}

package c.proyecto.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

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
    }

    private static MainPresenter mPresenter;
    private static Usuario user;
    private SectionsPagerAdapter vpAdapter;
    private ViewPager viewPager;
    private SmartTabLayout tabLayout;
    private AllowFilters listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = MainPresenter.getPresentador(getActivity());
        user = ((MainActivity) getActivity()).getmUser();
        confViewPager();
    }

    private void confViewPager() {
        vpAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        viewPager.setAdapter(vpAdapter);
        tabLayout = (SmartTabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1); // el fragmento principal será el de anuncios
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                AdvertsRecyclerViewFragment fragmento = (AdvertsRecyclerViewFragment) vpAdapter.getItem(viewPager.getCurrentItem());
                ((AdvertsRecyclerViewAdapter.OnAdapterItemLongClick) getActivity()).setAdapterAllowMultiDeletion(fragmento.getmAdapter());
                AdvertsRecyclerViewAdapter adapter = fragmento.getmAdapter();
                if (adapter != null && adapter.getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
                    listener.showFilterIcon();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(viewPager.getCurrentItem());
                if (f != null)
                    f.disableMultideletion();

                AdvertsRecyclerViewAdapter adapter = f.getmAdapter();
                if (adapter != null && adapter.getAdapter_type() != AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
                    listener.hideFilterIcon();
            }
        });
        mPresenter.initializeFirebaseListeners(user);
    }

    public void addAdvertToAdapter(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().addItem(a);
    }

    public void replaceAdvertFromAdapter(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().replaceItem(a);
    }

    public void addSubToAdapter(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(0);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
            f.getmAdapter().addItem(a);
    }

    public void replaceSubFromAdapter(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(0);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
            f.getmAdapter().replaceItem(a);
    }

    public void addUserAdvertToAdapter(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(2);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            f.getmAdapter().addItem(a);
    }

    public void replaceUserAdvertFromAdapter(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(2);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            f.getmAdapter().replaceItem(a);
    }

    public void removeSub(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(0);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
            f.getmAdapter().removeItem(a);
    }

    public void removeAdvert(Anuncio a) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().removeItem(a);
    }


    public void loadFilteredAdverts(ArrayList<Anuncio> filteredAdverts) {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().addItems(filteredAdverts);
    }


    public void removeFilter() {
        AdvertsRecyclerViewFragment f = (AdvertsRecyclerViewFragment) vpAdapter.getItem(1);
        if (f.getmAdapter().getAdapter_type() == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
            f.getmAdapter().setFiltersApplied(false);
    }

    public Usuario getUser() {
        return user;
    }

    //Adaptader
    class SectionsPagerAdapter extends CachedFragmentPagerAdapter {
        AdvertsRecyclerViewFragment frgSolicitudes;
        AdvertsRecyclerViewFragment frgAnuncios;
        AdvertsRecyclerViewFragment frgMisAnuncios;

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
            switch (position) {
                case 0:
                    if (frgSolicitudes == null)
                        frgSolicitudes = AdvertsRecyclerViewFragment.newInstance(AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS);
                    return frgSolicitudes;
                case 1:
                    if (frgAnuncios == null)
                        frgAnuncios = AdvertsRecyclerViewFragment.newInstance(AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS);
                    return frgAnuncios;
                case 2:
                    if (frgMisAnuncios == null)
                        frgMisAnuncios = AdvertsRecyclerViewFragment.newInstance(AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS);
                    return frgMisAnuncios;
            }
            return null;
        }

        //Establece los títulos de los tabs.
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getActivity().getString(R.string.tab_solicitudes);
                case 1:
                    return getActivity().getString(R.string.tab_anuncios);
                case 2:
                    return getActivity().getString(R.string.tab_mis_anuncios);
            }
            return null;
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
}

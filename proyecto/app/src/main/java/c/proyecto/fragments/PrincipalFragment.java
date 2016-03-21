package c.proyecto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.CachedFragmentPagerAdapter;
import c.proyecto.adapters.MyRecyclerViewAdapter;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;


public class PrincipalFragment extends Fragment {

    private MainPresenter mPresenter;
    private SectionsPagerAdapter vpAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Usuario user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = MainPresenter.getPresentador(getActivity());
        user = ((MainActivity) getActivity()).getUser();
        confViewPager();
    }

    private void confViewPager() {
        vpAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        viewPager.setAdapter(vpAdapter);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mPresenter.getAllUserSubs(user);
                        break;
                    case 1:
                        mPresenter.getAdverts(user);
                        break;
                    case 2:
                        mPresenter.getAllUserPublishedAdverts(user);
                        break;
                }
                //Coloca como tab actual la presionada
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void advertsHaveBeenObtained(ArrayList<Anuncio> anuncios){
        if (tabLayout.getSelectedTabPosition() == 1) {
            MyRecyclerViewFragment f = (MyRecyclerViewFragment) vpAdapter.getItem(1);
            if (f.getmAdapter().getAdapter_type() == MyRecyclerViewAdapter.ADAPTER_TYPE_ADVS)
                f.getmAdapter().replaceAll(anuncios);
        }
    }

    public void userSubsHaveBeenObtained(ArrayList<Anuncio> anuncios) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            MyRecyclerViewFragment f = (MyRecyclerViewFragment) vpAdapter.getItem(0);
            if (f.getmAdapter().getAdapter_type() == MyRecyclerViewAdapter.ADAPTER_TYPE_SUBS)
                f.getmAdapter().replaceAll(anuncios);
        }
    }

    public void advertsPublishedByUserObtained(ArrayList<Anuncio> anuncios) {
        if (tabLayout.getSelectedTabPosition() == 2) {
            MyRecyclerViewFragment f = (MyRecyclerViewFragment) vpAdapter.getItem(2);
            if (f.getmAdapter().getAdapter_type() == MyRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
                f.getmAdapter().replaceAll(anuncios);
        }
    }

    //Adaptader
    class SectionsPagerAdapter extends CachedFragmentPagerAdapter {
        MyRecyclerViewFragment frgSolicitudes;
        MyRecyclerViewFragment frgAnuncios;
        MyRecyclerViewFragment frgMisAnuncios;

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
                        frgSolicitudes = MyRecyclerViewFragment.newInstance(MyRecyclerViewAdapter.ADAPTER_TYPE_SUBS);
                    return frgSolicitudes;
                case 1:
                    if (frgAnuncios == null)
                        frgAnuncios = MyRecyclerViewFragment.newInstance(MyRecyclerViewAdapter.ADAPTER_TYPE_ADVS);
                    return frgAnuncios;
                case 2:
                    if (frgMisAnuncios == null)
                        frgMisAnuncios = MyRecyclerViewFragment.newInstance(MyRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS);
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
}

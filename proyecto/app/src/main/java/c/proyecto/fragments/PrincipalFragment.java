package c.proyecto.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.CachedFragmentPagerAdapter;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;


public class PrincipalFragment extends Fragment{

    MainPresenter mPresenter;
    private SectionsPagerAdapter vpAdapter;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = MainPresenter.getPresentador(getActivity());
        confViewPager();
    }

    private void confViewPager() {
            vpAdapter = new SectionsPagerAdapter(getChildFragmentManager());
            viewPager = (ViewPager) getActivity().findViewById(R.id.container);
            viewPager.setAdapter(vpAdapter);

            final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
    }


    //Adaptader
    class SectionsPagerAdapter extends CachedFragmentPagerAdapter {
        RecyclerViewFragment frgSolicitudes;
        RecyclerViewFragment frgAnuncios;
        RecyclerViewFragment frgMisAnuncios;

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
            MainPresenter presenter = ((MainActivity) getActivity()).getmPresenter();
            Usuario u = ((MainActivity) getActivity()).getUser();

            switch (position){
                case 0:
                    if(frgSolicitudes == null)
                        frgSolicitudes = RecyclerViewFragment.newInstance(presenter.getAllUserSubs(u), false);
                    return frgSolicitudes;
                case 1:
                    if(frgAnuncios == null)
                        frgAnuncios = RecyclerViewFragment.newInstance(presenter.getAdverts(), false);
                    return frgAnuncios;
                case 2:
                    if(frgMisAnuncios == null)
                        frgAnuncios  = RecyclerViewFragment.newInstance(presenter.getAllUserPublishAdverts(u), true);
                    return frgAnuncios;
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

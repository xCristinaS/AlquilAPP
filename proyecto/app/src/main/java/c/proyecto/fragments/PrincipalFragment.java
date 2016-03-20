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

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.CachedFragmentPagerAdapter;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;


public class PrincipalFragment extends Fragment {

    private MainPresenter mPresenter;
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

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                            moverFab(300, R.drawable.ic_photo_camera_white_24dp);
                        else {
                            //Consigue la posición exacta en pantalla del final del imgFoto
                            int posFinal = (int) (fab.getY() - ((EditorFragment) getItem(0)).getPosDebajoImgFoto());
                            moverFab(-posFinal, R.drawable.ic_photo_camera_white_24dp);
                        }
                        break;
                    case 1:
                        moverFab(0, R.drawable.ic_add);
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
        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void advertsHaveBeenObtained(ArrayList<Anuncio> anuncios){
        RecyclerViewFragment f = (RecyclerViewFragment)vpAdapter.getItem(viewPager.getCurrentItem());
        f.getmAdapter().replaceAll(anuncios);
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
            Usuario u = ((MainActivity) getActivity()).getUser();

            switch (position) {
                case 0:
                    if (frgSolicitudes == null)
                        frgSolicitudes = RecyclerViewFragment.newInstance(false);
                    return frgSolicitudes;
                case 1:
                    if (frgAnuncios == null)
                        frgAnuncios = RecyclerViewFragment.newInstance(false);
                    return frgAnuncios;
                case 2:
                    if (frgMisAnuncios == null)
                        frgAnuncios = RecyclerViewFragment.newInstance(true);
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

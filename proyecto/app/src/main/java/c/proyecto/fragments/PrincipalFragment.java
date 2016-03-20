package c.proyecto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.CachedFragmentPagerAdapter;
import c.proyecto.presenters.MainPresenter;


public class PrincipalFragment extends Fragment{

    MainPresenter mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = MainPresenter.getPresentador(getActivity());
    }



    //Adaptader
    class SectionsPagerAdapter extends CachedFragmentPagerAdapter {
        RecyclerViewFragment frgSolicitudes;
        RecyclerViewFragment frgAnuncios;
        RecyclerViewFragment frgMisAnuncios;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //Especifica que fragmento irá en cada página del viewPager
        @Override
        public Fragment getItem(int position) {
            MainPresenter presenter = ((MainActivity) getActivity()).getmPresenter();

            switch (position){
                case 0:
                    if(frgSolicitudes == null)
                        frgSolicitudes = RecyclerViewFragment.newInstance();

                    return frgSolicitudes;
                case 1:
                    if(frgAnuncios == null)
                        frgAnuncios = VisitasFragment.newInstance(mAlumno);
                    return frgAnuncios;
                case 2:
                    if(frgMisAnuncios == null)
                        frgAnuncios  = new RecyclerViewFragment();
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

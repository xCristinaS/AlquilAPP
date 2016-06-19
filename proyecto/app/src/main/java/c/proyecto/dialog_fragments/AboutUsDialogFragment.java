package c.proyecto.dialog_fragments;


import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import c.proyecto.R;
import c.proyecto.pojo.Usuario;

public class AboutUsDialogFragment extends AppCompatDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_about_us, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    private void initView(View view) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/nunito.regular.ttf");
        ((TextView) view.findViewById(R.id.lblNombreApp1)).setTypeface(typeface);
        ((TextView) view.findViewById(R.id.lblNombreApp2)).setTypeface(typeface);

        try {
            String version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            ((TextView) view.findViewById(R.id.lblVersion)).setText("Versión " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }
}

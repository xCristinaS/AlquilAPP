package c.proyecto.dialog_fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import c.proyecto.R;
import c.proyecto.pojo.Usuario;

public class AboutUsDialogFragment extends AppCompatDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_about_us, container, false);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle(R.string.title_AboutUsDialog);
        super.setupDialog(dialog, style);
    }
}

package c.proyecto.dialog_fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.appyvet.rangebar.RangeBar;

import c.proyecto.Constantes;
import c.proyecto.R;

public class FilterDialogFramgent extends AppCompatDialogFragment {

    public interface ApplyFilters {
        void filterRequest(String[] tipoVivienda, int minPrice, int maxPrice, int minSize, int maxSize);
    }

    private ImageView imgPiso, imgCasa, imgHabitacion;
    private RangeBar rangeBarPrecio, rangeBarTamanio;
    private Button btnFiltrar;
    private ApplyFilters listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_filters, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle("Filtrar Anuncios");
        super.setupDialog(dialog, style);
    }

    private void initViews(View view) {
        imgPiso = (ImageView) view.findViewById(R.id.imgPiso);
        imgCasa = (ImageView) view.findViewById(R.id.imgCasa);
        imgHabitacion = (ImageView) view.findViewById(R.id.imgHabitacion);
        btnFiltrar = (Button) view.findViewById(R.id.btnFiltrar);
        rangeBarPrecio = (RangeBar) view.findViewById(R.id.rangeBarPrecio);
        rangeBarTamanio = (RangeBar) view.findViewById(R.id.rangeBarTamanio);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainData();
                dismiss();
            }
        });

        confImgTipoVivienda();
    }

    private void confImgTipoVivienda() {
        imgCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintVivienda(imgCasa);
            }
        });
        imgHabitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintVivienda(imgHabitacion);
            }
        });
        imgPiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tintVivienda(imgPiso);
            }
        });
    }

    private void tintVivienda(ImageView v) {
        if (v.getColorFilter() == null)
            v.setColorFilter(getResources().getColor(R.color.colorAccent));
        else
            v.clearColorFilter();
    }

    private void obtainData() {
        String[] viviendasSeleccionadas = new String[3];
        int minTam, maxTam, minPrice, maxPrice;

        minTam = rangeBarTamanio.getLeftIndex() + 1;
        maxTam = rangeBarTamanio.getRightIndex() + 1;
        minPrice = rangeBarPrecio.getLeftIndex() + 1;
        maxPrice = rangeBarPrecio.getRightIndex() + 1;

        if (imgPiso.getColorFilter() != null)
            viviendasSeleccionadas[0] = Constantes.PISO;
        if (imgCasa.getColorFilter() != null)
            viviendasSeleccionadas[1] = Constantes.CASA;
        if (imgHabitacion.getColorFilter() != null)
            viviendasSeleccionadas[2] = Constantes.HABITACION;

        listener.filterRequest(viviendasSeleccionadas, minPrice, maxPrice, minTam, maxTam);
    }

    @Override
    public void onAttach(Context context) {
        listener = (ApplyFilters) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}

package aroundwise.nepi.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import aroundwise.nepi.R;
import aroundwise.nepi.util.Filter;
import aroundwise.nepi.util.views.SAutoBgButton;

/**
 * Created by mihai on 31/08/16.
 */
public class FilterDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_filter, null);
        builder.setView(dialogView);
        SAutoBgButton btn_close = (SAutoBgButton) dialogView.findViewById(R.id.shop_filter_btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialog.this.dismiss();
            }
        });
        SAutoBgButton filterButton = (SAutoBgButton) dialogView.findViewById(R.id.shop_filter_btn_filter);
        Spinner genderSpinner = (Spinner) dialogView.findViewById(R.id.gender_type);
        initiailizeGenderSpinner(genderSpinner);
        final Spinner dealTypeSpinner = (Spinner) dialogView.findViewById(R.id.spinner_deal_type);
        EditText etLocation = (EditText) dialogView.findViewById(R.id.shop_filter_et_location);
        Spinner categorySpinner = (Spinner) dialogView.findViewById(R.id.spinner_category);
        final RangeSeekBar seekBar = (RangeSeekBar) dialogView.findViewById(R.id.shop_filter_seekbar_radius);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filter.rangeMin = seekBar.getSelectedMinValue().intValue();
                Filter.rangeMax = seekBar.getSelectedMaxValue().intValue();
                Filter.filterSet = true;

                //TODO finish this
            }
        });

        return builder.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initiailizeGenderSpinner(Spinner spinner) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.array.gender);
        spinner.setAdapter(arrayAdapter);
    }
}

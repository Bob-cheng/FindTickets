package space.bobcheng.myapplication;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {
    private EditText start_time;


    public CheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar c = Calendar.getInstance();
        start_time = (EditText) getView().findViewById(R.id.start_date);
        start_time.setText(c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DAY_OF_MONTH)+"日");
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(getActivity(),
                                    new DatePickerDialog.OnDateSetListener(){
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            start_time.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日");
                                        }
                                    }
                                    , c.get(Calendar.YEAR)
                                    , c.get(Calendar.MONTH)
                                    , c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}

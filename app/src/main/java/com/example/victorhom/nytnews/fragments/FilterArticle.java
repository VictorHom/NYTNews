package com.example.victorhom.nytnews.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.victorhom.nytnews.R;

import java.util.ArrayList;
import java.util.Locale;

// CLEAN UP - get rid of the unneeded biolerplate that came with the fragment
public class FilterArticle extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText etCalendar;
    Spinner sortOrder;
    CheckBox checkboxArt;
    CheckBox checkboxFashion;
    CheckBox checkboxSports;
    Button btnSave;
    OnDataPass dataPasser;
    public static final String ART = "arts";
    public static final String FASHION = "fashion";
    public static final String SPORTS = "sports";

    String date = "";
    String order = "";
    ArrayList<String> topics;



    private String mParam1;
    private String mParam2;

    public FilterArticle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterArticle.
     */
    public static FilterArticle newInstance(String param1, String param2) {
        FilterArticle fragment = new FilterArticle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pass the settings
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // need to set the calendar part
            if (bundle.get("date") != null) {
                this.date = convertQueryYearToUI(bundle.get("date").toString());
            }

            if (bundle.get("order") != null) {
                this.order = bundle.get("order").toString();
            }
            this.topics = new ArrayList<>();
            if (bundle.getStringArrayList("topics") != null) {
                this.topics = bundle.getStringArrayList("topics");
            }

        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void setCalendar() {
        final java.util.Calendar myCalendar = java.util.Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy" + ""; //In which you need put here
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);
                etCalendar.setText(sdf.format(myCalendar.getTime()));
            }

        };

        etCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        etCalendar = (EditText) view.findViewById(R.id.etCalendar);
        sortOrder = (Spinner) view.findViewById(R.id.spinnerSort);
        checkboxArt = (CheckBox) view.findViewById(R.id.art);
        checkboxFashion = (CheckBox) view.findViewById(R.id.fashion);
        checkboxSports = (CheckBox) view.findViewById(R.id.sports);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        setCalendar();
        setSavedValues();
        setSaveButtonListener();
    }

    private void setSavedValues() {
        // calendar
        if (!this.date.equals("")) {
            etCalendar.setText(this.date);
        }

        // sort order
        if (order.equals("")) {
            sortOrder.setSelection(1);
        } else {
            sortOrder.setSelection(0);
        }

        // check box values
        if (topics.contains(ART)) {
            checkboxArt.setChecked(true);
        }
        if (topics.contains(FASHION)) {
            checkboxFashion.setChecked(true);
        }
        if (topics.contains(SPORTS)) {
            checkboxSports.setChecked(true);
        }
    }

    private void setSaveButtonListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String calendarText = formatCalendarForQuery(etCalendar.getText().toString());

                // if selected "newest" don't need to add to the query
                 String orderText = formatOrderForQuery(sortOrder.getSelectedItem().toString());

                // get the checkbox desk values
                ArrayList<String> topics = formatTopicsForQuery();

                // data for the articles activity to access data for queries
                passData(calendarText, orderText, topics);
                // done with the fragment
                getActivity().getSupportFragmentManager().beginTransaction().remove(FilterArticle.this).commit();
            }
        });
    }

    // turn yyyymmdd to mm/dd/yyyy
    private String convertQueryYearToUI(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);
        return month + "/" + day + "/" + year;

    }

    ////// Helper methods to pass data from fragment back to articles activity ///////
    private String formatCalendarForQuery(String date) {
        if (!etCalendar.getText().toString().equals("")){
            StringBuilder res = new StringBuilder();
            String[] mdy = date.split("/");
            res.append(mdy[2]);
            res.append(mdy[0]);
            res.append(mdy[1]);
            // getting in the format of yyyymmdd;
            return res.toString();
        }
        return "";
    }

    private String formatOrderForQuery(String orderSelection) {
        if (orderSelection.equals("newest")) {
            return "";
        }
        return orderSelection;
    }

    private ArrayList<String> formatTopicsForQuery() {
        ArrayList<String> topics = new ArrayList<>();
        if (checkboxArt.isChecked()) {
            topics.add(ART);
        }
        if (checkboxFashion.isChecked()) {
            topics.add(FASHION);
        }
        if (checkboxSports.isChecked()) {
            topics.add(SPORTS);
        }
        return topics;
    }

    ////// END Helper methods to pass data from fragment back to articles activity ///////

    public interface OnDataPass {
        public void getSetttings(String date, String order, ArrayList<String> topics);
    }

    public void passData(String date, String sortOrder, ArrayList<String> topics) {
        dataPasser.getSetttings(date, sortOrder, topics);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_article, container, false);
    }

    @Override
    public void onResume() {
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        getDialog().getWindow().setLayout(width, height);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }
}

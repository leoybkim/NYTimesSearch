package com.leoybkim.nytimessearch.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.leoybkim.nytimessearch.R;

/**
 * Created by leo on 23/03/17.
 */

public class FilterDialogFragment extends android.support.v4.app.DialogFragment {

    // View
    private View mView;
    private Spinner mSpinnerDate;
    private Spinner mSpinnerOrder;
    private Switch mSwitchArts;
    private Switch mSwitchEducation;
    private Switch mSwitchSports;

    // Filter data
    private String mFilterDate;
    private String mFilterOrder;
    private boolean mFilterArts;
    private boolean mFilterEducation;
    private boolean mFilterSports;

    // Listener
    private OnFilteredArticleListener mListener;

    // Empty constructor required for DialogFragment
    public FilterDialogFragment() { }

    // Custom listener
    public interface OnFilteredArticleListener {
        void onFilteredArticle(String filterDate, String filterOrder, boolean filterArts, boolean filterEducation, boolean filterSports);
    }

    public void setOnFilteredArticleListener(OnFilteredArticleListener listener) {
        mListener = listener;
    }

    // Android uses empty constructor for fragments by default
    // By creating newInstance, we can send data to this fragment when recreating
    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        return frag;
    }

    // Inflate dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_filter, container, false);
        return mView;
    }

    // Find view items
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_date_filter);
        mSpinnerOrder = (Spinner) view.findViewById(R.id.spinner_sort_order);
        mSwitchArts  = (Switch) view.findViewById(R.id.switch_topic_arts);
        mSwitchEducation  = (Switch) view.findViewById(R.id.switch_topic_education);
        mSwitchSports  = (Switch) view.findViewById(R.id.switch_topic_sports);
    }

    // Sends data back to SearchAcvity when dialog is dismissed
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mFilterDate = mSpinnerDate.getSelectedItem().toString();
        mFilterOrder = mSpinnerOrder.getSelectedItem().toString();
        mFilterArts = mSwitchArts.isChecked();
        mFilterEducation = mSwitchEducation.isChecked();
        mFilterSports = mSwitchSports.isChecked();
        mListener.onFilteredArticle(mFilterDate, mFilterOrder, mFilterArts, mFilterEducation, mFilterSports);
    }
}

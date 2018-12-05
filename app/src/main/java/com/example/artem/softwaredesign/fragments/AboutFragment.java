package com.example.artem.softwaredesign.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.interfaces.OnFragmentAboutListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    private OnFragmentAboutListener onFragmentAboutListener;

    private TextView textViewForImei;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        textViewForImei = view.findViewById(R.id.text_view_for_imei);
        Button buttonForUpdateImei = view.findViewById(R.id.button_for_update_imei);
        TextView textViewForVersion = view.findViewById(R.id.text_view_for_version);


        textViewForVersion.setText(onFragmentAboutListener.getVersion());

        String imei = onFragmentAboutListener.getImei();
        if (imei != null){
            textViewForImei.setText(imei);
        }
        buttonForUpdateImei.setOnClickListener(v -> onUpdateButtonClick());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnFragmentAboutListener) {
            onFragmentAboutListener = (OnFragmentAboutListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private boolean setImeiInTextView(){
        String imei = onFragmentAboutListener.getImei();
        if (imei != null){
            textViewForImei.setText(imei);
            return true;
        }
        return false;
    }

    private void onUpdateButtonClick(){
        if (!setImeiInTextView()){
            final View view = getActivity().findViewById(android.R.id.content);
            final String description = onFragmentAboutListener.getDescriptionPermission();

            final Snackbar bar = Snackbar.make(view, description, Snackbar.LENGTH_INDEFINITE);
            bar.setAction(getResources().getString(R.string.ok), v -> {
                onFragmentAboutListener.requestPermissionForImei();
                setImeiInTextView();
            }).show();
        }
    }

}

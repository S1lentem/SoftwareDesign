package com.example.artem.softwaredesign.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentNewSourceListener;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class NewSourceFragment extends Fragment {

    private OnFragmentNewSourceListener onFragmentNewSourceListener;

    private TextView newsSourceTextView;
    private TextInputLayout newsSourceTextInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_source, container, false);

        newsSourceTextView = view.findViewById(R.id.first_news_source);
        newsSourceTextInputLayout = view.findViewById(R.id.first_news_source_text_input_layout);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentNewSourceListener) {
            onFragmentNewSourceListener = (OnFragmentNewSourceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentNewSourceListener");
        }
    }

    public boolean isValidSource(){
        if (!newsSourceTextView.getText().toString().isEmpty()){
            return true;
        }
        newsSourceTextInputLayout.setError("Ooops");
        return false;
    }
}

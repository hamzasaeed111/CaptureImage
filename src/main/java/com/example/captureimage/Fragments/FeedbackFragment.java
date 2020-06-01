package com.example.captureimage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.captureimage.Fragments.FirstFragment;
import com.example.captureimage.R;
import com.example.captureimage.Fragments.SecondFragment;

public class FeedbackFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        final Button suggest = (Button) root.findViewById(R.id.suggestion);
        final Button bug = (Button) root.findViewById(R.id.bugReport);

        View.OnClickListener topButtonsListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (view.getId() == R.id.suggestion)
                {
                    bug.setSelected(false);
                    suggest.setSelected(true);

                    Fragment first = new FirstFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, first);
                    fragmentTransaction.commit();
                }

                else {
                    suggest.setSelected(false);
                    bug.setSelected(true);

                    Fragment second = new SecondFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.commit();
                }
            }
        };

        suggest.setOnClickListener(topButtonsListener);
        bug.setOnClickListener(topButtonsListener);
        suggest.performClick();
        return root;
    }
}
package gamal.myappnew.clientside.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gamal.myappnew.clientside.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCommentFragment extends Fragment {
RecyclerView recyclerView;

    public ShowCommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_comment, container, false);
    }

}

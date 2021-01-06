package com.example.socialmacropad.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialmacropad.R;
import com.example.socialmacropad.models.Action;
import com.example.socialmacropad.models.MacroPad;
import com.example.socialmacropad.util.MacroPadAdapter;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private ListView listView;
    private MacroPadAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.macroPad_list);
        ArrayList<MacroPad> macroPadList = new ArrayList<>();

        // EJEMPLOS
        macroPadList.add(new MacroPad("test", "test" , "name",  new Action("a", "a"), new Action("b", "b"), new Action("c", "c"),
                new Action("d", "d"), new Action("e", "e"), new Action("f", "f")));

        macroPadList.add(new MacroPad("test", "test" , "name2",  new Action("a", "a"), new Action("b", "b"), new Action("c", "c"),
                new Action("d", "d"), new Action("e", "e"), new Action("f", "f")));

        mAdapter = new MacroPadAdapter(getActivity(), macroPadList);
        listView.setAdapter(mAdapter);
    }
}
package com.example.socialmacropad.activities.bottomNavActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialmacropad.R;
import com.example.socialmacropad.models.MacroPad;
import com.example.socialmacropad.util.MacroPadAdapterDiscover;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment {

    private ListView listView;
    private MacroPadAdapterDiscover mAdapter;
    private FirebaseFirestore db;
    private String TAG = DiscoverFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.macroPad_list);
        ArrayList<MacroPad> macroPadList = new ArrayList<>();
        mAdapter = new MacroPadAdapterDiscover(getActivity(), macroPadList);
        listView.setAdapter(mAdapter);



        // get macropads stored in firestore
        retrieveMacroPadsFromFirestore(macroPadList, mAdapter);
    }



    private void retrieveMacroPadsFromFirestore(ArrayList<MacroPad> macroPadList, MacroPadAdapterDiscover mAdapter) {
        db = FirebaseFirestore.getInstance();
        db.collection("macropad").orderBy("padId", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                MacroPad macropad = document.toObject(MacroPad.class);
                                macroPadList.add(macropad);
                                mAdapter.notifyDataSetChanged();

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
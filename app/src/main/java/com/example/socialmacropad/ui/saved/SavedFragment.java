package com.example.socialmacropad.ui.saved;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialmacropad.R;
import com.example.socialmacropad.models.MacroPad;
import com.example.socialmacropad.util.MacroPadAdapterSaved;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private SavedViewModel savedViewModel;
    private ListView listView;
    private MacroPadAdapterSaved mAdapter;
    private FirebaseFirestore db;
    private String TAG = SavedFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel =
                new ViewModelProvider(this).get(SavedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.macroPad_list);
        TextView emptyText = (TextView) getView().findViewById(R.id.txtNoElements);
        listView.setEmptyView(emptyText);


        // Set adapter for macropadlist
        ArrayList<MacroPad> macroPadList = new ArrayList<>();
        mAdapter = new MacroPadAdapterSaved(getActivity(), macroPadList);
        listView.setAdapter(mAdapter);


        // get macropads stored in firestore
        retrieveMacroPadsFromFirestore(macroPadList, mAdapter);



    }



    private void retrieveMacroPadsFromFirestore(ArrayList<MacroPad> macroPadList, MacroPadAdapterSaved mAdapter) {
        db = FirebaseFirestore.getInstance();
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users/"+UserID+"/savedPads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Get macropad by reference
                                DocumentReference ref = (DocumentReference) document.getData().get("padId");
                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot rawMacropad = task.getResult();
                                            if (rawMacropad.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + rawMacropad.getData());
                                                MacroPad macropad = rawMacropad.toObject(MacroPad.class);
                                                macroPadList.add(macropad);
                                                mAdapter.notifyDataSetChanged();
                                                Log.d(TAG, "macropadID: " + macropad.getPadId());
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}
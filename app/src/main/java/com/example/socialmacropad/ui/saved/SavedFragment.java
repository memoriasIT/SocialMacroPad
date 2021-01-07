package com.example.socialmacropad.ui.saved;

import android.os.Bundle;
import android.util.Log;
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
import com.example.socialmacropad.ui.discover.DiscoverFragment;
import com.example.socialmacropad.util.MacroPadAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private SavedViewModel savedViewModel;
    private ListView listView;
    private MacroPadAdapter mAdapter;
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
        ArrayList<MacroPad> macroPadList = new ArrayList<>();
        mAdapter = new MacroPadAdapter(getActivity(), macroPadList);
        listView.setAdapter(mAdapter);

        // Demo macropad
//        macroPadList.add(new MacroPad("test", "test" , "name",  "description", "000000",    new Action("a", "a", "000000"), new Action("b", "b", "000000"), new Action("c", "c", "000000"),
//                new Action("d", "d", "000000"), new Action("e", "e", "000000"), new Action("f", "f", "000000")));

        // get macropads stored in firestore
        retrieveMacroPadsFromFirestore(macroPadList, mAdapter);

    }

    private void retrieveMacroPadsFromFirestore(ArrayList<MacroPad> macroPadList, MacroPadAdapter mAdapter) {
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
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
//                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                                DatabaseReference ref = database.child("");
//                                Query connectedUser = ref.equalTo(document.getData().get("padId"));
//                                connectedUser.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                        // Getting Post failed, log a message
//                                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                                        // ...
//                                    }
//                                });
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}
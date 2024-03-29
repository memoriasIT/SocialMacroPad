package com.example.socialmacropad.activities.bottomNavActivities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.activityGroups.AddNewGroupActivity;
import com.example.socialmacropad.activities.communication.BluetoothList;
import com.example.socialmacropad.activities.introAuth.IntroActivity;
import com.example.socialmacropad.event.UIToastEvent;
import com.example.socialmacropad.helper.EnhancedSharedPreferences;
import com.example.socialmacropad.models.MacroPad;
import com.example.socialmacropad.util.Config;
import com.example.socialmacropad.util.Constants;
import com.example.socialmacropad.util.GroupAdapterHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private BluetoothAdapter bluetoothAdapter = null;
    private EnhancedSharedPreferences sharedPreferences;
    private GroupAdapterHome mAdapter;
    private ListView listView;
    private String TAG = HomeFragment.class.getSimpleName();
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup sharedPreferences (our implementation of singletonmap)
        sharedPreferences = EnhancedSharedPreferences.getInstance(getActivity(), getString(R.string.sharedPreferencesKey));

        // Set up OnClickListeners
        Button btnConnect = getView().findViewById(R.id.btnConnectBluetooth);
        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BluetoothList.class);
                startActivityForResult(intent, Constants.CONNECT_DEVICE_SECURE);
            }
        });

        Button btnLogout = getView().findViewById(R.id.btnDisconnect);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), IntroActivity.class);
                startActivity(intent);
            }
        });


        Button btnCreateGroup = getView().findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewGroupActivity.class);
                startActivity(intent);
            }
        });


        // Set up listview for showing groups
        listView = (ListView) getView().findViewById(R.id.groups_list);
        ArrayList<MacroPad> macroPadList = new ArrayList<>();

        mAdapter = new GroupAdapterHome(getActivity(), macroPadList);
        listView.setAdapter(mAdapter);

        // get macropads stored in firestore
        retrieveMacroPadsFromFirestore(macroPadList, mAdapter);


        // Reload data when FireBase sends event
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Notify list adapter
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Nothing
            }
        };


    }

    // Get grops from firestore
    private void retrieveMacroPadsFromFirestore(ArrayList<MacroPad> macroPadList, GroupAdapterHome mAdapter) {
        db = FirebaseFirestore.getInstance();
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users/" + UserID + "/pads")
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ///////////////////////////////////////////////
    //    EVENT BUS!
    // ///////////////////////////////////////////////
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    // Our custom customizable Toast event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIToastEvent(UIToastEvent event) {
        Config.Mensaje(getActivity(), event.getMessage(), event.getLongToast(), event.getIsWarning());
    }


}
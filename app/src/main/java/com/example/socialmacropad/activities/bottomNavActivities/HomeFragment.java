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
import com.example.socialmacropad.activities.communication.CommunicateActivity;
import com.example.socialmacropad.activities.introAuth.IntroActivity;
import com.example.socialmacropad.event.UIToastEvent;
import com.example.socialmacropad.helper.EnhancedSharedPreferences;
import com.example.socialmacropad.models.GroupOfActivities;
import com.example.socialmacropad.util.Config;
import com.example.socialmacropad.util.Constants;
import com.example.socialmacropad.util.GroupAdapterHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

        sharedPreferences = EnhancedSharedPreferences.getInstance(getActivity(), getString(R.string.sharedPreferencesKey));

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


        Button btnLastDevice = getView().findViewById(R.id.btnLastDevice);
        btnLastDevice .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String macAddress = sharedPreferences.getString(getString(R.string.preference_last_connected_device_macAddress), "");
                String deviceName = sharedPreferences.getString(getString(R.string.preference_last_connected_device_name), "");

                Log.d(TAG, macAddress);
                Log.d(TAG, deviceName);

                Intent intent = new Intent(getActivity(), CommunicateActivity.class);
                intent.putExtra("device_name", deviceName);
                intent.putExtra("device_mac", macAddress);
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

        listView = (ListView) getView().findViewById(R.id.groups_list);
        ArrayList<GroupOfActivities> groupsList = new ArrayList<>();

        mAdapter = new GroupAdapterHome(getActivity(), groupsList);
        listView.setAdapter(mAdapter);

        // get macropads stored in firestore
        retrieveGroupsFromFirestore(groupsList, mAdapter);

    }

    private void retrieveGroupsFromFirestore(ArrayList<GroupOfActivities> groupsList, GroupAdapterHome mAdapter) {
        db = FirebaseFirestore.getInstance();
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users/"+UserID+"/pads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GroupOfActivities group = document.toObject(GroupOfActivities.class);
                                groupsList.add(group);
                                mAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIToastEvent(UIToastEvent event) {
        Config.Mensaje(getActivity(), event.getMessage(), event.getLongToast(), event.getIsWarning());
    }


}
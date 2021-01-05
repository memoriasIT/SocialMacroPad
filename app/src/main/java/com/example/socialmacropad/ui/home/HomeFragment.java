package com.example.socialmacropad.ui.home;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialmacropad.activities.BluetoothList;
import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.MainActivity;
import com.example.socialmacropad.event.UIToastEvent;
import com.example.socialmacropad.helper.EnhancedSharedPreferences;
import com.example.socialmacropad.util.Config;
import com.example.socialmacropad.util.Constants;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private BluetoothAdapter bluetoothAdapter = null;
    private EnhancedSharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        Button btnAction1 = getView().findViewById(R.id.btnLastDevice);
        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String macAddress = sharedPreferences.getString(getString(R.string.preference_last_connected_device), "");
                EventBus.getDefault().post(new UIToastEvent("ULTIMO DISPOSITIVO " + macAddress, true, true));
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
//
//        switch (requestCode) {
//            case Constants.CONNECT_DEVICE_INSECURE:
//            case Constants.CONNECT_DEVICE_SECURE:
//                if (resultCode == Activity.RESULT_OK) {
//                    String macAddress = Objects.requireNonNull(data.getExtras()).getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    sharedPreferences.edit().putString(getString(R.string.preference_last_connected_device), macAddress).apply();
//                    Log.e("MAC_ADDRESS: ", macAddress);
//                }
//
//        }
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
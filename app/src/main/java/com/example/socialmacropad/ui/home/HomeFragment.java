package com.example.socialmacropad.ui.home;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.socialmacropad.activities.DeviceListActivity;
import com.example.socialmacropad.R;
import com.example.socialmacropad.androidbluetoothserial.BluetoothPairedDevices;
import com.example.socialmacropad.event.UIToastEvent;
import com.example.socialmacropad.helper.EnhancedSharedPreferences;
import com.example.socialmacropad.util.Config;
import com.example.socialmacropad.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private BluetoothAdapter bluetoothAdapter = null;
    private EnhancedSharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
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

        Button btnConnect = getView().findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                new AlertDialog.Builder(getActivity()).setTitle(R.string.text_disconnect).setMessage(R.string.text_disconnect_confirm).setPositiveButton(getString(R.string.text_yes_confirm), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(), "Botón de conexión", Toast.LENGTH_SHORT);
//                    }
//                }).setNegativeButton(getString(R.string.text_cancel), null).show();

//                Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                Intent intent = new Intent(getActivity(), BluetoothPairedDevices.class);
                startActivityForResult(intent, Constants.CONNECT_DEVICE_SECURE);
            }
        });


        Button btnAction1 = getView().findViewById(R.id.btnAction1);
        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String macAddress = sharedPreferences.getString(getString(R.string.preference_last_connected_device), "");
                EventBus.getDefault().post(new UIToastEvent("ULTIMO DISPOSITIVO " + macAddress, true, true));
            }
        });



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    @Override
    public void onResume() {
        super.onResume();



//        Activamos bluetooth
        if(!bluetoothAdapter.isEnabled()){
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        bluetoothAdapter.enable();
                    } catch (RuntimeException e ){
                        EventBus.getDefault().post(new UIToastEvent(getString(R.string.text_no_bluetooth_permission), true, true));
                    }
                }
            };
            thread.start();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.CONNECT_DEVICE_INSECURE:
            case Constants.CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    String macAddress = Objects.requireNonNull(data.getExtras()).getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    sharedPreferences.edit().putString(getString(R.string.preference_last_connected_device), macAddress).apply();
                    Log.e("MAC_ADDRESS: ", macAddress);
                }

        }
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
package com.example.socialmacropad.activities.communication;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.bottomNavActivities.MainContent;
import com.example.socialmacropad.helper.EnhancedSharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BluetoothList extends AppCompatActivity {

    private BluetoothListViewModel viewModel;
    private EnhancedSharedPreferences sharedPreferences;
    private String TAG = BluetoothList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        // Setup sharedPreferences
        sharedPreferences = EnhancedSharedPreferences.getInstance(getApplicationContext(), getString(R.string.sharedPreferencesKey));

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(BluetoothListViewModel.class);

        // This method return false if there is an error, so if it does, we should close.
        if (!viewModel.setupViewModel()) {
            finish();
            return;
        }

        // Setup our Views
        RecyclerView deviceList = findViewById(R.id.main_devices);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.main_swiperefresh);

        // Setup the RecyclerView
        deviceList.setLayoutManager(new LinearLayoutManager(this));
        DeviceAdapter adapter = new DeviceAdapter();
        deviceList.setAdapter(adapter);

        // Setup the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshPairedDevices();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Start observing the data sent to us by the ViewModel
        viewModel.getPairedDeviceList().observe(BluetoothList.this, adapter::updateList);

        // Immediately refresh the paired devices list
        viewModel.refreshPairedDevices();
    }

    // Called when clicking on a device entry to start the CommunicateActivity
    public void openCommunicationsActivity(String deviceName, String macAddress) {
        sharedPreferences.edit().putString(getString(R.string.preference_last_connected_device_macAddress), macAddress).apply();
        sharedPreferences.edit().putString(getString(R.string.preference_last_connected_device_name), deviceName).apply();

        Log.d(TAG, deviceName);

//        Intent intent = new Intent(this, CommunicateActivity.class);
        Intent intent = new Intent(this, MainContent.class);
//        intent.putExtra("device_name", deviceName);
//        intent.putExtra("device_mac", macAddress);
        startActivity(intent);
    }

    // Called when a button in the action bar is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // If the back button was pressed, handle it the normal way
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called when the user presses the back button
    @Override
    public void onBackPressed() {
        // Close the activity
        finish();
    }

    // A class to hold the data in the RecyclerView
    private class DeviceViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout layout;
        private final TextView text1;
        private final TextView text2;

        DeviceViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.list_item);
            text1 = view.findViewById(R.id.list_item_text1);
            text2 = view.findViewById(R.id.list_item_text2);
        }

        void setupView(BluetoothDevice device) {
            text1.setText(device.getName());
            text2.setText(device.getAddress());
            layout.setOnClickListener(view -> openCommunicationsActivity(device.getName(), device.getAddress()));
        }
    }

    // A class to adapt our list of devices to the RecyclerView
    private class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {
        private BluetoothDevice[] deviceList = new BluetoothDevice[0];

        @NotNull
        @Override
        public DeviceViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NotNull DeviceViewHolder holder, int position) {
            holder.setupView(deviceList[position]);
        }

        @Override
        public int getItemCount() {
            return deviceList.length;
        }

        void updateList(Collection<BluetoothDevice> deviceList) {
            this.deviceList = deviceList.toArray(new BluetoothDevice[0]);
            notifyDataSetChanged();
        }
    }
}
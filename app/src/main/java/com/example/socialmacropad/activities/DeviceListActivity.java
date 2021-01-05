package com.example.socialmacropad.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.socialmacropad.R;
import com.example.socialmacropad.event.UIToastEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.Set;

public class DeviceListActivity  extends Activity {
    public static final String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_device_list);
        setResult(Activity.RESULT_CANCELED);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter(); //Retorna el adaptador del bluetooth
        if(mBtAdapter == null){ // No está soportado en este dispositivo
            EventBus.getDefault().post(new UIToastEvent(getString(R.string.text_bluetooth_adapter_error), true, true));
            finish();
        }

        ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.list_item);

        // Vinculamos nuestro adaptador a nuestra lista
        ListView pairedListView = null;
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);


        Set<BluetoothDevice> pairedDevices = Collections.emptySet();
        try {
            //Obtenermos la lista de dispositivos BT sincronizados
//            pairedDevices = mBtAdapter.getBondedDevices();
        }catch (Exception e){
            EventBus.getDefault().post(new UIToastEvent(getString(R.string.text_bluetooth_adaptor_error), true, true));
            finish();
        }

        if (pairedDevices.size() > 0) {
            //Barremos la lista de dispositivos sincronizados
            for (BluetoothDevice device : pairedDevices) {
//                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            pairedDevicesArrayAdapter.add(getString(R.string.text_no_paired_devices));
        }
    }

    // Respondemos al evento click de cada item de la lista
    private final AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
//            mBtAdapter.cancelDiscovery();

            String info = ((TextView) v).getText().toString();

            if(info.length() > 16){
                String address = info.substring(info.length() - 17);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

//                Devolver resultado OK a la clase que ha llamado
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {
//            mBtAdapter.cancelDiscovery();
        }
    }


}


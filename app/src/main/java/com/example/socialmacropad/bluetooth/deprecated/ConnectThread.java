package com.example.socialmacropad.bluetooth.deprecated;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.socialmacropad.util.Constants;

import java.io.IOException;
import java.util.UUID;

@Deprecated
public class ConnectThread  {
////    private final BluetoothSocket mmSocket;
////    private final BluetoothDevice mmDevice;
//
//    private final String TAG = BluetoothSerialService.class.getSimpleName();
//    final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//
//    BluetoothAdapter mAdapter;
//
//    public ConnectThread(BluetoothDevice device) {
//        // Use a temporary object that is later assigned to mmSocket
//        // because mmSocket is final.
//        BluetoothSocket tmp = null;
////        mmDevice = device;
//
//        try {
//            // Get a BluetoothSocket to connect with the given BluetoothDevice.
//            // MY_UUID is the app's UUID string, also used in the server code.
////            tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
//////        } catch (IOException e) {
////            Log.e(TAG, "Socket's create() method failed", e);
////        }
////        mmSocket = tmp;
//    }
//
//    public void run() {
//        // Cancel discovery because it otherwise slows down the connection.
////        mAdapter.cancelDiscovery();
//
//        try {
//            // Connect to the remote device through the socket. This call blocks
//            // until it succeeds or throws an exception.
////            mmSocket.connect();
//        } catch (IOException connectException) {
//            // Unable to connect; close the socket and return.
//            try {
//                mmSocket.close();
//            } catch (IOException closeException) {
//                Log.e(TAG, "Could not close the client socket", closeException);
//            }
//            return;
//        }
//
//        // The connection attempt succeeded. Perform work associated with
//        // the connection in a separate thread.
////        connected(mmSocket, mmDevice, mSocketType);
//    }
//
//    private synchronized void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice, String mSocketType) {
////        if (mConnectThread != null){
////            mConnectThread.cancel();
////            mConnectThread = null;
////        }
////
////        if (mConnectedThread != null){
////            mConnectedThread.cancel();
////            mConnectedThread = null;
////        }
////
////
////        mConnectedThread = new BluetoothSerialService.ConnectedThread(mmSocket, mSocketType);
////        mConnectedThread.start();
////
////        if(mHandlerActivity != null) {
////            Message msg = mHandlerActivity.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
////            Bundle bundle = new Bundle();
////            bundle.putString(Constants.DEVICE_NAME, mmDevice.getName());
////            msg.setData(bundle);
////            mHandlerActivity.sendMessage(msg);
////        }
//        }
//
//    // Closes the client socket and causes the thread to finish.
//    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) {
//            Log.e(TAG, "Could not close the client socket", e);
//        }
//    }
}

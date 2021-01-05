package com.example.socialmacropad.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.socialmacropad.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BluetoothSerialService extends Service {
    private static final String TAG = BluetoothSerialService.class.getSimpleName();
    public static final String KEY_MAC_ADDRESS = "KEY_MAC_ADDRESS";

    static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter mAdapter;

    private Handler mHandlerActivity;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


        public ConnectedThread(BluetoothSocket socket, String socketType){
            Log.d(TAG, " Created connected Thread!");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException | NullPointerException e){
                Log.e(TAG, "No se pudo obtener input stream o outputstream");
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
//            mState = STATE_CONNECTED;
        }

        public void run(){
            Log.i(TAG, "Podría usarse para recibir datos, pero no se necesita por ahora");
        }

        public void write(byte[] buffer){
            try{
                mmOutStream.write(buffer);
            } catch (IOException | NullPointerException e){
                Log.e(TAG, "Exception while writing" ,  e);
            }
        }

        public void cancel(){
            try {
                mmSocket.close();

            } catch (IOException | NullPointerException e){

                Log.e(TAG, "Exception while closing socket" ,  e);
            }
        }

//        public synchronized void stop(){
//            if (mConnectThread != null){
//                mConnectThread.cancel();
//                mConnectThread = null;
//            }
//
//            if (mConnectedThread != null){
//                mConnectedThread.cancel();
//                mConnectedThread = null;
//            }
//
//        }

        private void connectionFailed(){
            this.start();
            Log.e(TAG, "Connection failed");
        }

        private void connectionLost(){
            this.start();
            Log.e(TAG, "Connection lost");
        }


        private void SerialWriteBytes(byte[] b){
            ConnectedThread r;

            synchronized (this){
                r = mConnectedThread;
            }

            r.write(b);
        }

        public void serialWriteString(String s){
            byte buffer[] = s.getBytes();
            this.SerialWriteBytes(buffer);
//            Log.d(TAG, "Send data", s);

        }

    }



    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure){
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            try{
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(BluetoothSerialService.MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(BluetoothSerialService.MY_UUID_INSECURE);
                }
            } catch (IOException | NullPointerException e) {
                Log.e(TAG, "Constructor failed");
            }

            mmSocket = tmp;
//            mState = STATE_CONNECTING;

        }


        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
           mAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            synchronized (this){
                mConnectThread = null;
            }

            connected(mmSocket, mmDevice, mSocketType);
        }

        private synchronized void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice, String mSocketType) {
            if (mConnectThread != null){
                mConnectThread.cancel();
                mConnectThread = null;
            }

            if (mConnectedThread != null){
                mConnectedThread.cancel();
                mConnectedThread = null;
            }


            mConnectedThread = new ConnectedThread(mmSocket, mSocketType);
            mConnectedThread.start();

            if(mHandlerActivity != null) {
                Message msg = mHandlerActivity.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DEVICE_NAME, mmDevice.getName());
                msg.setData(bundle);
                mHandlerActivity.sendMessage(msg);
            }

        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }

    }
}

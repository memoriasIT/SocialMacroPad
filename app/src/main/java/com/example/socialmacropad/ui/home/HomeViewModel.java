package com.example.socialmacropad.ui.home;

import android.bluetooth.BluetoothAdapter;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialmacropad.R;

public class HomeViewModel extends ViewModel {

    BluetoothAdapter bluetoothAdapter = null;
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");



    }




    public LiveData<String> getText() {
        return mText;
    }
}
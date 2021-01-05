package com.example.socialmacropad.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialmacropad.DeviceListActivity;
import com.example.socialmacropad.R;
import com.example.socialmacropad.event.UIToastEvent;
import com.example.socialmacropad.util.Config;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

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

                Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                startActivity(intent);
            }
        });


        Button btnAction1 = getView().findViewById(R.id.btnAction1);
        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UIToastEvent("Ejemplo", true, true));
            }
        });
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
package com.example.robotto_opcua;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddConnectionDialog extends AppCompatDialogFragment {
    private EditText editTextServerName;
    private EditText editTextServerURI;
    private AddConnectionDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_connect_dialog, null);

        builder.setView(view)
                .setTitle("Add New Connection")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ServerName = editTextServerName.getText().toString();
                        String ServerURI = editTextServerURI.getText().toString();
                        listener.getData(ServerName, ServerURI);
                    }
                });

        editTextServerName = view.findViewById(R.id.server_name);
        editTextServerURI = view.findViewById(R.id.server_uri);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddConnectionDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                        "must implement AddConnectDialogListener");
        }

    }

    public interface AddConnectionDialogListener{
        void getData(String ServerName, String ServerURI);
    }
}

package com.wilsonmanzano.electrocardiograma;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ${User} on 16/08/2017.
 */

public class IPDIALOG extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View inflator = layoutInflater.inflate(R.layout.ip_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText editIP = (EditText) inflator.findViewById(R.id.edit_ip);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflator)
                // Add action buttons
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ((RegisterActivity) getActivity()).IP = editIP.getText().toString();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IPDIALOG.this.getDialog().cancel();


                        IPDIALOG.this.getDialog().dismiss();
                        Log.i("AddOrderFragment", "cancel");
                    }
                });

        return builder.create();

    }
}

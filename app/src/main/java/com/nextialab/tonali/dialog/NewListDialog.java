package com.nextialab.tonali.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.nextialab.tonali.R;
import com.nextialab.tonali.model.ListType;

/**
 * Created by Nelson on 8/16/2016.
 */
public class NewListDialog extends DialogFragment {

    public interface Listener {

        void onAccept(String name);

    }

    private Listener mListener;
    private EditText mListName;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.new_list_layout, null);
        mListName = (EditText) view.findViewById(R.id.lists_input_new_list);
        builder.setView(view);
        builder.setPositiveButton(R.string.lists_create_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListName.length() > 0) {
                    if (mListener != null) mListener.onAccept(mListName.getText().toString());
                }
            }
        });
        builder.setNegativeButton(R.string.lists_cancel_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

}

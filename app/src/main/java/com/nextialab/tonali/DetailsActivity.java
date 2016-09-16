package com.nextialab.tonali;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nextialab.tonali.dialog.EditListDialog;
import com.nextialab.tonali.model.TonaliList;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getName();

    public static final String LIST = "list";
    public static final int REQUEST_CODE = 0;

    private TextView mListContent;

    private TonaliList mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mListContent = (TextView) findViewById(R.id.list_content);
        mList = getIntent().getParcelableExtra(LIST);
        ((TextView) findViewById(R.id.list_name)).setText(mList.getListName());
        updateContent();
    }

    public void updateContent() {
        String content = mList.getContent();
        if (content.length() > 0) {
            mListContent.setText(content);
            mListContent.setTextColor(getResources().getColor(R.color.tonali_black));
        } else {
            mListContent.setText(R.string.description_placeholder);
            mListContent.setTextColor(getResources().getColor(R.color.tonali_gray));
        }
    }

    public void onRename(View view) {
        EditListDialog dialog = new EditListDialog();
        dialog.setCurrentName(mList.getListName());
        dialog.setListener(new EditListDialog.Listener() {
            @Override
            public void onAccept(String name) {
                mList.setListName(name);
                if (mList.save()) {
                    ((TextView) findViewById(R.id.list_name)).setText(mList.getListName());
                } else {
                    Log.e(TAG, "Could not save new list name");
                }
            }
        });
        dialog.show(getSupportFragmentManager(), null);
    }

    public void onEditContent(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.CONTENT, mList.getContent());
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?");
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mList.delete();
                finish();
            }
        });
        builder.setPositiveButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra(EditActivity.CONTENT);
                mList.setContent(content);
                if (mList.save()) {
                    Log.i(TAG, "Content saved");
                } else {
                    Log.e(TAG, "Could not save list");
                }
                updateContent();
            }
        }
    }

}

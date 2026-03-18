package com.app.fastprint.ui.FilePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.app.fastprint.R;
import com.app.fastprint.utills.AppControler;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FilePickerDropBoxActivity extends DropboxActivity  {
  Boolean isBackForced=false;
    public static final int REQUEST_CODE_DropBox = 11;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_box);
        requestSignIn();
     //  findViewById(R.id.tvSignIn).setOnClickListener(view -> requestSignIn());
      //  findViewById(R.id.tvOpenDrive).setOnClickListener(view -> openFilePicker());
     //   findViewById(R.id.tvOpenDrive2).setOnClickListener(view -> openFilePicker2());
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        isBackForced=true;
        Log.e("--------onRestart","onRestart");
    }

    public void GoBack(View view) {
        finish();
    }
    private void openFilePicker2() {
        Intent openWithIntent = new Intent(FilePickerDropBoxActivity.this, OpenWithActivity.class);
        startActivity(openWithIntent);
    }

    private void openFilePicker() {
        startActivityForResult(FilesActivity.getIntent(FilePickerDropBoxActivity.this, ""),REQUEST_CODE_DropBox);
    }

    private void requestSignIn() {
        AppControler.getInstance().put(AppControler.Key.ISDROPBOXFile_SELECTED,"0");

        DropboxActivity.startOAuth2Authentication(FilePickerDropBoxActivity.this, getString(R.string.dropbox_app_key), Arrays.asList("account_info.read", "files.content.read"));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DropBox && resultCode == RESULT_OK) {
            File file = (File)data.getExtras().get("SelectedDropBoxFile");
            Intent dataIndent = new Intent();
            dataIndent.putExtra("SelectedDropBoxFile",file);

            setResult(RESULT_OK,dataIndent);
            finish();


        }

    }
    @Override
    protected void loadData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                if(isBackForced && AppControler.getInstance().getString(AppControler.Key.ISDROPBOX_LOADED, "") == "0"){
                    isBackForced=false;
                    finish();
                }else {

                    openFilePicker();
                }

              //  Log.d("r",result.getEmail());
             //   Log.d("rs",result.getName().getDisplayName());

            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }
}
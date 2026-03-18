package com.app.fastprint.ui.uploadFileForm.responseModel;
import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class FileModel {

    public String key;

    public String ext;

    public MultipartBody.Part fileData;
    public FileModel(String key, String ext, MultipartBody.Part fileData){
        this.key=key;
        this.ext=ext;
        this.fileData=fileData;

    }
}

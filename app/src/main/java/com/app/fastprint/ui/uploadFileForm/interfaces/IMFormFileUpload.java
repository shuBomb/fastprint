package com.app.fastprint.ui.uploadFileForm.interfaces;

import okhttp3.MultipartBody;

public
interface IMFormFileUpload {
    void uploadFileRestCalls(String name, String email, String url_link, String phone, String order_number, MultipartBody.Part upload_file1,MultipartBody.Part upload_file2,MultipartBody.Part upload_file3, MultipartBody.Part upload_signature);

}

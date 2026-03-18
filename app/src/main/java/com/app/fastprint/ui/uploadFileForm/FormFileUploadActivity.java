package com.app.fastprint.ui.uploadFileForm;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.FilePicker.FilePickerDropBoxActivity;
import com.app.fastprint.ui.FilePicker.GoogleDriveActivity;
import com.app.fastprint.ui.address.AddressActivity;
import com.app.fastprint.ui.login.LoginActivity;
import com.app.fastprint.ui.uploadFileForm.interfaces.IFormFileUpload;
import com.app.fastprint.ui.uploadFileForm.interfaces.IPFormFileUpload;
import com.app.fastprint.ui.uploadFileForm.presenter.PFormFileUpload;
import com.app.fastprint.ui.uploadFileForm.responseModel.FileModel;
import com.app.fastprint.ui.uploadFileForm.responseModel.UploadFileSubmitResponseModel;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.CommonMethods;
import com.app.fastprint.utills.DriveServiceHelper;
import com.app.fastprint.utills.FileUtils;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;
import com.app.fastprint.R;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class FormFileUploadActivity extends BaseClass implements IPickResult, IFormFileUpload {
    public static final int REQUEST_CODE_DropBox = 11;
    private static final String TAG = "Google Signin" ;
    public static final int PICK_IMAGE = 15;


    int FileUploader=0;
    String intent_From = "";
    Context context;

    private ImageView imgBack;
    private TextView tvTittle;
    private TextView tvSubmit;
    private EditText edtUplaodFile1;
    private EditText edtUplaodFile2;
    private EditText edtUplaodFile3;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;

    private RelativeLayout layoutUploadFile;
    private RelativeLayout layoutUploadFile2;
    private RelativeLayout layoutUploadFile3;
    private RelativeLayout layoutOver;
    private RelativeLayout layoutName;
    private RelativeLayout layoutEmail;
    private RelativeLayout layoutPhone;


    private ImageView imgView1;


    MultipartBody.Part signatureToUpload;
    public static MultipartBody.Part imageToUpload;
    private SparseIntArray mErrorString;
    private static final int REQUEST_PERMISSIONS = 20;
    private static final int REQUEST_CODE_PICK_FILE = 30;
    private static final int FILE_REQUEST_CODE = 40;
    IPFormFileUpload ipFormFileUpload;
    Dialog dialog;
    String auth_token = "";
    String address = "";

            //Google Drive Picker
    DriveServiceHelper mDriveServiceHelper;
    GoogleSignInClient mclientAccount;
    public static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    public static ArrayList<FileModel> fileModelArrayList = new ArrayList<FileModel>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_file_upload);
        context = FormFileUploadActivity.this;


        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        tvSubmit = findViewById(R.id.tvSubmit);
        edtUplaodFile1 = findViewById(R.id.edtUplaodFile1);
        edtUplaodFile2 = findViewById(R.id.edtUplaodFile2);
        edtUplaodFile3 = findViewById(R.id.edtUplaodFile3);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);

        layoutUploadFile = findViewById(R.id.layoutUploadFile);
        layoutUploadFile2 = findViewById(R.id.layoutUploadFile2);
        layoutUploadFile3 = findViewById(R.id.layoutUploadFile3);
        layoutOver = findViewById(R.id.layoutOver);
        layoutName = findViewById(R.id.layoutName);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPhone = findViewById(R.id.layoutPhone);
        imgView1 = findViewById(R.id.imgView1);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationOnUploadFile();
            }
        });

        edtUplaodFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUploader = 1;
                handleFileUpload();
            }
        });

        edtUplaodFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUploader = 2;
                handleFileUpload();
            }
        });

        edtUplaodFile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUploader = 3;
                handleFileUpload();
            }
        });

        auth_token = AppControler.getInstance(context).getString(AppControler.Key.AUTH_TOKEN);
        address = AppControler.getInstance(context).getString(AppControler.Key.ADDRESS);

        ipFormFileUpload = new PFormFileUpload(this);
        intent_From = getIntent().getStringExtra("intent_From");
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        FileModel f1 = new FileModel("","",null);
        FileModel f2 = new FileModel("","",null);
        FileModel f3 = new FileModel("","",null);
        fileModelArrayList.add(f1);
        fileModelArrayList.add(f2);
        fileModelArrayList.add(f3);

        if (intent_From.equalsIgnoreCase("extras")) {
            layoutUploadFile.setVisibility(View.VISIBLE);
            layoutUploadFile2.setVisibility(View.VISIBLE);
            layoutUploadFile3.setVisibility(View.VISIBLE);
            layoutOver.setVisibility(View.VISIBLE);
            layoutName.setVisibility(View.VISIBLE);
            layoutEmail.setVisibility(View.VISIBLE);
            layoutPhone.setVisibility(View.VISIBLE);


        } else {
            layoutUploadFile.setVisibility(View.VISIBLE);
            layoutUploadFile2.setVisibility(View.VISIBLE);
            layoutUploadFile3.setVisibility(View.VISIBLE);
            layoutOver.setVisibility(View.VISIBLE);
            layoutName.setVisibility(View.GONE);
            layoutEmail.setVisibility(View.GONE);
            layoutPhone.setVisibility(View.GONE);


        }

    }

    private void handleFileUpload() {
        if (runTimePermission()) {
            // Handle permission granted case if needed
        } else {
            openMediaDialog(); // upload file option
        }
    }




    private void validationOnUploadFile() {
     String fileField1="",fileField2="",fileField3="";

                fileField1 = edtUplaodFile1.getText().toString();

                fileField2 = edtUplaodFile3.getText().toString();

                fileField3 = edtUplaodFile3.getText().toString();


        if (intent_From.equalsIgnoreCase("extras")) {

            if (fileField1.trim().isEmpty() && fileField2.trim().isEmpty() && fileField3.trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            } else if (edtName.getText().toString().trim().isEmpty()) {
                edtName.setError("Enter your name");
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                edtEmail.setError("Enter your email");
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                edtEmail.setError("Enter valid email");
            } else if (edtPhone.getText().toString().trim().isEmpty()) {
                edtPhone.setError("Enter phone number");
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                edtPhone.setError("Enter phone number");
            } else {
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    FileModel fileObj= fileModelArrayList.get(0);
                    MultipartBody.Part file1= fileObj.fileData;
                    FileModel fileOb2= fileModelArrayList.get(1);
                    MultipartBody.Part file2= fileOb2.fileData;
                    FileModel fileOb3= fileModelArrayList.get(2);
                    MultipartBody.Part file3= fileOb3.fileData;

                    ipFormFileUpload.uploadFile(edtName.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            "",
                            edtPhone.getText().toString().trim(),
                            "",
                            file1,file2,file3,
                            signatureToUpload);
                }
                //file name kaha hai?

            }

        } else {


            if (fileField1.trim().isEmpty() && fileField2.trim().isEmpty() && fileField3.trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
                return;
            }
/*
            else {
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipFormFileUpload.uploadFile(edtName.getText().toString().trim(),
                            "",
                            "",
                            "",
                            "",
                            imageToUpload,
                            signatureToUpload);
                }
            }
*/

            Intent intent = new Intent(FormFileUploadActivity.this, AddressActivity.class);
            intent.putExtra("intent_type", "from_file_upload");
            startActivity(intent);

        }


    }

    private boolean runTimePermission() {

        mErrorString = new SparseIntArray();
        int currentapiVersion = Build.VERSION.SDK_INT;
        // if current version is M or greater than M
        if(currentapiVersion >= Build.VERSION_CODES.TIRAMISU){
            String[] array = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            };
            requestAppPermissions(array, R.string.permission, REQUEST_PERMISSIONS);
        }else if (currentapiVersion >= Build.VERSION_CODES.M) {
            String[] array = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
            };

            requestAppPermissions(array, R.string.permission, REQUEST_PERMISSIONS);
        } else {
            onPermissionsGranted(REQUEST_PERMISSIONS);
        }

        return false;
    }

    // check requested permissions are on or off
    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(FormFileUploadActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
        }
    }


    // if permissions granted succesfully
    private void onPermissionsGranted(int requestcode) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    // upload file option
    private void openMediaDialog() {
        Dialog mediaDialog = new Dialog(FormFileUploadActivity.this);
        mediaDialog.setContentView(R.layout.dialog_media);
        mediaDialog.setCancelable(true);

        TextView tvChoose = (TextView) mediaDialog.findViewById(R.id.tvChoose);
        TextView tvMedia = (TextView) mediaDialog.findViewById(R.id.tvMedia);
        TextView tvUploadFile = (TextView) mediaDialog.findViewById(R.id.tvUploadFile);
        TextView tvCancel = (TextView) mediaDialog.findViewById(R.id.tvCancel);
        TextView tvUploadFileDropBox = (TextView) mediaDialog.findViewById(R.id.tvUploadFileDropBox);
        TextView tvUploadFileDrive = (TextView) mediaDialog.findViewById(R.id.tvUploadFileDrive);

        tvChoose.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvCancel.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));

        tvUploadFileDropBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormFileUploadActivity.this, FilePickerDropBoxActivity.class);
                startActivityForResult(intent,REQUEST_CODE_DropBox);
               // startActivityForResult(intent, Constant.REQUEST_CODE_PICK_FILE);

                mediaDialog.dismiss();

            }
        });
        tvUploadFileDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mclientAccount !=null){
                    mclientAccount.signOut();
                }
                requestGoogleSignIn();
//                Intent intent = new Intent(FormFileUploadActivity.this, GoogleDriveActivity.class);
//                startActivity(intent);
//
//                 startActivityForResult(intent, Constant.REQUEST_CODE_PICK_FILE);

                mediaDialog.dismiss();

            }
        });

        tvMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(gallery, PICK_IMAGE);
                mediaDialog.dismiss();
                //PickImageDialog.build(new PickSetup().setButtonOrientation(LinearLayout.HORIZONTAL)).show(FormFileUploadActivity.this);
               // mediaDialog.dismiss();
            }
        });

        tvUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(FormFileUploadActivity.this, NormalFilePickActivity.class);
                intent.putExtra(Constant.MAX_NUMBER, 1);
                intent.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"doc", "docx", "pdf"});
                startActivityForResult(intent, Constant.REQUEST_CODE_PICK_FILE);*/

                mediaDialog.dismiss();

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
            }
        });
        mediaDialog.show();
    }

    @Override
    public void onPickResult(PickResult result) {
        if (result.getError() == null) {
            String[] parts = result.getPath().split("/");
            final String fileName = parts[parts.length - 1];
            String fileParts[] =fileName.split("\\.");
            switch (FileUploader){
                case 1:
                    edtUplaodFile1.setText(fileName);
                    break;
                case 2:
                    edtUplaodFile2.setText(fileName);
                    break;
                case 3:
                    edtUplaodFile3.setText(fileName);
                    break;
            }
            String path = result.getPath();
            String key=String.valueOf(FileUploader);
            MultipartBody.Part mfiledata = sendOtherFileToserver(path,key);
         //   Bitmap bitmap = result.getBitmap();
            String keyPass="file"+key;
            if (intent_From.equalsIgnoreCase("extras")) {
                keyPass="upload_file"+key;
            }

            FileModel fileObj=new FileModel(keyPass,fileParts[fileParts.length-1], mfiledata);
            this.fileModelArrayList.set(FileUploader-1,fileObj);


          //  sendImageFileToserver(bitmap);
        }
    }

    private MultipartBody.Part fileDataReturn(String path,String key){

       String currentTime = String.valueOf(Calendar.getInstance().getTimeInMillis());

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(PDFInterface.IMAGEURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();

        //Create a file object using file path
        File file = new File(path);
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
       String fname=file.getName();
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData(key, fname, requestBody);
     //   RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), fname);
        Log.d("fileToUploaded",fileToUpload.toString());
       return fileToUpload;

    }

//    private MultipartBody.Part sendImageFileToserver(Bitmap bitmap) {
//        File filesDir = context.getFilesDir();
//        File file = new File(filesDir, "image" + System.currentTimeMillis() + ".png");
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
//        byte[] bitmapdata = bos.toByteArray();
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            fos.write(bitmapdata);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody reqFile = null;
//
//        try {
//            reqFile = RequestBody.create(
//                    MediaType.parse(Files.probeContentType(file.toPath())), file);
//        } catch (IOException e) {
//            e.printStackTrace();
//            reqFile = RequestBody.create(MediaType.parse("*//*"), file);
//        }
//        StringBuilder builder = new StringBuilder();
//        String path = file.getPath();
//        builder.append(path + "\n");
//        String[] parts = file.getPath().split("/");
//        String fileName = parts[parts.length - 1];
//
//        //Extras    upload_file & upload_signature
//        if (layoutSignature.getVisibility() == View.VISIBLE)
//            imageToUpload = MultipartBody.Part.createFormData("upload_file", fileName, reqFile);
//        else
//            imageToUpload = MultipartBody.Part.createFormData("file1", fileName, reqFile);
//        Log.d("multipart++", "multipart++" + imageToUpload);
//        return imageToUpload;
//    }
    private byte[] readFile(String path){
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }


    private MultipartBody.Part sendOtherFileToserver(String pathO,String key) {
        String[] parts2 = pathO.split("/");

        final String fileName1 = parts2[parts2.length - 1];
        String fileParts[] =fileName1.split("\\.");

        //File filesDir = context.getFilesDir();
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String ext=fileParts[fileParts.length-1];
        File file = new File(filesDir, "file" + System.currentTimeMillis() + "."+ext);
        //file.createNewFile();
        //  ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] bitmapdata = readFile(pathO);
        //Create file..



        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = null;

        // create RequestBody instance from file

        ext=ext.toLowerCase();
        if(ext.equals("png") || ext.equals("jpeg")|| ext.equals("jpg")){

            reqFile = RequestBody.create(file,MediaType.parse("image/*"));
        }
        else if(ext.equals("pdf")){
            reqFile = RequestBody.create(file,MediaType.parse("application/pdf"));
        }
        else if(ext.equals("ai")){
            reqFile = RequestBody.create(file,MediaType.parse("application/postscript"));
        }
        else if(ext == "psd"){
            reqFile = RequestBody.create(file,MediaType.parse("application/x-photoshop"));
        }
        //  reqFile = RequestBody.create(file,MediaType.parse("multipart/form-data");
//        try {
//            reqFile = RequestBody.create(
//                    MediaType.parse(Files.probeContentType(file.toPath())), file);
//        } catch (IOException e) {
//            e.printStackTrace();
//            reqFile = RequestBody.create(MediaType.parse("*//*"), file);
//        }
        StringBuilder builder = new StringBuilder();
        String path = file.getPath();
        builder.append(path + "\n");
        String[] parts = file.getPath().split("/");
        String fileName = parts[parts.length - 1];

        imageToUpload = MultipartBody.Part.createFormData("file"+ key, fileName, reqFile);
        Log.d("multipart++", "multipart++" + imageToUpload);
        return imageToUpload;
    }


    private MultipartBody.Part sendOtherFileToserver__(String pathO,String key) {
        String[] parts2 = pathO.split("/");

        final String fileName1 = parts2[parts2.length - 1];
        String fileParts[] =fileName1.split("\\.");

        //File filesDir = context.getFilesDir();
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String ext=fileParts[fileParts.length-1];
        File file = new File(filesDir, "file" + System.currentTimeMillis() + "."+ext);
        //file.createNewFile();
      //  ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] bitmapdata = readFile(pathO);
        //Create file..



        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = null;

        // create RequestBody instance from file

        ext=ext.toLowerCase();
    if(ext.equals("png") || ext.equals("jpeg")|| ext.equals("jpg")){

        reqFile = RequestBody.create(file,MediaType.parse("image/*"));
    }
    else if(ext.equals("pdf")){
        reqFile = RequestBody.create(file,MediaType.parse("application/pdf"));
    }
    else if(ext.equals("ai")){
        reqFile = RequestBody.create(file,MediaType.parse("application/postscript"));
    }
    else if(ext == "psd"){
        reqFile = RequestBody.create(file,MediaType.parse("application/x-photoshop"));
    }
      //  reqFile = RequestBody.create(file,MediaType.parse("multipart/form-data");
//        try {
//            reqFile = RequestBody.create(
//                    MediaType.parse(Files.probeContentType(file.toPath())), file);
//        } catch (IOException e) {
//            e.printStackTrace();
//            reqFile = RequestBody.create(MediaType.parse("*//*"), file);
//        }
        StringBuilder builder = new StringBuilder();
        String path = file.getPath();
        builder.append(path + "\n");
        String[] parts = file.getPath().split("/");
        String fileName = parts[parts.length - 1];

        //Extras    upload_file & upload_signature
        return imageToUpload;
    }


    private MultipartBody.Part getfilefromContent(String content,String fname,String key) {

        byte[] bytesArr  = content.getBytes();




        String fileParts[] =fname.split("\\.");

        File filesDir = context.getFilesDir();
        String ext=fileParts[fileParts.length-1];
        File file = new File(filesDir, "file" + System.currentTimeMillis() + "."+ext);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bytesArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = null;
        ext=ext.toLowerCase();
        if(ext.equals("png") || ext.equals("jpeg")|| ext.equals("jpg")){

            reqFile = RequestBody.create(file,MediaType.parse("image/*"));
        }
        else if(ext.equals("pdf")){
            reqFile = RequestBody.create(file,MediaType.parse("application/pdf"));
        }
        else if(ext.equals("ai")){
            reqFile = RequestBody.create(file,MediaType.parse("application/postscript"));
        }
        else if(ext == "psd"){
            reqFile = RequestBody.create(file,MediaType.parse("application/x-photoshop"));
        }

      //  reqFile = RequestBody.create(MediaType.parse("*//*"), file);
//        try {
//            reqFile = RequestBody.create(
//                    MediaType.parse(Files.probeContentType(file.toPath())), file);
//        } catch (IOException e) {
//            e.printStackTrace();
//            reqFile = RequestBody.create(MediaType.parse("*//*"), file);
//        }
        StringBuilder builder = new StringBuilder();
        String path = file.getPath();
        builder.append(path + "\n");
        String[] parts = file.getPath().split("/");
        String fileName = parts[parts.length - 1];

        //Extras    upload_file & upload_signature
        Log.d("multipart++", "multipart++" + imageToUpload);
        return imageToUpload;
    }
    private MultipartBody.Part sendSignatureToserver(Bitmap bitmap) {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, "image" + System.currentTimeMillis() + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = null;
        StringBuilder builder = new StringBuilder();
        String fileName = null;
        String path = file.getPath();
        builder.append(path + "\n");
        String[] parts = file.getPath().split("/");
        fileName = parts[parts.length - 1];

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                reqFile = RequestBody.create(
                        MediaType.parse(Files.probeContentType(file.toPath())), file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        }


        signatureToUpload = MultipartBody.Part.createFormData("upload_signature", fileName, reqFile);
        Log.d("multipart++", "multipart++" + signatureToUpload);
        return signatureToUpload;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            Uri uri = data.getData();

            String yourRealPath="";
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            if(cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                yourRealPath = cursor.getString(columnIndex);


            } else {
                //boooo, cursor doesn't have rows ...
            }
            cursor.close();



            String[] parts = yourRealPath.split("/");
            String fileName = parts[parts.length - 1];
            String extension = yourRealPath.substring(yourRealPath.lastIndexOf(".") + 1);
            String key=String.valueOf(FileUploader);
            switch (FileUploader){
                case 1:
                    edtUplaodFile1.setText(fileName);
                    break;
                case 2:
                    edtUplaodFile2.setText(fileName);
                    break;
                case 3:
                    edtUplaodFile3.setText(fileName);
                    break;
            }


            MultipartBody.Part mfiledata = sendOtherFileToserver(yourRealPath,key);
            //   Bitmap bitmap = result.getBitmap();
            String keyPass="file"+key;
            if (intent_From.equalsIgnoreCase("extras")) {
                keyPass="upload_file"+key;
            }

            FileModel fileObj=new FileModel(keyPass,extension, mfiledata);
            this.fileModelArrayList.set(FileUploader-1,fileObj);




        }

        /*else if (requestCode == Constant.REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {

            ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
            StringBuilder builder = new StringBuilder();
            String fileName = null;
            for (NormalFile file : list) {
                String path = file.getPath();
                builder.append(path + "\n");
                String[] parts = file.getPath().split("/");
                fileName = parts[parts.length - 1];
                String extension = path.substring(path.lastIndexOf(".") + 1);
                String key=String.valueOf(FileUploader);
                MultipartBody.Part mfiledata = sendOtherFileToserver(path,key);
               // MultipartBody.Part mfiledata = fileDataReturn(path,key);
                // Bitmap bitmap = result.getBitmap();
                String keyPass="file"+key;
                if (intent_From.equalsIgnoreCase("extras")) {
                    keyPass="upload_file"+key;
                }
                FileModel fileObj=new FileModel(keyPass,extension, mfiledata);
                this.fileModelArrayList.set(FileUploader-1,fileObj);

            }
            switch (FileUploader){
                case 1:
                    edtUplaodFile1.setText(fileName);
                    break;
                case 2:
                    edtUplaodFile2.setText(fileName);
                    break;
                case 3:
                    edtUplaodFile3.setText(fileName);
                    break;
            }


        }*/
        else if(requestCode == REQUEST_CODE_SIGN_IN){
          //  if (resultCode == Activity.RESULT_OK && data != null) {
                //Log.e(TAG, "Sign-in failed.");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, handle the account.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult();
                Log.w("TAGGoogleSignInName", "Google sign in failed: " + account.getAccount().name.toString());
                //handleSignInResult(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately.
                Log.w("TAGGoogleSignIn", "Google sign in failed: " + e.getStatusCode());
            }

          //  }

        }
        else if(requestCode == REQUEST_CODE_OPEN_DOCUMENT){
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    openFileFromFilePicker(uri);
                }
            }

        }
        else if(requestCode == REQUEST_CODE_DropBox){
            if (resultCode == Activity.RESULT_OK && data != null) {
                String key=String.valueOf(FileUploader);
                File mProduct1File = (File)data.getExtras().get("SelectedDropBoxFile");

                String fileParts[] =mProduct1File.getName().split("\\.");
                String ext=fileParts[fileParts.length-1];

                RequestBody reqFile = null;
                ext=ext.toLowerCase();
                if(ext.equals("png") || ext.equals("jpeg")|| ext.equals("jpg")){
                    reqFile = RequestBody.create(mProduct1File,MediaType.parse("image/*"));
                }
                else if(ext.equals("pdf")){
                    reqFile = RequestBody.create(mProduct1File,MediaType.parse("application/pdf"));
                }
                else if(ext.equals("ai")){
                    reqFile = RequestBody.create(mProduct1File,MediaType.parse("application/postscript"));
                }
                else if(ext == "psd"){
                    reqFile = RequestBody.create(mProduct1File,MediaType.parse("application/x-photoshop"));
                }

                MultipartBody.Part mfiledata = MultipartBody.Part.createFormData("file"+key, mProduct1File.getName(), reqFile);


                String filename = mProduct1File.getName();
                String extension []= filename.split("\\.");
                 // Bitmap bitmap = result.getBitmap();
                String keyPass="file"+key;
                if (intent_From.equalsIgnoreCase("extras")) {
                    keyPass="upload_file"+key;
                }
                FileModel fileObj=new FileModel(keyPass,extension[extension.length-1], mfiledata);
                this.fileModelArrayList.set(FileUploader-1,fileObj);

              /*  RequestBody requestFiles = null;
                try {
                    requestFiles = RequestBody.create(
                            MediaType.parse(Files.probeContentType(file.toPath())), file);
                } catch (IOException e) {
                    e.printStackTrace();
                    requestFiles = RequestBody.create(MediaType.parse("*//*"), file);
                }

                //requestFiles = RequestBody.create(MediaType.parse("image/*"), files);

                if (layoutSignature.getVisibility() == View.VISIBLE)
                    imageToUpload = MultipartBody.Part.createFormData("upload_file", filename, requestFiles);
                else
                    imageToUpload = MultipartBody.Part.createFormData("file", filename, requestFiles);

                Log.d("file++", "file++" + imageToUpload);
                */
                switch (FileUploader){
                    case 1:
                        edtUplaodFile1.setText(filename);
                        break;
                    case 2:
                        edtUplaodFile2.setText(filename);
                        break;
                    case 3:
                        edtUplaodFile3.setText(filename);
                        break;
                }
            }

        }

    }
    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * }.
     */
    private void handleSignInResult() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null){

        }
        else
        {

            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(
                            this, Collections.singleton(DriveScopes.DRIVE_READONLY));
            credential.setSelectedAccount(account.getAccount());
            com.google.api.services.drive.Drive googleDriveService =
                    new com.google.api.services.drive.Drive.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            new GsonFactory(),
                            credential)
                            .setApplicationName("FastPrint")
                            .build();
            mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
            openFilePicker();
        }

    }

    /**
     * Opens the Storage Access Framework file picker using {@link #REQUEST_CODE_OPEN_DOCUMENT}.
     */
    private void openFilePicker() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening file picker.");

            Intent pickerIntent = mDriveServiceHelper.createFilePickerIntent();

            // The result of the SAF Intent is handled in onActivityResult.
            startActivityForResult(pickerIntent, REQUEST_CODE_OPEN_DOCUMENT);
        }
    }

    /**
     * Opens a file from its {@code uri} returned from the Storage Access Framework file picker
     * initiated by {@link #openFilePicker()}.
     */
    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening " + uri.getPath());

            Log.d("OpeningOpening", "Opening " + uri.getPath());
            imgView1.setImageURI(uri);
            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                        switch (FileUploader){
                            case 1:
                                edtUplaodFile1.setText(name);
                                break;
                            case 2:
                                edtUplaodFile2.setText(name);
                                break;
                            case 3:
                                edtUplaodFile3.setText(name);
                                break;
                        }

                        String key=String.valueOf(FileUploader);

                        String fileParts[] =name.split("\\.");
                        String ext=fileParts[fileParts.length-1];


                        File productImage1 = new File(getRealPathFromURI(uri, context));
                        RequestBody reqFile = null;
                        ext=ext.toLowerCase();
                        if(ext.equals("png") || ext.equals("jpeg")|| ext.equals("jpg")){

                            reqFile = RequestBody.create(productImage1,MediaType.parse("image/*"));
                        }
                        else if(ext.equals("pdf")){
                            reqFile = RequestBody.create(productImage1,MediaType.parse("application/pdf"));
                        }
                        else if(ext.equals("ai")){
                            reqFile = RequestBody.create(productImage1,MediaType.parse("application/postscript"));
                        }
                        else if(ext == "psd"){
                            reqFile = RequestBody.create(productImage1,MediaType.parse("application/x-photoshop"));
                        }

                        MultipartBody.Part mfiledata = MultipartBody.Part.createFormData("file"+key, productImage1.getName(), reqFile);

                        //MultipartBody.Part mfiledata = getfilefromContent(content,name,key);

                        String keyPass="file"+key;
                        Log.d("FileModel",keyPass.toString());
                        if (intent_From.equalsIgnoreCase("extras")) {
                            keyPass="upload_file"+key;
                            //Log.d("FileModel",keyPass.toString());
                        }
                        FileModel fileObj=new FileModel(keyPass,fileParts[fileParts.length-1], mfiledata);
                        Log.d("FileModel",fileObj.toString());
                        this.fileModelArrayList.set(FileUploader-1,fileObj);
                       // Log.d("FileModel",fileModelArrayList.toString());

                        //    mFileTitleEditText.setText(name);
                        //  mDocContentEditText.setText(content);

                        // Files opened through SAF cannot be modified.
                        //setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to open file from picker.", exception));
        }
    }

    private static String getRealPathFromURI(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }


    @Override
    public void uploadFileSuccessResponseFromPresenter(UploadFileSubmitResponseModel uploadFileSubmitResponseModel) {
        dialog.dismiss();
        String message = uploadFileSubmitResponseModel.getData().getUpload();
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

        if (intent_From.equalsIgnoreCase("extras")) {
            finish();
        } else {
            if (auth_token != null && !auth_token.isEmpty()) {
//                if (address!=null && !address.equals("")){
//                    Intent intent = new Intent(FormFileUploadActivity.this, PaymentActivity.class);
//                    startActivity(intent);
//                }else {
                Intent intent = new Intent(FormFileUploadActivity.this, AddressActivity.class);
                intent.putExtra("intent_type", "from_file_upload");
                startActivity(intent);
                //}

            } else {
                Intent intent = new Intent(FormFileUploadActivity.this, LoginActivity.class);
                intent.putExtra("intent_type", "from_file_upload");
                startActivity(intent);
            }
        }


    }

    @Override
    public void uploadFileErrorResponseFromPresenter(String message) {
        dialog.dismiss();
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
        private void requestGoogleSignIn() {
        Log.d("TAGFASTPRINT", "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_READONLY))
                        .build();
         mclientAccount = GoogleSignIn.getClient(this, signInOptions);

                // User is not signed in, start the Google Sign In activity.

                startActivityForResult(mclientAccount.getSignInIntent(), REQUEST_CODE_SIGN_IN);
                Log.d("DriveResourceClient",mclientAccount.getSignInIntent().toString());



            // The result of the sign-in Intent is handled in onActivityResult.
        //startActivityForResult(mclientAccount.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

}
package com.app.fastprint.ui.category.commericalPrinting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.category.commericalPrinting.adapters.NumberOfColorAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.OrientationAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.OtherAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.PaperGramAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.PaperTypeAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.SelectAJobAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.SelectColorAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.SizeAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.SlidesAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.SpecialAdOneAdapter;
import com.app.fastprint.ui.category.commericalPrinting.adapters.StandardSizeAdapter;
import com.app.fastprint.ui.category.commericalPrinting.interfaces.ICommercialPrinting;
import com.app.fastprint.ui.category.commericalPrinting.interfaces.IPCommercialPrinting;
import com.app.fastprint.ui.category.commericalPrinting.presenters.PCommericalPrinting;
import com.app.fastprint.ui.category.commericalPrinting.responseModel.CommercialFromSubmitResponseModel;
import com.app.fastprint.ui.category.commericalPrinting.responseModel.CommericalPrintingResponseModel;
import com.app.fastprint.ui.category.commericalPrinting.viewModel.Model;
import com.app.fastprint.ui.chat.chatActivity;
import com.app.fastprint.utills.AppConstants;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.CommonMethods;
import com.app.fastprint.utills.GpsUtils;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommercialPrintingActivity extends BaseClass implements IPickResult, ICommercialPrinting {

    private ImageView imgBack;
    private ImageView imgClear;
    private TextView tvTittle;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private EditText edtCompany;
    private EditText edtPhone;
    private EditText edtQuantity;
    private EditText edtPagePerSet;
    private EditText edtNumberofSet;
    private EditText edtEnterSize;
    private EditText edtFinishedSize;
    private EditText edtOpenSize;
    private LinearLayout llPapaerPerSet;
    private LinearLayout llNumberofSet;
    private LinearLayout llNumberofSheet;
    private LinearLayout llSize;
    private LinearLayout llEnterSize;
    private LinearLayout llFinishedSize;
    private LinearLayout llOpenSize;
    private LinearLayout llSlide;
    private LinearLayout llJob;

    public static final int PICK_IMAGE = 15;

    public static EditText edtSelectJob;
    public static EditText edtColorCode;
    public static EditText edtNumberOfColor;
    public static EditText edtOrientation;
    public static EditText edtSize;
    public static EditText edtSlide;
    public static EditText edtSpecialADOns;
    public static EditText edtPaperType;
    public static EditText edtPaperGram;
    public static EditText edtOther;
    public static EditText edtJob;
    public static EditText edtNumberofSheet;
    public static View viewOther;
    public static View viewedtStandredSize;
    public static TextView tvOk;

    private EditText edtUplaodFile;
    private EditText edtLocation;
    private EditText edtComment;
    private SignaturePad signaturePad;
    private TextView tvSubmit;
    private LinearLayout layputCommericalPrinting;
    private TextView tvSignatureHere;


    Context context;
    Dialog dialog;
    public static Dialog seletJodialog;
    public static Dialog seletColordialog;
    public static Dialog numberofColordialog;
    public static Dialog orientationDialog;
    public static Dialog sizeDialog;
    public static Dialog standardDialog;
    public static Dialog slidesDialog;
    public static Dialog specialAdOneDialog;
    public static Dialog paperTypeDialog;
    public static Dialog paperGrmDialog;
    public static Dialog otherDialog;

    List<String> selectjobList;
    List<String> selectColorList;
    List<String> numberofColorList;
    List<String> orientationList;
    List<String> sizeList;
    List<String> jobsList;
    List<String> slideList;
    List<String> specialAdOneList;
    List<String> paperTypeList;
    List<String> paperGramList;
    List<String> numberofSheetList;
    ArrayList<Model> categoryModelArrayList;
    IPCommercialPrinting ipCommercialPrinting;
    private SparseIntArray mErrorString;
    private static final int REQUEST_PERMISSIONS = 20;
    String image_path = "";
    String commercial_category_id = "";
    String commercial_category_name = "";
    String printing_name = "";
    String current_Address = "";

    MultipartBody.Part signatureToUpload;
    MultipartBody.Part imageToUpload;
    private FusedLocationProviderClient mFusedLocationClient;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    String currentLatitude = "";
    String currentLongitude = "";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isGPS = false;
    public static int REQUEST_CODE_PICK_FILE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial_printing);

        imgBack = findViewById(R.id.imgBack);
        imgClear = findViewById(R.id.imgClear);
        tvTittle = findViewById(R.id.tvTittle);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtCompany = findViewById(R.id.edtCompany);
        edtPhone = findViewById(R.id.edtPhone);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtPagePerSet = findViewById(R.id.edtPagePerSet);
        edtNumberofSet = findViewById(R.id.edtNumberofSet);
        edtEnterSize = findViewById(R.id.edtEnterSize);
        edtFinishedSize = findViewById(R.id.edtFinishedSize);
        edtOpenSize = findViewById(R.id.edtOpenSize);
        llPapaerPerSet = findViewById(R.id.llPapaerPerSet);
        llNumberofSet = findViewById(R.id.llNumberofSet);
        llNumberofSheet = findViewById(R.id.llNumberofSheet);
        llSize = findViewById(R.id.llSize);
        llEnterSize = findViewById(R.id.llEnterSize);
        llFinishedSize = findViewById(R.id.llFinishedSize);
        llOpenSize = findViewById(R.id.llOpenSize);
        llSlide = findViewById(R.id.llSlide);
        llJob = findViewById(R.id.llJob);

        edtUplaodFile = findViewById(R.id.edtUplaodFile);
        edtLocation = findViewById(R.id.edtLocation);
        edtComment = findViewById(R.id.edtComment);
        signaturePad = findViewById(R.id.signature_pad);
        tvSubmit = findViewById(R.id.tvSubmit);
        layputCommericalPrinting = findViewById(R.id.layputCommericalPrinting);
        tvSignatureHere = findViewById(R.id.tvSignatureHere);


        commercial_category_id = getIntent().getStringExtra("commercial_category_id");
        commercial_category_name = getIntent().getStringExtra("commercial_category_name");
        printing_name = getIntent().getStringExtra("printing_name");
        ipCommercialPrinting = new PCommericalPrinting(this);
        context = CommercialPrintingActivity.this;
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            //ipCommercialPrinting.getCommercialPrinting(commercial_category_id);
            ipCommercialPrinting.getCommercialPrinting(commercial_category_id);
        }
        layputCommericalPrinting.setVisibility(View.GONE);
        initSignatuer();
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvTittle.setText(commercial_category_name.toLowerCase().substring(0, 1).toUpperCase() + commercial_category_name.substring(1).toLowerCase());
        edtSelectJob = (EditText) findViewById(R.id.edtSelectJob);
        edtColorCode = (EditText) findViewById(R.id.edtColorCode);
        edtNumberOfColor = (EditText) findViewById(R.id.edtNumberOfColor);
        edtOrientation = (EditText) findViewById(R.id.edtOrientation);
        edtSize = (EditText) findViewById(R.id.edtSize);
        edtSlide = (EditText) findViewById(R.id.edtSlide);
        edtSpecialADOns = (EditText) findViewById(R.id.edtSpecialADOns);
        edtPaperType = (EditText) findViewById(R.id.edtPaperType);
        edtPaperGram = (EditText) findViewById(R.id.edtPaperGram);
        edtJob = (EditText) findViewById(R.id.edtJob);
        edtNumberofSheet = (EditText) findViewById(R.id.edtNumberofSheet);


        if (commercial_category_id.equals("1")) {

            llSlide.setVisibility(View.VISIBLE);
            llEnterSize.setVisibility(View.VISIBLE);

        } else if (commercial_category_id.equals("2")) {

            llSlide.setVisibility(View.VISIBLE);
            llSize.setVisibility(View.VISIBLE);
            llFinishedSize.setVisibility(View.VISIBLE);
            llOpenSize.setVisibility(View.VISIBLE);

        } else if (commercial_category_id.equals("3")) {

            llSize.setVisibility(View.VISIBLE);

        } else if (commercial_category_id.equals("4")) {

            llSlide.setVisibility(View.VISIBLE);
            llSize.setVisibility(View.VISIBLE);
            llFinishedSize.setVisibility(View.VISIBLE);
            llOpenSize.setVisibility(View.VISIBLE);
            llNumberofSheet.setVisibility(View.VISIBLE);

        } else if (commercial_category_id.equals("5")) {

            llEnterSize.setVisibility(View.VISIBLE);
            llJob.setVisibility(View.VISIBLE);

        } else if (commercial_category_id.equals("6")) {

            llSlide.setVisibility(View.VISIBLE);
            llPapaerPerSet.setVisibility(View.VISIBLE);
            llNumberofSet.setVisibility(View.VISIBLE);
            llSize.setVisibility(View.VISIBLE);

        } else if (commercial_category_id.equals("7")) {

            llSlide.setVisibility(View.VISIBLE);
            llFinishedSize.setVisibility(View.VISIBLE);
            llOpenSize.setVisibility(View.VISIBLE);

        } else {

        }


        getCurrentLocation();
        if (!isGPS) {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        getLocations();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtSelectJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectJobDialog();
            }
        });

        edtJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobsDialog();
            }
        });

        edtNumberofSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                othersDialog();
            }
        });

        edtColorCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColorDialog();
            }
        });

        edtNumberOfColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberofColorDialog();
            }
        });

        edtOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orientationDialog();
            }
        });

        edtSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizesDialog();
            }
        });

        edtSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideDialog();
            }
        });

        edtSpecialADOns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specialAdOneDialogs();
            }
        });
        edtPaperType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                papertypesDialog();
            }
        });
        edtPaperGram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paperGramDialog();
            }
        });
        edtUplaodFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runTimePermission()) {
                } else {
                    openMediaDialog(); // upload file option
                }
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationOnCommericalForm();
            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });


    }


    private void getLocations() {
        if (ActivityCompat.checkSelfPermission(CommercialPrintingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CommercialPrintingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CommercialPrintingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(CommercialPrintingActivity.this, location -> {
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
                    currentLatitude = String.valueOf(wayLongitude);
                    currentLongitude = String.valueOf(wayLongitude);
                    Log.d("mydata++", "first" + wayLatitude + "" + wayLongitude);
                    getAddress(context, wayLatitude, wayLongitude);
                    edtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                    //    txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                }
            });
        }

    }

    private void getCurrentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        currentLatitude = String.valueOf(wayLongitude);
                        currentLongitude = String.valueOf(wayLongitude);
                        Log.d("mydata++", "second" + wayLatitude + "" + wayLongitude);
                        edtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));

                        // txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

    }

    private void initSignatuer() {
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                tvSignatureHere.setText("");
                imgClear.setVisibility(View.GONE);
                //Toast.makeText(CommercialPrintingActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                tvSignatureHere.setText("");
                imgClear.setVisibility(View.VISIBLE);
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                Log.d("data++++", "data++++" + signatureBitmap);
                sendSignatureToserver(signatureBitmap);
            }

            @Override
            public void onClear() {
                tvSignatureHere.setText(getString(R.string.signature_here));
                imgClear.setVisibility(View.GONE);
            }
        });
    }




    private boolean runTimePermission() {

        mErrorString = new SparseIntArray();
        int currentapiVersion = Build.VERSION.SDK_INT;
        // if current version is M or greater than M
        if (currentapiVersion >= Build.VERSION_CODES.M) {
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
                        ActivityCompat.requestPermissions(CommercialPrintingActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 102: {
                int permissionCheck = PackageManager.PERMISSION_GRANTED;
                for (int permission : grantResults) {
                    permissionCheck = permissionCheck + permission;
                }
                if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    onPermissionsGranted(requestCode);
                }
                break;
            }
            case 103: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(CommercialPrintingActivity.this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            currentLatitude = String.valueOf(wayLongitude);
                            currentLongitude = String.valueOf(wayLongitude);
                            Log.d("mydata++", "third" + wayLatitude + "" + wayLongitude);
                            edtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));

                            // txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    });

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }

    }


    // if permissions granted succesfully
    private void onPermissionsGranted(int requestcode) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @Override
    public void successResponseFromPresenter(CommericalPrintingResponseModel commericalPrintingResponseModel) {
        dialog.dismiss();
        if (commericalPrintingResponseModel != null && commericalPrintingResponseModel.getData().getCommerical().size() > 0) {
            Log.d("data+++", "data+++" + commericalPrintingResponseModel.getData().getCommerical());
            layputCommericalPrinting.setVisibility(View.VISIBLE);

            for (int i = 0; i < commericalPrintingResponseModel.getData().getCommerical().size(); i++) {
                selectjobList = commericalPrintingResponseModel.getData().getCommerical().get(i).getJob();
                selectColorList = commericalPrintingResponseModel.getData().getCommerical().get(i).getColorCode();
                numberofColorList = commericalPrintingResponseModel.getData().getCommerical().get(i).getNumberOfColors();
                orientationList = commericalPrintingResponseModel.getData().getCommerical().get(i).getOrientation();
                sizeList = commericalPrintingResponseModel.getData().getCommerical().get(i).getSize();
                jobsList = commericalPrintingResponseModel.getData().getCommerical().get(i).getJobs();
                slideList = commericalPrintingResponseModel.getData().getCommerical().get(i).getSide();
                categoryModelArrayList = new ArrayList<>();
                for (int j = 0; j < commericalPrintingResponseModel.getData().getCommerical().get(i).getFinishing().size(); j++) {
                    Model imageModel = new Model();
                    imageModel.setName(commericalPrintingResponseModel.getData().getCommerical().get(i).getFinishing().get(j));
                    categoryModelArrayList.add(imageModel);
                }
                paperTypeList = commericalPrintingResponseModel.getData().getCommerical().get(i).getPaperType();
                paperGramList = commericalPrintingResponseModel.getData().getCommerical().get(i).getPaperGram();
                numberofSheetList = commericalPrintingResponseModel.getData().getCommerical().get(i).getNoOfSheets();
            }

        } else {

        }
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
        layputCommericalPrinting.setVisibility(View.GONE);
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successSubmitResponseFromPresenter(CommercialFromSubmitResponseModel commercialFromSubmitResponseModel) {
        dialog.dismiss();
        String success_message = commercialFromSubmitResponseModel.getData().getCommerical();
        Toast.makeText(context, "" + success_message, Toast.LENGTH_SHORT).show();

       // if (!AppControler.getInstance(CommercialPrintingActivity.this).getBoolean(AppConstants.KEY_RATING))

        finish();
    }


    @Override
    public void errorSubmitResponseFromPresenter(String message) {
        dialog.dismiss();
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }

    // select job dialog
    private void selectJobDialog() {
        seletJodialog = new Dialog(CommercialPrintingActivity.this);
        seletJodialog.setContentView(R.layout.dialog_select_job);
        seletJodialog.setCancelable(true);
        RecyclerView recyclerSelectJob = seletJodialog.findViewById(R.id.recyclerSelectJob);
        SelectAJobAdapter selectAJobAdapter = new SelectAJobAdapter(context, selectjobList);
        recyclerSelectJob.setLayoutManager(new LinearLayoutManager(this));
        recyclerSelectJob.setAdapter(selectAJobAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerSelectJob.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerSelectJob.addItemDecoration(divider);
        seletJodialog.show();
    }

    // select color dialog
    private void selectColorDialog() {
        seletColordialog = new Dialog(CommercialPrintingActivity.this);
        seletColordialog.setContentView(R.layout.dialog_select_color);
        seletColordialog.setCancelable(true);
        RecyclerView recyclerColor = seletColordialog.findViewById(R.id.recyclerColor);
        SelectColorAdapter selectColorAdapter = new SelectColorAdapter(context, selectColorList);
        recyclerColor.setLayoutManager(new LinearLayoutManager(this));
        recyclerColor.setAdapter(selectColorAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerColor.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerColor.addItemDecoration(divider);
        seletColordialog.show();
    }

    // numberofcolor dialog
    private void numberofColorDialog() {
        numberofColordialog = new Dialog(CommercialPrintingActivity.this);
        numberofColordialog.setContentView(R.layout.dialog_number_of_color);
        numberofColordialog.setCancelable(true);
        RecyclerView recyclerNumberOfColor = numberofColordialog.findViewById(R.id.recyclerNumberOfColor);
        NumberOfColorAdapter numberOfColorAdapter = new NumberOfColorAdapter(context, numberofColorList);
        recyclerNumberOfColor.setLayoutManager(new LinearLayoutManager(this));
        recyclerNumberOfColor.setAdapter(numberOfColorAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerNumberOfColor.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerNumberOfColor.addItemDecoration(divider);
        numberofColordialog.show();
    }

    // orientation dialog
    private void orientationDialog() {
        orientationDialog = new Dialog(CommercialPrintingActivity.this);
        orientationDialog.setContentView(R.layout.dialog_orientation);
        orientationDialog.setCancelable(true);
        RecyclerView recyclerOrientation = orientationDialog.findViewById(R.id.recyclerOrientation);
        OrientationAdapter orientationAdapter = new OrientationAdapter(context, orientationList);
        recyclerOrientation.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrientation.setAdapter(orientationAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerOrientation.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerOrientation.addItemDecoration(divider);
        orientationDialog.show();
    }

    // size dialog
    private void sizesDialog() {
        sizeDialog = new Dialog(CommercialPrintingActivity.this);
        sizeDialog.setContentView(R.layout.dialog_size);
        sizeDialog.setCancelable(true);
        RecyclerView recyclerSize = sizeDialog.findViewById(R.id.recyclerSize);
        SizeAdapter sizeAdapter = new SizeAdapter(context, sizeList);
        recyclerSize.setLayoutManager(new LinearLayoutManager(this));
        recyclerSize.setAdapter(sizeAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerSize.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerSize.addItemDecoration(divider);
        sizeDialog.show();
    }

    // standardsize dialog
    private void JobsDialog() {
        standardDialog = new Dialog(CommercialPrintingActivity.this);
        standardDialog.setContentView(R.layout.dialog_standard_size);
        standardDialog.setCancelable(true);
        RecyclerView recyclerstandardSize = standardDialog.findViewById(R.id.recyclerstandardSize);
        StandardSizeAdapter standardSizeAdapter = new StandardSizeAdapter(context, jobsList);
        recyclerstandardSize.setLayoutManager(new LinearLayoutManager(this));
        recyclerstandardSize.setAdapter(standardSizeAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerstandardSize.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerstandardSize.addItemDecoration(divider);
        standardDialog.show();
    }

    // slide dialog
    private void slideDialog() {
        slidesDialog = new Dialog(CommercialPrintingActivity.this);
        slidesDialog.setContentView(R.layout.dialog_slides);
        slidesDialog.setCancelable(true);
        RecyclerView recyclerSlides = slidesDialog.findViewById(R.id.recyclerSlides);
        SlidesAdapter slidesAdapter = new SlidesAdapter(context, slideList);
        recyclerSlides.setLayoutManager(new LinearLayoutManager(this));
        recyclerSlides.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerSlides.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerSlides.addItemDecoration(divider);
        slidesDialog.show();
    }

    // special AdOne Dialogs
    private void specialAdOneDialogs() {
        specialAdOneDialog = new Dialog(CommercialPrintingActivity.this);
        specialAdOneDialog.setContentView(R.layout.dialog_special_ad_one);
        specialAdOneDialog.setCancelable(true);

        RecyclerView recyclerSpecialAdOne = specialAdOneDialog.findViewById(R.id.recyclerSpecialAdOne);
        tvOk = specialAdOneDialog.findViewById(R.id.tvOk);
        TextView tvCancel = specialAdOneDialog.findViewById(R.id.tvCancel);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialAdOneDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialAdOneDialog.dismiss();
            }
        });
        SpecialAdOneAdapter specialAdOneAdapter = new SpecialAdOneAdapter(context, categoryModelArrayList, CommercialPrintingActivity.this);
        recyclerSpecialAdOne.setLayoutManager(new LinearLayoutManager(this));
        recyclerSpecialAdOne.setAdapter(specialAdOneAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerSpecialAdOne.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerSpecialAdOne.addItemDecoration(divider);
        specialAdOneDialog.show();
    }

    //  paper types Dialogs
    private void papertypesDialog() {

        paperTypeDialog = new Dialog(CommercialPrintingActivity.this);
        paperTypeDialog.setContentView(R.layout.dialog_paper_type);
        paperTypeDialog.setCancelable(true);
        RecyclerView recyclerPaperType = paperTypeDialog.findViewById(R.id.recyclerPaperType);
        PaperTypeAdapter paperTypeAdapter = new PaperTypeAdapter(context, paperTypeList);
        recyclerPaperType.setLayoutManager(new LinearLayoutManager(this));
        recyclerPaperType.setAdapter(paperTypeAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerPaperType.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerPaperType.addItemDecoration(divider);
        paperTypeDialog.show();
    }

    //  paper gram Dialogs
    private void paperGramDialog() {
        paperGrmDialog = new Dialog(CommercialPrintingActivity.this);
        paperGrmDialog.setContentView(R.layout.dialog_paper_gram);
        paperGrmDialog.setCancelable(true);
        RecyclerView recyclerPaperGram = paperGrmDialog.findViewById(R.id.recyclerPaperGram);
        PaperGramAdapter paperGramAdapter = new PaperGramAdapter(context, paperGramList);
        recyclerPaperGram.setLayoutManager(new LinearLayoutManager(this));
        recyclerPaperGram.setAdapter(paperGramAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerPaperGram.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerPaperGram.addItemDecoration(divider);
        paperGrmDialog.show();
    }

    //Others dialog
    private void othersDialog() {
        otherDialog = new Dialog(CommercialPrintingActivity.this);
        otherDialog.setContentView(R.layout.dialog_other);
        otherDialog.setCancelable(true);
        RecyclerView recyclerOthers = otherDialog.findViewById(R.id.recyclerOthers);
        OtherAdapter otherAdapter = new OtherAdapter(context, numberofSheetList);
        recyclerOthers.setLayoutManager(new LinearLayoutManager(this));
        recyclerOthers.setAdapter(otherAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerOthers.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerOthers.addItemDecoration(divider);
        otherDialog.show();
    }

    // upload file option
    private void openMediaDialog() {
        Dialog mediaDialog = new Dialog(CommercialPrintingActivity.this);
        mediaDialog.setContentView(R.layout.dialog_media);
        mediaDialog.setCancelable(true);

        TextView tvChoose = (TextView) mediaDialog.findViewById(R.id.tvChoose);
        TextView tvMedia = (TextView) mediaDialog.findViewById(R.id.tvMedia);
        TextView tvGDrive = (TextView) mediaDialog.findViewById(R.id.tvUploadFileDrive);
        TextView tvDDrive = (TextView) mediaDialog.findViewById(R.id.tvUploadFileDropBox);

        TextView tvUploadFile = (TextView) mediaDialog.findViewById(R.id.tvUploadFile);
        TextView tvCancel = (TextView) mediaDialog.findViewById(R.id.tvCancel);

        tvChoose.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvCancel.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvGDrive.setVisibility(View.GONE);
        tvDDrive.setVisibility(View.GONE);
        tvMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(gallery, PICK_IMAGE);
              //  PickImageDialog.build(new PickSetup().setButtonOrientation(LinearLayout.HORIZONTAL)).show(CommercialPrintingActivity.this);

                mediaDialog.dismiss();
            }
        });
        tvUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);

                /*Intent intent = new Intent(CommercialPrintingActivity.this, NormalFilePickActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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



            imageToUpload = sendOtherFileToserver(yourRealPath);


        }
//        else if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
//            ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
//            StringBuilder builder = new StringBuilder();
//            String fileName = null;
//            for (NormalFile file : list) {
//                String path = file.getPath();
//                builder.append(path + "\n");
//                String[] parts = file.getPath().split("/");
//                fileName = parts[parts.length - 1];
//
//                File files = new File(path);
//                RequestBody requestFiles = null;
//
//                try {
//                    requestFiles = RequestBody.create(
//                            MediaType.parse(Files.probeContentType(files.toPath())), files);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    requestFiles = RequestBody.create(MediaType.parse("*//*"), files);
//                }
//                imageToUpload = MultipartBody.Part.createFormData("file_name", fileName, requestFiles);
//                Log.d("file++", "file++" + imageToUpload);
//            }
//            edtUplaodFile.setText(fileName);
//        }
          else if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                // Multiple files selected
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        Log.d("FilePicker", "Selected file URI: " + fileUri.toString());
                    }
                }
                // Single file selected
                else if (data.getData() != null) {
                    Uri fileUri = data.getData();
                    Log.d("FilePicker", "Selected file URI: " + fileUri.toString());
                }
            }
        }else if (requestCode == AppConstants.GPS_REQUEST && requestCode == RESULT_OK) {
            isGPS = true; // flag maintain before get location

        }


    }
    private MultipartBody.Part sendOtherFileToserver(String pathO) {
        String[] parts2 = pathO.split("/");

        final String fileName1 = parts2[parts2.length - 1];
        edtUplaodFile.setText(fileName1);
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


        StringBuilder builder = new StringBuilder();
        String path = file.getPath();
        builder.append(path + "\n");
        String[] parts = file.getPath().split("/");
        String fileName = parts[parts.length - 1];


            imageToUpload = MultipartBody.Part.createFormData("file_name", fileName, reqFile);
        Log.d("multipart++", "multipart++" + imageToUpload);
        return imageToUpload;
    }
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
    // validation
    private void validationOnCommericalForm() {

        if (commercial_category_id.equals("1")) {

            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
//            else if (edtCompany.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
//            }
//            else if (!edtCompany.getText().toString().isEmpty()&&
//            edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtEnterSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter size", Toast.LENGTH_SHORT).show();
            } else if (edtSlide.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select slides", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                            "",
                            edtQuantity.getText().toString().trim(),
                            "",
                            "",
                            "",
                            edtSlide.getText().toString().trim(),
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                            edtEnterSize.getText().toString().trim(),
                            "",
                            "",
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }

        } else if (commercial_category_id.equals("2")) {

            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
//            else if (edtCompany.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
//            }
//            else if (!edtCompany.getText().toString().isEmpty()&&edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select size", Toast.LENGTH_SHORT).show();
            } else if (edtFinishedSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Finished size", Toast.LENGTH_SHORT).show();
            } else if (edtOpenSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter open size", Toast.LENGTH_SHORT).show();
            } else if (edtSlide.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select slides", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                            "",
                            edtQuantity.getText().toString().trim(),
                            "",
                            "",
                            "",
                            edtSlide.getText().toString().trim(),
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                            edtSize.getText().toString().trim(),
                            edtFinishedSize.getText().toString().trim(),
                            edtOpenSize.getText().toString().trim(),
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }

        } else if (commercial_category_id.equals("3")) {


            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
//            else if (edtCompany.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
//            }
//            else if (!edtCompany.getText().toString().isEmpty()&&
//                    edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select size", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                            "",
                            edtQuantity.getText().toString().trim(),
                            "",
                            "",
                            "",
                            "",
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                            edtSize.getText().toString().trim(),
                            edtFinishedSize.getText().toString().trim(),
                            edtOpenSize.getText().toString().trim(),
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }

        } else if (commercial_category_id.equals("4")) {

            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
          /*  else if (edtCompany.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
            }*/
//            else if (!edtCompany.getText().toString().isEmpty()&&
//                    edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtNumberofSheet.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter number of sheet", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select size", Toast.LENGTH_SHORT).show();
            } else if (edtFinishedSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Finished size", Toast.LENGTH_SHORT).show();
            } else if (edtOpenSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter open size", Toast.LENGTH_SHORT).show();
            } else if (edtSlide.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select slides", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                            "",
                            edtQuantity.getText().toString().trim(),
                            edtNumberofSheet.getText().toString().trim(),
                            "",
                            "",
                            edtSlide.getText().toString().trim(),
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                            edtSize.getText().toString().trim(),
                            "",
                            "",
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }

        } else if (commercial_category_id.equals("5")) {

            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
/*
            else if (edtCompany.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
            }
*/
//            else if (!edtCompany.getText().toString().isEmpty()&&edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtEnterSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter size", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                            edtJob.getText().toString().trim(),
                            edtQuantity.getText().toString().trim(),
                            "",
                            "",
                            "",
                            "",
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                            edtEnterSize.getText().toString().trim(),
                            "",
                            "",
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }


        } else if (commercial_category_id.equals("6")) {

            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
         /*   else if (edtCompany.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
            }*/
//            else if (!edtCompany.getText().toString().isEmpty()&&
//                    edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtPagePerSet.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter page per set", Toast.LENGTH_SHORT).show();
            } else if (edtNumberofSet.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter number of set", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select size", Toast.LENGTH_SHORT).show();
            } else if (edtSlide.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select slides", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                           "",
                            edtQuantity.getText().toString().trim(),
                            "",
                            edtPagePerSet.getText().toString().trim(),
                            edtNumberofSet.getText().toString().trim(),
                            edtSlide.getText().toString().trim(),
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                            edtSize.getText().toString().trim(),
                            "",
                            "",
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }

        } else if (commercial_category_id.equals("7")) {
            if (edtFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (edtFirstName.length() < 3 || edtFirstName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (edtLastName.length() < 3 || edtLastName.length() > 15) {
                Toast.makeText(context, "name should be between 3 to 15 characters", Toast.LENGTH_SHORT).show();
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidEmail(edtEmail.getText().toString())) {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show();
            }
//            else if (edtCompany.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter your company name", Toast.LENGTH_SHORT).show();
//            }
//            else if (!edtCompany.getText().toString().isEmpty()&&
//                    edtCompany.length() < 5 || edtCompany.length() > 25) {
//                Toast.makeText(context, "company should be between 5 to 25 characters", Toast.LENGTH_SHORT).show();
//            }
            else if (edtPhone.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show();
            } else if (!CommonMethods.isValidMobile(edtPhone.getText().toString())) {
                Toast.makeText(context, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            } else if (edtSelectJob.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select Job type", Toast.LENGTH_SHORT).show();
            } else if (edtQuantity.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
            } else if (edtColorCode.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select color code", Toast.LENGTH_SHORT).show();
            } else if (edtNumberOfColor.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select number of color", Toast.LENGTH_SHORT).show();
            } else if (edtOrientation.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select orientation", Toast.LENGTH_SHORT).show();
            } else if (edtFinishedSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter Finished size", Toast.LENGTH_SHORT).show();
            } else if (edtOpenSize.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Enter open size", Toast.LENGTH_SHORT).show();
            } else if (edtSlide.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Select slides", Toast.LENGTH_SHORT).show();
            }
//            else if (edtSpecialADOns.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select special Ad one", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperType.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper type", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtPaperGram.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Select paper gram", Toast.LENGTH_SHORT).show();
//            }
            else if (edtUplaodFile.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please upload file", Toast.LENGTH_SHORT).show();
            }
//            else if (edtLocation.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Enter location", Toast.LENGTH_SHORT).show();
//            }
//            else if (edtComment.getText().toString().trim().isEmpty()) {
//                Toast.makeText(context, "Write your comment", Toast.LENGTH_SHORT).show();
//            }
            else {
                String userName = edtFirstName.getText().toString().trim() + "" + edtLastName.getText().toString().trim();
                Toast.makeText(context, "7", Toast.LENGTH_SHORT).show();
                if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                    dialog = UtilsAlertDialog.ShowDialog(context);
                    ipCommercialPrinting.submitCommercialForm(
                            printing_name,
                            commercial_category_name,
                            userName,
                            edtCompany.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtPhone.getText().toString().trim(),
                            edtSelectJob.getText().toString().trim(),
                            "",
                            edtQuantity.getText().toString().trim(),
                            "",
                            edtPagePerSet.getText().toString().trim(),
                            edtNumberofSet.getText().toString().trim(),
                            edtSlide.getText().toString().trim(),
                            edtOrientation.getText().toString().trim(),
                            edtColorCode.getText().toString().trim(),
                            edtNumberOfColor.getText().toString().trim(),
                            edtPaperType.getText().toString().trim(),
                            edtPaperGram.getText().toString().trim(),
                           "",
                            edtFinishedSize.getText().toString().trim(),
                            edtOpenSize.getText().toString().trim(),
                            edtSpecialADOns.getText().toString().trim(),
                            edtComment.getText().toString().trim(),
                            imageToUpload,
                            signatureToUpload,
                            current_Address);
                }
            }

        } else {

        }
    }

    public void OnOptionClick(List<String> list) {
        edtSpecialADOns.setText(Arrays.toString(new List[]{list}).replaceAll("\\[|\\]", ""));
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                current_Address = address;

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onPickResult(PickResult result) {
        if (result.getError() == null) {
            String[] parts = result.getPath().split("/");
            final String fileName = parts[parts.length - 1];
            edtUplaodFile.setText(fileName);
            Bitmap bitmap = result.getBitmap();
            sendImageFileToserver(bitmap);


        }
    }

    private MultipartBody.Part sendImageFileToserver(Bitmap bitmap) {
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
            reqFile = RequestBody.create(
                    MediaType.parse(Files.probeContentType(file.toPath())), file);
        } catch (IOException e) {
            e.printStackTrace();
            reqFile = RequestBody.create(MediaType.parse("*//*"), file);
        }

        imageToUpload = MultipartBody.Part.createFormData("file_name", fileName, reqFile);
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

        StringBuilder builder = new StringBuilder();
        String fileName = null;
        String path = file.getPath();
        builder.append(path + "\n");
        String[] parts = file.getPath().split("/");
        fileName = parts[parts.length - 1];

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        signatureToUpload = MultipartBody.Part.createFormData("signature_file", fileName, reqFile);
        Log.d("multipart++", "multipart++" + signatureToUpload);
        return signatureToUpload;
    }
}


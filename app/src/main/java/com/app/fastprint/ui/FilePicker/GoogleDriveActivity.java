package com.app.fastprint.ui.FilePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.fastprint.R;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.chat.chatActivity;
import com.app.fastprint.ui.login.LoginActivity;
import com.app.fastprint.ui.others.OthersActivity;
import com.app.fastprint.ui.resetPassword.ResetPasswordActivity;
import com.app.fastprint.ui.signup.SignUpActivity;
import com.app.fastprint.utills.AppConstants;
import com.app.fastprint.utills.DriveServiceHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

import butterknife.OnClick;

public class GoogleDriveActivity extends AppCompatActivity {
    private static final String TAG = "log";
    GoogleSignInClient mGoogleSignInClient;
    DriveServiceHelper mDriveServiceHelper;
    TextView tvSignIn;
    TextView tvOpenDrive;


    public static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);
        tvSignIn = findViewById(R.id.tvSignIn);
        tvOpenDrive = findViewById(R.id.tvOpenDrive);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSignIn();
            }
        });

        tvOpenDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });

            requestSignIn();

      //  findViewById(R.id.tvSignIn).setOnClickListener(view -> requestSignIn());
       // findViewById(R.id.tvOpenDrive).setOnClickListener(view -> openFilePicker());

      //  mGoogleSignInClient = buildGoogleSignInClient();

    }

    public void GoBack(View view) {
        finish();
  }
    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
    private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_READONLY))
                        .build();
        GoogleSignInClient  mclientAccount = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(mclientAccount.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }


    public void  validationOnLogin() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            requestSignIn();
        } else {

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


        }
    }
      @Override
        public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

            switch (requestCode) {
                case REQUEST_CODE_SIGN_IN:
                    if (resultCode == Activity.RESULT_OK && resultData != null) {
                        Log.e(TAG, "Sign-in failed.");
                        handleSignInResult(resultData);
                    }
                    break;

                case REQUEST_CODE_OPEN_DOCUMENT:
                    if (resultCode == Activity.RESULT_OK && resultData != null) {
                        Uri uri = resultData.getData();
                        if (uri != null) {
                            openFileFromFilePicker(uri);
                        }
                    }
                    break;
            }

            super.onActivityResult(requestCode, resultCode, resultData);
        }


    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
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

            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                    //    mFileTitleEditText.setText(name);
                      //  mDocContentEditText.setText(content);

                        // Files opened through SAF cannot be modified.
                        //setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to open file from picker.", exception));
        }
    }



}
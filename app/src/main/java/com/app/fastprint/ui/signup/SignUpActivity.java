package com.app.fastprint.ui.signup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.login.LoginActivity;
import com.app.fastprint.ui.signup.interfaces.IPSignUp;
import com.app.fastprint.ui.signup.interfaces.ISignUp;
import com.app.fastprint.ui.signup.presenter.PSignUp;
import com.app.fastprint.ui.signup.responseModel.SignUpResponseModel;
import com.app.fastprint.utills.CommonMethods;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseClass implements ISignUp {

    ImageView imgBack;
    TextView tvTittle;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPhone;
    EditText editPassword;
    EditText editCPassword;
    TextView tvSignUp;
    TextView tvLogin;

    Context context;
    Dialog dialog;
    IPSignUp ipSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = SignUpActivity.this;

        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);
        editCPassword = findViewById(R.id.editCPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        ipSignUp=new PSignUp(this);
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(this));
        tvLogin.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvSignUp.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(this));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationOnSignUp();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void validationOnSignUp() {

        if (editFirstName.getText().toString().trim().isEmpty()) {
            editFirstName.setError("Enter your name");
        } else if (editFirstName.length() < 3 || editFirstName.length() > 15) {
            editFirstName.setError("name should be between 3 to 15 characters");
        } else if (editLastName.getText().toString().trim().isEmpty()) {
            editLastName.setError("Enter your name");
        } else if (editLastName.length() < 3 || editLastName.length() > 15) {
            editLastName.setError("last name should be between 3 to 15 characters");
        } else if (editEmail.getText().toString().trim().isEmpty()) {
            editEmail.setError("Enter your email");
        } else if (!CommonMethods.isValidEmail(editEmail.getText().toString())) {
            editEmail.setError("Enter valid email");
        } else if (editPhone.getText().toString().trim().isEmpty()) {
            editPhone.setError("Enter phone number");
        } else if (!CommonMethods.isValidMobile(editPhone.getText().toString())) {
            editPhone.setError("Enter valid phone number");
        }else if (editPassword.getText().toString().trim().isEmpty()) {
            editPassword.setError("Enter password");
        } else if (editPassword.length() < 6 || editPassword.length() > 16) {
            editPassword.setError("Password between 6 and 16 alphanumeric characters");
        }  else if (editCPassword.getText().toString().trim().isEmpty()) {
            editCPassword.setError("Enter confirm password");
        } else if (!editCPassword.getText().toString().trim().equals(editPassword.getText().toString().trim())) {
            editCPassword.setError("Password does not match");

        } else {

            if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                dialog = UtilsAlertDialog.ShowDialog(context);
                ipSignUp.signUp(editFirstName.getText().toString().trim(),editLastName.getText().toString().trim(),
                        editPhone.getText().toString().trim(),editEmail.getText().toString().trim(),editCPassword.getText().toString().trim());
            }
        }
    }
    @Override
    public void successResponseFromPresenter(SignUpResponseModel signUpResponseModel) {
        dialog.dismiss();
        Toast.makeText(context, ""+signUpResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
        if (signUpResponseModel.getData()!=null){
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    }
}

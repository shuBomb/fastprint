package com.app.fastprint.ui.galleryImages;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.galleryImages.adapters.GalleryImageAdapter;
import com.app.fastprint.ui.galleryImages.interfaces.IGalleryImage;
import com.app.fastprint.ui.galleryImages.interfaces.IPGalleryImage;
import com.app.fastprint.ui.galleryImages.presenters.PGalleryImage;
import com.app.fastprint.ui.galleryImages.responseModel.GalleryImageResponseModel;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryImagesActivity extends BaseClass implements IGalleryImage {

    private ImageView imgBack;
    private TextView tvTittle;
    private TextView tvName;
    private TextView tvTotalImages;
    private RecyclerView recyclerPhoto;
    private RelativeLayout layoutImages;

    Context context;
    Dialog dialog;
    IPGalleryImage ipGalleryImage;
    GalleryImageAdapter galleryImageAdapter;

    String photo_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_images);

        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        tvName = findViewById(R.id.tvName);
        tvTotalImages = findViewById(R.id.tvTotalImages);
        recyclerPhoto = findViewById(R.id.recyclerPhoto);
        layoutImages = findViewById(R.id.layoutImages);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        context=GalleryImagesActivity.this;
        photo_id=getIntent().getStringExtra("photo_id");
        ipGalleryImage=new PGalleryImage(this);
        layoutImages.setVisibility(View.GONE);
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipGalleryImage.getGallery(photo_id);
        }
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvTotalImages.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvName.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
    }



    @Override
    public void successResponseFromPresenters(GalleryImageResponseModel galleryImageResponseModel) {
        dialog.dismiss();

        layoutImages.setVisibility(View.VISIBLE);
        if (galleryImageResponseModel!=null){

            tvTotalImages.setText(String.valueOf(galleryImageResponseModel.getData().getCount()));
            tvName.setText(galleryImageResponseModel.getData().getName());
            tvTittle.setText(galleryImageResponseModel.getData().getName());

            updateListUI(galleryImageResponseModel);
        }
    }

    private void updateListUI(GalleryImageResponseModel galleryImageResponseModel) {
        galleryImageAdapter = new GalleryImageAdapter(this, galleryImageResponseModel.getData().getImages());
        recyclerPhoto.setLayoutManager(new LinearLayoutManager(this));
        recyclerPhoto.setAdapter(galleryImageAdapter);
    }

    @Override
    public void errorResponseFromPresenters(String message) {
        dialog.dismiss();
        layoutImages.setVisibility(View.GONE);
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }
}
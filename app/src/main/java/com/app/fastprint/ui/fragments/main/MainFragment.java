package com.app.fastprint.ui.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.fastprint.R;
import com.app.fastprint.ui.fragments.main.adapters.ProductCategoriesAdapter2;
import com.app.fastprint.ui.product.adapters.StoreAdapter;
import com.app.fastprint.ui.main.adapter.CategoriesAdapter;
import com.app.fastprint.ui.main.responseModel.CategoriesModel;
import com.app.fastprint.ui.productDetails.ImageModel;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment {

    RecyclerView recyclerProductSubCategories;
    RecyclerView recyclerProduct;

    ProductCategoriesAdapter2 productCategoriesAdapter;
    StoreAdapter productSubCategoriesAdapter;

    ViewPager pager;
    CirclePageIndicator indicator;
    RecyclerView RecyclerCategories;

    public MainFragment() {
        // Required empty public constructor
    }

    private ArrayList<ImageModel> imageModelArrayList;
    private int[] myImageList = new int[]{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    CategoriesAdapter categoriesAdapter;

    View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        // Find views
        pager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);
        RecyclerCategories = view.findViewById(R.id.RecyclerCategories);

        context = getActivity();
        viewInitialization();
        initViewPager();
        return view;
    }

    private void initViewPager() {
        imageModelArrayList = new ArrayList<>();
        // imageModelArrayList = populateList();

        mPager = view.findViewById(R.id.pager);
        // mPager.setAdapter(new ImageSlidingAdapter(getActivity(), imageModelArrayList));
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        // Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = imageModelArrayList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
    }

    private void viewInitialization() {
        CategoriesModel[] categoriesModels = new CategoriesModel[]{
                new CategoriesModel("About us", R.drawable.ic_view),
                new CategoriesModel("Services", R.drawable.ic_view),
                new CategoriesModel("Definition of Logo", R.drawable.ic_view),
                new CategoriesModel("Contact us", R.drawable.ic_view),
                new CategoriesModel("Find us", R.drawable.ic_view),
                new CategoriesModel("Enquiry Form", R.drawable.ic_view),
                new CategoriesModel("Store", R.drawable.ic_view),
                new CategoriesModel("Social Media", R.drawable.ic_view),
                new CategoriesModel("Photo Gallery", R.drawable.ic_view),
        };

        /*int columns = 3;
        categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesModels);
        RecyclerCategories.setLayoutManager(new GridLayoutManager(getActivity(), columns));
        RecyclerCategories.setAdapter(categoriesAdapter);*/
    }
}

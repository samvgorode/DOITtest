package com.example.who.doittest.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.who.doittest.R;
import com.example.who.doittest.adapter.GalleryAdapter;
import com.example.who.doittest.interfaces.IGalleryView;
import com.example.who.doittest.pojo.ImagePojo;
import com.example.who.doittest.presenter.GalleryActivityPresenter;
import com.example.who.doittest.utils.GridSpacingItemDecoration;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.who.doittest.global.Constants.TOKEN;

public class GalleryActivity extends AppCompatActivity implements IGalleryView, View.OnClickListener {

    @BindView(R.id.rvGallery)
    public RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progress;

    private GalleryAdapter adapter;
    private View customBarView;
    private GalleryActivityPresenter presenter;

    public static Intent getNewIntent(Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setCustomBar();
        presenter = new GalleryActivityPresenter(GalleryActivity.this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress.setVisibility(View.VISIBLE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        presenter.fetchImages();
                    }
                }, 1500);
    }

    private void setCustomBar() {
        if (getSupportActionBar() != null) {
            ActionBar bar = getSupportActionBar();
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setDisplayShowCustomEnabled(true);
            bar.setCustomView(R.layout.custom_bar_gallery);
            customBarView = bar.getCustomView();
            setBarClickListeners();
        }
    }

    @Override
    public void setDataToAdapter(List<ImagePojo> albumList) {
        adapter = new GalleryAdapter(this, albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFetchImagesSuccess() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchImagesFailure() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "Not Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGif(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        WebView view = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        view.setLayoutParams(params);
        builder.setView(view);
        builder.create().show();
        view.loadUrl(url);
    }

    @Override
    public void noImages() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "You have no images", Toast.LENGTH_SHORT).show();
    }

    private void setBarClickListeners() {
        ImageView plus = (ImageView) customBarView.findViewById(R.id.ivPlus);
        ImageView play = (ImageView) customBarView.findViewById(R.id.ivDoGif);
        ImageView logout = (ImageView) customBarView.findViewById(R.id.ivLogOut);
        plus.setOnClickListener(this);
        play.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivPlus:
                addNewImage();
                break;

            case R.id.ivDoGif:
                getGif();
                break;

            case R.id.ivLogOut:
                doLogOut();
                break;

            default:
                break;
        }
    }

    void addNewImage() {
        startActivity(AddImageActivity.getNewIntent(this));
    }

    void getGif() {
        progress.setVisibility(View.VISIBLE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        presenter.showGif();
                    }
                }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(SplashActivity.getNewIntent(this, true));
        finish();
    }

    private void doLogOut() {
        if (Hawk.contains(TOKEN)) Hawk.delete(TOKEN);
        startActivity(SplashActivity.getNewIntent(this, false));
        finish();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

package com.iva.osmandtest.presentation.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.iva.osmandtest.R;
import com.iva.osmandtest.databinding.ActivityMainBinding;
import com.iva.osmandtest.domain.Constants;
import com.iva.osmandtest.domain.thread.MainThreadImpl;
import com.iva.osmandtest.presentation.adapters.RegionRecyclerViewAdapter;
import com.iva.osmandtest.presentation.presenters.MainPresenter;
import com.iva.osmandtest.presentation.presenters.impl.MainPresenterImpl;
import com.iva.osmandtest.presentation.views.RecyclerViewItemDivider;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {
    private final String TAG = "MainActivity";

    private MainPresenter mPresenter;
    private ActivityMainBinding binding;
    private RegionRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getMemoryInformation();
    }

    private void getMemoryInformation() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableGigs = mi.availMem / 1073741824D; // 1 GB = 1073741824 B

        int percentAvail = (int) ((mi.availMem * 100) / mi.totalMem);

        binding.memoryProgressBar.setProgress(percentAvail);

        binding.count.setText((String.valueOf(
                new DecimalFormat(Constants.MEMORY_COUNT_PATTERN).format(availableGigs)))
        );
    }

    private void initRecycler() {
        mAdapter = new RegionRecyclerViewAdapter(
                new ArrayList<>(mPresenter.getWorldRegionList()),
                this
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.regionRecyclerView.setLayoutManager(layoutManager);
        binding.regionRecyclerView.addItemDecoration(new RecyclerViewItemDivider(
                this,
                RecyclerViewItemDivider.VERTICAL_LIST
        ));
        binding.regionRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter = new MainPresenterImpl(
                MainThreadImpl.getInstance(),
                this
        );
        initRecycler();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public XmlPullParser newInstanceXMLParser() {
        return getResources().getXml(R.xml.regions);
    }

    @Override
    public void onUpdateList(String name) {

    }

    @Override
    public void getRegions(String name) {
        mAdapter.setWorldRegions(false);
        mAdapter.setRegionList(mPresenter.getRegions(name));
    }

}

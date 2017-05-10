package com.iva.osmandtest.presentation.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
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

        mPresenter = new MainPresenterImpl(
                MainThreadImpl.getInstance(),
                this
        );
        getMemoryInformation();
        initRecycler();
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
                new ArrayList<>(mPresenter.getContinentsList()),
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

    @Override
    public void getContinents() {
        mAdapter.setWorldRegions(true);
        mAdapter.setRegionList(mPresenter.getContinentsList());
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressed();
    }

    @Override
    public void exit() {
        new AlertDialog.Builder(this).setMessage(R.string.really_want_exit)
                .setTitle(R.string.exit_dialog_title)
                .setPositiveButton(R.string.positive_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.negative_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

}

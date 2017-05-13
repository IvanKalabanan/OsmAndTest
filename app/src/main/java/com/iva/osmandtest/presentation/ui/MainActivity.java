package com.iva.osmandtest.presentation.ui;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;

import com.iva.osmandtest.R;
import com.iva.osmandtest.databinding.ActivityMainBinding;
import com.iva.osmandtest.domain.Constants;
import com.iva.osmandtest.domain.network.DownloadService;
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
    private BroadcastReceiver receiver;
    private boolean isReceiverRegistered;
    private Intent downloadIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        downloadIntent = new Intent(this, DownloadService.class);

        mPresenter = new MainPresenterImpl(
                MainThreadImpl.getInstance(),
                this
        );
        getMemoryInformation();
        initRecycler();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (binding.memoryInfContainer.getVisibility() == View.GONE) {
                    binding.memoryInfContainer.setVisibility(View.VISIBLE);
                }
                int progress = intent.getIntExtra(DownloadService.PROGRESS, 100);
                binding.progressBarTitle.setText(intent.getStringExtra(DownloadService.NAME));
                binding.memoryProgressBar.setProgress(progress);
                binding.count.setText(getString(R.string.percent, String.valueOf(progress)));
                if (progress == 100) {
                    topBarVisibleAction();
                }
            }
        };
        registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                    new IntentFilter(DownloadService.UPDATE_PROGRESS));
            isReceiverRegistered = true;
        }
    }

    private void getMemoryInformation() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableGigs = mi.availMem / 1073741824D; // 1 GB = 1073741824 B

        int percentAvail = (int) ((mi.availMem * 100) / mi.totalMem);

        binding.memoryProgressBar.setProgress(percentAvail);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding.count.setText(Html.fromHtml(getString(R.string.count, (String.valueOf(
                    new DecimalFormat(Constants.MEMORY_COUNT_PATTERN).format(availableGigs)))), Html.FROM_HTML_MODE_LEGACY)
            );
        } else {
            binding.count.setText(Html.fromHtml(getString(R.string.count, (String.valueOf(
                    new DecimalFormat(Constants.MEMORY_COUNT_PATTERN).format(availableGigs)))))
            );
        }
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
        topBarVisibleAction();
    }

    @Override
    public void getContinents() {
        mAdapter.setWorldRegions(true);
        mAdapter.setRegionList(mPresenter.getContinentsList());
        topBarVisibleAction();
    }

    @Override
    public void downloadMap(String name) {
        downloadIntent.putExtra(DownloadService.URL, "url of the file to download");
        downloadIntent.putExtra(DownloadService.NAME, name);
        startService(downloadIntent);
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressed();
    }

    private void topBarVisibleAction() {
        if (!isServiceRunning()) {
            if (mPresenter.isRootPage()) {
                binding.progressBarTitle.setText(R.string.device_memory);
                binding.worldRegionTitle.setVisibility(View.VISIBLE);
                binding.memoryInfContainer.setVisibility(View.VISIBLE);
                getMemoryInformation();
            } else {
                binding.memoryInfContainer.setVisibility(View.GONE);
                binding.worldRegionTitle.setVisibility(View.GONE);
            }
        }
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

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DownloadService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

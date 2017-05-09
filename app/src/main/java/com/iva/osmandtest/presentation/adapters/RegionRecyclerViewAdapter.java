package com.iva.osmandtest.presentation.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iva.osmandtest.R;
import com.iva.osmandtest.domain.model.Region;
import com.iva.osmandtest.presentation.ContentRequestListener;

import java.util.List;

/**
 * Created by Z580 on 08.05.2017.
 */

public class RegionRecyclerViewAdapter extends RecyclerView.Adapter<RegionRecyclerViewAdapter.RegionHolder> {

    private final int WORLD_REGION_TYPE = 1;
    private final int LOCALE_REGION_TYPE = 2;
    private List<Region> items;
    private ContentRequestListener requestListener;
    private boolean isWorldRegions;

    public RegionRecyclerViewAdapter(
            List<Region> items,
            ContentRequestListener requestListener) {
        this.items = items;
        this.requestListener = requestListener;
        this.isWorldRegions = true;
    }

    @Override
    public RegionHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v;
        if (viewType == WORLD_REGION_TYPE) {
             v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_world_region, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_locale_region, parent, false);
        }
        return new RegionHolder(v);
    }

    @Override
    public void onBindViewHolder(final RegionHolder holder, final int position) {
        Region region = items.get(position);
        if (!isWorldRegions) {
            if (!region.isCanDownload()) {
                holder.ivDownload.setVisibility(View.GONE);
            } else {
                holder.ivDownload.setVisibility(View.VISIBLE);
            }
        }
        holder.tvName.setText(region.getName());
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isWorldRegions) {
            return WORLD_REGION_TYPE;
        }
        return LOCALE_REGION_TYPE;
    }

    public void setWorldRegions(boolean isWorldRegions) {
        this.isWorldRegions = isWorldRegions;
    }

    public void setRegionList(List<Region> regionList) {
        if (!regionList.isEmpty()) {
            this.items = regionList;
            notifyDataSetChanged();
        }
    }

    class RegionHolder extends RecyclerView.ViewHolder {

        ImageView ivRegion;
        TextView tvName;
        ImageView ivDownload;

        //TODO Bind views
        RegionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestListener.getRegions(items.get(getAdapterPosition()).getName());
                }
            });
            if (!isWorldRegions) {
                ivDownload = (ImageView) itemView.findViewById(R.id.ivDownload);
                ivDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            ivRegion = (ImageView) itemView.findViewById(R.id.regionIcon);
            tvName = (TextView) itemView.findViewById(R.id.regionName);
        }
    }
}
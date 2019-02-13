package com.example.aydil.nestedrecyclerview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aydil.nestedrecyclerview.R;
import com.example.aydil.nestedrecyclerview.dataproviders.HorizontalRVDataProviders;
import com.example.aydil.nestedrecyclerview.dataproviders.VerticalRVDataProviders;

import java.util.ArrayList;

import static com.example.aydil.nestedrecyclerview.MainActivity.getHorizontalData;
import static com.example.aydil.nestedrecyclerview.MainActivity.getVerticalData;

public class MainRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Object> items;
    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;
    RecyclerView.LayoutManager layoutManagerVertical, layoutManagerHorizontal;
//    RecyclerView recyclerView;

    public MainRVAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case VERTICAL:
                view = inflater.inflate(R.layout.vertical_recycler_view, parent, false);
                holder = new VerticalViewHolder(view);
                break;
            case HORIZONTAL:
                view = inflater.inflate(R.layout.horizontal_recycler_view, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.horizontal_recycler_view
                        , parent, false);
                holder = new HorizontalViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VERTICAL)
            verticalView((VerticalViewHolder) holder);
        else if (holder.getItemViewType() == HORIZONTAL)
            horizontalView((HorizontalViewHolder) holder);
    }

    private void verticalView(VerticalViewHolder holder) {
        VerticalRVAdapter verticalRVAdapter = new VerticalRVAdapter(getVerticalData());
        layoutManagerVertical = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerViewVertical.setLayoutManager(layoutManagerVertical);
        holder.recyclerViewVertical.setAdapter(verticalRVAdapter);
    }

    private void horizontalView(HorizontalViewHolder holder) {
        HorizontalRVAdapter horizontalRVAdapter = new HorizontalRVAdapter(getHorizontalData());
        layoutManagerHorizontal = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        holder.recyclerViewHorizontal.setAdapter(horizontalRVAdapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getItemViewType(int position) {
        if (items.get(position) instanceof VerticalRVDataProviders)
            return VERTICAL;
        if (items.get(position) instanceof HorizontalRVDataProviders)
            return HORIZONTAL;
        return -1;
//  Horizontal value is 2 so we return -1 to make it 1.
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerViewVertical;
        VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerViewVertical = (RecyclerView) itemView.findViewById(R.id.vertical_recycler_view);
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerViewHorizontal;
        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerViewHorizontal = (RecyclerView) itemView.findViewById(R.id.horizontal_recycler_view);
        }
    }
}

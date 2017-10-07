package com.lapism.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchVariantsAdapter extends RecyclerView.Adapter<SearchVariantsAdapter.ResultViewHolder> {

    protected List<OnItemClickListener> mItemClickListeners;
    protected List<SearchVariant> variants;
    protected Context mContext;


    // todo The first constructor of searchAdapter should work... weird.
    public SearchVariantsAdapter(Context context) {// getContext();
        this.mContext = context;
        setData(false);
    }

    public void setData(boolean more) {
        if (more) {
            initMoreData();
        } else {
            initLessData();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void initLessData() {
        variants = new ArrayList<>();
        String[] search_variants_key = mContext.getResources().
                getStringArray(R.array.search_variants_less);

        TypedArray imagesTypedArray = mContext.getResources().
                obtainTypedArray(R.array.search_drawables_less);
        int imagesLength = mContext.getResources().
                getIntArray(R.array.search_drawables_less).length;

        for (int i = 0; i < imagesLength; i++) {
            SearchVariant variant = new SearchVariant(
                    imagesTypedArray.getResourceId(i, 0), search_variants_key[i]);
            variants.add(variant);
        }
        imagesTypedArray.recycle();
    }

    public void initMoreData() {
        variants = new ArrayList<>();
        String[] search_variants_key = mContext.getResources().
                getStringArray(R.array.search_variants_more);

        TypedArray imagesTypedArray = mContext.getResources().
                obtainTypedArray(R.array.search_drawables_more);
        int imagesLength = mContext.getResources().
                getIntArray(R.array.search_drawables_more).length;

        for (int i = 0; i < imagesLength; i++) {
            SearchVariant variant = new SearchVariant(
                    imagesTypedArray.getResourceId(i, 0), search_variants_key[i]);
            variants.add(variant);
        }
        imagesTypedArray.recycle();
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.search_variant_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        viewHolder.imgIcon.setImageResource(variants.get(position).imageResId);
        viewHolder.tvKey.setText(variants.get(position).getSearchKey());
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        addOnItemClickListener(listener, null);
    }

    protected void addOnItemClickListener(OnItemClickListener listener, Integer position) {
        if (mItemClickListeners == null)
            mItemClickListeners = new ArrayList<>();
        if (position == null)
            mItemClickListeners.add(listener);
        else
            mItemClickListeners.add(position, listener);
    }

    @Deprecated
    /* Use addOnItemClickListener. */
    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        addOnItemClickListener(mItemClickListener);
    }

    @SuppressWarnings("UnusedParameters")
    public interface OnItemClickListener {
        void onItemClick(View view, int position, String searchKey);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView imgIcon;
        protected TextView tvKey;

        public ResultViewHolder(View view) {
            super(view);
            imgIcon = (ImageView) view.findViewById(R.id.img_item);
            tvKey = (TextView) view.findViewById(R.id.tv_item_key);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListeners != null) {
                int layoutPosition = getLayoutPosition();
                if (variants.get(layoutPosition).getSearchKey().equalsIgnoreCase("more")) {
                    setData(true);
                    return;
                } else if (variants.get(layoutPosition).getSearchKey().equalsIgnoreCase("less")) {
                    setData(false);
                    return;
                }
                for (OnItemClickListener listener : mItemClickListeners)
                    listener.onItemClick(v, layoutPosition,
                            variants.get(layoutPosition).getSearchKey());
            }
        }
    }

    private class SearchVariant implements Serializable {
        private Integer imageResId;
        private String searchKey;

        public SearchVariant(Integer imageResId, String searchKey) {
            this.imageResId = imageResId;
            this.searchKey = searchKey;
        }

        public Integer getImageResId() {
            return imageResId;
        }

        public void setImageResId(Integer imageResId) {
            this.imageResId = imageResId;
        }

        public String getSearchKey() {
            return searchKey;
        }

        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }
}
package com.azizova.loftmoney;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemList = new ArrayList<Item>();
    private final String TYPE;
    private ItemsAdapterListener mListener;

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public void clearSelections(){
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(int position){
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(int position){
        mSelectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize(){
        int result=0;
        for (int i = 0; i < mItemList.size(); i++) {
            if (mSelectedItems.get(i))
                result++;
        }
        return result;
    }

    public List<Integer> getSelectedItemsId(){
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (Item item : mItemList) {
            if (mSelectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

    public ItemsAdapter(final String type) {
        this.TYPE = type;
    }

    public void setListener(ItemsAdapterListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);
        if (TYPE== MainActivity.INCOME){
            TextView textView = itemView.findViewById(R.id.price_view);
            textView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.apple_green));
        }
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindItem(mItemList.get(position), mSelectedItems.get(position));
        holder.setListener(mListener, mItemList.get(position), position);
    }

    public void addItem(Item item){
        mItemList.add(item);
        notifyDataSetChanged();
    }

    public void clearItems(){
        mItemList.clear();;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private TextView mNameView;
        private TextView mPriceView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mNameView = itemView.findViewById(R.id.name_view);
            mPriceView = itemView.findViewById(R.id.price_view);
        }

        public void bindItem(final Item item, final boolean isSelected){
            mItemView.setSelected(isSelected);
            mNameView.setText(item.getName());
            mPriceView.setText(
                    mPriceView.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(item.getPrice()))
            );
        }

        public void setListener(final ItemsAdapterListener listener, final Item item, final int position){
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });
            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLingClick(item, position);
                    return false;
                }
            });
        }
    }
}

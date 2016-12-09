package com.example.elenahorton.mobilefinalproject.adapter;

/**
 * Created by elenahorton on 12/9/16.
 */
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class PostTouchHelperCallback extends
        ItemTouchHelper.Callback {

    private PostTouchHelperAdapter postTouchHelperAdapter;

    public PostTouchHelperCallback(PostTouchHelperAdapter cityTouchHelperAdapter) {
        this.postTouchHelperAdapter = cityTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        postTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition()
        );
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        postTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
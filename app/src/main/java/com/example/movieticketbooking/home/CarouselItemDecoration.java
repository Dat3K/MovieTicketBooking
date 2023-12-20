package com.example.movieticketbooking.home;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselItemDecoration extends RecyclerView.ItemDecoration {
    private final int horizontalOffset;
    private final float minScale;
    public CarouselItemDecoration(int horizontalOffset, float minScale) {
        this.horizontalOffset = horizontalOffset;
        this.minScale = minScale;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();

        int startMargin = position == 0 ? 0 : -horizontalOffset / 2; // Adjust the start margin
        int endMargin = position == itemCount - 1 ? 0 : -horizontalOffset / 2; // Adjust the end margin

        outRect.set(startMargin, 0, endMargin, 0);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            float offsetFactor = Math.abs(child.getX() / parent.getWidth());
            float scaleFactor = 1 - offsetFactor * (1 - minScale);
            child.setScaleX(scaleFactor);
            child.setScaleY(scaleFactor);
            child.setAlpha(minScale + (1 - offsetFactor) * (1 - minScale));
        }
    }
}

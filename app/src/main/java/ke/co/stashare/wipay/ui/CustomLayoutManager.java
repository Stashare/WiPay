package ke.co.stashare.wipay.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Ken Wainaina on 07/04/2017.
 */

public class CustomLayoutManager extends LinearLayoutManager {
    private int mParentWidth;
    private int mItemWidth;

    public CustomLayoutManager(Context context, int parentWidth, int itemWidth) {
        super(context);
        mParentWidth = parentWidth;
        mItemWidth = itemWidth;
    }

    @Override
    public int getPaddingLeft() {
        return Math.round(mParentWidth / 2f - mItemWidth / 2f);
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }
}

package com.eber.bfs.views.decoration;

import android.content.Context;

import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

/**
 * Created by WangLibo on 2017/5/16.
 */

public class HomeGridItemDecoration extends Y_DividerItemDecoration {
    public HomeGridItemDecoration(Context context) {
        super(context, 1, 0xffe4e4e4);
    }

    @Override
    public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
        //顺时针顺序:left, top, right, bottom
        if (itemPosition == 0 || itemPosition == 3 || itemPosition == 2 || itemPosition == 5) {
            return new boolean[]{false, false, false, true};
        } else if (itemPosition == 1 || itemPosition == 4) {
            return new boolean[]{true, false, true, true};
        } else if (itemPosition == 7) {
            return new boolean[]{true, false, true, false};
        }
        return new boolean[]{false, false, false, false};//默认不显示
    }
}

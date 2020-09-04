package com.tekrevol.mantra.callbacks;

import android.view.View;

public interface OnSubItemClickListener {
    void onItemClick(int parentPosition, int childPosition, Object object, View view, String adapterName);


}

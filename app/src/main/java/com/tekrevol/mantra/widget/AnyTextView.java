package com.tekrevol.mantra.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;


import com.tekrevol.mantra.utils.utility.Utils;

public class AnyTextView extends AppCompatTextView {



    public AnyTextView(Context context) {
        super(context);
    }

    public AnyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.setTypeface(attrs, this);
    }

    public AnyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Utils.setTypeface(attrs, this);
    }



    public String getStringTrimmed(){
        return  getText().toString().trim() ;
    }


}

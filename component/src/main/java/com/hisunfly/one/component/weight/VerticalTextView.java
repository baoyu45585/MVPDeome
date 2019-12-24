//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalTextView extends TextView {
    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalTextView(Context context) {
        super(context);
    }

    public void setText(CharSequence text, BufferType type) {
        if (!"".equals(text) && text != null && text.length() != 0) {
            int m = text.length();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < m; ++i) {
                CharSequence index = text.toString().subSequence(i, i + 1);
                sb.append(index + "\n");
            }

            super.setText(sb, type);
        }
    }
}

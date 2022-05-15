package com.example.music_app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class marqueueText extends androidx.appcompat.widget.AppCompatTextView {
    public marqueueText(Context context) {
        super(context);
    }

    public marqueueText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public marqueueText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //返回textview是否处在选中的状态
    //而只有选中的textview才能够实现跑马灯效果
    @Override
    public boolean isFocused() {
        return true;
    }

}

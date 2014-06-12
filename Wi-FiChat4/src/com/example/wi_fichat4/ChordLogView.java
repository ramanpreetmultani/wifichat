package com.example.wi_fichat4;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ChordLogView extends ScrollView {
	
    private TextView logTextView;
    
    public ChordLogView(Context context, AttributeSet attr) {
        super(context, attr);
        
        logTextView = new TextView(context);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        
        logTextView.setLayoutParams(lp);
        addView(logTextView);
    }
    
    public void appendLog(String log) {
    	
        logTextView.append(log + "\n");
        
        post(new Runnable() {
            @Override
            public void run() {
                fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}

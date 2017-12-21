package tfn.android.lollipops.view.widget.blogtextview;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tangfunan on 2017/12/20.
 */

public class ClickableTextViewMentionLinkOnTouchListener implements View.OnTouchListener {

    private int color;
    private boolean find = false;

    public ClickableTextViewMentionLinkOnTouchListener(int color) {
        this.color = color;
    }

    public ClickableTextViewMentionLinkOnTouchListener() {
        this.color = Color.parseColor("#33969696");
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Layout layout = ((TextView) view).getLayout();
        if (layout == null) {
            return false;
        } else {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int line = layout.getLineForVertical(y);
            int offset = layout.getOffsetForHorizontal(line, (float) x);
            TextView tv = (TextView) view;
            SpannableString value = SpannableString.valueOf(tv.getText());
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    MyUrlSpan[] urlSpans = value.getSpans(0, value.length(), MyUrlSpan.class);
                    int findStart = 0;
                    int findEnd = 0;
                    MyUrlSpan[] spans = urlSpans;
                    int length = urlSpans.length;
                    for (int i = 0; i < length; i++) {
                        MyUrlSpan urlSpan = spans[i];
                        int start = value.getSpanStart(urlSpan);
                        int end = value.getSpanEnd(urlSpan);
                        if (start <= offset && offset <= end) {
                            find = true;
                            findStart = start;
                            findEnd = end;
                            break;
                        }
                    }
                    float lineWidth = layout.getLineWidth(line);
                    find &= lineWidth >= (float) x;
                    if (find) {
                        LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(color);
                        value.setSpan(backgroundColorSpan, findStart, findEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        tv.setText(value);
                    }
                    return this.find;

                case MotionEvent.ACTION_UP:


                case MotionEvent.ACTION_CANCEL:
                    if (find) {
                        LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                        LongClickableLinkMovementMethod.getInstance().removeLongClickCallback();
                    }
                    BackgroundColorSpan[] backgroundColorSpans = value.getSpans(0, value.length(), BackgroundColorSpan.class);
                    int len = backgroundColorSpans.length;
                    for (int j = 0; j < len; j++) {
                        BackgroundColorSpan span = backgroundColorSpans[j];
                        value.removeSpan(span);
                    }
                    tv.setText(value);
                    find = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (find) {
                        LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                    }
            }
            return false;
        }
    }
}
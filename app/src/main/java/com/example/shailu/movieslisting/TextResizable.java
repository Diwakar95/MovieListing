package com.example.shailu.movieslisting;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by shailu on 3/8/16.
 */

public class TextResizable {

    public static void makeTextViewResizable(final TextView tv,
                                             final int maxLine, final String expandText, final boolean viewMore) {

        try {
            if (tv.getTag() == null) {
                tv.setTag(tv.getText());
            }
            ViewTreeObserver vto = tv.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {

                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine > 0 && tv.getLineCount() <= maxLine) {
                        int lineEndIndex = 0;
                        if (tv.getLayout() != null)
                            lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0,
                                lineEndIndex)
                                + "";
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(tv.getText()
                                                .toString(), tv, maxLine, "",
                                        viewMore, maxLine), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() > maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0,
                                lineEndIndex).toString();
                        if (text.charAt(text.length() - 1) != '\n')
                            text += '\n';
                        text += expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(tv.getText()
                                                .toString(), tv, maxLine, expandText,
                                        viewMore, maxLine), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(
                                tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex).toString();
                        if (text.charAt(text.length() - 1) != '\n')
                            text += '\n';
                        text += expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(tv.getText()
                                                .toString(), tv, lineEndIndex, expandText,
                                        viewMore, maxLine), TextView.BufferType.SPANNABLE);
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(
            final String strSpanned, final TextView tv, final int maxLine,
            final String spanableText, final boolean viewMore, final int maxx) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (strSpanned.contains(spanableText)) {
            ssb.setSpan(
                    new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {

                            if (viewMore) {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, -5, "-Less",
                                        false);
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();

                                makeTextViewResizable(tv, 5, "+More",
                                        true);
                                tv.setTextColor(Color.BLACK);
                            }

                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#3F51B5"));
                        }
                    }, strSpanned.indexOf(spanableText),
                    strSpanned.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
}

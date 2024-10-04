package com.snow.suggestion;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.EditText;

public class SuggestiveEditor extends EditText implements TextWatcher {

    private SuggestiveMenu menu;
    private TextWatcher watcher;
    private boolean lock = false;

    public SuggestiveEditor(Context context) {
        super(context);
        menu = new SuggestiveMenu(context, this);
        super.addTextChangedListener(this);
    }

    public SuggestiveEditor(Context context, AttributeSet set) {
        super(context, set);
        menu = new SuggestiveMenu(context, this);
        super.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        try {
            if (watcher != null) {
                watcher.beforeTextChanged(charSequence, start, count, after);
            }
        } catch (Exception e) {
            Log.e("SuggestiveEditor", e.getMessage(), e.getCause());
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (menu != null) {
            if (charSequence.length() > 0) {
                if (!lock) {
                    updateTextColor();
                }
                if (!menu.isShowing()) {
                    menu.show();
                }
            } else {
                if (menu.isShowing()) {
                    menu.dismiss();
                }
            }
        }
        try {
            if (watcher != null) {
                watcher.onTextChanged(charSequence, start, before, count);
            }
        } catch (Exception e) {
            Log.e("SuggestiveEditor", e.getMessage(), e.getCause());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            if (watcher != null) {
                watcher.afterTextChanged(editable);
            }
        } catch (Exception e) {
            Log.e("SuggestiveEditor", e.getMessage(), e.getCause());
        }
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        this.watcher = watcher;
    }

    private void updateTextColor() {
        if (lock) return;

        Editable text = getText();
        String currentText = text.toString();
        SpannableString spannableString = new SpannableString(currentText);
        int start = 0;
        int cursorPosition = getSelectionStart();

        while (start < currentText.length()) {
            while (start < currentText.length()
                    && !Character.isLetterOrDigit(currentText.charAt(start))) {
                start++;
            }

            int end = start;
            while (end < currentText.length()
                    && Character.isLetterOrDigit(currentText.charAt(end))) {
                end++;
            }

            String token = currentText.substring(start, end);
            if (!token.isEmpty()) {
                if (token.matches("\\d+")) {
                    if (start == 0 || !Character.isLetter(currentText.charAt(start - 1))) {
                        int color = Suggestion.Outline.Blue.getHexaColor();
                        spannableString.setSpan(
                                new ForegroundColorSpan(color),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    Suggestion matchingSuggestion = suggestion(token);
                    if (matchingSuggestion != null) {
                        int color = matchingSuggestion.getColor().getHexaColor();
                        spannableString.setSpan(
                                new ForegroundColorSpan(color),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            start = end;

            while (start < currentText.length()
                    && Character.isWhitespace(currentText.charAt(start))) {
                start++;
            }
        }

        start = 0;
        while (start < currentText.length()) {
            int lineStart = start;
            int lineEnd = currentText.indexOf('\n', lineStart);
            if (lineEnd == -1) lineEnd = currentText.length();

            int hashIndex = currentText.indexOf('#', lineStart);
            if (hashIndex != -1 && hashIndex < lineEnd) {
                int color = Color.parseColor("#006400");
                spannableString.setSpan(
                        new ForegroundColorSpan(color),
                        hashIndex,
                        lineEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            start = lineEnd + 1;
        }

        lock = true;
        setText(spannableString);
        setSelection(Math.min(cursorPosition, spannableString.length()));
        lock = false;
    }

    private Suggestion suggestion(String token) {
        for (Suggestion suggestion : menu.getSuggestions()) {
            if (suggestion.getName().equalsIgnoreCase(token)) {
                return suggestion;
            }
        }
        return null;
    }

    public SuggestiveMenu getMenu() {
        return this.menu;
    }

    public void setMenu(SuggestiveMenu menu) {
        this.menu = menu;
    }
}

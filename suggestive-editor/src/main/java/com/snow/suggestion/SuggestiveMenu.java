package com.snow.suggestion;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import com.snow.suggestion.databinding.SuggestionMenuBinding;
import java.util.ArrayList;
import java.util.List;

public class SuggestiveMenu extends PopupWindow {

    private Context context;
    private EditText anchor;
    private SuggestionMenuBinding binding;
    private List<Suggestion> suggestions = new ArrayList<>();
    private int minChar = 1;

    public SuggestiveMenu(Context context, EditText anchor) {
        super(context);
        this.context = context;
        this.anchor = anchor;
        this.binding = SuggestionMenuBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        setFocusable(false);
        setOutsideTouchable(true);
    }

    public void suggest(String name, String completeName, String type) {
        suggestions.add(new Suggestion(name, completeName, type));
    }

    public void suggest(Suggestion suggestion) {
        suggestions.add(suggestion);
    }

    public String getString() {
        String text = anchor.getText().toString();
        int cursorPosition = anchor.getSelectionStart();

        if (text.length() == 0 || cursorPosition == 0) {
            return null;
        }

        int wordStart = cursorPosition - 1;
        while (wordStart >= 0 && Character.isLetter(text.charAt(wordStart))) {
            wordStart--;
        }

        return text.substring(wordStart + 1, cursorPosition);
    }

    public void setString(String anterior, String str) {
        Editable text = anchor.getText();
        int cursorPosition = anchor.getSelectionStart();

        String sufixoFaltante = str.substring(anterior.length());

        text.insert(cursorPosition, sufixoFaltante);
        anchor.setSelection(cursorPosition + sufixoFaltante.length());
    }

    public int getMinChar() {
        return this.minChar;
    }

    public void setMinChar(int minChar) {
        this.minChar = minChar;
    }

    public void show() {
        binding.suggestions.removeAllViews();
        String currentWord = getString();
        if (currentWord != null && !currentWord.isEmpty()) {
            if (currentWord.length() >= minChar) {
                for (Suggestion suggestion : suggestions) {
                    if (suggestion.getName().startsWith(currentWord)) {
                        if (suggestion.getName().length() != currentWord.length()) {
                            View view = suggestion.inflate(LayoutInflater.from(context));
                            view.setOnClickListener(
                                    v -> {
                                        String str = null;
                                        if (suggestion
                                                .getName()
                                                .equals(suggestion.getCompleteName())) {
                                            str = suggestion.getCompleteName() + " ";
                                        } else {
                                            str = suggestion.getCompleteName();
                                        }
                                        setString(currentWord, str);
                                        dismiss();
                                    });
                            binding.suggestions.addView(view);
                        }
                    }
                }

                int[] location = new int[2];
                anchor.getLocationOnScreen(location);

                int cursorPosition = anchor.getSelectionStart();
                int cursorX = (int) anchor.getLayout().getPrimaryHorizontal(cursorPosition);
                int cursorY =
                        anchor.getLayout()
                                .getLineBottom(anchor.getLayout().getLineForOffset(cursorPosition));

                showAtLocation(anchor, 0, location[0] + cursorX, location[1] + cursorY);
            }
        } else {
            if (isShowing()) {
                dismiss();
            }
        }
    }

    public List<Suggestion> getSuggestions() {
        return this.suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
}

package com.snow.suggestion;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.snow.suggestion.databinding.SuggestionBinding;

import com.snow.suggestion.R;
import com.snow.suggestion.R.drawable;

public class Suggestion {

    private SuggestionBinding binding;
    public String name;
    public String completeName;
    public String type;

    public Suggestion.Outline color;

    public Suggestion(String name, String completeName, String type) {
        this.name = name;
        this.completeName = completeName;
        this.type = type;
        color = Suggestion.Outline.Default;
    }

    public View inflate(LayoutInflater inflater) {
        binding = SuggestionBinding.inflate(inflater);
        binding.name.setText(name);
        binding.completeName.setText(completeName);
        binding.type.setText(type);
        invalidate();
        return binding.getRoot();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompleteName() {
        return this.completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Suggestion.Outline getColor() {
        return this.color;
    }

    public void setColor(Suggestion.Outline color) {
        this.color = color;
        invalidate();
    }

    public void invalidate() {
        if (binding != null) {
            LinearLayout layout = binding.getRoot();
            layout.setBackgroundResource(color.getResource());
            layout.invalidate();
        }
    }

    public enum Outline {
        Default(drawable.ic_outline, Color.parseColor("#CCCCCC")),
        Blue(drawable.ic_outline_blue, Color.parseColor("#4CAFEB")),
        Green(drawable.ic_outline_green, Color.parseColor("#4CAF50")),
        Orange(drawable.ic_outline_orange, Color.parseColor("#FF9800")),
        Purple(drawable.ic_outline_purple, Color.parseColor("#8A2BE2")),
        Red(drawable.ic_outline_red, Color.parseColor("#F00000"));

        private final int id;
        private final int hexaColor;

        private Outline(int id, int hexaColor) {
            this.id = id;
            this.hexaColor = hexaColor;
        }

        public int getResource() {
            return id;
        }

        public int getHexaColor() {
            return hexaColor;
        }
    }
}

package com.snow.shadereditor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.snow.shadereditor.databinding.ActivityMainBinding;
import com.snow.suggestion.Suggestion;
import com.snow.suggestion.SuggestiveMenu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SuggestiveMenu menu = binding.shader.getMenu();
        menu.setMinChar(2);
        String jsonString = loadJSONFromAssets("GLSL.json");
        if (jsonString != null) {
            addSuggestionsFromJson(menu, jsonString);
        }
    }

    private String loadJSONFromAssets(String filename) {
        StringBuilder jsonString = new StringBuilder();
        try (InputStream is = getAssets().open(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString.toString();
    }

    private void addSuggestionsFromJson(SuggestiveMenu menu, String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            addJsonItems(menu, jsonObject.getJSONArray("keywords"), false);
            addJsonItems(menu, jsonObject.getJSONArray("functions"), true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addJsonItems(SuggestiveMenu menu, JSONArray jsonArray, boolean isFunction)
            throws JSONException {

        if (isFunction) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String name = item.getString("name");
                String type = item.getString("return_type");
                Suggestion suggestion = new Suggestion(name, name + "()", type);
                suggestion.setColor(Suggestion.Outline.Purple);
                menu.suggest(suggestion);
            }
        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String name = item.getString("name");
                String type = item.getString("type");
                if (type.equals("type")) {
                    Suggestion suggestion = new Suggestion(name, name, type);
                    suggestion.setColor(Suggestion.Outline.Default);
                    menu.suggest(suggestion);
                } else {
                    Suggestion suggestion = new Suggestion(name, name, type);
                    suggestion.setColor(Suggestion.Outline.Red);
                    menu.suggest(suggestion);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}

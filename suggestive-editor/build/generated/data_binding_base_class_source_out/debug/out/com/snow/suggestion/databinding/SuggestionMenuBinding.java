// Generated by view binder compiler. Do not edit!
package com.snow.suggestion.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.snow.suggestion.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SuggestionMenuBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final LinearLayout suggestions;

  private SuggestionMenuBinding(@NonNull ScrollView rootView, @NonNull LinearLayout suggestions) {
    this.rootView = rootView;
    this.suggestions = suggestions;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static SuggestionMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SuggestionMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.suggestion_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SuggestionMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.suggestions;
      LinearLayout suggestions = ViewBindings.findChildViewById(rootView, id);
      if (suggestions == null) {
        break missingId;
      }

      return new SuggestionMenuBinding((ScrollView) rootView, suggestions);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
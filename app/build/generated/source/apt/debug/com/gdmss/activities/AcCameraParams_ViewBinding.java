// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import com.widget.ToggleButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcCameraParams_ViewBinding<T extends AcCameraParams> implements Unbinder {
  protected T target;

  @UiThread
  public AcCameraParams_ViewBinding(T target, View source) {
    this.target = target;

    target.tgScale = Utils.findRequiredViewAsType(source, R.id.tg_scale, "field 'tgScale'", ToggleButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tgScale = null;

    this.target = null;
  }
}

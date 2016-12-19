// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcSearchLocalDevice_ViewBinding<T extends AcSearchLocalDevice> implements Unbinder {
  protected T target;

  @UiThread
  public AcSearchLocalDevice_ViewBinding(T target, View source) {
    this.target = target;

    target.lvLocaldevices = Utils.findRequiredViewAsType(source, R.id.lv_localdevices, "field 'lvLocaldevices'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.lvLocaldevices = null;

    this.target = null;
  }
}

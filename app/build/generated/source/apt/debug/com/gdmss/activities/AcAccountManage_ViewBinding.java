// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import com.widget.ToggleButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcAccountManage_ViewBinding<T extends AcAccountManage> implements Unbinder {
  protected T target;

  @UiThread
  public AcAccountManage_ViewBinding(T target, View source) {
    this.target = target;

    target.tgAutologin = Utils.findRequiredViewAsType(source, R.id.tg_autologin, "field 'tgAutologin'", ToggleButton.class);
    target.tvModifypwd = Utils.findRequiredViewAsType(source, R.id.tv_modifypwd, "field 'tvModifypwd'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tgAutologin = null;
    target.tvModifypwd = null;

    this.target = null;
  }
}

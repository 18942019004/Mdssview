// Generated code from Butter Knife. Do not modify!
package com.gdmss.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FgMore_ViewBinding<T extends FgMore> implements Unbinder {
  protected T target;

  @UiThread
  public FgMore_ViewBinding(T target, View source) {
    this.target = target;

    target.tvUsermanage = Utils.findRequiredViewAsType(source, R.id.tv_usermanage, "field 'tvUsermanage'", TextView.class);
    target.tvSetting = Utils.findRequiredViewAsType(source, R.id.tv_setting, "field 'tvSetting'", TextView.class);
    target.tvAbout = Utils.findRequiredViewAsType(source, R.id.tv_about, "field 'tvAbout'", TextView.class);
    target.btnLogout = Utils.findRequiredViewAsType(source, R.id.btn_logout, "field 'btnLogout'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvUsermanage = null;
    target.tvSetting = null;
    target.tvAbout = null;
    target.btnLogout = null;

    this.target = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

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

public class AcLogin_ViewBinding<T extends AcLogin> implements Unbinder {
  protected T target;

  @UiThread
  public AcLogin_ViewBinding(T target, View source) {
    this.target = target;

    target.tvRegister = Utils.findRequiredViewAsType(source, R.id.tv_register, "field 'tvRegister'", TextView.class);
    target.tvForget = Utils.findRequiredViewAsType(source, R.id.tv_forget, "field 'tvForget'", TextView.class);
    target.btnLocallogin = Utils.findRequiredViewAsType(source, R.id.btn_locallogin, "field 'btnLocallogin'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvRegister = null;
    target.tvForget = null;
    target.btnLocallogin = null;

    this.target = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcForgetPassword_ViewBinding<T extends AcForgetPassword> implements Unbinder {
  protected T target;

  @UiThread
  public AcForgetPassword_ViewBinding(T target, View source) {
    this.target = target;

    target.etUsername = Utils.findRequiredViewAsType(source, R.id.et_username, "field 'etUsername'", EditText.class);
    target.btnRetrive = Utils.findRequiredViewAsType(source, R.id.btn_retrive, "field 'btnRetrive'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etUsername = null;
    target.btnRetrive = null;

    this.target = null;
  }
}

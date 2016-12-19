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

public class AcRegister_ViewBinding<T extends AcRegister> implements Unbinder {
  protected T target;

  @UiThread
  public AcRegister_ViewBinding(T target, View source) {
    this.target = target;

    target.etUsername = Utils.findRequiredViewAsType(source, R.id.et_username, "field 'etUsername'", EditText.class);
    target.etPwd = Utils.findRequiredViewAsType(source, R.id.et_pwd, "field 'etPwd'", EditText.class);
    target.etEnsurepwd = Utils.findRequiredViewAsType(source, R.id.et_ensurepwd, "field 'etEnsurepwd'", EditText.class);
    target.etEmail = Utils.findRequiredViewAsType(source, R.id.et_email, "field 'etEmail'", EditText.class);
    target.btnRegister = Utils.findRequiredViewAsType(source, R.id.btn_register, "field 'btnRegister'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etUsername = null;
    target.etPwd = null;
    target.etEnsurepwd = null;
    target.etEmail = null;
    target.btnRegister = null;

    this.target = null;
  }
}

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

public class AcModifyDevicePassword_ViewBinding<T extends AcModifyDevicePassword> implements Unbinder {
  protected T target;

  @UiThread
  public AcModifyDevicePassword_ViewBinding(T target, View source) {
    this.target = target;

    target.etPwd = Utils.findRequiredViewAsType(source, R.id.et_pwd, "field 'etPwd'", EditText.class);
    target.etNewpwd = Utils.findRequiredViewAsType(source, R.id.et_newpwd, "field 'etNewpwd'", EditText.class);
    target.etEnsurepwd = Utils.findRequiredViewAsType(source, R.id.et_ensurepwd, "field 'etEnsurepwd'", EditText.class);
    target.btnSure = Utils.findRequiredViewAsType(source, R.id.btn_sure, "field 'btnSure'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etPwd = null;
    target.etNewpwd = null;
    target.etEnsurepwd = null;
    target.btnSure = null;

    this.target = null;
  }
}

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

public class AcAddIP_ViewBinding<T extends AcAddIP> implements Unbinder {
  protected T target;

  @UiThread
  public AcAddIP_ViewBinding(T target, View source) {
    this.target = target;

    target.leftBtn = Utils.findRequiredViewAsType(source, R.id.leftBtn, "field 'leftBtn'", Button.class);
    target.btnSave = Utils.findRequiredViewAsType(source, R.id.btn_save, "field 'btnSave'", Button.class);
    target.etDeviceName = Utils.findRequiredViewAsType(source, R.id.et_deviceName, "field 'etDeviceName'", EditText.class);
    target.etIpaddress = Utils.findRequiredViewAsType(source, R.id.et_ipaddress, "field 'etIpaddress'", EditText.class);
    target.etUsername = Utils.findRequiredViewAsType(source, R.id.et_username, "field 'etUsername'", EditText.class);
    target.etPwd = Utils.findRequiredViewAsType(source, R.id.et_pwd, "field 'etPwd'", EditText.class);
    target.etPort = Utils.findRequiredViewAsType(source, R.id.et_port, "field 'etPort'", EditText.class);
    target.btnSearch = Utils.findRequiredViewAsType(source, R.id.btn_search, "field 'btnSearch'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.leftBtn = null;
    target.btnSave = null;
    target.etDeviceName = null;
    target.etIpaddress = null;
    target.etUsername = null;
    target.etPwd = null;
    target.etPort = null;
    target.btnSearch = null;

    this.target = null;
  }
}

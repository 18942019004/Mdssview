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

public class AcModifyName_ViewBinding<T extends AcModifyName> implements Unbinder {
  protected T target;

  @UiThread
  public AcModifyName_ViewBinding(T target, View source) {
    this.target = target;

    target.etName = Utils.findRequiredViewAsType(source, R.id.et_name, "field 'etName'", EditText.class);
    target.btnSure = Utils.findRequiredViewAsType(source, R.id.btn_sure, "field 'btnSure'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etName = null;
    target.btnSure = null;

    this.target = null;
  }
}

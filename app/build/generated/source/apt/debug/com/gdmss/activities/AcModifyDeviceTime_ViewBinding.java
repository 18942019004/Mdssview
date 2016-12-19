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

public class AcModifyDeviceTime_ViewBinding<T extends AcModifyDeviceTime> implements Unbinder {
  protected T target;

  @UiThread
  public AcModifyDeviceTime_ViewBinding(T target, View source) {
    this.target = target;

    target.tvPhoneTime = Utils.findRequiredViewAsType(source, R.id.tv_phoneTime, "field 'tvPhoneTime'", TextView.class);
    target.tvDevTime = Utils.findRequiredViewAsType(source, R.id.tv_devTime, "field 'tvDevTime'", TextView.class);
    target.btnSure = Utils.findRequiredViewAsType(source, R.id.btn_sure, "field 'btnSure'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvPhoneTime = null;
    target.tvDevTime = null;
    target.btnSure = null;

    this.target = null;
  }
}

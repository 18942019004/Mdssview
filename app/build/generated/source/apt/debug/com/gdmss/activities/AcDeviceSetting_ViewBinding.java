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

public class AcDeviceSetting_ViewBinding<T extends AcDeviceSetting> implements Unbinder {
  protected T target;

  @UiThread
  public AcDeviceSetting_ViewBinding(T target, View source) {
    this.target = target;

    target.tvModifyparam = Utils.findRequiredViewAsType(source, R.id.tv_modifyparam, "field 'tvModifyparam'", TextView.class);
    target.tvModifypwd = Utils.findRequiredViewAsType(source, R.id.tv_modifypwd, "field 'tvModifypwd'", TextView.class);
    target.tvSynctime = Utils.findRequiredViewAsType(source, R.id.tv_synctime, "field 'tvSynctime'", TextView.class);
    target.btnDelete = Utils.findRequiredViewAsType(source, R.id.btn_delete, "field 'btnDelete'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvModifyparam = null;
    target.tvModifypwd = null;
    target.tvSynctime = null;
    target.btnDelete = null;

    this.target = null;
  }
}

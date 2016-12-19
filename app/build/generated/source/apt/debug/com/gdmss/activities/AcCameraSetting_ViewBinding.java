// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcCameraSetting_ViewBinding<T extends AcCameraSetting> implements Unbinder {
  protected T target;

  @UiThread
  public AcCameraSetting_ViewBinding(T target, View source) {
    this.target = target;

    target.tvModifyname = Utils.findRequiredViewAsType(source, R.id.tv_modifyname, "field 'tvModifyname'", TextView.class);
    target.tvWifisetting = Utils.findRequiredViewAsType(source, R.id.tv_wifisetting, "field 'tvWifisetting'", TextView.class);
    target.tvAlarm = Utils.findRequiredViewAsType(source, R.id.tv_alarm, "field 'tvAlarm'", TextView.class);
    target.tvCameraparam = Utils.findRequiredViewAsType(source, R.id.tv_cameraparam, "field 'tvCameraparam'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvModifyname = null;
    target.tvWifisetting = null;
    target.tvAlarm = null;
    target.tvCameraparam = null;

    this.target = null;
  }
}

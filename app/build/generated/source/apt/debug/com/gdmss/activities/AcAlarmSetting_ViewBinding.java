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
import com.widget.ToggleButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcAlarmSetting_ViewBinding<T extends AcAlarmSetting> implements Unbinder {
  protected T target;

  @UiThread
  public AcAlarmSetting_ViewBinding(T target, View source) {
    this.target = target;

    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    target.tgPush = Utils.findRequiredViewAsType(source, R.id.tg_push, "field 'tgPush'", ToggleButton.class);
    target.tgAlarmtoggle = Utils.findRequiredViewAsType(source, R.id.tg_alarmtoggle, "field 'tgAlarmtoggle'", ToggleButton.class);
    target.tvSensor = Utils.findRequiredViewAsType(source, R.id.tv_sensor, "field 'tvSensor'", TextView.class);
    target.btnSave = Utils.findRequiredViewAsType(source, R.id.btn_save, "field 'btnSave'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvName = null;
    target.tgPush = null;
    target.tgAlarmtoggle = null;
    target.tvSensor = null;
    target.btnSave = null;

    this.target = null;
  }
}

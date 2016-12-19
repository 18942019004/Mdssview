// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import com.widget.TimePickerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcChoosePlayBackDevice_ViewBinding<T extends AcChoosePlayBackDevice> implements Unbinder {
  protected T target;

  @UiThread
  public AcChoosePlayBackDevice_ViewBinding(T target, View source) {
    this.target = target;

    target.btnAddToPlay = Utils.findRequiredViewAsType(source, R.id.btn_addToPlay, "field 'btnAddToPlay'", Button.class);
    target.elvDevicelist = Utils.findRequiredViewAsType(source, R.id.elv_devicelist, "field 'elvDevicelist'", ExpandableListView.class);
    target.cboxTimeStart = Utils.findRequiredViewAsType(source, R.id.cbox_time_start, "field 'cboxTimeStart'", CheckBox.class);
    target.cboxTimeEnd = Utils.findRequiredViewAsType(source, R.id.cbox_time_end, "field 'cboxTimeEnd'", CheckBox.class);
    target.timePicker = Utils.findRequiredViewAsType(source, R.id.time_picker, "field 'timePicker'", TimePickerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.btnAddToPlay = null;
    target.elvDevicelist = null;
    target.cboxTimeStart = null;
    target.cboxTimeEnd = null;
    target.timePicker = null;

    this.target = null;
  }
}

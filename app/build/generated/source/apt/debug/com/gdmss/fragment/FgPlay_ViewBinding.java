// Generated code from Butter Knife. Do not modify!
package com.gdmss.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import com.widget.MyHorizontalScrollView;
import com.widget.MyPlayer;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FgPlay_ViewBinding<T extends FgPlay> implements Unbinder {
  protected T target;

  @UiThread
  public FgPlay_ViewBinding(T target, View source) {
    this.target = target;

    target.ibtnDevice = Utils.findRequiredViewAsType(source, R.id.ibtn_device, "field 'ibtnDevice'", Button.class);
    target.btnDivision = Utils.findRequiredViewAsType(source, R.id.btn_division, "field 'btnDivision'", Button.class);
    target.btnSnap = Utils.findRequiredViewAsType(source, R.id.btn_snap, "field 'btnSnap'", Button.class);
    target.btnRecord = Utils.findRequiredViewAsType(source, R.id.btn_record, "field 'btnRecord'", Button.class);
    target.btnStream = Utils.findRequiredViewAsType(source, R.id.btn_stream, "field 'btnStream'", Button.class);
    target.btnReplay = Utils.findRequiredViewAsType(source, R.id.btn_replay, "field 'btnReplay'", Button.class);
    target.cbtnSound = Utils.findRequiredViewAsType(source, R.id.cbtn_sound, "field 'cbtnSound'", CheckBox.class);
    target.btnShowptz = Utils.findRequiredViewAsType(source, R.id.btn_showptz, "field 'btnShowptz'", Button.class);
    target.btnZoom = Utils.findRequiredViewAsType(source, R.id.btn_zoom, "field 'btnZoom'", Button.class);
    target.btnLeft = Utils.findRequiredViewAsType(source, R.id.btn_left, "field 'btnLeft'", Button.class);
    target.btnApertureAdd = Utils.findRequiredViewAsType(source, R.id.btn_aperture_add, "field 'btnApertureAdd'", Button.class);
    target.btnApertureSub = Utils.findRequiredViewAsType(source, R.id.btn_aperture_reduce, "field 'btnApertureSub'", Button.class);
    target.btnFocusAdd = Utils.findRequiredViewAsType(source, R.id.btn_focus_add, "field 'btnFocusAdd'", Button.class);
    target.btnFocusreduce = Utils.findRequiredViewAsType(source, R.id.btn_focus_reduce, "field 'btnFocusreduce'", Button.class);
    target.btnDown = Utils.findRequiredViewAsType(source, R.id.btn_down, "field 'btnDown'", Button.class);
    target.btnUp = Utils.findRequiredViewAsType(source, R.id.btn_up, "field 'btnUp'", Button.class);
    target.btnRight = Utils.findRequiredViewAsType(source, R.id.btn_right, "field 'btnRight'", Button.class);
    target.btnLeftUp = Utils.findRequiredViewAsType(source, R.id.btn_left_up, "field 'btnLeftUp'", Button.class);
    target.btnLeftDown = Utils.findRequiredViewAsType(source, R.id.btn_left_down, "field 'btnLeftDown'", Button.class);
    target.btnRightUp = Utils.findRequiredViewAsType(source, R.id.btn_right_up, "field 'btnRightUp'", Button.class);
    target.btnRightDown = Utils.findRequiredViewAsType(source, R.id.btn_right_down, "field 'btnRightDown'", Button.class);
    target.rlControlBtns = Utils.findRequiredViewAsType(source, R.id.rl_controlBtns, "field 'rlControlBtns'", RelativeLayout.class);
    target.bottomMenu = Utils.findRequiredViewAsType(source, R.id.bottom_menu, "field 'bottomMenu'", MyHorizontalScrollView.class);
    target.titlebar = Utils.findRequiredViewAsType(source, R.id.titlebar, "field 'titlebar'", RelativeLayout.class);
    target.myPlayer = Utils.findRequiredViewAsType(source, R.id.pagerPlayer, "field 'myPlayer'", MyPlayer.class);
    target.indicator = Utils.findRequiredViewAsType(source, R.id.tv_indicator, "field 'indicator'", TextView.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'tvTitle'", TextView.class);
    target.btnIndicatorLeft = Utils.findRequiredViewAsType(source, R.id.btn_indicatorLeft, "field 'btnIndicatorLeft'", TextView.class);
    target.btnIndicatorRight = Utils.findRequiredViewAsType(source, R.id.btn_indicatorRight, "field 'btnIndicatorRight'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ibtnDevice = null;
    target.btnDivision = null;
    target.btnSnap = null;
    target.btnRecord = null;
    target.btnStream = null;
    target.btnReplay = null;
    target.cbtnSound = null;
    target.btnShowptz = null;
    target.btnZoom = null;
    target.btnLeft = null;
    target.btnApertureAdd = null;
    target.btnApertureSub = null;
    target.btnFocusAdd = null;
    target.btnFocusreduce = null;
    target.btnDown = null;
    target.btnUp = null;
    target.btnRight = null;
    target.btnLeftUp = null;
    target.btnLeftDown = null;
    target.btnRightUp = null;
    target.btnRightDown = null;
    target.rlControlBtns = null;
    target.bottomMenu = null;
    target.titlebar = null;
    target.myPlayer = null;
    target.indicator = null;
    target.tvTitle = null;
    target.btnIndicatorLeft = null;
    target.btnIndicatorRight = null;

    this.target = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.gdmss.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gdmss.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AcLocalVideoPlayer_ViewBinding<T extends AcLocalVideoPlayer> implements Unbinder {
  protected T target;

  @UiThread
  public AcLocalVideoPlayer_ViewBinding(T target, View source) {
    this.target = target;

    target.seekBar = Utils.findRequiredViewAsType(source, R.id.seekBar, "field 'seekBar'", SeekBar.class);
    target.ibtnPausePlay = Utils.findRequiredViewAsType(source, R.id.ibtn_pause_play, "field 'ibtnPausePlay'", ImageButton.class);
    target.ibtnStop = Utils.findRequiredViewAsType(source, R.id.ibtn_stop, "field 'ibtnStop'", ImageButton.class);
    target.ibtnSnap = Utils.findRequiredViewAsType(source, R.id.ibtn_snap, "field 'ibtnSnap'", ImageButton.class);
    target.img = Utils.findRequiredViewAsType(source, R.id.img, "field 'img'", ImageView.class);
    target.tvCurrent = Utils.findRequiredViewAsType(source, R.id.tv_current, "field 'tvCurrent'", TextView.class);
    target.tvTotal = Utils.findRequiredViewAsType(source, R.id.tv_total, "field 'tvTotal'", TextView.class);
    target.rlTitle = Utils.findRequiredViewAsType(source, R.id.rl_title, "field 'rlTitle'", RelativeLayout.class);
    target.llBottom = Utils.findRequiredViewAsType(source, R.id.ll_bottom, "field 'llBottom'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.seekBar = null;
    target.ibtnPausePlay = null;
    target.ibtnStop = null;
    target.ibtnSnap = null;
    target.img = null;
    target.tvCurrent = null;
    target.tvTotal = null;
    target.rlTitle = null;
    target.llBottom = null;

    this.target = null;
  }
}

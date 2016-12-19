// Generated code from Butter Knife. Do not modify!
package com.gdmss.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gdmss.R;
import com.widget.PlayBackLayout;
import com.widget.PlayLayout;
import com.widget.SeekTimeBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FgPlayBack_ViewBinding<T extends FgPlayBack> implements Unbinder {
  protected T target;

  private View view2131558656;

  private View view2131558657;

  private View view2131558681;

  private View view2131558659;

  private View view2131558660;

  @UiThread
  public FgPlayBack_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.ibtnDevice = Utils.findRequiredViewAsType(source, R.id.ibtn_device, "field 'ibtnDevice'", Button.class);
    view = Utils.findRequiredView(source, R.id.btn_snap, "field 'btnSnap' and method 'onClick'");
    target.btnSnap = Utils.castView(view, R.id.btn_snap, "field 'btnSnap'", Button.class);
    view2131558656 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_record, "field 'btnRecord' and method 'onClick'");
    target.btnRecord = Utils.castView(view, R.id.btn_record, "field 'btnRecord'", Button.class);
    view2131558657 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_play, "field 'btnPlay' and method 'onClick'");
    target.btnPlay = Utils.castView(view, R.id.btn_play, "field 'btnPlay'", Button.class);
    view2131558681 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_replay, "field 'btnReplay' and method 'onClick'");
    target.btnReplay = Utils.castView(view, R.id.btn_replay, "field 'btnReplay'", Button.class);
    view2131558659 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.cbtn_sound, "field 'cbtnSound' and method 'onClick'");
    target.cbtnSound = Utils.castView(view, R.id.cbtn_sound, "field 'cbtnSound'", CheckBox.class);
    view2131558660 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.bottomMenu = Utils.findRequiredViewAsType(source, R.id.bottom_menu, "field 'bottomMenu'", HorizontalScrollView.class);
    target.player = Utils.findRequiredViewAsType(source, R.id.player, "field 'player'", PlayLayout.class);
    target.player1 = Utils.findRequiredViewAsType(source, R.id.player1, "field 'player1'", PlayBackLayout.class);
    target.seekBar = Utils.findRequiredViewAsType(source, R.id.seekBar, "field 'seekBar'", SeekTimeBar.class);
    target.btnMenu = Utils.findRequiredViewAsType(source, R.id.btn_menu, "field 'btnMenu'", Button.class);
    target.titlebar = Utils.findRequiredViewAsType(source, R.id.titlebar, "field 'titlebar'", RelativeLayout.class);
    target.layoutFunctions = Utils.findRequiredViewAsType(source, R.id.layout_functions, "field 'layoutFunctions'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ibtnDevice = null;
    target.btnSnap = null;
    target.btnRecord = null;
    target.btnPlay = null;
    target.btnReplay = null;
    target.cbtnSound = null;
    target.bottomMenu = null;
    target.player = null;
    target.player1 = null;
    target.seekBar = null;
    target.btnMenu = null;
    target.titlebar = null;
    target.layoutFunctions = null;

    view2131558656.setOnClickListener(null);
    view2131558656 = null;
    view2131558657.setOnClickListener(null);
    view2131558657 = null;
    view2131558681.setOnClickListener(null);
    view2131558681 = null;
    view2131558659.setOnClickListener(null);
    view2131558659 = null;
    view2131558660.setOnClickListener(null);
    view2131558660 = null;

    this.target = null;
  }
}

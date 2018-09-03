/*
 * ***************************************************
 * Copyright (c) 2018. Kevin.Suo. All Rights Reserved.
 * Email: jinghao530@gmail.com
 * ***************************************************
 */

package com.kevinsuo.kstabpager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Create by Kevin Suo on 2018/8/31
 */
public class TabIndicator extends View {

  private int indicatorColor = Color.WHITE;
  private int indicatorRadius;
  private int indicatorWidth;
  private int indicatorHeight;
  private float start;
  private Paint paint;

  public TabIndicator(Context context) {
    this(context, null);
  }

  public TabIndicator(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    // 设置宽度铺满父控件

    indicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
        getResources().getDisplayMetrics());
    indicatorRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5,
        getResources().getDisplayMetrics());

    paint = new Paint();
    paint.setColor(indicatorColor);
    paint.setAntiAlias(true);
  }

  public void setIndicatorColor(int indicatorColor) {
    this.indicatorColor = indicatorColor;
    if (paint != null) {
      paint.setColor(indicatorColor);
    }
  }

  public void setIndicatorWidth(int indicatorWidth) {
    if (indicatorWidth > 0) {
      this.indicatorWidth = indicatorWidth;
    }
  }

  public void setIndicatorHeight(int indicatorHeight) {
    if (indicatorHeight > 0) {
      this.indicatorHeight = indicatorHeight;
    }
  }

  public void setCurrentMasterView(final View master) {
    if (master != null) {
      master.post(new Runnable() {
        @Override public void run() {
          computeLocation(master);
        }
      });
    }
  }

  private void computeLocation(View master) {
    // 计算下次显示的 指示器 位置
    int left = master.getLeft();
    int width = master.getWidth();
    float nextStart = left + (width - indicatorWidth) / 2;
    smoothToNext(nextStart);
  }

  public void setStart(float start) {
    this.start = start;
    postInvalidate();
  }

  private void smoothToNext(final float nextStart) {
    if (start == 0) {
      start = nextStart;
      invalidate();
    } else {
      ObjectAnimator animator = ObjectAnimator.ofFloat(this, "start", this.start, nextStart);
      animator.setDuration(150L);
      animator.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);
          start = nextStart;
          postInvalidate();
        }
      });
      animator.start();
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    indicatorHeight = getMeasuredHeight();
    indicatorRadius = getMeasuredHeight() / 2;
  }

  @Override protected void onDraw(Canvas canvas) {

    canvas.save();
    canvas.drawColor(Color.TRANSPARENT);
    canvas.drawRoundRect(start, 0, indicatorWidth + start, indicatorHeight, indicatorRadius,
        indicatorRadius, paint);

    canvas.restore();
  }
}

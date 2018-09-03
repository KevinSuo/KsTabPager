/*
 * ***************************************************
 * Copyright (c) 2018. Kevin.Suo. All Rights Reserved.
 * Email: jinghao530@gmail.com
 * ***************************************************
 */

package com.kevinsuo.kstabpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Kevin Suo on 2018/8/31
 */
public class TabPager extends FrameLayout
    implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener,
    CompoundButton.OnCheckedChangeListener {

  private TabIndicator indicator;
  private RadioGroup titleContainer;
  private ViewPager itemViewPagers;
  private TabPagerAdapter adapter;

  private int titleItemColor;
  private int titleItemSize;

  public TabPager(@NonNull Context context) {
    this(context, null);
  }

  public TabPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TabPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.layout_tab_pager, this);

    indicator = findViewById(R.id.tab_indicator);
    titleContainer = findViewById(R.id.title_container);
    View titleBar = findViewById(R.id.title_bar);
    itemViewPagers = findViewById(R.id.item_pagers);

    if (context instanceof FragmentActivity) {
      FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
      adapter = new TabPagerAdapter(fragmentManager, context);
      itemViewPagers.setAdapter(adapter);

      itemViewPagers.addOnPageChangeListener(this);
    }

    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabPager);

      boolean indicatorDisplay =
          typedArray.getBoolean(R.styleable.TabPager_indicator_display, true);
      int indicatorWidth =
          typedArray.getDimensionPixelOffset(R.styleable.TabPager_indicator_width, 0);
      int indicatorHeight =
          typedArray.getDimensionPixelOffset(R.styleable.TabPager_indicator_height, 0);
      int indicatorColor = typedArray.getColor(R.styleable.TabPager_indicator_color, Color.WHITE);
      indicator.setVisibility(indicatorDisplay ? View.VISIBLE : View.INVISIBLE);
      if (indicatorDisplay) {
        indicator.setIndicatorWidth(indicatorWidth);
        indicator.setIndicatorHeight(indicatorHeight);
        indicator.setIndicatorColor(indicatorColor);
      }

      int titleBarBgColor = typedArray.getColor(R.styleable.TabPager_title_bar_background, 0);
      if (titleBarBgColor > 0) {
        titleBar.setBackgroundColor(titleBarBgColor);
      }

      titleItemSize = typedArray.getDimensionPixelOffset(R.styleable.TabPager_title_text_size, 0);
      titleItemColor = typedArray.getColor(R.styleable.TabPager_title_text_color, 0);

      typedArray.recycle();
    }
  }

  @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
    Log.d("TabPager", "onCheckedChanged " + checkedId);
    //View child = group.getChildAt(checkedId - 1);
    //if (child != null) {
    //  indicator.setCurrentMasterView(child);
    //}
    //itemViewPagers.setCurrentItem(checkedId - 1);
  }

  @Override public void onPageScrolled(int i, float v, int i1) {

  }

  @Override public void onPageSelected(int i) {
    RadioButton child = (RadioButton) titleContainer.getChildAt(i);
    if (child != null) {
      child.setChecked(true);
    }
  }

  @Override public void onPageScrollStateChanged(int i) {

  }

  public void setItems(TabPagerItemBuilder builder) {
    initTitleItems(builder);
    if (adapter != null) {
      adapter.update(builder.build());
    }
  }

  private void initTitleItems(final TabPagerItemBuilder builder) {
    if (builder != null) {
      titleContainer.removeAllViews();
      for (TabPagerItem item : builder.build()) {
        RadioButton titleItem = (RadioButton) LayoutInflater.from(getContext())
            .inflate(R.layout.layout_tab_pager_title_item, titleContainer, false);
        titleItem.setText(item.getTitle());
        if (titleItemSize > 0) {
          titleItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleItemSize);
        }
        if (titleItemColor > 0) {
          titleItem.setTextColor(titleItemColor);
        }
        titleItem.setOnCheckedChangeListener(this);
        titleContainer.addView(titleItem);
      }

      if (builder.currentIndex < builder.build().size()) {
        titleContainer.post(new Runnable() {
          @Override public void run() {
            ((RadioButton) titleContainer.getChildAt(builder.currentIndex)).setChecked(true);
          }
        });
      }
    }
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
      int index = titleContainer.indexOfChild(buttonView);
      indicator.setCurrentMasterView(buttonView);
      itemViewPagers.setCurrentItem(index, false);
    }
  }

  public static class TabPagerItemBuilder {
    private List<TabPagerItem> items = new ArrayList<>();
    private int currentIndex = 0;

    public TabPagerItemBuilder addItem(TabPagerItem item) {
      this.items.add(item);
      return this;
    }

    public TabPagerItemBuilder setCurrentIndex(int index) {
      this.currentIndex = index;
      return this;
    }

    public List<TabPagerItem> build() {
      return items;
    }
  }

  public static class TabPagerItem {
    private String title;
    private Class pageClass;

    private TabPagerItem(String title, Class<Fragment> pageClass) {
      this.title = title;
      this.pageClass = pageClass;
    }

    public String getTitle() {
      return title;
    }

    public Class getPageClass() {
      return pageClass;
    }

    public static TabPagerItem build(String title, Class pageClass) {
      return new TabPagerItem(title, pageClass);
    }
  }

  private class TabPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<TabPagerItem> items = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm, Context mContext) {
      super(fm);
      this.mContext = mContext;
    }

    public void update(List<TabPagerItem> items) {
      this.items.clear();
      if (items != null && items.size() > 0) {
        this.items.addAll(items);
        notifyDataSetChanged();
      }
    }

    @Override public Fragment getItem(int i) {
      if (i < items.size()) {
        String pName = items.get(i).getPageClass().getName();
        return Fragment.instantiate(mContext, pName);
      }
      return Fragment.instantiate(mContext, Fragment.class.getName());
    }

    @Override public int getCount() {
      return items.size();
    }
  }
}

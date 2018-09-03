package com.kevinsuo.kstabpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TabPager tabPager = findViewById(R.id.tab_pager);
    tabPager.setItems(new TabPager.TabPagerItemBuilder().addItem(
        TabPager.TabPagerItem.build("聊天", TabFragment.class))
        .addItem(TabPager.TabPagerItem.build("联系人", TabFragment.class))
        .addItem(TabPager.TabPagerItem.build("群组", TabFragment.class)).setCurrentIndex(1));
  }
}

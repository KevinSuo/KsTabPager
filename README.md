## KsTabPager
> 项目中用到，其他开源的感觉太重了，就自己写了个，封装了ViewPager

## 用法



### 添加依赖
[ ![Download](https://api.bintray.com/packages/kevinsuo/maven/kstabpager/images/download.svg) ](https://bintray.com/kevinsuo/maven/kstabpager/_latestVersion) 

```groovy
	dependencies {
	
	   implementation 'com.kevinsuo:kstabpager:0.0.2'
	        
	}
```
           
### 在代码中使用

```xml
<com.kevinsuo.kstabpager.TabPager
      android:id="@+id/tab_pager"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:indicator_color="@color/white"
      app:indicator_display="true"
      app:indicator_width="10dp"
      app:title_bar_background="@color/tab_pager_header_background"
      app:title_text_color="@color/white"
      app:title_text_size="16sp"
      />
``` 

```java
 TabPager tabPager = findViewById(R.id.tab_pager);

 tabPager.useChildFragmentManager(fragment); // 如果控件在fragment中使用 调用此方法切换FragmentManager

 tabPager.setItems(new TabPager.TabPagerItemBuilder()
              .addItem(TabPager.TabPagerItem.build("聊天", TabFragment.class))
              .addItem(TabPager.TabPagerItem.build("联系人", TabFragment.class))
              .addItem(TabPager.TabPagerItem.build("群组", TabFragment.class)));
```

![avatar](screenshot/Screenshot_20180903-113323.jpg)


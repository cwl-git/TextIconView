# TextIconView
多行文本默认放置自适应图标，可动态设置图标的宽高、文本颜色、文本尺寸

## 布局中使用
```
 <com.cwl.texticon.TextIconView
        android:id="@+id/text_icon_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="5dp"
        android:layout_margin="5dp"
        app:text="不设置图片宽高，则图片默认为文本的单行高度，并保持原比例"
        app:icon="@mipmap/ic_launcher"
        app:icon_margin_left="5dp"
        app:text_raw_distance="10dp"
        app:text_color="@color/purple_700"
        app:text_size="30dp" />
 ```

## 代码中动态改变
```
text_icon_view_bg.apply {
            setOnLongClickListener {
                val textBgDrawable = resources.getDrawable(R.drawable.shape_conner_5_purple, null)
                val iconDrawable = resources.getDrawable(R.drawable.ic_play,null)
                //更新文本内容
                mLeftText = "新的文本"
                //更新文本背景
                mLeftTextBg = textBgDrawable
                //更新文本字体颜色
                mLeftTextColor = Color.WHITE
                //更新文本字体大小
                mLeftTextSize = resources.getDimension(R.dimen.common_text_size)
                //更新文本与末尾图标的间距
                mIconMarginLeft = resources.getDimension(R.dimen.common_text_size)
                //更新文本末尾图标
                mIcon = iconDrawable
                return@setOnLongClickListener true
            }
 }
```

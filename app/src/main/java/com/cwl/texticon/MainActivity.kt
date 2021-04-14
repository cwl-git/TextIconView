package com.cwl.texticon

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //动态修改TextIconView的各属性
        text_icon_view_bg.apply {
            setOnLongClickListener {
                val textBgDrawable = resources.getDrawable(R.drawable.shape_conner_5_purple, null)
                val iconDrawable = resources.getDrawable(R.drawable.ic_play,null)
                mLeftText = "新的文本"
                mLeftTextBg = textBgDrawable
                mLeftTextColor = Color.WHITE
                mLeftTextSize = resources.getDimension(R.dimen.common_text_size)
                mIconMarginLeft = resources.getDimension(R.dimen.common_text_size)
                mIcon = iconDrawable
                return@setOnLongClickListener true
            }
        }
    }
}
package com.cwl.texticon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.min


/**
 * 图文混排View
 *
 */
class TextIconView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    //图片左边文本的背景 默认为空
    var mLeftTextBg: Drawable? = null
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //图片左边文本，默认""
    var mLeftText = ""
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //图片左边文本的颜色，默认红色
    var mLeftTextColor = Color.RED
        set(value) {
            if (field == value) return
            field = value
            invalidate()
        }

    //图片左边文本的大小，默认hx_px_12
    var mLeftTextSize = resources.getDimension(R.dimen.common_text_size)
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //左边文本绘制画笔
    private var mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    //Icon 默认为空
    var mIcon: Drawable? = null
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //Icon宽
    var mIconWidth = -1f
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //Icon高
    var mIconHeight = -1f
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //最行间距
    var mLineSpacing = resources.getDimension(R.dimen.common_line_width)
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //行高
    private var mRowHeight = 0

    //Icon画笔
    private var mIconPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //Icon左外边距,默认common_line_width
    var mIconMarginLeft = resources.getDimension(R.dimen.common_line_width)
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    //文本自动换行布局
    private lateinit var staticLayoutLeft: StaticLayout

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextIconView)
        mLeftText = typedArray.getString(R.styleable.TextIconView_text) ?: mLeftText
        mLeftTextColor =
            typedArray.getColor(R.styleable.TextIconView_text_color, mLeftTextColor)
        mLeftTextSize =
            typedArray.getDimension(R.styleable.TextIconView_text_size, mLeftTextSize)
        mLeftTextBg = typedArray.getDrawable(R.styleable.TextIconView_text_background)
        mIcon = typedArray.getDrawable(R.styleable.TextIconView_icon)
        mIconWidth = typedArray.getDimension(R.styleable.TextIconView_icon_width, mIconWidth)
        mIconHeight = typedArray.getDimension(R.styleable.TextIconView_icon_height, mIconHeight)
        mLineSpacing =
            typedArray.getDimension(R.styleable.TextIconView_text_raw_distance, mLineSpacing)
        mIconMarginLeft =
            typedArray.getDimension(R.styleable.TextIconView_icon_margin_left, mIconMarginLeft)
        typedArray.recycle()
    }

    private fun setPaint() {
        mTextPaint.reset()
        mTextPaint.color = mLeftTextColor
        mTextPaint.textSize = mLeftTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (mLeftText.isBlank() && mIcon == null) {
            widthSize = 0
            heightSize = 0
        } else {
            setPaint()
            //左边文本的宽度
            val leftTextWidth = mTextPaint.measureText(mLeftText).toInt()
            //设置图片的默认宽高
            if (mIconWidth < 0f) mIconWidth = mLeftTextSize
            if (mIconHeight < 0f) mIconHeight = mLeftTextSize
            //总宽度
            val sum: Int =
                (paddingLeft + paddingRight + leftTextWidth + mIconWidth + mIconMarginLeft).toInt()
            widthSize = min(sum, widthSize)
            val realLeftTextWidth = min(leftTextWidth, widthSize - paddingLeft - paddingRight)
            //文本自动换行布局
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                staticLayoutLeft = StaticLayout.Builder
                    .obtain(mLeftText, 0, mLeftText.length, mTextPaint, realLeftTextWidth)
                    .setLineSpacing(mLineSpacing, 1f)
                    .build()
            }
            //纯文本的行高
            mRowHeight = staticLayoutLeft.height / staticLayoutLeft.lineCount
            heightSize = min(heightSize, staticLayoutLeft.height + paddingTop + paddingBottom)
            mIcon?.let {
                val scale = min(mRowHeight.toFloat(), mIconHeight) / mIconHeight
                mIconHeight = min(mRowHeight.toFloat(), mIconHeight)
                mIconWidth *= scale
                val mIconLeft =
                    staticLayoutLeft.getLineWidth(staticLayoutLeft.lineCount - 1) + mIconMarginLeft
                if (widthSize - mIconLeft - mIconWidth < 0) heightSize += mRowHeight
            }
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制文本的背景
        val w = staticLayoutLeft.width + paddingLeft + paddingRight
        val h = staticLayoutLeft.height + paddingBottom + paddingTop
        val b = mLeftTextBg?.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        if (b != null) {
            canvas?.drawBitmap(b, 0f, 0f, mIconPaint)
        }
        canvas?.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        //绘制文字
        staticLayoutLeft.draw(canvas)
        //绘制图片
        mIcon?.let {
            //Icon的Top Left
            var mIconLeft =
                staticLayoutLeft.getLineWidth(staticLayoutLeft.lineCount - 1) + mIconMarginLeft
            val index: Int
            if (width - mIconLeft - mIconWidth > 0) {
                //剩余宽度足以放置图片
                index = staticLayoutLeft.lineCount - 1
            } else {
                //剩余宽度不足以放置图片
                index = staticLayoutLeft.lineCount
                mIconLeft = paddingLeft.toFloat()
            }
            val top =
                if (index >= 0) mRowHeight * index + (mRowHeight - mIconHeight) * 0.5f else (mRowHeight - mIconHeight) * 0.5f
            val bitmap: Bitmap =
                mIcon?.toBitmap(mIconWidth.toInt(), mIconHeight.toInt(), Bitmap.Config.ARGB_8888)
                    ?: return
            canvas?.drawBitmap(bitmap, mIconLeft, top, mIconPaint)
        }
    }
}
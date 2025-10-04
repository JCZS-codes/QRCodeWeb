package uitool

import android.R
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import tool.getShare
import utils.getColorByBuildVersion
import utils.logi




/**
 * 開啟全屏模式
 */
fun hideSystemUI(view: View) {
    // Set the IMMERSIVE flag.
    // Set the content to appear under the system bars so that the content
    // doesn't resize when the system bars hide and show.
    //開啟全屏模式
    view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

            or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
}
/**朝上方動畫打開隱藏Layout的方法，使用動態設定ConstraintSet。
 * @author 謝蝦米
 * @Date 2021/08/22
 * outLayout 控制所有內部Constraint的最外層Layout
 * @param openBoolean 開啟狀態，關閉時應傳入true，並以傳入之內容指定回該變數，例如open = openLayout(open,...)
 * @param hideLayout 隱藏在後面，要往下滑出的Layout
 * @param anchorLayout 顯示在上面，用於定位hideLayout的Layout
 * */
fun ConstraintLayout.popUpLayout(openBoolean: Boolean, hideLayout: ConstraintLayout, anchorLayout: ConstraintLayout): Boolean {

    val outLayout = this
    val duration = this.context.getShare().getAnimationDuration()
    TransitionManager.beginDelayedTransition(outLayout, AutoTransition().apply { this.duration = duration })
    this.getAConstraintSetApplyToPopUpLayout(openBoolean, hideLayout, anchorLayout)
    return !openBoolean
}

/**實際動態設定ConstraintSet的方法，將new一個ConstraintSet並指定回outLayout
 * @author 謝蝦米
 * @Date 2021/08/22
 * outLayout 控制所有內部Constraint的最外層Layout
 * @param openBoolean 開啟狀態，關閉時應傳入true，使其開啟。
 * @param hideLayout 隱藏在後面，要往下滑出的Layout
 * @param anchorLayout 顯示在上面，用於定位hideLayout的Layout
 * */
private fun ConstraintLayout.getAConstraintSetApplyToPopUpLayout(openBoolean: Boolean, hideLayout: ConstraintLayout, anchorLayout: ConstraintLayout) {
    val outLayout = this
    ConstraintSet().apply {
        clone(outLayout)
        clear(hideLayout.id)
        constrainHeight(hideLayout.id, if (openBoolean) ConstraintSet.WRAP_CONTENT else 0) // 收合的時候高度要設為0

        connect(hideLayout.id, ConstraintSet.LEFT, outLayout.id, ConstraintSet.LEFT, 0)
        connect(hideLayout.id, ConstraintSet.RIGHT, outLayout.id, ConstraintSet.RIGHT, 0)

        if (openBoolean) //靠上與靠下的不同對齊方式，主要解決設定Item和顯示Item高度不相同的問題(設定Item比顯示Item還大的時候會出現問題)
            connect(hideLayout.id, ConstraintSet.BOTTOM, anchorLayout.id, ConstraintSet.TOP)
        else
            connect(hideLayout.id, ConstraintSet.BOTTOM, anchorLayout.id, ConstraintSet.TOP)

        applyTo(outLayout)
    }
}

/**朝下方動畫打開隱藏Layout的方法，使用動態設定ConstraintSet。
 * @author 謝蝦米
 * @Date 2021/08/22
 * outLayout 控制所有內部Constraint的最外層Layout
 * @param openBoolean 開啟狀態，關閉時應傳入true，並以傳入之內容指定回該變數，例如open = openLayout(open,...)
 * @param hideLayout 隱藏在後面，要往下滑出的Layout
 * @param anchorLayout 顯示在上面，用於定位hideLayout的Layout
 * */
fun ConstraintLayout.openLayout(openBoolean: Boolean, hideLayout: ConstraintLayout, anchorLayout: ConstraintLayout): Boolean {

    val outLayout = this
    val duration = this.context.getShare().getAnimationDuration()
    TransitionManager.beginDelayedTransition(outLayout, AutoTransition().apply { this.duration = duration })
    this.getAConstraintSetApplyToOutLayout(openBoolean, hideLayout, anchorLayout)
    return !openBoolean
}

/**實際動態設定ConstraintSet的方法，將new一個ConstraintSet並指定回outLayout
 * @author 謝蝦米
 * @Date 2021/08/22
 * outLayout 控制所有內部Constraint的最外層Layout
 * @param openBoolean 開啟狀態，關閉時應傳入true，使其開啟。
 * @param hideLayout 隱藏在後面，要往下滑出的Layout
 * @param anchorLayout 顯示在上面，用於定位hideLayout的Layout
 * */
private fun ConstraintLayout.getAConstraintSetApplyToOutLayout(openBoolean: Boolean, hideLayout: ConstraintLayout, anchorLayout: ConstraintLayout) {
    val outLayout = this
    ConstraintSet().apply {
        clone(outLayout)
        clear(hideLayout.id)
        constrainHeight(hideLayout.id, if (openBoolean) ConstraintSet.WRAP_CONTENT else 0) // 收合的時候高度要設為0

        connect(hideLayout.id, ConstraintSet.LEFT, outLayout.id, ConstraintSet.LEFT, 0)
        connect(hideLayout.id, ConstraintSet.RIGHT, outLayout.id, ConstraintSet.RIGHT, 0)

        if (openBoolean) //靠上與靠下的不同對齊方式，主要解決設定Item和顯示Item高度不相同的問題(設定Item比顯示Item還大的時候會出現問題)
            connect(hideLayout.id, ConstraintSet.TOP, anchorLayout.id, ConstraintSet.BOTTOM)
        else
            connect(hideLayout.id, ConstraintSet.BOTTOM, anchorLayout.id, ConstraintSet.BOTTOM)

        applyTo(outLayout)
    }
}

/**
 *
 * 取得螢幕寬度 單位為整數(pixel)
 * @author Robert Chou didi31139@gmail.com
 * @date 2015/5/29 下午1:37:13
 * @version
 */
fun getScreenWidthPixels(context: Context?): Int {
    return context!!.resources.displayMetrics.widthPixels
}

/**
 *
 * 取得螢幕高度 單位為整數(pixel)
 * @author Robert Chou didi31139@gmail.com
 * @date 2015/5/29 下午1:37:13
 * @version
 */
fun getScreenHeightPixels(context: Context?): Int {
    return context!!.resources.displayMetrics.heightPixels
}


/**測試畫面比例用的方法
 * @param testRatio 測試中的比例(會於畫面中印出)
 * @param TAG 測試的View，可傳入或不傳入
 * 使用範例： ivIcon.setViewSizeByDpUnit(,width.testViewSize(0.1,"ivIcon width"),width.testViewSize(0.1,"ivIcon height"))
 * @author 蝦米
 * @date 2021/08/07
 * */
fun Double.testViewSize(testRatio: Double, TAG: String = "testViewSize"): Double {
    logi("testViewSize", "$TAG,ratio=>${testRatio}")
    logi("testViewSize", "$TAG,result=>${this * testRatio}")
    return this * testRatio
}


/**
 * 設定 壓下的按鈕切換效果(為TRAQ專門製作，產生「背景顏色變換」且「字變色」的效果)
 * @param unPressTextColor 未按下的文字顏色 (資源檔路徑)
 * @param pressTextColor 按下的文字顏色 (資源檔路徑)
 * @param unPressBackgroundColor 未按下的背景顏色 (資源檔路徑)
 * @param pressBackgroundColor 按下的背景顏色 (資源檔路徑)
 * @param unpressedBorderColor 未按下的邊框顏色 (資源檔路徑)(預設與按下的背景顏色相同)
 * @param pressedBorderColor 按下的邊框顏色 (資源檔路徑)(預設與未按下的背景顏色相同)
 * @author 蝦米
 * @date 2019/11/07
 */
//fun Button.setDefaultBackgroundAndTouchListener(
//    unPressTextColor: Int,
//    pressTextColor: Int,
//    unPressBackgroundColor: Int,
//    pressBackgroundColor: Int,
//    unpressedBorderColor: Int = pressBackgroundColor,
//    pressedBorderColor: Int = unPressBackgroundColor
//) {
//    val corner = this.context.resources.getDimensionPixelSize(com.timmy.mapmetropia.R.dimen.dialog_button_corner)        //圓角弧度
//    //按鈕設定參數：
////    val btnTextSize = context.resources.getDimensionPixelSize(com.smartq.smartcube.R.dimen.btn_text_size) //按鍵文字大小
//    val strokeWidth = context.resources.getDimensionPixelSize(com.timmy.mapmetropia.R.dimen.btn_stroke_width) //按鈕邊界寬度
//    setPressedBackground(
//        getRectangleBg(context, corner, corner, corner, corner, unPressBackgroundColor, unpressedBorderColor, strokeWidth),
//        getRectangleBg(context, corner, corner, corner, corner, pressBackgroundColor, pressedBorderColor, strokeWidth)
//    )
//    setPressedTextColor(unPressTextColor, pressTextColor)
//}

/**
 * 設定 view的長寬 單位為畫素(pixel)
 * @param view
 * @param w
 * @param h
 * @author Wang / Robert
 * @date 2015/5/8 下午3:13:42
 * @version
 */
fun View.setViewSize(w: Int, h: Int) {
    try {
        this.layoutParams.width = w
        this.layoutParams.height = h
    } catch (e: Exception) {
        //如果prams不存在 則重新建立
        val params = ViewGroup.LayoutParams(w, h)
        params.width = w
        params.height = h
        this.layoutParams = params
    }
    this.requestLayout()
}

/**
 * 設定 view的長寬 單位為dp
 * @param view
 * @param w
 * @param h
 * @author Wang / Robert
 * @date 2015/5/8 下午3:13:42
 * @version
 */
fun View.setViewSizeByDpUnit(w: Int, h: Int) {
    setViewSize(getPixelFromDpByDevice(this.context, w), getPixelFromDpByDevice(this.context, h))
}

/**
 * 設定 view的長寬 單位為畫素(pixel) 自動高度
 * @param view
 * @param w
 * @param rid
 * @author Wang / Robert
 * @date 2015/5/8 下午3:13:42
 * @version
 */
fun View.setViewSizeByResWidth(w: Int, rid: Int) {
    val h = ViewTool.getImageHeight(this.context, rid, w)
    try {
        this.layoutParams.width = w
        this.layoutParams.height = h
    } catch (e: Exception) {
        //如果prams不存在 則重新建立
        val params = ViewGroup.LayoutParams(w, h)
        params.width = w
        params.height = h
        this.layoutParams = params
    }
}

/**
 * 設定 view的長寬 單位為畫素(pixel) 自動寬度
 * @param view
 * @param h
 * @param rid
 * @author Wang / Robert
 * @date 2015/5/8 下午3:13:42
 * @version
 */
fun View.setViewSizeByResHeight(h: Int, rid: Int) {
    val w = ViewTool.getImageWidth(this.context, rid, h)
    try {
        this.layoutParams.width = w
        this.layoutParams.height = h
    } catch (e: Exception) {
        //如果prams不存在 則重新建立
        val params = ViewGroup.LayoutParams(w, h)
        params.width = w
        params.height = h
        this.layoutParams = params
    }
}

fun View.setTextSize(sp: Int) {
    val displayMetrics = this.context.resources.displayMetrics
    val realSpSize =
        ((sp * displayMetrics.widthPixels).toFloat() / displayMetrics.density / 360f).toInt()
    if (this is TextView) {
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
    } else if (this is Button) {
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
    } else if (this is EditText) {
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
    } else {
        (this as TextView).setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
    }
}

/**
 *
 * 輸入DP單位數值 根據裝置動態 回傳像素:
 * @author Robert Chou didi31139@gmail.com
 * @param dpSize 整數 單位為dp
 * @date 2015/6/17 下午5:25:39
 * @return dp根據裝置動態計算 回傳pixel
 * @version
 */
fun getPixelFromDpByDevice(context: Context, dpSize: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    val realSpSize =
        ((dpSize * displayMetrics.widthPixels).toFloat() / displayMetrics.density / 360f).toInt()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        realSpSize.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

/**
 *
 * 設定物件間距  單位為畫素(pixel)
 * 上層類別須為 RelativeLayout or LinearLayout
 * @author Wang / Robert Chou didi31139@gmail.com
 * @date 2015/5/26 下午3:25:33
 * @version
 */
fun View.setMarginByDpUnit(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
    val params = this.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.setMargins(
            getPixelFromDpByDevice(this.context, leftMargin),
            getPixelFromDpByDevice(this.context, topMargin),
            getPixelFromDpByDevice(this.context, rightMargin),
            getPixelFromDpByDevice(this.context, bottomMargin)
        )
    }
//    if (params is LinearLayout.LayoutParams) {
//        params.setMargins(
//            getPixelFromDpByDevice(this.context, leftMargin),
//            getPixelFromDpByDevice(this.context, topMargin),
//            getPixelFromDpByDevice(this.context, rightMargin),
//            getPixelFromDpByDevice(this.context, bottomMargin)
//        )
//    } else if (params is RelativeLayout.LayoutParams) {
//        params.setMargins(
//            getPixelFromDpByDevice(this.context, leftMargin),
//            getPixelFromDpByDevice(this.context, topMargin),
//            getPixelFromDpByDevice(this.context, rightMargin),
//            getPixelFromDpByDevice(this.context, bottomMargin)
//        )
//    } else if (params is ConstraintLayout.LayoutParams) {
//        params.setMargins(
//            getPixelFromDpByDevice(this.context, leftMargin),
//            getPixelFromDpByDevice(this.context, topMargin),
//            getPixelFromDpByDevice(this.context, rightMargin),
//            getPixelFromDpByDevice(this.context, bottomMargin)
//        )
//    }else if (params is RecyclerView.LayoutParams) {
//        params.setMargins(
//            getPixelFromDpByDevice(this.context, leftMargin),
//            getPixelFromDpByDevice(this.context, topMargin),
//            getPixelFromDpByDevice(this.context, rightMargin),
//            getPixelFromDpByDevice(this.context, bottomMargin)
//        )
//    }else if (params is CoordinatorLayout.LayoutParams) {
//        params.setMargins(
//            getPixelFromDpByDevice(this.context, leftMargin),
//            getPixelFromDpByDevice(this.context, topMargin),
//            getPixelFromDpByDevice(this.context, rightMargin),
//            getPixelFromDpByDevice(this.context, bottomMargin)
//        )
//    }
    this.layoutParams = params
    this.requestLayout()
}

fun View.setPaddingByDpUnit(
    leftPadding: Int,
    topPadding: Int,
    rightPadding: Int,
    bottomPadding: Int
) {
    this.setPadding(
        getPixelFromDpByDevice(this.context, leftPadding),
        getPixelFromDpByDevice(this.context, topPadding),
        getPixelFromDpByDevice(this.context, rightPadding),
        getPixelFromDpByDevice(this.context, bottomPadding)
    )
}

/**
 * 設定 壓下的圖片切換效果
 * @param unPressedDrawable 未按下的圖片 R.drawable.image
 * @param pressedDrawable 未按下的圖片 R.drawable.pressedimage
 */
fun View.setPressedImage(unPressedDrawable: Drawable?, pressedDrawable: Drawable?) {
    if (pressedDrawable == null) {
        this.background = unPressedDrawable
        return
    }
    val states = StateListDrawable()
    states.addState(intArrayOf(R.attr.state_pressed), pressedDrawable)
    states.addState(intArrayOf(R.attr.state_focused), pressedDrawable)
    states.addState(intArrayOf(R.attr.state_checked), pressedDrawable)
    states.addState(intArrayOf(), unPressedDrawable)
    if (this is Button) {
        this.background = states
    } else {
        (this as ImageView).setImageDrawable(states)
    }
}

/**
 * 設定 壓下的圖片切換效果
 * @param unPressedDrawableID 未按下的圖片 R.drawable.image
 * @param pressedDrawableID 未按下的圖片 R.drawable.pressedimage
 */
@SuppressLint("UseCompatLoadingForDrawables")
fun View.setPressedImage(unPressedDrawableID: Int, pressedDrawableID: Int) {
    setPressedImage(
        this.context.getDrawable(unPressedDrawableID),
        this.context.getDrawable(pressedDrawableID)
    )
}

/**
 * 設定 壓下的圖片切換效果
 * @param unPressedDrawable 未按下的圖片 R.drawable.image
 * @param pressedDrawable 未按下的圖片 R.drawable.pressedimage
 */
fun View.setPressedBackground(unPressedDrawable: Drawable, pressedDrawable: Drawable?) {
    if (pressedDrawable == null) {
        this.background = unPressedDrawable
        return
    }
    val states = StateListDrawable()
    states.addState(intArrayOf(R.attr.state_pressed), pressedDrawable)
    states.addState(intArrayOf(R.attr.state_focused), pressedDrawable)
    states.addState(intArrayOf(R.attr.state_checked), pressedDrawable)
    states.addState(intArrayOf(), unPressedDrawable)
    this.background = states
}

/**
 * 設定 壓下的圖片切換效果
 * @param unPressedDrawable 未按下的圖片 R.drawable.image
 * @param pressedDrawable 未按下的圖片 R.drawable.pressedimage
 */
fun View.setPressedBackground(unPressedDrawable: Int, pressedDrawable: Int) {
    setPressedBackground(
        this.context.resources.getDrawable(unPressedDrawable),
        this.context.resources.getDrawable(pressedDrawable)
    )
}

/**
 * 設定按鈕 被按住的顏色背景
 * @param unPressedColor 未按下的顏色背景 R.color.color1
 * @param pressedColor 按下的顏色 R.color.color 0為不給
 */
fun View.setPressedBackgroundColor(unPressedColor: Int, pressedColor: Int) {
    val context = this.context
    if (pressedColor == 0) {
        this.setBackgroundResource(unPressedColor)
        return
    }
    val unPressedcolorDrawable = ColorDrawable(context.getColorByBuildVersion(unPressedColor))
    val pressedcolorDrawable = ColorDrawable(context.getColorByBuildVersion(pressedColor))
    val states = StateListDrawable()
    states.addState(intArrayOf(R.attr.state_pressed), pressedcolorDrawable)
    states.addState(intArrayOf(R.attr.state_focused), pressedcolorDrawable)
    states.addState(intArrayOf(R.attr.state_checked), pressedcolorDrawable)
    states.addState(intArrayOf(), unPressedcolorDrawable)
    this.background = states
}


/**
 * 設定按鈕 被按住的顏色背景
 * @param unPressedColor 未按下的顏色背景 R.color.color1
 * @param pressedColor 按下的顏色 R.color.color 0為不給
 */
fun View.setPressedTextColor(unPressedColor: Int, pressedColor: Int) {
    val context = this.context
    if (pressedColor == 0) {
        (this as TextView).setTextColor(context.getColorByBuildVersion(unPressedColor))
        return
    }
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(R.attr.state_pressed),
            intArrayOf(R.attr.state_focused),
            intArrayOf(R.attr.state_checked),
            intArrayOf()
        ),
        intArrayOf(
            context.getColorByBuildVersion(pressedColor),
            context.getColorByBuildVersion(pressedColor),
            context.getColorByBuildVersion(pressedColor),
            context.getColorByBuildVersion(unPressedColor)
        )
    )
    (this as TextView).setTextColor(colorStateList)
}

/**
 * check box 狀態設定
 * @param basedrawable 未按下的圖片 R.drawable.image
 * @param checkeddrawable 未按下的圖片 R.drawable.pressedimage 0為不給
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
fun View.setCheckDrawable(basedrawable: Int, checkeddrawable: Int) {
    if (checkeddrawable == 0) {
        this.setBackgroundResource(basedrawable)
        return
    }
    val states = StateListDrawable()
    states.addState(
        intArrayOf(R.attr.state_checkable),
        context.resources.getDrawable(basedrawable)
    )
    states.addState(
        intArrayOf(R.attr.state_checked),
        context.resources.getDrawable(checkeddrawable)
    )
    states.addState(intArrayOf(), context.resources.getDrawable(basedrawable))

    if (this is CheckBox) {
        this.buttonDrawable = states
    } else if (Build.VERSION.SDK_INT >= 16) {
        this.background = states
    } else {
        this.setBackgroundDrawable(states)
    }
}

/**
 * 設定Tab按鈕 被按住的顏色背景
 */
fun View.setTabPressedImage(unPressedDrawable: Drawable, pressedDrawable: Drawable?) {
    if (pressedDrawable == null) {
        this.background = unPressedDrawable
        return
    }
    val states = StateListDrawable()
    states.addState(intArrayOf(R.attr.state_pressed), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_focused), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_checked), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_selected), pressedDrawable)
    states.addState(intArrayOf(), unPressedDrawable)
    if (this is Button) {
        this.background = states
    } else {
        (this as ImageView).setImageDrawable(states)
    }
}

/**
 * 設定Tab按鈕 被按住的顏色背景
 * @param unPressedDrawableID 未按下的顏色背景 R.color.color1
 * @param pressedDrawableID 按下的顏色 R.color.color 0為不給
 */
fun View.setTabPressedImage(unPressedDrawableID: Int, pressedDrawableID: Int) {
    var unPressedDrawable: Drawable? = null
    var pressedDrawable: Drawable? = null
    if (unPressedDrawableID == 0) {
        unPressedDrawable = ColorDrawable(this.context.getColorByBuildVersion(R.color.transparent))
    } else {
        unPressedDrawable = this.resources.getDrawable(unPressedDrawableID)
    }
    if (pressedDrawableID == 0) {
        pressedDrawable = ColorDrawable(this.context.getColorByBuildVersion(R.color.transparent))
    } else {
        pressedDrawable = this.resources.getDrawable(pressedDrawableID)
    }
    if (pressedDrawable == null) {
        this.background = unPressedDrawable
        return
    }
    val states = StateListDrawable()
    states.addState(intArrayOf(R.attr.state_pressed), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_focused), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_checked), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_selected), pressedDrawable)
    states.addState(intArrayOf(), unPressedDrawable)
    if (this is Button) {
        this.background = states
    } else {
        (this as ImageView).setImageDrawable(states)
    }

}

/**
 * 設定Tab按鈕 被按住的顏色背景
 * @param unPressedDrawableID 未按下的顏色背景 R.color.color1
 * @param pressedDrawableID 按下的顏色 R.color.color 0為不給
 */
fun View.setTabPressedBackgroundColor(unPressedDrawableID: Int, pressedDrawableID: Int) {
    var unPressedDrawable: Drawable? = null
    var pressedDrawable: Drawable? = null
    if (unPressedDrawableID == 0) {
        unPressedDrawable = ColorDrawable(this.context.getColorByBuildVersion(R.color.transparent))
    } else {
        unPressedDrawable = this.resources.getDrawable(unPressedDrawableID)
    }
    if (pressedDrawableID == 0) {
        pressedDrawable = ColorDrawable(this.context.getColorByBuildVersion(R.color.transparent))
    } else {
        pressedDrawable = this.resources.getDrawable(pressedDrawableID)
    }
    if (pressedDrawable == null) {
        this.background = unPressedDrawable
        return
    }
    val states = StateListDrawable()
    states.addState(intArrayOf(R.attr.state_pressed), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_focused), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_checked), unPressedDrawable)
    states.addState(intArrayOf(R.attr.state_selected), pressedDrawable)
    states.addState(intArrayOf(), unPressedDrawable)
    //設定水波文
    this.setRippleBackround(R.color.holo_blue_bright, states)
}

/**
 * 設定Tab按鈕 被按住的顏色背景
 * @param unPressedColor 未按下的顏色背景 R.color.color1
 * @param pressedColor 按下的顏色 R.color.color 0為不給
 */
fun View.setTabPressedTextColor(unPressedColor: Int, pressedColor: Int) {
    val context = this.context
    if (pressedColor == 0) {
        (this as TextView).setTextColor(context.getColorByBuildVersion(unPressedColor))
        return
    }
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(R.attr.state_pressed),
            intArrayOf(R.attr.state_focused),
            intArrayOf(R.attr.state_checked),
            intArrayOf(R.attr.state_selected),
            intArrayOf()
        ),
        intArrayOf(
            context.getColorByBuildVersion(unPressedColor),
            context.getColorByBuildVersion(unPressedColor),
            context.getColorByBuildVersion(unPressedColor),
            context.getColorByBuildVersion(pressedColor),
            context.getColorByBuildVersion(unPressedColor)
        )
    )
    (this as TextView).setTextColor(colorStateList)
}

/**設定水波文*/
fun View.setRippleBackround(color: Int, states: Drawable?) {
    //            val rippleDrawable = RippleDrawable(ColorStateList.valueOf(Color.BLACK) , states , null)
    val attrs = intArrayOf(R.attr.selectableItemBackground)
    val typedArray = this.context.obtainStyledAttributes(attrs)
    val rippleDrawable = typedArray.getDrawable(0) as RippleDrawable
    typedArray.recycle()
    rippleDrawable.setColor(ColorStateList.valueOf(this.context.getColorByBuildVersion(color)))
    if (states != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable.addLayer(states)
        }
    }
    this.background = rippleDrawable
}
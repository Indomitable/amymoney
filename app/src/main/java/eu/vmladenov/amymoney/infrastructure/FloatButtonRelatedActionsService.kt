package eu.vmladenov.amymoney.infrastructure

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.PopupWindow

class FloatButtonRelatedActionsService {

    fun openActivities(activity: Activity, fabMain: View, actionsResourceId: Int) {
        val parentView = fabMain.parent as ViewGroup
        val content = LayoutInflater.from(activity).inflate(actionsResourceId, parentView, false) as ViewGroup
        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val viewLocation = IntArray(2)
        fabMain.getLocationInWindow(viewLocation)
        val actionWidth = (content.getChildAt(0) as ViewGroup).getChildAt(1).measuredWidth

        val x = viewLocation[0] - content.measuredWidth + fabMain.width/2 + actionWidth/2
        val y = viewLocation[1] - content.measuredHeight
        val popupWindow = PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.showAtLocation(parentView, Gravity.START or Gravity.TOP, x, y)


        val rotationClockwise = RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 200
            fillAfter = true
        }
        val rotationCounterClockwise = RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 200
            fillAfter = true
        }
        fabMain.startAnimation(rotationClockwise)
        popupWindow.setOnDismissListener {
            fabMain.startAnimation(rotationCounterClockwise)
        }
    }
}

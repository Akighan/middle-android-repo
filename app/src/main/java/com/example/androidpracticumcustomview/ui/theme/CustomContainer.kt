package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount > 0) {
            val childHeight = bottom/2

            if (childCount == 1) {
                val firstChild = getChildAt(0).also {
                    it.alpha = 0f
                    it.animate().alpha(1f).setDuration(2000).translationY(-(childHeight / 2).toFloat()).setDuration(5000)
                }

                val firstChildLeft = left
                val firstChildTop = (top + childHeight) / 2
                val firstChildRight = right
                val firstChildBottom = (bottom + childHeight) / 2
                firstChild.layout(firstChildLeft, firstChildTop, firstChildRight, firstChildBottom)
            } else if (childCount > 1) {
                val secondChild = getChildAt(1).also {
                    it.alpha = 0f
                    it.animate().alpha(1f).setDuration(2000).translationY((childHeight / 2).toFloat()).setDuration(5000)
                }

                val secondChildLeft = left
                val secondChildTop = (top + childHeight) / 2
                val secondChildRight = right
                val secondChildBottom = (bottom + childHeight) / 2
                secondChild.layout(secondChildLeft, secondChildTop, secondChildRight, secondChildBottom)
            }
        }
    }

    override fun addView(child: View) {
        if (childCount >= 2) {
            Log.e(TAG, "Not allowed more than 2 children", IllegalStateException())
        }
        super.addView(child)
    }

    companion object {
        private val TAG = CustomContainer::class.qualifiedName
    }
}
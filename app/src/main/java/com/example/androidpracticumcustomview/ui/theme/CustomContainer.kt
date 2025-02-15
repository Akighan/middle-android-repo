package com.example.androidpracticumcustomview.ui.theme

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import java.util.Random

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val viewIdToAnimationEndedMap = mutableMapOf<Int, Boolean>()

    init {
        setWillNotDraw(false)
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        Log.d(TAG, "onViewAdded")
        post {
            child?.let {
                startAnimation(it)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        fun isViewAlreadyExist(id: Int) = viewIdToAnimationEndedMap.keys.contains(id)
        fun isViewAnimationEnded(id: Int) = viewIdToAnimationEndedMap.getOrDefault(id, false)

        Log.d(TAG, "onLayout")
        children.forEachIndexed { index, view ->
            if (!isViewAlreadyExist(view.id)) { //put view at start position
                val childHeight = bottom / 2
                view.layout(left, (top + childHeight) / 2, right, (bottom + childHeight) / 2)
            } else if (isViewAnimationEnded(view.id)) { //put view at animation ended position
                val childHeight = bottom / 2
                if (index == 0) {
                    view.layout(left, top, right, childHeight)
                } else {
                    view.layout(left, childHeight, right, bottom)
                }
            }
        }
    }

    override fun addView(child: View) {
        if (childCount >= 2) {
            throw IllegalStateException("Not allowed more than 2 children")
        }
        child.id = Random().nextInt()
        child.alpha = 0f
        super.addView(child)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        val idList = mutableListOf<Int>()
        val boolList = mutableListOf<Boolean>()
        for ((id, isEnded) in viewIdToAnimationEndedMap) {
            if (id != View.NO_ID) {
                idList.add(id)
                boolList.add(isEnded)
            }
        }
        savedState.viewIds = idList.toIntArray()
        savedState.boolValues = boolList.toBooleanArray()

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) {
            val viewIds = state.viewIds ?: intArrayOf()
            val boolValues = state.boolValues ?: booleanArrayOf()

            if (viewIds.size == boolValues.size) {
                viewIdToAnimationEndedMap.clear()
                for (i in viewIds.indices) {
                    viewIdToAnimationEndedMap[id] = boolValues[i]
                }
            }
        }
    }

    private fun startAnimation(view:View) {
        Log.d(TAG, "StartAnimation")
        val childHeight = height / 2

        val translationYAnimDestination = if (children.indexOf(view) == 0) {
            -(childHeight / 2).toFloat()
        } else {
            (childHeight / 2).toFloat()
        }

        val animatorSet = AnimatorSet()
        val translateAnimation =
            ObjectAnimator.ofFloat(view, "translationY", 0f, translationYAnimDestination)
        translateAnimation.duration = TRANSLATION_Y_ANIM_DURATION

        val alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        alphaAnimation.duration = ALPHA_ANIM_DURATION

        animatorSet.playTogether(translateAnimation, alphaAnimation)
        animatorSet.start()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                viewIdToAnimationEndedMap[view.id] = false
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewIdToAnimationEndedMap[view.id] = true
            }
        })
    }

    class SavedState(superState: Parcelable?) : BaseSavedState(superState) {
        var viewIds: IntArray? = null
        var boolValues: BooleanArray? = null

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeIntArray(viewIds)
            out.writeBooleanArray(boolValues)
        }
    }

    companion object {
        private const val TAG = "CustomContainer"
        private const val TRANSLATION_Y_ANIM_DURATION = 5_000L
        private const val ALPHA_ANIM_DURATION = 2_000L
    }
}
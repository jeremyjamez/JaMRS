/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/21/20 1:47 PM
 *
 */

package xyz.james.shared.anims

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class DropDownAnim(val view: View, val sourceHeight: Int, val targetHeight: Int, val down: Boolean) : Animation() {

    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation?
    ) {
        val newHeight: Int
        newHeight = if (down) {
            sourceHeight + (targetHeight * interpolatedTime).toInt()
        } else {
            sourceHeight + targetHeight - (targetHeight * interpolatedTime).toInt() //(int) (targetHeight * (1 - interpolatedTime));
        }
        view.layoutParams.height = newHeight
        view.requestLayout()
        view.visibility = if (down) View.VISIBLE else View.GONE
    }

    override fun initialize(
        width: Int, height: Int, parentWidth: Int,
        parentHeight: Int
    ) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}
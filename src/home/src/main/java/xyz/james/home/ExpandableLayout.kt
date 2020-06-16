/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/27/20 10:42 AM
 *
 */

package xyz.james.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.updateLayoutParams
import kotlinx.android.synthetic.main.expandable_layout.view.*
import kotlin.properties.Delegates

class ExpandableLayout(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    private var isExpanded by Delegates.notNull<Boolean>()

    init {
        View.inflate(context, R.layout.expandable_layout, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
        this.expander_title.text = attributes.getString(R.styleable.ExpandableLayout_title)
        this.isExpanded = attributes.getBoolean(R.styleable.ExpandableLayout_isExpanded, false)

        attributes.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val scale = context.resources.displayMetrics.density
        val pixels = (50 * scale + 0.5f).toInt()
        if (!isExpanded){
            updateLayoutParams {
                height = pixels
            }
            expand_button.rotation = 0F
        } else {
            updateLayoutParams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            expand_button.rotation = 180F
        }

        expand_button.setOnClickListener {
            if (!isExpanded){
                updateLayoutParams {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                isExpanded = true
                expand_button.rotation = 180F
            } else {
                updateLayoutParams {
                    height = pixels
                }
                isExpanded = false
                expand_button.rotation = 0F
            }
        }
    }
}
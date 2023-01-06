package com.nszalas.timefulness.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2

data class LeadingMargin(
    val margin: Int,
    val lines: Int,
): LeadingMarginSpan2 {
    override fun getLeadingMargin(p0: Boolean): Int {
        return if(p0) {
            /*
            * This indentation is applied to the number of rows returned
            * getLeadingMarginLineCount ()
            */
            margin
        } else {
            // Offset for all other Layout layout { }
            /*
            * Returns * the number of rows which should be applied * indent
            * returned by getLeadingMargin (true) Note:* Indent only
            * applies to N lines of the first paragraph.
            */
            0
        }
    }

    override fun drawLeadingMargin(
        p0: Canvas?,
        p1: Paint?,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Int,
        p6: Int,
        p7: CharSequence?,
        p8: Int,
        p9: Int,
        p10: Boolean,
        p11: Layout?
    ) {
        // TODO Auto-generated method stub
    }

    override fun getLeadingMarginLineCount(): Int {
        return lines
    }
}
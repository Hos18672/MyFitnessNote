package com.example.myfitneesnote

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


class add_popup_fragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        var rootView :View = inflater.inflate(R.layout.fragment_add_popup_fragment,container,false)
        return rootView
        setWidthPercent(95)
    }

    fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * Call this method (in onActivityCreated or later)
     * to make the dialog near-full screen.
     */
    fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}

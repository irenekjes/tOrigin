package kr.dada.torigin

import android.view.View

private var clickable = true

class OnThrottleClickListener(
    private val interval: Long,
    private val action: (View) -> Unit
) : View.OnClickListener {

    override fun onClick(v: View) {
        if (clickable && v.isEnabled) {
            clickable = false
            v.run {
                postDelayed({ clickable = true }, interval)
                action(v)
            }
        }
    }
}
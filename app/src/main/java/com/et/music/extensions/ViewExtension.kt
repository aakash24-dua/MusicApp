package com.et.music.extensions

import android.view.View



fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}



fun View.gone() {
    this.visibility = View.GONE
}

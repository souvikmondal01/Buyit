package com.buyit.buyit.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Activity.setStatusBarColor(color: Int) {
    window.statusBarColor = ContextCompat.getColor(this, color)
}

fun Fragment.setStatusBarColor(color: Int) {
    activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), color)
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun Fragment.fragmentTo(activity: AppCompatActivity) {
    startActivity(Intent(requireContext(), activity::class.java))
}

fun ImageView.setColourFilter(color: Int) {
    setColorFilter(ContextCompat.getColor(context, color))
}


package com.joosung.pickme.extensions

import android.content.Context
import android.databinding.BindingAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.GenericTransitionOptions
import com.joosung.imagelist.util.LogUtil
import com.joosung.pickme.common.App
import com.joosung.pickme.model.Persist
import com.joosung.pickme.preference.ImagePreferences
import com.joosung.library.rx.delay
import com.joosung.pickme.R
import com.joosung.pickme.di.GlideApp

@BindingAdapter("imageUrl", "imageWidth", "imageHeight")
fun loadImage(view: ImageView, url: String?, width: Int?, height: Int?) {
    val height = height ?: ImagePreferences.imageMinimumHeight
    delay { view.layoutParams.height = height }

    url?.let {
        GlideApp.with(view.context)
            .load(it)
            .transition(GenericTransitionOptions.withNoTransition())
            .centerCrop()
            .placeholder(R.color.basic_divider)
            .error(R.color.basic_divider)
            .into(view)
    } ?: run {
        GlideApp.with(view.context)
            .load(R.color.basic_divider)
            .placeholder(R.color.basic_divider)
            .error(R.color.basic_divider)
            .into(view)

    }
}

@BindingAdapter("fullImageUrl")
fun loadFullPhotoImage(view: ImageView, url: String?) {
    GlideApp.with(view.context)
        .load(url)
        .transition(GenericTransitionOptions.withNoTransition())
        .centerCrop()
        .placeholder(R.color.basic_divider)
        .error(R.color.basic_divider)
        .into(view)
}

@BindingAdapter("onEditorAction")
fun setOnEditorActionListener(editText: EditText, listener: TextView.OnEditorActionListener) {
    editText.setOnEditorActionListener(listener)
}
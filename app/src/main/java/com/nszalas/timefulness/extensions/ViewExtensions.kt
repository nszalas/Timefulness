package com.nszalas.timefulness.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nszalas.timefulness.R

fun ImageView.loadImage(photoUrl: String) {
    Glide
        .with(this.context)
        .load(photoUrl)
        .centerCrop()
        .error(R.drawable.profile_picture_placeholder)
        .into(this)
}
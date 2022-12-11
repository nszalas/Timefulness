package com.nszalas.timefulness.utils

import javax.inject.Inject

class PlaceholderPhotoProvider @Inject constructor() {
    fun getPhotoUrl(name: String) = "$API_URL?name=$name&format=png"

    companion object {
        private const val API_URL = "https://ui-avatars.com/api/"
    }
}
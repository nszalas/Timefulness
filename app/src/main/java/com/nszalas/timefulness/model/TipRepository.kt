package com.nszalas.timefulness.model

import androidx.lifecycle.LiveData

class TipRepository (private val appDao: TipDao) {
    val getAll: LiveData<List<Tip>> = appDao.getAll()
}
package com.nszalas.timefulness.repository

import androidx.lifecycle.LiveData
import com.nszalas.timefulness.infrastructure.local.room.TipDao
import com.nszalas.timefulness.infrastructure.local.room.entity.TipEntity

class TipRepository (private val appDao: TipDao) {
    val getAll: LiveData<List<TipEntity>> = appDao.getAll()
}
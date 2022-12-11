package com.nszalas.timefulness.infrastructure.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nszalas.timefulness.infrastructure.local.entity.TipEntity

@Dao
interface TipDao {
    @Query("SELECT * FROM tip_table")
    fun getAll(): LiveData<List<TipEntity>>
}
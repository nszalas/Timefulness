package com.nszalas.timefulness.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TipDao {
    @Query("SELECT * FROM tip_table")
    fun getAll(): LiveData<List<Tip>>
}
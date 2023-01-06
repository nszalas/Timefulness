package com.nszalas.timefulness.infrastructure.local.room

import androidx.room.*
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_ADVICE_ID
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_ADVICE
import com.nszalas.timefulness.infrastructure.local.room.entity.AdviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdviceDao {
    @Query("SELECT * FROM $TABLE_ADVICE")
    suspend fun getAll(): List<AdviceEntity>

    @Query("SELECT * FROM $TABLE_ADVICE WHERE $COLUMN_ADVICE_ID == :id")
    suspend fun getAdviceWithId(id: Int): AdviceEntity
}
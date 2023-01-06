package com.nszalas.timefulness.infrastructure.local.room

import androidx.room.Dao
import androidx.room.Query
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TECHNIQUE_ID
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_TECHNIQUE
import com.nszalas.timefulness.infrastructure.local.room.entity.TechniqueEntity

@Dao
interface TechniqueDao {
    @Query("SELECT * FROM $TABLE_TECHNIQUE")
    suspend fun getAllTechniques(): List<TechniqueEntity>

    @Query("SELECT * FROM $TABLE_TECHNIQUE WHERE $COLUMN_TECHNIQUE_ID == :id")
    suspend fun getTechniqueWithId(id: Int): TechniqueEntity
}
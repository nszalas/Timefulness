package com.nszalas.timefulness.infrastructure.local.room

import androidx.room.Dao
import androidx.room.Query
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_CATEGORY
import com.nszalas.timefulness.infrastructure.local.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM $TABLE_CATEGORY")
    suspend fun getCategories(): List<CategoryEntity>

}
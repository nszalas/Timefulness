package com.nszalas.timefulness.infrastructure.local

import androidx.room.Dao
import androidx.room.Query
import com.nszalas.timefulness.infrastructure.local.Constants.TABLE_CATEGORY
import com.nszalas.timefulness.infrastructure.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM $TABLE_CATEGORY")
    suspend fun getCategories(): List<CategoryEntity>

}
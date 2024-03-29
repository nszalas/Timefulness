package com.nszalas.timefulness.infrastructure.local.room

object Constants {
    const val DATABASE_NAME = "timefulness_database"

    const val TABLE_TASK = "tasks_table"
    const val TABLE_ADVICE = "advice_table"
    const val TABLE_CATEGORY = "category_table"
    const val TABLE_TECHNIQUE = "technique_table"

    const val COLUMN_TASK_ID = "id"
    const val COLUMN_TASK_CATEGORY_ID = "categoryId"
    const val COLUMN_TASK_START_TIMESTAMP = "startTimestamp"
    const val COLUMN_TASK_COMPLETED = "completed"
    const val COLUMN_TASK_END_TIMESTAMP = "endTimestamp"
    const val COLUMN_TASK_USER_ID = "userId"

    const val COLUMN_CATEGORY_ID = "id"

    const val COLUMN_ADVICE_ID = "id"

    const val COLUMN_TECHNIQUE_ID = "id"
}
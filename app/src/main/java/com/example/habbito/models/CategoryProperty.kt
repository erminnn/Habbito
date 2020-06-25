package com.example.habbito.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryProperty(
    @Embedded
    val category: Category,
    @Relation(parentColumn = "category_id",entityColumn = "property_id")
    var properties : List<CategoryAdditionalProperty>
)
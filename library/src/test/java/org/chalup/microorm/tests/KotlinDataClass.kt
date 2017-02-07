package org.chalup.microorm.tests

import android.provider.BaseColumns
import org.chalup.microorm.annotations.Column
import org.chalup.microorm.annotations.Embedded
import org.chalup.microorm.tests.EmbeddedTest.*

data class KotlinDataClass(
    @Column(BaseColumns._ID) val id: Long
)

data class KotlinParentDataClass(
    @Embedded val person: KotlinPerson
)

data class KotlinPerson(
    @Column(AGE_COLUMN) val age: Int,
    @Embedded val name: KotlinName
)

data class KotlinName(
    @Column(FIRST_NAME_COLUMN) val firstName: String,
    @Column(LAST_NAME_COLUMN) val lastName: String
)

package org.chalup.microorm.tests

import android.provider.BaseColumns
import org.chalup.microorm.annotations.Column

data class KotlinDataClass(
    @Column(BaseColumns._ID) val id: Long
)
package top.aobanagi.simpcalculator.calculator

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class Calculation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val expression: String,
    val result: String
)
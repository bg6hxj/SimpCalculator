package top.aobanagi.simpcalculator.calculator

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Calculation::class], version = 1)
abstract class CalculationDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao

    companion object {
        @Volatile
        private var INSTANCE: CalculationDatabase? = null

        fun getDatabase(context: Context): CalculationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalculationDatabase::class.java,
                    "calculation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

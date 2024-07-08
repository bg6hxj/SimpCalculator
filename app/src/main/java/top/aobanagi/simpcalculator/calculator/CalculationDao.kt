package top.aobanagi.simpcalculator.calculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalculationDao {
    @Insert
    suspend fun insertCalculation(calculation: Calculation)

    @Query("SELECT * FROM calculations")
    suspend fun getAllCalculations(): List<Calculation>

    @Query("DELETE FROM calculations")
    suspend fun clearCalculations()
}

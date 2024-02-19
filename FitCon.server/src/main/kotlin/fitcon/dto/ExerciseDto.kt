package fitcon.dto

import fitcon.entity.Exercise
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

class ExerciseDto {
    var id: Long? = null

    @NotEmpty(message = "Name cannot be empty")
    var exerciseName: String? = null

    @NotNull(message = "Reps count cannot be empty")
    var reps: Int? = null

    @NotNull(message = "Amount of series should not be empty")
    var series: Int? = null

    var info: String? = null
}
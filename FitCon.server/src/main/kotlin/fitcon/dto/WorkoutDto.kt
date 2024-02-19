package fitcon.dto

import fitcon.entity.Exercise
import fitcon.entity.User
import jakarta.validation.constraints.NotEmpty

class WorkoutDto {
    private val id: Long? = null

    @NotEmpty(message = "Must be named!")
    var name: String? = null

    @NotEmpty(message = "Must have description!")
    var description: String? = null

    var exercises: List<ExerciseDto>? = null
}
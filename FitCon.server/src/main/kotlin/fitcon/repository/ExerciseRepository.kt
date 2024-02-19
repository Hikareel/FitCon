package fitcon.repository

import fitcon.entity.Exercise
import fitcon.entity.Workout
import org.springframework.data.jpa.repository.JpaRepository

interface ExerciseRepository: JpaRepository<Exercise?, Long?> {
    fun findByExerciseName(name: String?): Exercise?
    fun findAllByWorkoutId(workout: Workout): List<Exercise>
}
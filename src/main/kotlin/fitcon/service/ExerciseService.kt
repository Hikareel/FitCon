package fitcon.service

import fitcon.dto.ExerciseDto
import fitcon.dto.WorkoutDto
import fitcon.entity.Exercise
import fitcon.entity.Workout

interface ExerciseService {
    fun findExerciseByName(name: String): Exercise?
    fun findAllExerciseByWorkout(workoutDto: WorkoutDto): List<Exercise>
    fun saveExercise(exerciseDto: ExerciseDto, workoutName: String)
}
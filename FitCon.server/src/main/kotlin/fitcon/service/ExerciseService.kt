package fitcon.service

import fitcon.dto.ExerciseDto
import fitcon.dto.WorkoutDto
import fitcon.entity.Exercise
import fitcon.entity.Workout

interface ExerciseService {
    fun findExerciseByName(name: String): Exercise?
    fun findAllExercisesByWorkout(workout: Workout?): List<ExerciseDto>
    fun saveExercise(exerciseDto: ExerciseDto, workoutName: String, workoutId: Long?)
    fun saveExercises(exerciseDtoList: List<ExerciseDto>?, workoutName: String, workoutId: Long?)
}
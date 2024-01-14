package fitcon.service

import fitcon.dto.ExerciseDto
import fitcon.dto.WorkoutDto
import fitcon.entity.Exercise

interface ExerciseService {
    fun findExerciseByName(name: String): Exercise?
    fun findAllExerciseByWorkout(workoutDto: WorkoutDto): List<ExerciseDto>
    fun saveExercise(exerciseDto: ExerciseDto, workoutName: String)
    fun saveExercises(exerciseDtoList: List<ExerciseDto>?, workoutName: String)
}
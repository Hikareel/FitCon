package fitcon.service.impl

import fitcon.dto.ExerciseDto
import fitcon.dto.WorkoutDto
import fitcon.entity.Exercise
import fitcon.entity.Workout
import fitcon.repository.ExerciseRepository
import fitcon.repository.WorkoutRepository
import fitcon.service.ExerciseService
import org.springframework.stereotype.Service

@Service
class ExerciseServiceImpl(
    val exerciseRepository: ExerciseRepository,
    val workoutRepository: WorkoutRepository
): ExerciseService {
    override fun findExerciseByName(name: String): Exercise? {
        return exerciseRepository.findByExerciseName(name)
    }

    override fun findAllExerciseByWorkout(workoutDto: WorkoutDto): List<Exercise> {
        val workout = workoutRepository.findByName(workoutDto.name)
        return exerciseRepository.findAllByWorkoutId(workout!!)
    }

    override fun saveExercise(exerciseDto: ExerciseDto, workoutName: String) {
        val exercise = Exercise()
        exercise.exerciseName = exerciseDto.exerciseName
        exercise.info = exerciseDto.info
        exercise.reps = exerciseDto.reps
        exercise.series = exerciseDto.series
        exercise.workoutId = workoutRepository.findByName(workoutName)
        exerciseRepository.save(exercise)
    }

}
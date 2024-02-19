package fitcon.service.impl

import fitcon.dto.ExerciseDto
import fitcon.dto.WorkoutDto
import fitcon.entity.Exercise
import fitcon.entity.Workout
import fitcon.repository.ExerciseRepository
import fitcon.repository.WorkoutRepository
import fitcon.service.ExerciseService
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class ExerciseServiceImpl(
    val exerciseRepository: ExerciseRepository,
    val workoutRepository: WorkoutRepository
): ExerciseService {
    override fun findExerciseByName(name: String): Exercise? {
        return exerciseRepository.findByExerciseName(name)
    }

    override fun findAllExercisesByWorkout(workout: Workout?): List<ExerciseDto> {
        val exercises = exerciseRepository.findAllByWorkoutId(workout!!)
        return exercises
            .stream()
            .map { exercise -> mapToExerciseDto(exercise!!) }
            .collect(Collectors.toList())
    }

    override fun saveExercise(exerciseDto: ExerciseDto, workoutName: String, workoutId: Long?) {
        val exercise = Exercise()
        exercise.exerciseName = exerciseDto.exerciseName
        exercise.info = exerciseDto.info
        exercise.reps = exerciseDto.reps
        exercise.series = exerciseDto.series
        exercise.workoutId = workoutId?.let { workoutRepository.findById(it).orElse(null) }
            ?: workoutRepository.findByName(workoutName)
        exerciseRepository.save(exercise)
    }

    override fun saveExercises(exerciseDtoList: List<ExerciseDto>?, workoutName: String, workoutId: Long?) {
        exerciseDtoList?.forEach {
                saveExercise(it, workoutName, workoutId)
            }
    }

    private fun mapToExerciseDto(exercise: Exercise): ExerciseDto{
        val exerciseDto = ExerciseDto()
        exerciseDto.exerciseName = exercise.exerciseName
        exerciseDto.info = exercise.info
        exerciseDto.reps = exercise.reps
        exerciseDto.series = exercise.series
        return exerciseDto
    }

}
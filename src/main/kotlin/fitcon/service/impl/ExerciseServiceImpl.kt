package fitcon.service.impl

import fitcon.dto.ExerciseDto
import fitcon.dto.WorkoutDto
import fitcon.entity.Exercise
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

    override fun findAllExerciseByWorkout(workoutDto: WorkoutDto): List<ExerciseDto> {
        val workout = workoutRepository.findByName(workoutDto.name)
        val exercises = exerciseRepository.findAllByWorkoutId(workout!!)
        return exercises
            .stream()
            .map { exercise -> mapToExerciseDto(exercise!!) }
            .collect(Collectors.toList())
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

    override fun saveExercises(exerciseDtoList: List<ExerciseDto>?, workoutName: String) {
        exerciseDtoList?.forEach {
                saveExercise(it, workoutName)
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
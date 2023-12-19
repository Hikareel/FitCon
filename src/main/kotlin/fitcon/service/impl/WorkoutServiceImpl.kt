package fitcon.service.impl

import fitcon.dto.WorkoutDto
import fitcon.entity.User
import fitcon.entity.Workout
import fitcon.repository.UserRepository
import fitcon.repository.WorkoutRepository
import fitcon.service.WorkoutService
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class WorkoutServiceImpl(
    val workoutRepository: WorkoutRepository,
    val userRepository: UserRepository
): WorkoutService {
    override fun findWorkoutByName(name: String): Workout? {
        return workoutRepository.findByName(name)
    }

    override fun findAllWorkouts(): List<WorkoutDto> {
        val workouts = workoutRepository.findAll()
        return workouts
                .stream()
                .map { workout -> mapToWorkoutDto(workout!!) }
                .collect(Collectors.toList())
    }

    override fun saveWorkout(workoutDto: WorkoutDto, userEmail: String) {
        val workout = Workout()
        workout.name = workoutDto.name
        workout.description = workoutDto.description
        workout.createdBy = userRepository.findByEmail(userEmail)
        workoutRepository.save(workout)
    }

    override fun findWorkoutsByUserEmail(userEmail: String): List<WorkoutDto> {
        val user = userRepository.findByEmail(userEmail)
        val workouts = workoutRepository.findByCreatedBy(user!!)
        return workouts
                .stream()
                .map { workout -> mapToWorkoutDto(workout!!) }
                .collect(Collectors.toList())
    }

    private fun mapToWorkoutDto(workout: Workout): WorkoutDto{
        val workoutDto = WorkoutDto()
        workoutDto.name = workout.name
        workoutDto.description = workout.description
        return workoutDto
    }
}
package fitcon.service

import fitcon.dto.WorkoutDto
import fitcon.entity.User
import fitcon.entity.Workout

interface WorkoutService {
    fun findWorkoutByName(name: String): Workout?
    fun findAllWorkouts(): List<WorkoutDto>
    fun saveWorkout(workoutDto: WorkoutDto, userEmail: String)
    fun findWorkoutsByUserEmail(userEmail: String): List<WorkoutDto>
}
package jobrunner

object Special {
  fun prepare(job: Job){
    println("Special prepare for job ${job.id}")
    Jobs.transition(job, State.PREPARED)
  }
}

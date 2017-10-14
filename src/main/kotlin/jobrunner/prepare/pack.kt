package jobrunner

object Pack {
  fun prepare(job: Job){
    println("Pack prepare for job ${job.id}")
    Jobs.transition(job, State.PREPARED)
  }
}

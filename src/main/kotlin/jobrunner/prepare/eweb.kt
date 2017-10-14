package jobrunner

object EWeb {
  fun prepare(job: Job){
    println("EWeb prepare for job ${job.id}")
    Jobs.transition(job, State.PREPARED)
  }
}

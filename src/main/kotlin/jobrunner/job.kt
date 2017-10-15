package jobrunner

import java.sql.*
import java.util.*
import java.io.*
import java.util.Random
import java.util.UUID
import java.util.Date

enum class State {
  CREATED,
  PREPARED,
  PROCESSED
}

enum class JobType {
  PACK,
  EWEB,
  SPECIAL;
  companion object {
    fun random() : JobType = JobType.values()[random.nextInt(3)]
  }
}

data class Job(
  var id: Int? = null,
  val subject: String,
  var state: State,
  var type: JobType,
  val created_at: java.util.Date = java.util.Date(),
  var updated_at: java.util.Date? = null
)

object Jobs {
  fun all() : List<Job> = sql_to_jobs("SELECT * FROM jobs")

  fun unprocessed() : List<Job> = sql_to_jobs("SELECT * FROM jobs WHERE state != 'PROCESSED'")

  fun generate(sleep: Long) {
    while(true){
      create()
      Thread.sleep(sleep)
    }
  }

  fun create() {
    val connection = Database.handle()
    val sql = "INSERT INTO jobs (subject,state,type,created_at) VALUES (?,?,?,?)"
    val stmt = connection.prepareStatement(sql)
    stmt.setString(1, UUID.randomUUID().toString()) // subject
    stmt.setString(2, State.CREATED.toString()) // state
    stmt.setString(3, JobType.random().toString()) // type
    stmt.setTimestamp(4, Timestamp(System.currentTimeMillis())) // created_at
    stmt.executeUpdate()
    connection.close()
  }

  fun work(job: Job) {
    while (job.state != State.PROCESSED){
      when (job.state) {
        State.CREATED -> Jobs.prepare(job)
        State.PREPARED -> Jobs.process(job)
        else -> {
          // Jobs.fail(job)
        }
      }
      Thread.sleep((random.nextInt(10000+1 - 1000)+1000).toLong())
    }
  }

  fun prepare(job: Job) {
    when (job.type){
      JobType.PACK -> Pack.prepare(job)
      JobType.EWEB -> EWeb.prepare(job)
      JobType.SPECIAL -> Special.prepare(job)
      /*else -> {
        //Jobs.fail(job)
      }*/
    }
  }

  fun process(job: Job) {
    Jobs.transition(job, State.PROCESSED)
  }

  fun transition(job: Job, newState: State) {
    val connection = Database.handle()
    val sql = "UPDATE jobs SET state=? WHERE id=?"
    val stmt = connection.prepareStatement(sql)
    stmt.setString(1, newState.toString())
    stmt.setInt(2, job.id!!)
    stmt.executeUpdate()
    job.state = newState
    connection.close()
  }

  private fun sql_to_jobs(sql: String): List<Job> {
    val connection = Database.handle()
    val statement = connection.createStatement()
    val rs = statement.executeQuery(sql)
    val jobs = mutableListOf<Job>()
    while (rs.next()) {
      jobs.add(row_to_job(rs))
    }
    connection.close()
    return jobs
  }

  private fun row_to_job(row: ResultSet): Job {
    return Job(
      id = row.getInt("id"),
      subject = row.getString("subject"),
      state = State.valueOf(row.getString("state")),
      type = JobType.valueOf(row.getString("type")),
      created_at = row.getDate("created_at"),
      updated_at = row.getDate("updated_at")
    )
  }
}

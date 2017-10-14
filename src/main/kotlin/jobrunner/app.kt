package jobrunner

import java.sql.*
import java.util.Random
import java.util.concurrent.*
import kotlin.concurrent.thread

val random = Random()

fun main(args: Array<String>) {
  // Just generate jobs forever
  thread(block = { Jobs.generate(100) })
  val executor = Executors.newCachedThreadPool()
  while(true) {
    val jobs = Jobs.unprocessed()
    for( job in jobs ) {
      executor.submit({ Jobs.work(job) })
    }
    println("Sleeping for 10 seconds before grabbing more")
    Thread.sleep(10_000)
  }
}

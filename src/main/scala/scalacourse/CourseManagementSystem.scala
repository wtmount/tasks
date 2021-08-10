package scalacourse

import scala.util.Random

class CourseManagementSystem(courseName: String, developers: List[TaskExecutor], tasks: List[Task]) {
  print(s"${"-" * 50}\nWelcome to $courseName\n${"-" * 50}\n\n")

  def delegateTasks(): Unit = {
    val devsForLecture = Random.shuffle(developers)
    val it = tasks.iterator
    devsForLecture.foreach(_.doTask(it.next))
  }
}

trait TaskExecutor {
  def doTask(task: Task)
}

case class Developer(name: String) extends TaskExecutor {
  override def doTask(task: Task): Unit = println(s"Developer $name started task $task")
}

class Manager(courseManagementSystem: CourseManagementSystem) {
  def command(): Unit = courseManagementSystem.delegateTasks()
}

trait Task {
  val description: String
  override def toString: String = description
}

object PrepareLecture extends Task {
  override val description: String = "Prepare the lecture"
}

object PresentLecture extends Task {
  override val description: String = "Present the lecture"
}

object ReviewHomework extends Task {
  override val description: String = "Review the homework"
}

object QASession extends Task {
  override val description: String = "Q&A session"
}
package scalacourse

object Main extends App {
  val scalaCourse = new CourseManagementSystem(
    "Scala course",
    List("Jack", "John", "Jim", "Jason").map(Developer.apply),
    List(PrepareLecture, PresentLecture, ReviewHomework, QASession))
  val manager = new Manager(scalaCourse)
  val NumberOfLectures = 14
  for (i <- 1 to NumberOfLectures) {
    println(s"Lecture $i")
    manager.command()
    println()
    Thread.sleep(1000)
  }
}

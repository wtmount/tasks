package implicits

import org.scalatest.flatspec.AnyFlatSpec
import Orderings.ordering

class CsvEncoderTest extends AnyFlatSpec {

  behavior of "CsvEncoder"

  it should "encode List of Int to CSV in descending order" in {
    val encoder = new CsvEncoder[Int]
    assert(encoder.toCSV(List(1, 10, 5)) == "10,5,1")
  }

  it should "encode List of Double to CSV in descending order" in {
    val encoder = new CsvEncoder[Double]
    assert(encoder.toCSV(List(3.5, 5.8, 8.9)) == "8.9,5.8,3.5")
  }

  it should "encode List of String to CSV in descending order" in {
    val encoder = new CsvEncoder[String]
    assert(encoder.toCSV(List("Jack", "Bob", "Mike")) == "Mike,Jack,Bob")
  }
}

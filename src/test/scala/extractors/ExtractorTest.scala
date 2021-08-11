package extractors

import org.scalatest.flatspec.AnyFlatSpec

import java.io.ByteArrayOutputStream

class ExtractorTest extends AnyFlatSpec {

  behavior of "Extractors"

  it should "return email address when user and domain parts provided" in {
    assert(Email("user", "domain") == "user@domain")
  }

  it should "return local and domain parts of email when correct email provided" in {
    assert(Email.unapply("user@domain") == Some("user", "domain"))
  }

  it should "return None when incorrect email provided" in {
    assert(Email.unapply("user.domain").isEmpty)
  }

  it should "return subdomains starting with top domain when domain provided" in {
    assert(Domain.unapplySeq("domain.com").contains(Seq("com", "domain")))
  }

  it should "return only domains that contain in.ua" in {
    assert(Extractor.getDomainsInUa(List("something.in.ua", "something.ua")) == List("something.in.ua"))
  }

  it should "print only emails in gmail.com domain" in {
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      Extractor.printEmailsInGmailDomain(Array("email@notgmail.com", "email@gmail.com"))
    }
    assert(output.toString == "email@gmail.com\n")
  }
}

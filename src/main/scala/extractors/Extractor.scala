package extractors

object Extractor {
  def getDomainsInUa(domains: List[String]): List[String] = domains.filter(isDomainInUa)

  private def isDomainInUa(domain: String): Boolean = {
    domain match {
      case Domain("ua", "in", _*) => true
      case _ => false
    }
  }

  def printEmailsInGmailDomain(emails: Array[String]): Unit = getEmailsInGmailDomain(emails).foreach(println)

  private def getEmailsInGmailDomain(emails: Array[String]): Array[String] = emails.filter(isEmailInGmailDomain)

  private def isEmailInGmailDomain(email: String): Boolean = {
    email match {
      case Email(_, Domain("com", "gmail")) => true
      case _ => false
    }
  }
}

object Email {
  def apply(localPart: String, domain: String): String = localPart + "@" + domain

  def unapply(email: String): Option[(String, String)] = {
    email.split("@") match {
      case Array(first, second) => Some(first, second)
      case _ => None
    }
  }
}

object Domain {
  def unapplySeq(domain: String): Option[Seq[String]] = Some(domain.split("\\.").reverse)
}
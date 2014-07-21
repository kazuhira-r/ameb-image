package org.littlewings.amebimage

object AmebloUrlRepository {
  private val BASE_URL: String = "http://ameblo.jp"

  def asEntryList(amebaId: String): String =
    withAmebaId(amebaId)("entrylist.html")

  def asEntryList(amebaId: String, index: Int): String =
    withAmebaId(amebaId)(s"entrylist-${index}.html")

  def extractRelativeImagePath(imageUrlAsString: String): Option[String] = {
    val regex = """http://stat.ameba.jp/user_images/(.+)""".r

    imageUrlAsString match {
      case regex(relativePath) => Some(relativePath)
      case _ => None
    }
  }

  def extractLastPageIndex(entryListLastPageUrl: String): Int = {
    val matches =
      """\d+"""
        .r
        .findAllIn(entryListLastPageUrl)
        .toList

    matches match {
      case Nil => 1
      case _ => matches.last.toInt
    }
  }

  private def withAmebaId(amebaId: String)(detail: String): String =
    s"$BASE_URL/$amebaId/$detail"
}

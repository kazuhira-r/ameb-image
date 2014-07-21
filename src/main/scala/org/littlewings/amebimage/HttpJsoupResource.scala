package org.littlewings.amebimage

import scala.collection.JavaConverters._

import java.io.InputStream

import dispatch._
import dispatch.Defaults._

import org.jsoup.nodes.Element

object HttpJsoupResource {
  private val USER_AGENT: String = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)"

  private def newRequest(urlAsString: String): Req =
    url(urlAsString) <:< Map("User-Agent" -> USER_AGENT)

  private def executeHttp[T](urlAsString: String)(fun: (Req, Http) => T): T = {
    val request = newRequest(urlAsString)
    val http = Http.configure(_ setFollowRedirects true)

    try {
      fun(request, http)
    } finally {
      http.shutdown()
    }
  }

  def select(urlAsString: String, selector: String): Iterable[Element] = {
    val document =
      executeHttp(urlAsString) { (request, http) =>
        http.apply(request OK as.jsoup.Document).apply()
      }

    document.select(selector).asScala
  }

  def downloadAsStream(urlAsString: String): InputStream =
    executeHttp(urlAsString) { (request, http) =>
      http.apply(request OK ToInputStream).apply()
    }
}

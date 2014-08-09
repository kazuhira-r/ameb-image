package org.littlewings.amebimage

import java.util.concurrent.TimeUnit

import scopt.OptionParser

import org.apache.logging.log4j.LogManager

object PageSpecCollector {
  val SLEEP_TIME_UNIT: TimeUnit = TimeUnit.SECONDS

  def main(args: Array[String]): Unit = {
    val parser =
      PageSpecCollectorOption
        .newParser(getClass.getSimpleName.init.mkString)

    val option =
      parser
        .parse(args, PageSpecCollectorOption())
        .getOrElse {
          sys.exit(1)
        }

    val logger = LogManager.getLogger(getClass)

    logger.info("出力先ディレクトリ = {}", option.outputDir)
    logger.info("ダウンロード対象のページのURL = {}", option.pageUrl)

    val pageImageDownloader = new PageImageDownloader(option.outputDir)
    pageImageDownloader.execute(option.pageUrl)
  }
}

object PageSpecCollectorOption {
  def newParser(mainClassName: String): OptionParser[PageSpecCollectorOption] =
    new OptionParser[PageSpecCollectorOption](mainClassName) {
      opt[String]('o', "output-dir") required() valueName("<output-dir>") action { (x, o) =>
        o.copy(outputDir = x)
      } text("出力先ディレクトリ [必須]")

      opt[String]('u', "page-url") valueName("<page-url>") action { (x, o) =>
        o.copy(pageUrl = x)
      } text("ダウンロード対象のページのURL")
    }
}

case class PageSpecCollectorOption(outputDir: String = "",
                                   pageUrl: String = "")

package org.littlewings.amebimage

import java.util.concurrent.TimeUnit

import scopt.OptionParser

import org.slf4j.LoggerFactory

object LimitedAutoFullCollector {
  val SLEEP_TIME_UNIT: TimeUnit = TimeUnit.SECONDS

  def main(args: Array[String]): Unit = {
    val parser =
      LimitedAutoFullCollectorOption
        .newParser(getClass.getSimpleName.init.mkString)

    val option =
      parser
        .parse(args, LimitedAutoFullCollectorOption())
        .map { option =>
          if (option.outputDir.isEmpty)
            option.copy(outputDir = option.amebaId)
          else
            option
        } getOrElse {
          sys.exit(1)
        }

    val logger = LoggerFactory.getLogger(getClass)

    logger.info("アメーバID = {}", option.amebaId)
    logger.info("出力先ディレクトリ = {}", option.outputDir)
    logger.info("スリープ秒数 = {}秒", option.sleepTime)

    val entryListScanner = new EntryListScanner(option.amebaId)
    val pageImageDownloader = new PageImageDownloader(option.outputDir)

    val indexRange = entryListScanner.scan

    logger.info("アクセス対象の一覧ページ数（entrylist）= {}", indexRange.size)

    indexRange.foreach { index =>
      val entryRange = entryListScanner.scan(index)

      logger.info("entrylist-{}内のページ数 = {}", index, entryRange.size)

      entryRange.foreach { pageUrl =>
        logger.info("アクセス対象のページ[{}]", pageUrl)
        pageImageDownloader.execute(pageUrl)

        SLEEP_TIME_UNIT.sleep(option.sleepTime)
      }

      SLEEP_TIME_UNIT.sleep(option.sleepTime)
    }
  }
}

object LimitedAutoFullCollectorOption {
  def newParser(mainClassName: String): OptionParser[LimitedAutoFullCollectorOption] =
    new OptionParser[LimitedAutoFullCollectorOption](mainClassName) {
      opt[String]('a', "ameba-id") required() valueName("<ameba-id>") action { (x, o) =>
        o.copy(amebaId = x)
      } text("アメーバID [必須]")

      opt[String]('o', "output-dir") valueName("<output-dir>") action { (x, o) =>
        o.copy(outputDir = x)
      } text("出力先ディレクトリ （省略時、アメーバIDと同じ）")

      opt[Long]('s', "sleep-time") valueName("<sleep-time>") action { (x, o) =>
        o.copy(sleepTime = x)
      } text("各ページ、一覧ページ（entrylist）にアクセスする際にスリープする秒数 （デフォルト：10秒）")
    }
}

case class LimitedAutoFullCollectorOption(amebaId: String = "",
                                          outputDir: String = "",
                                          sleepTime: Long = 10)

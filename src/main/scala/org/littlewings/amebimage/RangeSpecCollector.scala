package org.littlewings.amebimage

import java.util.concurrent.TimeUnit

import scopt.OptionParser

import org.slf4j.LoggerFactory

object RangeSpecCollector {
  val SLEEP_TIME_UNIT: TimeUnit = TimeUnit.SECONDS

  def main(args: Array[String]): Unit = {
    val parser =
      RangeSpecCollectorOption
        .newParser(getClass.getSimpleName.init.mkString)

    val option =
      parser
        .parse(args, RangeSpecCollectorOption())
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
    logger.info("開始インデックス = {}", option.startIndex)
    logger.info("終了インデックス = {}", option.endIndex)
    logger.info("スリープ秒数 = {}秒", option.sleepTime)

    val entryListScanner = new EntryListScanner(option.amebaId)
    val pageImageDownloader = new PageImageDownloader(option.outputDir)

    (option.startIndex to option.endIndex).foreach { index =>
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

object RangeSpecCollectorOption {
  def newParser(mainClassName: String): OptionParser[RangeSpecCollectorOption] =
    new OptionParser[RangeSpecCollectorOption](mainClassName) {
      opt[String]('a', "ameba-id") required() valueName("<ameba-id>") action { (x, o) =>
        o.copy(amebaId = x)
      } text("アメーバID [必須]")

      opt[String]('o', "output-dir") valueName("<output-dir>") action { (x, o) =>
        o.copy(outputDir = x)
      } text("出力先ディレクトリ （省略時、アメーバIDと同じ）")

      opt[Int]('s', "start-index") valueName("<start-index>") action { (x, o) =>
        o.copy(startIndex = x)
      } text("entrylistの開始インデックス （省略時は1）")

      opt[Int]('e', "end-index") valueName("<end-index>") action { (x, o) =>
        o.copy(endIndex = x)
      } text("entrylistの終了インデックス （省略時は1）")

      opt[Long]('s', "sleep-time") valueName("<sleep-time>") action { (x, o) =>
        o.copy(sleepTime = x)
      } text("各ページ、一覧ページ（entrylist）にアクセスする際にスリープする秒数 （デフォルト：10秒）")

      checkConfig { c =>
        if (c.startIndex > c.endIndex)
          failure("開始インデックスは、終了インデックスより小さい値で指定してください")
        else
          success
      }
    }
}

case class RangeSpecCollectorOption(amebaId: String = "",
                                    outputDir: String = "",
                                    startIndex: Int = 1,
                                    endIndex: Int = 1,
                                    sleepTime: Long = 10)

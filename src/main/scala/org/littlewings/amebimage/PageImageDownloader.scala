package org.littlewings.amebimage

import resource._

import java.io.BufferedOutputStream
import java.nio.file.{Files, Paths, StandardOpenOption}

import org.slf4j.LoggerFactory

class PageImageDownloader(outputDir: String) {
  def execute(pageUrlAsString: String): Unit = {
    HttpJsoupResource
      .select(pageUrlAsString, ".detailOn img")
      .map(_.attr("src"))
      .foreach { url =>
        managed(HttpJsoupResource.downloadAsStream(url)).foreach { is =>
          AmebloUrlRepository.extractRelativeImagePath(url).foreach { imagePath =>
            val logger = LoggerFactory.getLogger(getClass)

            val savePath = Paths.get(s"$outputDir/$imagePath")
            val parent = savePath.getParent

            if (Files.notExists(parent)) {
              Files.createDirectories(parent)
              logger.info("ディレクトリ[{}]を作成しました", parent)
            }

            logger.info("[{}]から画像をダウンロード", url)

            for {
              os <- managed(Files.newOutputStream(savePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))
              bos <- managed(new BufferedOutputStream(os))
            } {
              Iterator
                .continually(is.read())
                .takeWhile(_ != -1)
                .foreach(bos.write)

              logger.info("ダウンロードしたファイルを、[{}]に保存しました", savePath)
            }
          }
        }
      }
  }
}

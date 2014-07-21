package org.littlewings.amebimage

class EntryListScanner(amebaId: String) {
  def scan: Iterable[Int] = {
    val lastPageUrl =
      HttpJsoupResource
        .select(AmebloUrlRepository.asEntryList(amebaId), ".lastPage")
        .head
        .attr("href")

    val lastIndex = AmebloUrlRepository.extractLastPageIndex(lastPageUrl)

    1 to lastIndex
  }

  def scan(index: Int): Iterable[String] = {
    HttpJsoupResource
      .select(AmebloUrlRepository.asEntryList(amebaId, index), ".newentrytitle a")
      .map(_.attr("href"))
  }
}

package org.littlewings.amebimage

import com.ning.http.client

object ToInputStream extends (client.Response => java.io.InputStream) {
  def apply(r: client.Response): java.io.InputStream =
    r.getResponseBodyAsStream
}

package org.littlewings.amebimage

import java.io.InputStream

import com.ning.http.client

object AsInputStream extends (client.Response => InputStream) {
  def apply(r: client.Response): InputStream =
    r.getResponseBodyAsStream
}

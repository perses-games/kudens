package games.perses.kudens.net

import org.w3c.xhr.XMLHttpRequest

/**
 * User: rnentjes
 * Date: 30-7-16
 * Time: 16:39
 */

actual fun getUrlAsString(url: String): String {
  val req = XMLHttpRequest()

  req.open("GET", url, false)
  req.send(null)

  return req.responseText
}

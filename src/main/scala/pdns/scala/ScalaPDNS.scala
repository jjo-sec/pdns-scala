package pdns.scala

import scalaj.http._
import org.json4s._
import org.json4s.native.JsonMethods._


/** Represents a Common Output Format formatted result */
case class COFQueryResponse(count: Int,time_first: Int, time_last: Int, rrname: String, rrtype: String, rdata: String)

/** Simple HTTP-based API query and response parsing class
  *
  * @constructor create a new query instance for the specified service, defaults to CIRCL.LU
  * @param xUrl URL of the Passive DNS web service to query
  * @param xUser Username for web service (optional)
  * @param xPasswd Password for web service (optional)
  * @param xAuthToken Authentication token to pass (optional)
  */
class ScalaPdns(xUrl: String = "https://www.circl.lu/pdns/query", xUser: String = null, xPasswd: String = null, xAuthToken: String = null) {
  var url: String = xUrl
  var user: String = xUser
  var passwd: String = xPasswd
  var authToken: String = xAuthToken

  /** Query web-service for a domain-name or IP address
    *
    * @param q Domain name or IP Address to query
    * @param sortBy Optional sorting parameter to pass to the web service
    * @return Array of COFQueryResponse objects containing PDNS results
    */
  def query(q: String,sortBy: String = "time_last"): Array[COFQueryResponse] = {

    var request: HttpRequest = Http("%s/%s".format(url,q))
    if (authToken != null) {
      request = request.header("authorization",authToken)
    }
    if (user != null && passwd != null) {
      request = request.auth(user,passwd)
    }
    val response = request.asString
    var ret: Array[COFQueryResponse] = Array[COFQueryResponse]()
    if (response.code == 200) {
      ret  = response.body.trim().split("\n").map(parseJson)
    } else {
      println("Non-200 error code returned: %s".format(response.statusLine))
    }
    return ret
  }

  /** Method that will extract JSON to the COFQuery response case class
    *
    * @param json JSON-formatted COF response as a string
    * @return COFQueryResponse object representing JSON string
    */
  def parseJson(json: String): COFQueryResponse = {
    implicit val formats = DefaultFormats
    val j = parse(json)
    return j.extract[COFQueryResponse]
  }
}
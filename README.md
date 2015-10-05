# pdns-scala
Attempt at a Scala translation of CIRCL.LU's PyPDNS Python module https://github.com/CIRCL/PyPDNS

# Example Usage
```scala
    scala> import pdns.scala.ScalaPdns
    scala> pdns = new ScalaPdns("https://www.circl.lu/pdns/query","<circl_user>","<circl_passwd>")
    scala> results = pdns.query("google.com")
    res0: Array[pdns.scala.COFQueryResponse] = Array(COFQueryResponse(<count>,<time_first>,<time_last>,google.com,<rrtype>,<rdata>),...
```

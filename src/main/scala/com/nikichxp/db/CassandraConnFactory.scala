package com.nikichxp.db

import java.net.InetSocketAddress

import com.datastax.oss.driver.api.core.CqlSession

object CassandraConnFactory {

  val connection: CqlSession = createConnection()

  private def createConnection() : CqlSession = {
    val conn = createConnectionInt()
    println(conn)
    conn
  }

  private def createConnectionInt() = CqlSession.builder()
    .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
    .withKeyspace("test")
    .withLocalDatacenter("datacenter1")
    .build()

}

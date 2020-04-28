package com.nikichxp.db

import java.net.InetSocketAddress

import com.datastax.oss.driver.api.core.CqlSession

object CassandraConnFactory {

  val connection: CqlSession = createConnection()

  private def createConnection() = CqlSession.builder()
    .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
    .withLocalDatacenter("dc1")
    .build();

}

// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-06

package nz.co.skepticalhumorist.sql

import oracle.jdbc.pool.OracleDataSource

object Test extends Application {
  val dataSource = new OracleDataSource
  dataSource.setURL("jdbc:oracle:thin:@localhost:1521:ORCL")
  dataSource.setUser("scalasql")
  dataSource.setPassword("scalasql")
  val db = new Db(dataSource)
  db.query("SELECT * FROM test WHERE name LIKE '%' || ? || '%'", "T") {resultSet =>
    println(resultSet.getString("name"))
  }
}

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
  println("Result 1:")
  db.query("SELECT * FROM test WHERE name LIKE '%' || ? || '%'", "T") {resultSet =>
    println(resultSet.getString("name"))
  }

  println("Result 2:")
  val rows = db.rows("SELECT * FROM test WHERE name NOT LIKE '%' || ? || '%'", "T") {meta =>
    println(meta.getColumnName(1) + ", " + meta.getColumnName(2))
  }
  for (row <- rows) {
    println(row(0) + ", " + row(1))
  }
}

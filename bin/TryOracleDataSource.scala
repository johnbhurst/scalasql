// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-07

import oracle.jdbc.pool.OracleDataSource

val dataSource = new OracleDataSource
dataSource.setURL("jdbc:oracle:thin:@localhost:1521:ORCL")
dataSource.setUser("scalasql")
dataSource.setPassword("scalasql")
val connection = dataSource.getConnection
println(connection.getAutoCommit)

connection.setAutoCommit(false)
println(connection.getAutoCommit)


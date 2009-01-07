// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-07

package nz.co.skepticalhumorist.sql

import javax.sql.DataSource
import oracle.jdbc.pool.OracleDataSource
import org.junit.Before
import org.junit.Test

class DbTest {

  def oracleDataSource() : DataSource = {
    val dataSource = new OracleDataSource
    dataSource.setURL("jdbc:oracle:thin:@localhost:1521:ORCL")
    dataSource.setUser("scalasql")
    dataSource.setPassword("scalasql")
    dataSource
  }

  @Before
  def setupData {
    println("setupData():")
    val db = new Db(oracleDataSource)
    db.execute("DELETE FROM test")
    db.execute("INSERT INTO test VALUES (1, 'ONE')")
    db.execute("INSERT INTO test VALUES (2, 'TWO')")
    db.execute("INSERT INTO test VALUES (3, 'THREE')")
  }

  @Test
  def testConstructWithDataSource {
    // JH_TODO
  }

  @Test
  def testConstructWithConnection {
    // JH_TODO
  }

  @Test
  def testNewInstanceWithUrl {
    // JH_TODO
  }

  @Test
  def testNewInstanceWithUrlAndProperties {
    // JH_TODO
  }

  @Test
  def testNewInstanceWithUrlAndPropertiesAndDriverClassName {
    // JH_TODO
  }

  @Test
  def testNewInstanceWithUrlAndDriverClassName {
    // JH_TODO
  }

  @Test
  def testNewInstanceWithUrlAndUserAndPassword {
    // JH_TODO
  }

  @Test
  def testNewInstanceWithUrlAndUserAndPasswordAndDriverClassName {
    // JH_TODO
  }

  @Test
  def testCall {
    // JH_TODO
  }

  @Test
  def testCallResultSet {
    // JH_TODO
  }

  @Test
  def testCommit {
    // JH_TODO
  }

  @Test
  def testExecute {
    println("testExecute():")
    // JH_TODO?
  }

  @Test
  def testExecuteInsert {
    // JH_TODO
  }

  @Test
  def testExecuteUpdate {
    // JH_TODO
  }

  @Test
  def testFirstRow {
    // JH_TODO
  }

  @Test
  def testFirstRowMeta {
    // JH_TODO
  }

  @Test
  def testQuery {
    // JH_TODO
  }

  @Test
  def testQueryMeta {
    // JH_TODO
  }

  @Test
  def testRollback {
    // JH_TODO
  }

  @Test
  def testRows {
    // JH_TODO
  }

  @Test
  def testRowsMeta {
    // JH_TODO
  }



}


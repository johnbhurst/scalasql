// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-07

package nz.co.skepticalhumorist.sql

import java.sql.ResultSetMetaData
import javax.sql.DataSource
import oracle.jdbc.pool.OracleDataSource
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import java.math.{BigDecimal => BigDec}

class DbTest {

  def oracleDataSource: DataSource = {
    val dataSource = new OracleDataSource
    dataSource.setURL("jdbc:oracle:thin:@localhost:1521:ORCL")
    dataSource.setUser("scalasql")
    dataSource.setPassword("scalasql")
    dataSource
  }

  def db: Db = new Db(oracleDataSource)

  var metaRun = false

  def assertMeta(meta: ResultSetMetaData) {
    assertEquals("ID", meta.getColumnName(1))
    assertEquals("NAME", meta.getColumnName(2))
    assertEquals(2, meta.getColumnCount)
    assertTrue(!metaRun)
    metaRun = true
  }

  def bd(i: Int): BigDec = new BigDec(i)

  @Before
  def setupData {
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
    val row = db.firstRow("SELECT * FROM test WHERE id < ? ORDER BY id", int2Integer(3))
    assertEquals(bd(1), row(0))
    assertEquals("ONE", row(1))
    assertFalse(metaRun)
  }

  @Test
  def testFirstRowMeta {
    val row = db.firstRowMeta("SELECT * FROM test WHERE id < ? ORDER BY id DESC", int2Integer(3)) (assertMeta)
    assertEquals(bd(2), row(0))
    assertEquals("TWO", row(1))
    assertTrue(metaRun)
  }

  @Test
  def testQuery {
    var l = List("TWO", "THREE")
    db.query("SELECT * FROM test WHERE name LIKE '%' || ? || '%' ORDER BY id", "T") {resultSet =>
      assertEquals(l.head, resultSet.getString("name"))
      l = l.tail
    }
    assertTrue(l.isEmpty)
    assertFalse(metaRun)
  }

  @Test
  def testQueryMeta {
    var l = List("TWO", "THREE")
    db.queryMeta("SELECT * FROM test WHERE name LIKE '%' || ? || '%' ORDER BY id", "T") (assertMeta) {resultSet =>
      assertEquals(l.head, resultSet.getString("name"))
      l = l.tail
    }
    assertTrue(l.isEmpty)
    assertTrue(metaRun)
  }

  @Test
  def testQueryForValue {
    assertEquals(bd(1), db.queryForValue("SELECT id FROM test WHERE id = ?", int2Integer(1)))
    assertEquals("ONE", db.queryForValue("SELECT name FROM test WHERE id = ?", int2Integer(1)))
  }

  @Test
  def testRollback {
    // JH_TODO
  }

  @Test
  def testRows {
    var l = List("ONE")
    val rows = db.rows("SELECT * FROM test WHERE name NOT LIKE '%' || ? || '%' ORDER BY id", "T")
    for (row <- rows) {
      assertEquals(l.head, row(1))
      l = l.tail
    }
    assertTrue(l.isEmpty)
    assertFalse(metaRun)
  }

  @Test
  def testRowsMeta {
    var l = List("ONE")
    val rows = db.rowsMeta("SELECT * FROM test WHERE name NOT LIKE '%' || ? || '%' ORDER BY id", "T") (assertMeta)
    for (row <- rows) {
      assertEquals(l.head, row(1))
      l = l.tail
    }
    assertTrue(l.isEmpty)
    assertTrue(metaRun)
  }

}


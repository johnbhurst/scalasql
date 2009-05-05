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
import org.junit.Assert.fail
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
    val db = new Db(oracleDataSource)
    assertTrue(db.dataSource != null)
    assertTrue(db.connection == null)
  }

  @Test
  def testConstructWithConnection {
    val connection = oracleDataSource.getConnection
    val db = new Db(connection)
    assertTrue(db.dataSource == null)
    assertTrue(db.connection != null)
  }

  @Test
  def testNewInstanceWithUrl {
    val db = Db("jdbc:oracle:thin:scalasql/scalasql@localhost:1521:ORCL")
    assertOptionEquals(3, db.queryForInt("SELECT COUNT(*) FROM test"))
  }

  @Test
  def testNewInstanceWithUrlAndProperties {
    // JH_TODO: Need Oracle-specific properties to test, or another DB
  }

  @Test
  def testNewInstanceWithUrlAndPropertiesAndDriverClassName {
    // JH_TODO: Need Oracle-specific properties to test, or another DB
  }

  @Test
  def testNewInstanceWithUrlAndDriverClassName {
    val db = Db("jdbc:oracle:thin:scalasql/scalasql@localhost:1521:ORCL", "oracle.jdbc.OracleDriver")
    assertOptionEquals(3, db.queryForInt("SELECT COUNT(*) FROM test"))
  }

  @Test
  def testNewInstanceWithUrlAndUserAndPassword {
    val db = Db("jdbc:oracle:thin:@localhost:1521:ORCL", "scalasql", "scalasql")
    assertOptionEquals(3, db.queryForInt("SELECT COUNT(*) FROM test"))
  }

  @Test
  def testNewInstanceWithUrlAndUserAndPasswordAndDriverClassName {
    val db = Db("jdbc:oracle:thin:@localhost:1521:ORCL", "scalasql", "scalasql", "oracle.jdbc.OracleDriver")
    assertOptionEquals(3, db.queryForInt("SELECT COUNT(*) FROM test"))
  }

  @Test
  def testCommit {
    // JH_TODO
  }

  @Test
  def testRollback {
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
  def testFirstRowWithResult {
    db.firstRow("SELECT * FROM test WHERE id < ? ORDER BY id", int2Integer(3)) match {
      case Some(row) =>
        assertEquals(bd(1), row(0))
        assertEquals("ONE", row(1))
        assertFalse(metaRun)
      case None => fail("No rows returned")
    }
  }

  @Test
  def testFirstRowWithoutResult {
    db.firstRow("SELECT * FROM test WHERE id < -1 ORDER BY id") match {
      case Some(row) => fail("Row returned, none expected")
      case None => assertFalse(metaRun)
    }
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

  def assertOptionEquals[T](expected: T, v: Option[T]) : Unit = {
    v match {
      case Some(actual) => assertEquals(expected, actual)
      case None => fail("Option None returned, value expected")
    }
  }

  def assertOptionNone[T](v: Option[T]) : Unit = {
    v match {
      case Some(actual) => fail("Option value returned, none expected")
      case None => assertTrue(true)
    }
  }

  @Test
  def testQueryFirst {
    assertOptionEquals(1, db.queryForInt("SELECT id FROM test WHERE id = ?", int2Integer(1)))
    assertOptionEquals("ONE", db.queryForString("SELECT name FROM test WHERE id = ?", int2Integer(1)))
    assertOptionNone(db.queryForInt("SELECT id FROM test WHERE id = -1"))
    assertOptionNone(db.queryForString("SELECT name FROM test WHERE id = -1"))
  }

  @Test
  def testQueryList {
    var l = List("TWO", "THREE")
    var ql = db.queryList("SELECT * FROM test WHERE name LIKE '%' ||  ? || '%' ORDER BY id", "T") {resultSet =>
      resultSet.getString("name")
    }
    while (!l.isEmpty) {
      assertEquals(l.head, ql.head)
      l = l.tail
      ql = ql.tail
    }
  }
}


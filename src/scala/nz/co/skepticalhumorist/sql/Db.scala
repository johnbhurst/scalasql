// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-06

package nz.co.skepticalhumorist.sql

import scala.collection.mutable.ListBuffer
import scala.runtime.RichBoolean
import java.sql._
import javax.sql._

class Db(dataSource: DataSource) {

  // JH_TODO: constructor with Connection
  // JH_TODO: newInstance with URL
  // JH_TODO: newInstance with URL, properties
  // JH_TODO: newInstance with URL, properties, driver class name
  // JH_TODO: newInstance with URL, driver class name
  // JH_TODO: newInstance with URL, user, password
  // JH_TODO: newInstance with URL, user, password, driver class name

  def call(sql: String, params: AnyRef*) = {
    // JH_TODO
  }

  def callResultSet(sql: String, params: AnyRef*)(f: ResultSet => Unit) = {
    // JH_TODO
  }

  def close = {
    // JH_TODO
  }

  def commit = {
    // JH_TODO
  }

//  def eachRow(sql: String, params: AnyRef*)(f: Seq[AnyRef] => Unit) = {
//    // JH_TODO
//  }
//
//  def eachRowMeta(sql: String, params: AnyRef*)(meta: ResultSetMetaData => Unit)(f: Seq[AnyRef] => Unit) = {
//    // JH_TODO
//  }
//

  def execute(sql: String, params: AnyRef*): Boolean = {
    prepareAndExecuteStatement(sql, params: _*) {_.execute}
  }

//  // JH_TODO
//  def executeInsert(sql: String, params: AnyRef*): List[Seq[AnyRef]] = {
//    List()
//  }

  def executeUpdate(sql: String, params: AnyRef*): Int = {
    prepareAndExecuteStatement(sql, params: _*) {_.executeUpdate()}
  }

  def firstRow(sql: String, params: AnyRef*) : Seq[AnyRef] = {
    firstRowMeta(sql, params: _*) {ResultSetMetaData => }
  }

  def firstRowMeta(sql: String, params: AnyRef*)(meta: ResultSetMetaData => Unit): Seq[AnyRef] = {
    prepareAndExecuteStatement(sql, params: _*) {
      executeFirstWithResultSet(_)(meta) {
        resultsToSeqRow(_)
      }
    }
  }

  // JH_TODO: getConnection?
  // JH_TODO: getDataSource?
  // JH_TODO: ResultSet concurrency?
  // JH_TODO: ResultSet holdability?
  // JH_TODO: ResultSet type?
  // JH_TODO: update count?

  def query(sql: String, params: AnyRef*)(f: ResultSet => Unit) {
    queryMeta(sql, params: _*) {ResultSetMetaData => } (f)
  }

  def queryMeta(sql: String, params: AnyRef*)(meta: ResultSetMetaData => Unit)(f: ResultSet => Unit) {
    prepareAndExecuteStatement(sql, params: _*) {
      executeWithResultSet(_)(meta) {
        f(_)
      }
    }
  }

  def queryForValue[T](sql: String, params: AnyRef*): T = {
    val row = firstRow(sql, params: _*)
    row(0).asInstanceOf[T]
  }

  def rollback() {
    // JH_TODO
  }

  def rows(sql: String, params: AnyRef*): List[Seq[AnyRef]] = {
    rowsMeta(sql, params: _*) {ResultSetMetaData => }
  }

  def rowsMeta(sql: String, params: AnyRef*)(meta: ResultSetMetaData => Unit): List[Seq[AnyRef]] = {
    val result = new ListBuffer[Seq[AnyRef]]
    prepareAndExecuteStatement(sql, params: _*) {
      executeWithResultSet(_)(meta) {
        result += resultsToSeqRow(_)
      }
    }
    result.toList
  }

  private def resultsToSeqRow(resultSet: ResultSet): Seq[AnyRef] = {
    // JH_TODO: I can't get the simpler "yield" version below to work.
    // It seems to be some kind of deferred evaluation problem, such that
    // the ResultSet is accessed after it is closed.
    // Maybe we need a way to force the yield to fully evaluate the elements.
    val columnCount = resultSet.getMetaData.getColumnCount
    val result = new scala.Array[AnyRef](columnCount)
    for (i <- 0 until columnCount) {
      result(i) = resultSet.getObject(i + 1)
    }
    result
//    for (i <- 0 until columnCount)
//      yield resultSet.getObject(i + 1)
  }

  private def executeWithConnection[T](f: Connection => T): T = {
    val connection = dataSource.getConnection
    try {
      f(connection)
    }
    finally {
      connection.close
    }
  }

  private def executeWithStatement[T](f: Statement => T): T = {
    executeWithConnection {connection =>
      val statement = connection.createStatement
      try {
        f(statement)
      }
      finally {
        statement.close
      }
    }
  }

  private def executeWithResultSet(preparedStatement: PreparedStatement)(meta: ResultSetMetaData => Unit)(f: ResultSet => Unit) {
    val resultSet = preparedStatement.executeQuery
    executeAndCloseResultSet(resultSet)(meta)(f)
  }

  private def executeAndCloseResultSet(resultSet: ResultSet)(meta: ResultSetMetaData => Unit)(f: ResultSet => Unit) {
    var first = true
    try {
      while (resultSet.next) {
        if (first) {
          meta(resultSet.getMetaData)
          first = false
        }
        f(resultSet)
      }
    }
    finally {
      resultSet.close
    }
  }

  private def executeFirstWithResultSet[T](preparedStatement: PreparedStatement)(meta: ResultSetMetaData => Unit)(f: ResultSet => T): T = {
    val resultSet = preparedStatement.executeQuery
    executeFirstAndCloseResultSet(resultSet)(meta)(f)
  }

  // JH_TODO: return Option?
  private def executeFirstAndCloseResultSet[T](resultSet: ResultSet)(meta: ResultSetMetaData => Unit)(f: ResultSet => T): T = {
    // JH_TODO: test result of resultSet.next()
    try {
      resultSet.next
      meta(resultSet.getMetaData)
      f(resultSet)
    }
    finally {
      resultSet.close
    }
  }

  private def prepareAndExecuteStatement[T](sql: String, params: AnyRef*)(f: PreparedStatement => T): T = {
    executeWithConnection {connection =>
      val statement = connection.prepareStatement(sql)
      try {
        for (i <- 0 until params.length) {
          statement.setObject(i + 1, params(i))
        }
        f(statement)
      }
      finally {
        statement.close
      }
    }
  }

}

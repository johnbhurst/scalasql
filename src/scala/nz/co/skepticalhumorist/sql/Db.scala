// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-06

package nz.co.skepticalhumorist.sql

import _root_.scala.collection.mutable.ListBuffer
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

  def call(sql: String, params: Object*) = {
    // JH_TODO
  }

  def call(sql: String, params: Object*)(f: ResultSet => Unit) = {
    // JH_TODO
  }

  def close = {
    // JH_TODO
  }

  def commit = {
    // JH_TODO
  }

  def eachRow(sql: String, params: Object*)(f: Seq[Object] => Unit) = {
    // JH_TODO
  }

  def eachRow(sql: String, params: Object*)(meta: ResultSetMetaData => Unit)(f: Seq[Object] => Unit) = {
    // JH_TODO
  }

  def execute(sql: String, params: Object*): Boolean = {
    true
    // JH_TODO
  }

  def executeInsert(sql: String, params: Object*): List[Object] = {
    List()
    // JH_TODO
  }

  def executeUpdate(sql: String, params: Object*): Int = {
    0
    // JH_TODO
  }

  def firstRow(sql: String, params: Object*) : Seq[Object] = {
    Nil
    // JH_TODO
  }

  // JH_TODO: getConnection?
  // JH_TODO: getDataSource?
  // JH_TODO: ResultSet concurrency?
  // JH_TODO: ResultSet holdability?
  // JH_TODO: ResultSet type?
  // JH_TODO: update count?

  def query(sql: String, params: Object*)(f: ResultSet => Unit) = {
    queryMeta(sql, params: _*) {ResultSetMetaData => } (f)
  }

  def queryMeta(sql: String, params: Object*)(meta: ResultSetMetaData => Unit)(f: ResultSet => Unit) = {
    prepareAndExecuteStatement(sql, params: _*) {preparedStatement =>
      val resultSet = preparedStatement.executeQuery()
      var first = true
      while (resultSet.next) {
        val metaData = resultSet.getMetaData
        if (first) {
          meta(metaData)
          first = false
        }
        f(resultSet)
      }
      resultSet.close
    }
  }

  def rollback() {
    // JH_TODO
  }

  def rows(sql: String, params: Object*): List[Seq[Object]] = {
    rowsMeta(sql, params: _*) {ResultSetMetaData => }
  }

  def rowsMeta(sql: String, params: Object*)(meta: ResultSetMetaData => Unit): List[Seq[Object]] = {
    val result = new ListBuffer[Seq[Object]]
    prepareAndExecuteStatement(sql, params: _*) {preparedStatement =>
      var first = true
      val resultSet = preparedStatement.executeQuery
      while (resultSet.next) {
        val metaData = resultSet.getMetaData
        if (first) {
          meta(metaData)
          first = false
        }
        val resultRow = new scala.Array[Object](metaData.getColumnCount)
        for (i <- 0 until metaData.getColumnCount) {
          resultRow(i) = resultSet.getObject(i + 1)
        }
        result += resultRow
      }
      resultSet.close
    }
    result.toList
  }

  private def executeWithConnection(f: Connection => AnyRef): AnyRef = {
    val connection = dataSource.getConnection
    val result = f(connection)
    connection.close
    result
  }

  private def executeWithStatement(f: Statement => AnyRef) = {
    executeWithConnection {connection =>
      val statement = connection.createStatement
      val result = f(statement)
      statement.close
      result
    }
  }

  private def prepareAndExecuteStatement(sql: String, params: Object*)(f: PreparedStatement => Unit) = {
    executeWithConnection {connection =>
      val statement = connection.prepareStatement(sql)
      for (i <- 0 until params.length) {
        statement.setObject(i + 1, params(i))
      }
      f(statement)
      statement.close
    }
  }

}

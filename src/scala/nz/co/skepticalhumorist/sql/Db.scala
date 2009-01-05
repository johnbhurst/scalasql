// $Id$
// John Hurst (jbhurst@attglobal.net)
// 2009-01-06

package nz.co.skepticalhumorist.sql

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
    executeWithStatement {statement =>
      val resultSet = statement.executeQuery(sql)
      while (resultSet.next) {
        f(resultSet)
      }
      resultSet.close
    }
  }

  def rollback() {
    // JH_TODO
  }

  def rows(sql: String, params: Object*): List[Seq[Object]] = {
    List()
    // JH_TODO
  }

  def rows(sql: String, params: Object*)(meta: ResultSetMetaData => Unit): List[Seq[Object]] = {
    List()
    // JH_TODO
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

}

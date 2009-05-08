
package nz.co.skepticalhumorist.sql

import org.apache.derby.jdbc.EmbeddedDriver
import java.sql.Connection
import javax.sql.DataSource

class DriverManagerDataSource(val url: String) extends javax.sql.DataSource {

  //def driver = "org.apache.derby.jdbc.EmbeddedDriver"
  val driver = new EmbeddedDriver

  def getConnection: Connection = {
    //java.lang.Class[EmbeddedDriver].forName(driver)
    //new EmbeddedDriver
    //null // JH_TODO
    driver.connect(url, new java.util.Properties)
  }

  def getConnection(username: String, password: String): Connection = {
    null // JH_TODO
  }

  def isWrapperFor(cls: java.lang.Class[_]): Boolean = {
    false
  }

  def unwrap[T](cls: java.lang.Class[T]): T = {
    throw new java.lang.UnsupportedOperationException("unwrap not supported")
  }

  def getLoginTimeout: Int = {
    0
  }

  def getLogWriter: java.io.PrintWriter = {
    null
  }

  def setLoginTimeout(timeout: Int): Unit = {
    //
  }

  def setLogWriter(writer: java.io.PrintWriter): Unit = {
    //
  }

}

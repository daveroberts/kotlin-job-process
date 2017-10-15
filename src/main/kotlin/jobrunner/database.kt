import org.apache.commons.dbcp2.*
import java.sql.*
import java.io.*
import org.h2.jdbcx.JdbcConnectionPool

object Database {
  private val _pool: JdbcConnectionPool
  init {
    /*_pool = BasicDataSource()
    _pool.setDriverClassName("com.mysql.jdbc.Driver")
    _pool.setUsername(Config.get("database.username"))
    _pool.setPassword(Config.get("database.password"))
    _pool.setUrl("jdbc:${Config.get("database.type")}://${Config.get("database.host")}:${Config.getLong("database.port")}/${Config.get("database.database")}?useSSL=false")
    _pool.setInitialSize(Config.getInt("database.connection_limit"))
    _pool.setMaxTotal(Config.getInt("database.connection_limit"))*/
    _pool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa")
    val conn = _pool.getConnection()
    val statement = conn.createStatement()
    val sql = "CREATE TABLE jobs (id INTEGER NOT NULL AUTO_INCREMENT, subject VARCHAR(255), state VARCHAR(255), type VARCHAR(255), created_at DATETIME NOT NULL, updated_at DATETIME, PRIMARY KEY (id))"
    statement.execute(sql)
    conn.close()
  }

  fun handle(): Connection {
    return _pool.getConnection()
  }
}

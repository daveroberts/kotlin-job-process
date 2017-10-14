import org.apache.commons.dbcp2.*
import java.sql.*
import java.io.*

object Database {
  private val _pool: BasicDataSource
  init {
    _pool = BasicDataSource()
    _pool.setDriverClassName("com.mysql.jdbc.Driver")
    _pool.setUsername(Config.get("database.username"))
    _pool.setPassword(Config.get("database.password"))
    _pool.setUrl("jdbc:${Config.get("database.type")}://${Config.get("database.host")}:${Config.getLong("database.port")}/${Config.get("database.database")}?useSSL=false")
    _pool.setInitialSize(Config.getInt("database.connection_limit"))
    _pool.setMaxTotal(Config.getInt("database.connection_limit"))
  }

  fun handle(): Connection {
    return _pool.getConnection()
  }
}

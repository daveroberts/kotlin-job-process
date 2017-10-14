import com.moandjiezana.toml.*
import java.io.*
import java.util.*

object Config {
  private val _config: Toml

  init {
    val content = Scanner(File("config.toml")).useDelimiter("\\Z").next()
    _config = Toml().read(content)
  }

  fun get(key: String): String = _config.getString(key)
  fun getLong(key: String): Long = _config.getLong(key)
  fun getInt(key: String): Int = _config.getLong(key).toInt()
}

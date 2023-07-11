package cc.dashify.plugin

import cc.dashify.plugin.DashifyKommand.registerKommand
import cc.dashify.plugin.DashifyServer.isServerRunning
import cc.dashify.plugin.DashifyServer.startKtor
import cc.dashify.plugin.DashifyServer.stopKtor
import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.DashifyUtil
import cc.dashify.plugin.util.StringUtil
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import org.mindrot.jbcrypt.BCrypt


/**
 * DashifyPluginMain
 * the main class of the plugin.
 */
class DashifyPluginMain : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this
        ConfigHandler.initConfig()

        val enabled = config.getBoolean("enabled")

        kommand {
            register("dashify") {
                requires { isPlayer && isOp }
                registerKommand(this)
            }
        }

        if (config.getString("key")?.trim().isNullOrBlank()) {
            val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
            config.set("key", key)
            DashifyUtil.key = key
            saveConfig()
        }

        if (enabled) {
            startKtor()
            logger.info("Dashify is enabled.")
        }
        else {
            logger.warning("Dashify is disabled.")
        }
    }

    override fun onDisable() {
        if (isServerRunning) stopKtor()
    }
}
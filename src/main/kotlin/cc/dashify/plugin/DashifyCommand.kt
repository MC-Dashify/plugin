package cc.dashify.plugin

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyUtil
import cc.dashify.plugin.util.DashifyUtil.enabled
import cc.dashify.plugin.util.StringUtil
import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.mindrot.jbcrypt.BCrypt

/**
 * @author aroxu, pybsh, lambdynma
 */

/**
 * Dashify Command
 *
 * This file contains in-game commands that can be used for Dashify Configuration.
 */
object DashifyCommand {
    fun registerCommand(manager: CommandManager<CommandSender>): Command.Builder<CommandSender> {
        val root = manager.commandBuilder("dashify")

        manager.command(root.literal("enable")
            .handler { ctx ->
                if (!enabled) {
                    plugin.config.set("enabled", true)
                    plugin.saveConfig()
                    ctx.sender.sendMessage(text("Dashify enabled.", NamedTextColor.GREEN))
                } else {
                    ctx.sender.sendMessage(text("Dashify is already enabled.", NamedTextColor.RED))
                }
            })

        manager.command(root.literal("disable")
            .handler {
                if (enabled) {
                    plugin.config.set("enabled", false)
                    plugin.saveConfig()
                    it.sender.sendMessage(text("Dashify disabled.", NamedTextColor.GREEN))
                } else {
                    it.sender.sendMessage(text("Dashify is already disabled.", NamedTextColor.RED))
                }
            })

        manager.command(root.literal("key")
            .handler {
                val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
                plugin.config.set("key", key)
                DashifyUtil.key = key
                plugin.saveConfig()
                it.sender.sendMessage(
                    text(
                        """New key generated.
                    |Please restart the server to change save changes to disk.""".trimMargin(), NamedTextColor.GREEN
                    )
                )
            })

        return root
    }
}
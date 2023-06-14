package io.dashify.plugin

import io.dashify.plugin.util.ConfigHandler
import io.dashify.plugin.util.StringUtil
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class DashifyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage(text("OP is required to run this command", TextColor.color(0xFF0000)))
        } else if (sender is ConsoleCommandSender) {
            sender.sendMessage(text("Cannot execute this command from console.", TextColor.color(0xFF0000)))
        } else {
            if (args[0] == "key") {
                val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
                ConfigHandler["key"] = key
                sender.sendMessage(text("Dashify key (re)generated."))
            }

            if (args[0] == "enable") {
                if (ConfigHandler["enabled"].toString().toBoolean()) {
                    sender.sendMessage(text("Dashify plugin is already enabled.", TextColor.color(0xFFA500)))
                } else {
                    ConfigHandler["enabled"] = true
                    sender.sendMessage(text("Dashify plugin enabled."))
                }
            }

            if (args[0] == "disable") {
                if (!ConfigHandler["enabled"].toString().toBoolean()) {
                    sender.sendMessage(text("Dashify plugin is already disabled.", TextColor.color(0xFFA500)))
                } else {
                    ConfigHandler["enabled"] = false
                    sender.sendMessage(text("Dashify plugin disabled."))
                }
            }
        }

        return true
    }
}

class DashifyCommandTabComplete : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (!sender.isOp || sender is ConsoleCommandSender) {
            return Collections.emptyList()
        }
        if (args.size == 1) return arrayListOf("key", "enable", "disable")
        return Collections.emptyList()
    }
}

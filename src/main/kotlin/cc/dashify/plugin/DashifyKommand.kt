package cc.dashify.plugin

import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.StringUtil
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

/**
 * DashifyCommand
 * Dashify plugin command executor
 */
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
                sender.sendMessage(
                    text("Click ").append(
                        text("here", TextColor.color(0xFFCF2C)).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)
                            .clickEvent(ClickEvent.copyToClipboard(key))
                    ).append(text(" to copy"))
                )
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

/**
 * DashifyCommandTabComplete
 * Dashify plugin command tab completer
 */
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
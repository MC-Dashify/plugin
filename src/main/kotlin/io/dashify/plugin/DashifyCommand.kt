package io.dashify.plugin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabCompleter
import org.mindrot.jbcrypt.BCrypt
import java.util.Collections

class DashifyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp || sender is ConsoleCommandSender) { return false }
        if ( args[0] == "key" ) {
            val key = BCrypt.hashpw("dashify_key", BCrypt.gensalt())
            ConfigHandler["key"] = key
            sender.sendMessage("dashify key generated.")
        }

        if ( args[0] == "enable" ) {
            if ( ConfigHandler["enabled"].toString().toBoolean() ) {
                sender.sendMessage("dashify-plugin is already enabled.")
                return false
            }
            
            ConfigHandler["enabled"] = true
            sender.sendMessage("dashify-plugin enabled.")
        }

        if ( args[0] == "disable" ) {
            if ( !ConfigHandler["enabled"].toString().toBoolean() ) {
                sender.sendMessage("dashify-plugin is already disabled.")
                return false
            }
            
            ConfigHandler["enabled"] = false
            sender.sendMessage("dashify-plugin disabled.")
        }
        return true
    }
}

class DashifyCommandTabComplete : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if (args.size == 1) return arrayListOf("key","enable","disable")
        return Collections.emptyList()
    }
}
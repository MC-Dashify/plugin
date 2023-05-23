package io.dashify.plugin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.mindrot.jbcrypt.BCrypt

class DashifyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp || sender !is ConsoleCommandSender) { return false }
        if( label == "key" ) {
            val key = BCrypt.hashpw("dashify_key", BCrypt.gensalt())
            ConfigHandler["key"] = key
        }
        return true
    }
}
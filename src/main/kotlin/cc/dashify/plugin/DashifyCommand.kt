package cc.dashify.plugin

import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.StringUtil
import cloud.commandframework.Command
import cloud.commandframework.paper.PaperCommandManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.mindrot.jbcrypt.BCrypt

/**
 * DashifyCommand
 * /dashify command
 */
object DashifyCommand {
    /**
     * registerCommand()
     * registers the /dashify command
     * @param manager PaperCommandManager<CommandSender>
     * @return Command.Builder<CommandSender>
     */
    fun registerCommand(manager: PaperCommandManager<CommandSender>): Command.Builder<CommandSender> {
        val builder = manager.commandBuilder("dashify")

        manager.command(builder.literal("key").permission { sender -> sender.isOp && sender !is ConsoleCommandSender }
            .handler { ctx ->
                val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
                ConfigHandler["key"] = key
                ctx.sender.sendMessage(text("Dashify key (re)generated."))
                ctx.sender.sendMessage(
                    text("Click ").append(
                        text("here", TextColor.color(0xFFCF2C))
                            .decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)
                            .clickEvent(ClickEvent.copyToClipboard(key))
                            .hoverEvent(text("Click to copy key"))
                    ).append(text(" to copy"))
                )
            })

        manager.command(builder.literal("enable")
            .permission { sender -> sender.isOp && sender !is ConsoleCommandSender }.handler { ctx ->
                if (ConfigHandler["enabled"].toString().toBoolean()) {
                    ctx.sender.sendMessage(
                        text(
                            "Dashify plugin is already enabled.", TextColor.color(0xFFA500)
                        )
                    )
                } else {
                    ConfigHandler["enabled"] = true
                    ctx.sender.sendMessage(text("Dashify plugin enabled."))
                }
            })

        manager.command(builder.literal("disable")
            .permission { sender -> sender.isOp && sender !is ConsoleCommandSender }.handler { ctx ->
                if (!ConfigHandler["enabled"].toString().toBoolean()) {
                    ctx.sender.sendMessage(
                        text(
                            "Dashify plugin is already disabled.", TextColor.color(0xFFA500)
                        )
                    )
                } else {
                    ConfigHandler["enabled"] = false
                    ctx.sender.sendMessage(text("Dashify plugin disabled."))
                }
            })
        return builder
    }
}

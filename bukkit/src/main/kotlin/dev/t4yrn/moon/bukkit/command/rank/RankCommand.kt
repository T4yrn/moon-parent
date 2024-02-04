package dev.t4yrn.moon.bukkit.command.rank

import com.google.common.collect.Lists
import dev.t4yrn.moon.bukkit.command.annotation.*
import dev.t4yrn.moon.bukkit.lang.Lang
import dev.t4yrn.moon.bukkit.util.Color
import dev.t4yrn.moon.shared.rank.RankHandler
import dev.t4yrn.moon.shared.rank.data.Rank
import dev.t4yrn.moon.shared.util.UnicodeUtil
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RankCommand(val name: String) {

    @Command(name = "", desc = "Rank command.", async = true)
    @Require("moon.rank")
    fun rank(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) {
            usage().forEach { it ->
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', it))
            }
            return
        }

        //RankCommandMenu().openMenu(player)

        listOf(
            "",
            "${ChatColor.GRAY}${ChatColor.ITALIC}To get the complete list of commands use '${ChatColor.GOLD}/rank help${ChatColor.GRAY}'.",
            ""
        ).forEach(sender::sendMessage)

        sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
    }

    @Command(name = "help", desc = "Rank help command.", async = true)
    @Require("moon.rank")
    fun help(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) {
            usage().forEach { it ->
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', it))
            }
            return
        }

        usage().forEach {it ->
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', it))
        }

        sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
    }

    @Command(name = "create", desc = "Create's a new rank.", async = true, usage = "<name>")
    @Require("moon.rank")
    fun create(
        @Sender sender: CommandSender,
        name: String
    ) {
        val rank = RankHandler.findByName(name)

        if (rank != null) {
            sender.sendMessage(Lang.PREFIX.value + Lang.RANK_ALREADY_EXISTS_ERROR.value.replace("{rank}", name))
            return
        }

        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CREATION_COMMAND.value.replace("{rank}", name))

        val newRank = Rank(name)
        newRank.save(true)
        RankHandler.ranks[name] = newRank

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "delete", desc = "Delete's an existing rank.", async = true, usage = "<name>")
    @Require("moon.rank")
    fun delete(
        @Sender sender: CommandSender,
        rank: Rank
    ) {
        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_DELETE_COMMAND.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}"))

        rank.delete()

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }

    }

    @Command(name = "info", aliases = ["information"], desc = "Get a rank's information.", async = true, usage = "<rank>")
    @Require("moon.rank")
    fun information(
        @Sender sender: CommandSender,
        rank: Rank
    ) {
        val information = listOf(
            "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 35)}",
            "&6&lRank Information &7${UnicodeUtil.VERTICAL_LINE} ${rank.displayName}",
            "",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eName&7: &f${rank.name}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eDisplay&7: &f${rank.displayName}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eColor&7: &f${ChatColor.valueOf(rank.color)}${Color.convert(rank.color)}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &ePrefix&7: &f${rank.prefix}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eWeight&7: &f${rank.weight}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eStaff&7: &f${if (rank.staff) "&aTrue" else "&cFalse"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eMedia&7: &f${if (rank.media) "&aTrue" else "&cFalse"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eHidden&7: &f${if (rank.hidden) "&aTrue" else "&cFalse"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eDiscordID&7: &f${if (rank.discordId == "") "&cNot set" else "&f${rank.discordId}"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eInherits&7: &f${if (rank.inherits.isEmpty()) "&cNone" else "&7(${rank.inherits.size}) &f[${rank.inherits.joinToString(", ")}]"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &ePermissions&7: &f${if (rank.permissions.isEmpty()) "&cNone" else "&7(${rank.permissions.size}) &f[${rank.permissions.joinToString(", ")}]"}",
            "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 35)}"
        )

        information.forEach { it ->
            sender.sendMessage(Color.color(it))
        }
    }


    @Command(name = "rename", desc = "Rename's an existing rank.", async = true, usage = "<rank> <newName>")
    @Require("moon.rank")
    fun rename(
        @Sender sender: CommandSender,
        rank: Rank,
        name: String
    ) {
        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_NAME.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("newName", name))

        rank.name = name
        rank.save(true)

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }

    }

    @Command(name = "display", desc = "Set's a rank's display name.", async = true, usage = "<rank> <display>")
    @Require("moon.rank")
    fun display(
        @Sender sender: CommandSender,
        rank: Rank,
        display: String
    ) {
        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_DISPLAY.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{display}", Color.color(display)))

        rank.displayName = display
        rank.save(true)

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }

    }

    @Command(name = "color", desc = "Set's a rank's color.", async = true, usage = "<rank> <color>")
    @Require("moon.rank")
    fun color(
        @Sender sender: CommandSender,
        rank: Rank,
        color: ChatColor
    ) {
        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_COLOR.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{color}", "${ChatColor.valueOf(color.name)}${Color.convert(color.name)}"))

        rank.color = color.name
        rank.save(true)

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }

    }

    @Command(name = "prefix", desc = "Set's a rank's prefix.", async = true, usage = "<rank> <prefix>")
    @Require("moon.rank")
    fun prefix(
        @Sender sender: CommandSender,
        rank: Rank,
        @Text prefix: String
    ) {
        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_PREFIX.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{prefix}", Color.color(prefix)))

        rank.prefix = Color.color(prefix)
        rank.save(true)

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "weight", desc = "Set's a rank's weight.", async = true, usage = "<rank> <weight>")
    @Require("moon.rank")
    fun weight(
        @Sender sender: CommandSender,
        rank: Rank,
        @OptArg("1") weight: Int
    ) {
        if (weight < 0) {
            sender.sendMessage("${ChatColor.RED}Weight cannot be negative.")
            return
        }

        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_WEIGHT.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{weight}", weight.toString()))

        rank.weight = weight
        rank.save(true)


        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "staff", desc = "Set's a rank's staff.", async = true, usage = "<rank> <status>")
    @Require("moon.rank")
    fun staff(
        @Sender sender: CommandSender,
        rank: Rank,
        status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_STAFF_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{status}", statusDisplay))

        rank.staff = status
        rank.save(true)


        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "media", desc = "Set's a rank's media.", async = true, usage = "<rank> <status>")
    @Require("moon.rank")
    fun media(
        @Sender sender: CommandSender,
        rank: Rank,
        status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_MEDIA_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{status}", statusDisplay))

        rank.media = status
        rank.save(true)


        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "hidden", desc = "Set's a rank's hidden.", async = true, usage = "<rank> <status>")
    @Require("moon.rank")
    fun hidden(
        @Sender sender: CommandSender,
        rank: Rank,
        status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_HIDDEN_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{status}", statusDisplay))

        rank.hidden = status
        rank.save(true)


        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "discordId", desc = "Set's a rank's discord id.", async = true, usage = "<rank> <discordId>")
    @Require("moon.rank")
    fun discordId(
        @Sender sender: CommandSender,
        rank: Rank,
        discordId: String
    ) {
        sender.sendMessage(Lang.PREFIX.value + Lang.RANK_CHANGED_DISCORD_ID.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{discordId}", discordId))

        rank.discordId = discordId;
        rank.save(true)

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "inherit", desc = "Set's a rank's inherit.", async = true, usage = "<rank> <inherit>")
    @Require("moon.rank")
    fun inherit(
        @Sender sender: CommandSender,
        rank: Rank,
        inherit: Rank,
    ) {
        if (!rank.inherits.contains(inherit.name)) {
            sender.sendMessage(Lang.PREFIX.value + Lang.RANK_INHERIT_ADDED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{inherit}", "${ChatColor.valueOf(inherit.color)}${inherit.name}"))

            rank.inherits.add(inherit.name)
            rank.save(true)
        } else {
            sender.sendMessage(Lang.PREFIX.value + Lang.RANK_INHERIT_REMOVED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{inherit}", "${ChatColor.valueOf(inherit.color)}${inherit.name}"))

            rank.inherits.remove(inherit.name)
            rank.save(true)
        }

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(name = "permission", desc = "Set's a rank's permission.", async = true, usage = "<rank> <permission>")
    @Require("moon.rank")
    fun permission(
        @Sender sender: CommandSender,
        rank: Rank,
        permission: String
    ) {
        if (!rank.permissions.contains(permission)) {
            sender.sendMessage(Lang.PREFIX.value + Lang.RANK_PERMISSION_ADDED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{permission}", permission))

            rank.permissions.add(permission)
            rank.save(true)
        } else {
            sender.sendMessage(Lang.PREFIX.value + Lang.RANK_PERMISSION_REMOVED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.name}").replace("{permission}", permission))

            rank.permissions.remove(permission)
            rank.save(true)
        }

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    private fun usage(): MutableList<String> {
        val toReturn: MutableList<String> = Lists.newArrayList()

        toReturn.addAll(
            listOf(
                "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 65)}",
                "&6&lRank Commands &7${UnicodeUtil.VERTICAL_LINE} &7Use '&e/rank <command>&7' for specific command details.",
                "",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank create &f<rank> &7- &fCreate a new rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank delete &f<rank> &7- &fDelete an existing rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank info &f<rank> &7- &fGet rank information.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank rename &f<rank> &f<newName> &7- &fRename a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank display &f<rank> &7- &fSet the display name for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank color &f<rank> &f<color> &7- &fSet the color for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank prefix &f<rank> &f<prefix> &7- &fSet the prefix for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank suffix &f<rank> &f<suffix> &7- &fSet the suffix for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank weight &f<rank> &f<weight> &7- &fSet the weight for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank staff &f<rank> &f<status> &7- &fSet staff status for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank media &f<rank> &f<status> &7- &fSet media status for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank hidden &f<rank> &f<status> &7- &fSet hidden status for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank discordId &f<rank> &f<discordId> &7- &fSet Discord ID for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank inherits &f<rank> &f<inherit> &7- &fSet inheritance for a rank.",
                "&f  ${UnicodeUtil.SMALL_ARROW} &e/rank permissions &f<rank> &f<permission> &7- &fSet permissions for a rank.",
                "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 65)}",
                "&7&oNeed help? Contact the developer T4yrn"
            )
        )

        return toReturn
    }

}
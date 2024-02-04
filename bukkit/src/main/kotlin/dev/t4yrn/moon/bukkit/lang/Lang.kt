package dev.t4yrn.moon.bukkit.lang

import org.bukkit.ChatColor

enum class Lang(val value: String) {

    PREFIX("${ChatColor.GOLD}[Moon] "),

    //Rank
    RANK_CREATION_COMMAND("${ChatColor.YELLOW}You've ${ChatColor.GREEN}created the rank ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_DELETE_COMMAND("${ChatColor.YELLOW}You've ${ChatColor.RED}deleted the rank ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_ALREADY_EXISTS_ERROR("${ChatColor.RED}There's an existing rank with that name."),
    RANK_PERMISSION_ALREADY_ADDED("${ChatColor.WHITE}{rank} ${ChatColor.GREEN}already ${ChatColor.YELLOW}has that permission."),
    RANK_PERMISSION_DOESNT_ADDED("${ChatColor.WHITE}{rank} ${ChatColor.RED}doesn't ${ChatColor.YELLOW}have that permission."),
    RANK_PERMISSION_ADDED("${ChatColor.YELLOW}You've ${ChatColor.GREEN}added ${ChatColor.WHITE}'${ChatColor.WHITE}{permission}${ChatColor.WHITE}' ${ChatColor.YELLOW}permission to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_PERMISSION_REMOVED("${ChatColor.YELLOW}You've ${ChatColor.RED}removed ${ChatColor.WHITE}'${ChatColor.WHITE}{permission}${ChatColor.WHITE}' ${ChatColor.YELLOW}permission from ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_INHERIT_ALREADY_ADDED("${ChatColor.WHITE}{rank} ${ChatColor.GREEN}already ${ChatColor.YELLOW}has that inherit."),
    RANK_INHERIT_DOESNT_ADDED("${ChatColor.WHITE}{rank} ${ChatColor.RED}doesn't ${ChatColor.YELLOW}have that inherit."),
    RANK_INHERIT_ADDED("${ChatColor.YELLOW}You've ${ChatColor.GREEN}added ${ChatColor.WHITE}'${ChatColor.WHITE}{inherit}${ChatColor.WHITE}' ${ChatColor.YELLOW}inherit to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_INHERIT_REMOVED("${ChatColor.YELLOW}You've ${ChatColor.RED}removed ${ChatColor.WHITE}'${ChatColor.WHITE}{inherit}${ChatColor.WHITE}' ${ChatColor.YELLOW}inherit from ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_NAME("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}'s name to ${ChatColor.WHITE}{newName}${ChatColor.YELLOW}."),
    RANK_CHANGED_DISPLAY("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}'s display to ${ChatColor.WHITE}{display}${ChatColor.YELLOW}."),
    RANK_CHANGED_PREFIX("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{prefix}${ChatColor.YELLOW}'s prefix to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_COLOR("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{color}${ChatColor.YELLOW}'s color to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_WEIGHT("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{weight}${ChatColor.YELLOW}'s weight to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_DISCORD_ID("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}'s discord id to ${ChatColor.WHITE}{discordId}${ChatColor.YELLOW}."),
    RANK_CHANGED_STAFF_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} staff boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_MEDIA_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} media boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_HIDDEN_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} hidden boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}.")

}
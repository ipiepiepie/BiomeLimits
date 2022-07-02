package xyz.nifti.biomelimits.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nifti.biomelimits.BiomeLimits;
import xyz.nifti.biomelimits.config.BiomeLimitsMessages;
import xyz.nifti.biomelimits.config.BlocksConfig;
import xyz.nifti.translate.NiftiTranslate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeLimitsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("biomelimits.reload") && args.length == 1 && args[0].equals("reload")) {
            BlocksConfig.load();
            BiomeLimitsMessages.setTranslatableCfg(NiftiTranslate.getInstance().initialiseTranslationFiles(BiomeLimits.getInstance()));
            BiomeLimits.getInstance().loadLimitedBlocks();
            BiomeLimitsMessages.send(sender, "msg_config_reloaded");
        }
        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 0)
            return completions;

        if (args.length == 1 && sender.hasPermission("biomelimits.reload"))
            completions.add("reload");

        return sortTabCompletions(completions, args[args.length - 1]);
    }

    private List<String> sortTabCompletions(List<String> completions, String startingWith) {
        if (completions == null || completions.isEmpty()) {
            return completions;
        }
        return completions.stream().filter(name -> name.toLowerCase().startsWith(startingWith.toLowerCase())).collect(Collectors.toList());
    }
}
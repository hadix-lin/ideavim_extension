package io.github.hadixlin.iss;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.command.CommandAdapter;
import com.intellij.openapi.command.CommandEvent;
import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.command.CommandProcessor;
import com.maddyhome.idea.vim.extension.VimExtension;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hadix on 31/03/2017.
 */
public class InputSourceSwitchExtension implements VimExtension {

    private static final Set<String> AUTO_SWITCH_TRIGGER_COMMANDS
      = ImmutableSet.of("Vim Exit Insert Mode");
    private CommandListener exitInsertModeListener;

    @NotNull
    @Override
    public String getName() {
        return "switch-to-english-when-exit-insert-mode";
    }

    @Override
    public void init() {
        if (exitInsertModeListener == null) {
            this.exitInsertModeListener = exitInsertModeListener();
        }
        CommandProcessor.getInstance().addCommandListener(this.exitInsertModeListener);
    }

    @NotNull
    private static CommandListener exitInsertModeListener() {
        return new CommandAdapter() {
            @Override
            public void beforeCommandFinished(CommandEvent commandEvent) {
                String commandName = commandEvent.getCommandName();
                if (StringUtils.isBlank(commandName)) {
                    return;
                }
                if (AUTO_SWITCH_TRIGGER_COMMANDS.contains(commandName)) {
                    SystemInputSource.switchToEnglish();
                }
            }
        };
    }

    @Override
    public void dispose() {
        if (exitInsertModeListener == null) {
            return;
        }
        CommandProcessor.getInstance().removeCommandListener(exitInsertModeListener);
    }

}

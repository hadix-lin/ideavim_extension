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
    //undo 和 typing 的存在导致 罗列貌似是一种更好的方式?
    // 会有 undo Vim Change Motion 之类东西的存在
    // S o O 显示的都是 typing (不知道能不能用其他方法解决)
    private static final Set<String> UPDATE_TRIGGER_COMMANDS = ImmutableSet.of(
            "Vim Insert Before Cursor",//i
            "Vim Insert After Cursor",//a
            "Vim Change Characters",//s
            "Vim Change to End-of-Line",//C
            "Vim Change Motion",//  c+action
            "Vim Insert Before First non-Blank",//I
            "Vim Insert After Line End"//A
    );
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
                if (UPDATE_TRIGGER_COMMANDS.contains(commandName)) {
                    SystemInputSource.switchToFormer();
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

package io.github.hadixlin.iss;

import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.command.CommandAdapter;
import com.intellij.openapi.command.CommandEvent;
import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.command.CommandProcessor;
import com.maddyhome.idea.vim.command.MappingMode;
import com.maddyhome.idea.vim.extension.VimExtension;
import com.maddyhome.idea.vim.extension.VimExtensionFacade;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;
import static io.github.hadixlin.iss.SystemInputSource.*;

/**
 * Created by hadix on 31/03/2017.
 */
public class KeepEnglishInNormalAndRestoreInInsertExtension implements VimExtension {

    private static final Set<String> SWITCH_TO_ENGLISH_COMMAND_NAMES
            = ImmutableSet.of("Vim Exit Insert Mode");
    private static final Set<String> SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES = ImmutableSet.of(
            "Vim Insert After Cursor", "Vim Insert After Line End", "Vim Insert Before Cursor",
            "Vim Insert Before First non-Blank", "Vim Insert Character Above Cursor",
            "Vim Insert Character Below Cursor", 
            "Vim Enter", "Vim Insert at Line Start", "Vim Insert New Line Above",
            "Vim Insert New Line Below", "Vim Insert Previous Text", "Vim Insert Previous Text",
            "Vim Insert Register" 
    );

    private boolean restoreInInsert = true;

    private CommandListener exitInsertModeListener;

    public KeepEnglishInNormalAndRestoreInInsertExtension() {
    }

    public KeepEnglishInNormalAndRestoreInInsertExtension(boolean restoreInInsert) {
        this.restoreInInsert = restoreInInsert;
    }

    @NotNull
    @Override
    public String getName() {
        return "keep-english-in-normal-and-restore-in-insert";
    }

    @Override
    public void init() {
        if (exitInsertModeListener == null) {
            this.exitInsertModeListener = exitInsertModeListener();
        }
        CommandProcessor.getInstance().addCommandListener(this.exitInsertModeListener);
        VimExtensionFacade.putKeyMapping(
                MappingMode.N, parseKeys("<Esc>"), parseKeys("a<Esc><Esc>"), false);
    }

    @NotNull
    private CommandListener exitInsertModeListener() {
        return new CommandAdapter() {
            private String lastInputSourceId;

            @Override
            public void beforeCommandFinished(CommandEvent commandEvent) {
                String commandName = commandEvent.getCommandName();
                if (StringUtils.isBlank(commandName)) {
                    return;
                }
                String currentInputSource = getCurrentInputSource();
                if (currentInputSource == null) {
                    return;
                }
                if (SWITCH_TO_ENGLISH_COMMAND_NAMES.contains(commandName)) {
                    lastInputSourceId = currentInputSource;
                    if (!currentInputSource.equals(ENGLISH_INPUT_SOURCE)) {
                        switchToEnglish();
                    }
                }
                if (!restoreInInsert) {
                    return;
                }
                if (SWITCH_TO_LAST_INPUT_SOURCE_COMMAND_NAMES.contains(commandName)) {
                    if (lastInputSourceId != null &&
                            !currentInputSource.equals(lastInputSourceId)) {
                        switchTo(lastInputSourceId);
                    }
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
        exitInsertModeListener = null;
    }

}

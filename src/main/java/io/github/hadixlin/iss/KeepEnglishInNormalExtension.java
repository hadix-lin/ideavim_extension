package io.github.hadixlin.iss;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hadix on 09/04/2017.
 */
public class KeepEnglishInNormalExtension
        extends KeepEnglishInNormalAndRestoreInInsertExtension {

    public KeepEnglishInNormalExtension() {
        super(false);
    }

    @NotNull
    @Override
    public String getName() {
        return "keep-english-in-normal";
    }
}

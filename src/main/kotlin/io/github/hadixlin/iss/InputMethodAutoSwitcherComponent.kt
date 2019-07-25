package io.github.hadixlin.iss

import com.intellij.openapi.components.ApplicationComponent
import com.maddyhome.idea.vim.option.OptionsManager
import com.maddyhome.idea.vim.option.ToggleOption

class InputMethodAutoSwitcherComponent : ApplicationComponent {

    private val keepEnglishInNormalOption: ToggleOption by lazy {
        OptionsManager.getOption(KeepEnglishInNormalExtension.NAME) as ToggleOption
    }

    private val keepEnglishInNormalAndRestoreInInsertOption: ToggleOption by lazy {
        OptionsManager.getOption(KeepEnglishInNormalAndRestoreInInsertExtension.NAME) as ToggleOption
    }

    override fun getComponentName(): String {
        return "IdeaVimExtension.InputMethodAutoSwitcher"
    }

    override fun initComponent() {
        keepEnglishInNormalOption.set()
        keepEnglishInNormalOption.addOptionChangeListener {
            keepEnglishInNormalAndRestoreInInsertOption.reset()
        }
    }

    override fun disposeComponent() {
        keepEnglishInNormalOption.reset()
    }
}

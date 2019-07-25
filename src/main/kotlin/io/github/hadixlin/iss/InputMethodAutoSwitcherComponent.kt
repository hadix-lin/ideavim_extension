package io.github.hadixlin.iss

import com.intellij.openapi.components.ApplicationComponent
import com.maddyhome.idea.vim.option.Option
import com.maddyhome.idea.vim.option.ToggleOption

class InputMethodAutoSwitcherComponent : ApplicationComponent {

    private val keepEnglishInNormalOption: ToggleOption by lazy {
        getOption(KeepEnglishInNormalExtension.NAME) as ToggleOption
    }

    private val keepEnglishInNormalAndRestoreInInsertOption: ToggleOption by lazy {
        getOption(KeepEnglishInNormalAndRestoreInInsertExtension.NAME) as ToggleOption
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

    companion object {
        fun getOption(name: String): Option {
            return try {
                val optionClass = Class.forName("com.maddyhome.idea.vim.option.Options")
                val getInstanceMethod = optionClass.getMethod("getInstance")
                val instance = getInstanceMethod.invoke(null)
                val getOptionMethod = optionClass.getMethod("getOption", String::class.java)
                getOptionMethod.invoke(instance, name) as Option
            } catch (e: ClassNotFoundException) {
                val optionManagerClass =
                    Class.forName("com.maddyhome.idea.vim.option.OptionsManager")
                val optionManager = optionManagerClass.getField("INSTANCE")
                val getOptionMethod = optionManagerClass.getMethod("getOption", String::class.java)
                getOptionMethod.invoke(optionManager.get(null), name) as Option
            }
        }
    }

}

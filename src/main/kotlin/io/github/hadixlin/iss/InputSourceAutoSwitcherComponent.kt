package io.github.hadixlin.iss

import com.intellij.openapi.components.ApplicationComponent

class InputSourceAutoSwitcherComponent : ApplicationComponent {

    override fun getComponentName(): String {
        return "IdeaVimExtension.InputSourceAutoSwitcher"
    }

    override fun initComponent() {
        InputSourceAutoSwitcher.enable()
    }

    override fun disposeComponent() {
        InputSourceAutoSwitcher.disable()
    }
}

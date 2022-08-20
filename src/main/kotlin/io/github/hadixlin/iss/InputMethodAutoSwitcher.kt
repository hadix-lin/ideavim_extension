package io.github.hadixlin.iss

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.util.messages.MessageBusConnection
import com.maddyhome.idea.vim.VimPlugin
import com.maddyhome.idea.vim.command.CommandState
import com.maddyhome.idea.vim.listener.VimInsertListener
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang3.CharUtils
import java.util.*
import kotlin.math.max
import kotlin.math.min

object InputMethodAutoSwitcher {
	private const val VIM_INSERT_EXIT_MODE_ACTION = "VimInsertExitModeAction"

	private val EDITING_MODES = EnumSet.of(
		CommandState.Mode.INSERT,
		CommandState.Mode.REPLACE
	)

	@Volatile
	var restoreInInsert: Boolean = false

	var contextAware: Boolean = true

	@Volatile
	var enabled: Boolean = false
		private set

	private val switcher = SystemInputMethodSwitcher()

	private var messageBusConnection: MessageBusConnection? = null

	private val exitInsertModeListener = object : CommandListener {
		override fun beforeCommandFinished(commandEvent: CommandEvent) {
			val commandName = commandEvent.commandName
			if (StringUtils.isBlank(commandName)) {
				return
			}
			val vimInsertExitModeAction = VIM_INSERT_EXIT_MODE_ACTION
			if (commandName == vimInsertExitModeAction) {
				ApplicationManager.getApplication().invokeLater { switcher.storeCurrentThenSwitchToEnglish() }
				return
			}
		}
	}
	private val insertListener = object : VimInsertListener {
		override fun insertModeStarted(editor: Editor) {
			if (!editor.isInsertMode) {
				return
			}
			if (contextAware) {
				if (editor.document.charsSequence.isEmpty()) {
					return
				}
				val pos = editor.caretModel.primaryCaret.offset
				val chars = editor.document.charsSequence.subSequence(
					max(pos - 1, 0),
					min(pos + 1, editor.document.textLength - 1)
				)
				if (chars.any { CharUtils.isAscii(it) }) {
					return
				}
			}
			ApplicationManager.getApplication().invokeLater { switcher.restore() }
		}
	}

	fun enable() {
		if (enabled) {
			return
		}
		enabled = true
		registerExitInsertModeListener()
		registerFocusChangeListener()
		if (restoreInInsert) {
			registerVimInsertListener()
		}
	}

	private fun registerExitInsertModeListener() {
		messageBusConnection = ApplicationManager.getApplication().messageBus.connect()
		messageBusConnection?.subscribe(CommandListener.TOPIC, exitInsertModeListener)
	}

	private fun unregisterExitInsertModeListener() {
		messageBusConnection?.disconnect()
	}

	private fun registerVimInsertListener() {
		VimPlugin.getChange().addInsertListener(insertListener)
	}

	private fun unregisterVimInsertListener() {
		VimPlugin.getChange().removeInsertListener(insertListener)
	}

	private fun registerFocusChangeListener() {
		val eventMulticaster =
			EditorFactory.getInstance().eventMulticaster as? EditorEventMulticasterEx ?: return
		eventMulticaster.addFocusChangeListener(focusListener) {}
	}

	private val focusListener = object : FocusChangeListener {

		override fun focusLost(editor: Editor) {}

		override fun focusGained(editor: Editor) {
			if (!enabled || !VimPlugin.isEnabled()) {
				return
			}
			val state = CommandState.getInstance(editor)
			if (state.mode !in EDITING_MODES) {
				ApplicationManager.getApplication().invokeLater { switcher.switchToEnglish() }
			}
		}
	}


	fun disable() {
		if (!enabled) {
			return
		}
		unregisterVimInsertListener()
		unregisterExitInsertModeListener()
		enabled = false
	}
}
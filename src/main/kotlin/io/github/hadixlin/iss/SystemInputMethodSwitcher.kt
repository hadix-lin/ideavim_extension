package io.github.hadixlin.iss

import com.maddyhome.idea.vim.VimPlugin
import io.github.hadixlin.iss.lin.LinFcitx5RimeSwitcher
import io.github.hadixlin.iss.lin.LinFcitxRemoteSwitcher
import io.github.hadixlin.iss.lin.LinuxIbusSwitcher
import io.github.hadixlin.iss.mac.MacInputMethodSwitcher
import io.github.hadixlin.iss.win.WinInputMethodSwitcher
import org.apache.commons.lang.SystemUtils
import java.util.*
import java.util.concurrent.TimeUnit


/** Created by hadix on 26/12/2018.  */
class SystemInputMethodSwitcher
	: InputMethodSwitcher {
	private val delegate: InputMethodSwitcher by lazy {

		//获取输入法设置的环境变量
		//当前系统环境变量判断
		val variableService = VimPlugin.getVariableService()
		val english = variableService.getGlobalVariableValue(INPUT_SOURCE_IN_NORMAL)
		val nonEnglish = variableService.getGlobalVariableValue(INPUT_SOURCE_IN_INSERT)
		return@lazy when {
			SystemUtils.IS_OS_WINDOWS -> {
				WinInputMethodSwitcher(
					english?.toVimNumber()?.value?.toLong(), nonEnglish?.toVimNumber()?.value?.toLong()
				)
			}

			SystemUtils.IS_OS_MAC -> {
				if (english != null) {
					MacInputMethodSwitcher(english.asString(), nonEnglish = nonEnglish?.asString())
				} else {
					MacInputMethodSwitcher(nonEnglish = nonEnglish?.asString())
				}
			}

			SystemUtils.IS_OS_LINUX -> {
				//获取输入法设置的环境变量
				val qtInputMethod = System.getenv(QT_INPUT_METHOD)
				val gtkInputMethod = System.getenv(GTK_INPUT_METHOD)
				//当前系统环境变量判断
				if (isFcitx(qtInputMethod, gtkInputMethod)) {
					if (canUseRimeAscii()) {
						LinFcitx5RimeSwitcher()
					} else {
						LinFcitxRemoteSwitcher()
					}
				} else if (isIbus(qtInputMethod, gtkInputMethod)) {
					LinuxIbusSwitcher()
				} else {
					throw IllegalArgumentException("Not Support Current Input Method [${qtInputMethod ?: gtkInputMethod}], Only Support Linux(with fcitx and ibus)")
				}
			}

			else -> throw IllegalArgumentException("Not Support Current System OS, Only Support Windows, MacOS and Linux(with fcitx and ibus)")
		}
	}


	override fun switchToEnglish() {
		delegate.switchToEnglish()
	}

	override fun storeCurrentThenSwitchToEnglish() {
		delegate.storeCurrentThenSwitchToEnglish()
	}

	override fun restore() {
		delegate.restore()
	}

	companion object {
		private const val INPUT_SOURCE_IN_NORMAL = "keep_input_source_in_normal"
		private const val INPUT_SOURCE_IN_INSERT = "keep_input_source_in_insert"
		private const val INPUT_METHOD_FCITX = "fcitx"
		private const val INPUT_METHOD_FCITX5 = "fcitx5"
		private const val INPUT_METHOD_IBUS = "ibus"
		private const val QT_INPUT_METHOD = "QT_IM_MODULE"
		private const val GTK_INPUT_METHOD = "GTK_IM_MODULE"
		private const val RIME_ASCII_MODE = "rime_ascii"

		fun isFcitx(qtInputMethod: String?, gtkInputMethod: String?): Boolean {
			return qtInputMethod == INPUT_METHOD_FCITX
					|| gtkInputMethod == INPUT_METHOD_FCITX
					|| qtInputMethod == INPUT_METHOD_FCITX5
					|| gtkInputMethod == INPUT_METHOD_FCITX5
		}

		fun canUseRimeAscii(): Boolean {
			val rimeAscii = VimPlugin.getVariableService().getGlobalVariableValue(RIME_ASCII_MODE)?.asBoolean() ?: false
			if (!rimeAscii) {
				return false
			}
			val cmd = arrayOf(
				"busctl", "--user", "call", "org.fcitx.Fcitx5", "/controller",
				"org.fcitx.Fcitx.Controller1", "CurrentInputMethod"
			)
			val proc = Runtime.getRuntime().exec(cmd)
			proc.waitFor(3, TimeUnit.SECONDS)
			return Scanner(proc.inputStream).use {
				if (it.hasNextLine()) {
					val line = it.nextLine()
					line.startsWith("s") && line.contains("rime")
				} else {
					false
				}
			}
		}

		fun isIbus(qtInputMethod: String?, gtkInputMethod: String?): Boolean =
			qtInputMethod == INPUT_METHOD_IBUS || gtkInputMethod == INPUT_METHOD_IBUS
	}
}
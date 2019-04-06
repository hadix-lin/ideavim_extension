package io.github.hadixlin.iss

/**
 * @author hadix
 * @date 09/04/2017
 */
class KeepEnglishInNormalExtension : KeepEnglishInNormalAndRestoreInInsertExtension(false) {

	override fun getName(): String {
		return "keep-english-in-normal"
	}
}

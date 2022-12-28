package io.github.hadixlin.iss.mac

import com.sun.jna.Native

/** Created by hadix on 30/03/2017.  */
object MacNative {

	init {
//		val jnaNounpack = System.getProperty("jna.nounpack")
//		System.setProperty("jna.nounpack", "false")
		Native.register("input-source-switcher")
//		if (jnaNounpack != null) {
//			System.setProperty("jna.nounpack", jnaNounpack)
//		}
	}

	external fun getCurrentInputSourceID(): String

	external fun switchInputSource(inputSourceID: String): Int
}
package io.github.hadixlin.iss.lin

import io.github.hadixlin.iss.InputMethodSwitcher
import java.util.*
import java.util.concurrent.TimeUnit

class LinuxIbusSwitcher : InputMethodSwitcher {
    private var lastStatus:String = STATUS_ENGLISH;
    override fun storeCurrentThenSwitchToEnglish() {
        val current = getStatus()
        if(current == STATUS_ENGLISH){
            return
        }
        lastStatus = current
        switchToEnglish()
    }

    override fun restore() {
        if (lastStatus!= STATUS_ENGLISH){
            execSet(lastStatus)
            lastStatus = STATUS_ENGLISH
        }
    }

    override fun switchToEnglish() {
        execSet(STATUS_ENGLISH)
    }

    companion object{
        private const val STATUS_ENGLISH="xkb:us::eng"
        private fun execSet(im:String):Process{
            val cmd = arrayOf("ibus engine",im)
            val proc = Runtime.getRuntime().exec(cmd)
            proc.waitFor(3, TimeUnit.SECONDS)
            return proc
        }

        private fun getStatus():String{
            val cmd = "ibus engine"
            val proc = Runtime.getRuntime().exec(cmd)
            proc.waitFor(3, TimeUnit.SECONDS)
            return Scanner(proc.inputStream).use {
                if (it.hasNextInt()) {
                    it.nextLine()
                } else{
                    ""
                }


            }
        }
    }
}
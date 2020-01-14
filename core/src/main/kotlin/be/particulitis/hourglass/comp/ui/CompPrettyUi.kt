package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.font.FontAnim

class CompPrettyUi : CompTxt() {

    internal var currentIndex = 0
    internal var time = .1f
    internal var anims = arrayListOf<FontAnim>()
    internal var phase = 0
    internal var phases = arrayOf(
            Phase(1, 40f),
            Phase(2, 100f),
            Phase(3, 99999999999999999f)
    )
    internal val currentPhase: Phase
        get() { return phases[phase] }

    fun changePhase(phase: Int) {
        this.phase = phase
        changeText(text, phases[phase].w)
    }

    data class Phase(val w: Int, val endTime: Float)
}
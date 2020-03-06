package be.particulitis.hourglass.comp

class CompTtl : Comp() {
    var remaining = 1f
    var onEnd = {}
    var triggered = false

    override fun reset() {
        super.reset()
        remaining = 1f
        triggered = false
        onEnd = {}
    }
}
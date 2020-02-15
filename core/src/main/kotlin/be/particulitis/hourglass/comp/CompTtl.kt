package be.particulitis.hourglass.comp

class CompTtl : Comp() {
    var remaining = 1f
    var onEnd = {}

    fun clear() {
        reset()
        remaining = 1f
    }

}
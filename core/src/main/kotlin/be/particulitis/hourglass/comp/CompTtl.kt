package be.particulitis.hourglass.comp

class CompTtl : Comp() {
    var remaining = 1f

    fun clear() {
        reset()
        remaining = 1f
    }

    fun finished() {
        remaining = 1f
    }
}
package be.particulitis.hourglass.comp

class CompIsPlayer : Comp() {

    var isPlayer = true
        private set

    fun setPlayer(isPlayer: Boolean) {
        this.isPlayer = isPlayer
    }

    override fun reset() {
        super.reset()
        isPlayer = true
    }

}
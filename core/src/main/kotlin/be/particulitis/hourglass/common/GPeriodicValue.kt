package be.particulitis.hourglass.common

class GPeriodicValue<T>(var periodicity: Float, val updateMethod: () -> T) {

    var value: T = updateMethod.invoke()
    private var nextTick = 0f
    private var time = 0f

    fun tick(delta: Float) {
        if (nextTick < time) {
            nextTick = time + periodicity
            value = updateMethod.invoke()
        }
        time += delta
    }

}
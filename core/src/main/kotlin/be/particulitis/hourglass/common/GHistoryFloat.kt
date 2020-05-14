package be.particulitis.hourglass.common

class GHistoryFloat(val historySize: Int = 10) {
    private val numbers = FloatArray(historySize)
    private var cursor = historySize + 1

    fun add(f: Float) {
        cursor++
        numbers.safePut(f, cursor)
    }

    fun get(): Float {
        return numbers.safeGet(cursor)
    }

    fun get(i: Int): Float {
        return numbers.safeGet(cursor + i)
    }

    fun fill(f: Float) {
        for (i in 0..numbers.size)
            add(f)
    }

}

private fun FloatArray.safeGet(i: Int): Float {
    return this[i % size]
}

private fun FloatArray.safePut(f: Float, i: Int) {
    this[i % size] = f
}

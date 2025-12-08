package me.salmonmoses.utils

enum class IteratorDirection {
    FORWARD,
    BACKWARD
}

class DequeIterator<out T>(
        private val deque: ArrayDeque<out T>,
        private val direction: IteratorDirection = IteratorDirection.FORWARD
) : Iterator<T> {
    override fun hasNext(): Boolean = deque.isNotEmpty()

    override fun next(): T = when (direction) {
        IteratorDirection.FORWARD -> deque.removeFirst()
        IteratorDirection.BACKWARD -> deque.removeLast()
    }
}
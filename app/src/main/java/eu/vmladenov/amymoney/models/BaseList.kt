package eu.vmladenov.amymoney.models

open class BaseList<T>(items: List<T> = emptyList()): ArrayList<T>(items) {
    fun fill(items: Collection<T>) {
        clear()
        addAll(items)
    }
}

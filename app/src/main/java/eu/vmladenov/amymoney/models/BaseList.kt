package eu.vmladenov.amymoney.models

open class BaseList<T>: ArrayList<T>(emptyList()) {
    fun fill(items: Collection<T>) {
        clear()
        addAll(items)
    }
}

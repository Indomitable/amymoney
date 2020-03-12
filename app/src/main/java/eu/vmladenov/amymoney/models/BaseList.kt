package eu.vmladenov.amymoney.models

open class BaseList<T>(): ArrayList<T>() {
    fun fill(items: Collection<T>) {
        clear()
        addAll(items)
    }
}


open class BaseMap<T: IModel>(): HashMap<String, T>()

fun <T> MutableMap<String, T>.fill(map: Map<String, T>) {
    for ((key, value) in map.entries) {
        this[key] = value
    }
}

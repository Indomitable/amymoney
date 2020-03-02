package eu.vmladenov.amymoney.models

import androidx.recyclerview.widget.DiffUtil

interface IModel {
    val id: String

    override operator fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}

open class Comparable<TModel: IModel>: DiffUtil.ItemCallback<TModel>() {
    override fun areItemsTheSame(oldItem: TModel, newItem: TModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TModel, newItem: TModel): Boolean {
        return oldItem == newItem
    }
}


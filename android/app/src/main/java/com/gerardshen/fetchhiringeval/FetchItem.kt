package com.gerardshen.fetchhiringeval

import kotlinx.serialization.Serializable

/**
 * Data class to hold an individual item received from the backend request.
 */
@Serializable
data class FetchItem(val id: Int, val listId: Int, val name: String?) : Comparable<FetchItem> {
    override fun compareTo(other: FetchItem): Int {
        return id.compareTo(other.id)
    }
}

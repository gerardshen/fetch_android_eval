package com.gerardshen.fetchhiringeval

import android.util.Log
import java.util.TreeMap
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * A service to handle communication with the Fetch backend.
 */
class FetchMessagingService {

    private val client = OkHttpClient()
    private val ITEM_REQUEST_API = "https://fetch-hiring.s3.amazonaws.com/hiring.json"
    private val TAG = "FetchMessagingService"

    /**
     * Parse the response JSON into an ordered Map whose keys are the ids of item lists and
     * whose values are lists of items sorted by item id.
     *
     * @param jsonString: JSON string from response body
     * @return the sorted Map, or null if parsing fails
     */
    private fun parseFetchItemsJson(jsonString: String): TreeMap<Int, MutableList<FetchItem>>? {
        val items: Array<FetchItem>
        try {
            items = Json.decodeFromString<Array<FetchItem>>(jsonString)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing response json:")
            e.printStackTrace()
            return null
        }
        val itemsFilteredAndSorted = items.filter {
            !it.name.isNullOrEmpty()
        }.sorted()
        val itemsMap = TreeMap<Int, MutableList<FetchItem>>()
        for (item in itemsFilteredAndSorted) {
            if (itemsMap.contains(item.listId)) {
                itemsMap[item.listId]?.add(item)
            } else {
                itemsMap[item.listId] = mutableListOf(item)
            }
        }
        return itemsMap
    }

    /**
     * Request a series of items, and return an ordered Map whose keys are the ids of item lists and
     * whose values are lists of items sorted by item id.
     *
     * @return the sorted Map, or null if parsing fails or the network request fails
     */
    fun requestFetchItems(): TreeMap<Int, MutableList<FetchItem>>? {
        val request = Request.Builder()
            .url(ITEM_REQUEST_API)
            .build()
        val result: Response
        try {
            result = client.newCall(request).execute()
        } catch (e: Exception) {
            Log.e(TAG, "Error requesting items:")
            e.printStackTrace()
            return null
        }
        result.use {
            if (null == it.body) {
                return null
            }
            return parseFetchItemsJson(it.body!!.string())
        }
    }
}
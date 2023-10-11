/*
package com.keval.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.keval.model.GetProductRes
import com.retailer.Network.ApiService
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, GetProductRes.Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetProductRes.Product> {
        val page = params.key ?: 0

        return try {
            val response = apiService.getProductList(10, page * 10, "test")

            if (response!=null) {
                val data = response.body()?.products ?: emptyList()
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (data.isEmpty()) null else page + 1

                LoadResult.Page(data, prevKey, nextKey)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetProductRes.Product>): Int? {
        return state.anchorPosition
    }
}
*/

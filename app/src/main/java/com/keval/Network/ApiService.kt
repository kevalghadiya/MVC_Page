package com.retailer.Network

import com.keval.model.GetProductRes
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("products")
    fun getProductList(@Query("limit") limit:Int,@Query("skip") skip:Int,@Query("select") select:String):Observable<Response<GetProductRes>>

   /* @POST("sub_category_list")
    fun getSubCategoryList(@Body categoryMainSubReq: CategoryMainSubReq): Observable<Response<CategoryMainSubRes>>
   */
}


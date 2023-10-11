package com.keval.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bistrocart.base.BaseActivity
import com.google.gson.Gson
import com.keval.adapter.ProductListAdapter
import com.keval.model.GetProductRes
import com.keval.mvc.R
import com.keval.mvc.databinding.ActivityMain2Binding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class Main2Activity : BaseActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var productListAdapter: ProductListAdapter
    private val productArrayList = ArrayList<GetProductRes.Product>()
    private var isLoading = false
    private var currentPage = 0
    private val itemsPerPage = 10
    private var totalItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)

        setProductListRv()
        loadMoreData()
    }

    private fun setProductListRv() {
        productListAdapter = ProductListAdapter(this, productArrayList, object : ProductListAdapter.OnClick {
            override fun onClick(catId: String) {
                // Handle item click
            }
        })
        binding.rvProductList.adapter = productListAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvProductList.layoutManager = layoutManager

        // Add a scroll listener to load more data when reaching the end of the list.
        binding.rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    loadMoreData()
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun loadMoreData() {
        if (!isLoading && (totalItems == 0 || productArrayList.size < totalItems)) {
            isLoading = true

            showLoading("Loading")

            apiService?.getProductList(itemsPerPage, 0, "test")
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { t: Response<GetProductRes> ->
                        hideLoading()
                        isLoading = false

                        if (isSuccess(t, t.message())) {
                            t.body()?.let { productArrayList.addAll(it.products) }
                            productListAdapter.notifyDataSetChanged()
                            totalItems = t.body()!!.total
                            currentPage++
                        } else {
                            // Handle error
                        }
                    },
                    { error ->
                        hideLoading()
                        isLoading = false
                        onFailure(error)
                    }
                )
        }
    }
}

package com.h.users.network

import com.google.gson.annotations.SerializedName
import com.h.users.data.Ad
import com.h.users.data.User

data class ApiResponse (
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val per_page: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("data")
    val data: List<User>,
    @SerializedName("ad")
    val ad: Ad
)
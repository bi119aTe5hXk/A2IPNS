// (C) 2019 Xlfdll Workstation
// Used with permission

package org.xlfdll.android.network

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

public class JsonObjectRequestWithCustomHeaders(
    method: Int,
    url: String,
    val customHeaders: MutableMap<String, String>?,
    jsonRequest: JSONObject,
    listener: Response.Listener<JSONObject>,
    errorListener: Response.ErrorListener
) :
    JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {


    override fun getHeaders(): MutableMap<String, String> {
        return customHeaders ?: super.getHeaders()
    }
}
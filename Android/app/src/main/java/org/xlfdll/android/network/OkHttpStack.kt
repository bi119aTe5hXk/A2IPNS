// (C) 2019 Xlfdll Workstation
// Used with permission

package org.xlfdll.android.network

import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.HttpResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class OkHttpStack : BaseHttpStack() {
    private val client = OkHttpClient()

    override fun executeRequest(request: Request<*>?, additionalHeaders: MutableMap<String, String>?): HttpResponse {
        val requestBuilder = okhttp3.Request.Builder().url(request!!.url)
        val requestHeaders = request.headers

        for (entry in requestHeaders) {
            requestBuilder.addHeader(entry.key, entry.value)
        }

        if (additionalHeaders != null) {
            for (entry in additionalHeaders) {
                requestBuilder.addHeader(entry.key, entry.value)
            }
        }

        setConnectionParametersForRequest(requestBuilder, request)

        val response = client.newCall(requestBuilder.build()).execute()
        val responseHeaders = mutableListOf<Header>()
        val responseContentLength = response.body?.contentLength()
        val responseInputStream = response.body?.byteStream()
        lateinit var httpResponse: HttpResponse

        for (header in response.headers) {
            responseHeaders.add(Header(header.first, header.second))
        }

        if (responseContentLength != null && responseInputStream != null) {
            httpResponse =
                HttpResponse(response.code, responseHeaders, responseContentLength.toInt(), responseInputStream)
        } else {
            httpResponse = HttpResponse(response.code, responseHeaders)
        }

        return httpResponse
    }

    private fun setConnectionParametersForRequest(builder: okhttp3.Request.Builder, request: Request<*>) {
        when (request.method) {
            Request.Method.DEPRECATED_GET_OR_POST -> {
                val postBody = createRequestBody(request)

                if (postBody != null) {
                    builder.post(postBody)
                }
            }
            Request.Method.GET -> builder.get()
            Request.Method.DELETE -> builder.delete()
            Request.Method.POST -> {
                val postBody = createRequestBody(request)

                if (postBody != null) {
                    builder.post(postBody)
                }
            }
            Request.Method.PUT -> {
                val putBody = createRequestBody(request)

                if (putBody != null) {
                    builder.put(putBody)
                }
            }
            Request.Method.PATCH -> {
                val patchBody = createRequestBody(request)

                if (patchBody != null) {
                    builder.patch(patchBody)
                }
            }
            Request.Method.HEAD -> builder.head()
            Request.Method.OPTIONS -> builder.method("OPTIONS", null)
            Request.Method.TRACE -> builder.method("TRACE", null)
            else -> throw IllegalStateException("Unknown method type.")
        }
    }

    private fun createRequestBody(request: Request<*>): RequestBody? {
        if (request.body != null) {
            return request.body.toRequestBody(request.bodyContentType.toMediaType())
        }

        return null
    }
}
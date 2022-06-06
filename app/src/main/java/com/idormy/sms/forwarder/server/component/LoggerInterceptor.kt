package com.idormy.sms.forwarder.server.component

import android.util.Log
import com.idormy.sms.forwarder.utils.HttpServerUtils
import com.yanzhenjie.andserver.annotation.Interceptor
import com.yanzhenjie.andserver.error.HttpException
import com.yanzhenjie.andserver.framework.HandlerInterceptor
import com.yanzhenjie.andserver.framework.handler.MethodHandler
import com.yanzhenjie.andserver.framework.handler.RequestHandler
import com.yanzhenjie.andserver.http.HttpMethod
import com.yanzhenjie.andserver.http.HttpRequest
import com.yanzhenjie.andserver.http.HttpResponse

@Suppress("PrivatePropertyName")
@Interceptor
class LoggerInterceptor : HandlerInterceptor {

    private val TAG: String = "LoggerInterceptor"

    override fun onIntercept(
        request: HttpRequest,
        respons: HttpResponse,
        handler: RequestHandler,
    ): Boolean {
        if (handler is MethodHandler) {
            val httpPath = request.path
            val method: HttpMethod = request.method
            val valueMap = request.parameter
            Log.i(TAG, "Path: $httpPath")
            Log.i(TAG, "Method: " + method.value())
            Log.i(TAG, "Param: $valueMap")

            //判断是否开启该功能
            if (
                (httpPath.startsWith("/clone") && !HttpServerUtils.enableApiClone)
                || (httpPath.startsWith("/sms/send") && !HttpServerUtils.enableApiSmsSend)
                || (httpPath.startsWith("/sms/query") && !HttpServerUtils.enableApiSmsQuery)
                || (httpPath.startsWith("/call/query") && !HttpServerUtils.enableApiCallQuery)
                || (httpPath.startsWith("/contact/query") && !HttpServerUtils.enableApiContactQuery)
                || (httpPath.startsWith("/battery/query") && !HttpServerUtils.enableApiBatteryQuery)
            ) {
                throw HttpException(500, "服务端未开启此功能")
            }

            /*
            //TODO:这里读取body会导致 MessageConverter 报错：RequestBody is missing.
            val body = request.body?.string()
            Log.i(TAG, "Body: $body")
            */
        }
        return false
    }
}
package com.github.theapache64.now.perf

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

object ApiPerfDataCollector {

    private val map: ConcurrentHashMap<String, ApiPerfData> = ConcurrentHashMap()

    fun addCallStartTime(key: String, timeMs: Long, url: String) {
        map[key] = ApiPerfData().also {
            it.url = url
            it.callTime.startMs.add(timeMs)
        }
    }

    fun addDnsStartTime(key: String, timeMs: Long) {
        map[key]?.dnsTime?.startMs?.add(timeMs)
    }

    fun addDnsEndTime(key: String, timeMs: Long) {
        map[key]?.dnsTime?.endMs?.add(timeMs)
    }

    fun addConnectStartTime(key: String, timeMs: Long) {
        map[key]?.connectionTime?.startMs?.add(timeMs)
    }

    fun addSecureConnectStartTime(key: String, timeMs: Long) {
        map[key]?.secureConnectionTime?.startMs?.add(timeMs)
    }

    fun addSecureConnectEndTime(key: String, timeMs: Long) {
        map[key]?.secureConnectionTime?.endMs?.add(timeMs)
    }

    fun addConnectEndTime(key: String, timeMs: Long) {
        map[key]?.connectionTime?.endMs?.add(timeMs)
    }

    fun addConnectionAcquiredTime(key: String, timeMs: Long) {
        map[key]?.connectionAcquiredTime?.startMs?.add(timeMs)
    }

    fun addRequestHeaderStartTime(key: String, timeMs: Long) {
        map[key]?.requestHeaderTime?.startMs?.add(timeMs)
    }

    fun addRequestHeaderEndTime(key: String, timeMs: Long) {
        map[key]?.requestHeaderTime?.endMs?.add(timeMs)
    }

    fun addRequestBodyStartTime(key: String, timeMs: Long) {
        map[key]?.requestBodyTime?.startMs?.add(timeMs)
    }

    fun addRequestBodyEndTime(key: String, timeMs: Long) {
        map[key]?.requestBodyTime?.endMs?.add(timeMs)
    }

    fun addResponseHeaderStartTime(key: String, timeMs: Long) {
        map[key]?.responseHeaderTime?.startMs?.add(timeMs)
    }

    fun addResponseHeaderEndTime(key: String, timeMs: Long) {
        map[key]?.responseHeaderTime?.endMs?.add(timeMs)
    }

    fun addResponseBodyStartTime(key: String, timeMs: Long) {
        map[key]?.responseBodyTime?.startMs?.add(timeMs)
    }

    fun addResponseBodyEndTime(key: String, timeMs: Long) {
        map[key]?.responseBodyTime?.endMs?.add(timeMs)
    }

    fun addConnectionReleasedTime(key: String, timeMs: Long) {
        map[key]?.connectionAcquiredTime?.endMs?.add(timeMs)
    }

    fun addCallEndTime(key: String, timeMs: Long) {
        map[key]?.callTime?.endMs?.add(timeMs)
    }

    fun setResponseBodySize(key: String, size: Long) {
        map[key]?.responseBodySizeByte = size
    }

    fun getApiPerfData(key: String): ApiPerfData? {
        return map[key]
    }

    fun clearApiPerfData(key: String) {
        map.remove(key)
    }

}

class ApiPerfData {

    var url: String = ""
    val callTime = TimeInfo()
    val dnsTime = TimeInfo()
    val connectionTime = TimeInfo()
    val secureConnectionTime = TimeInfo()
    val connectionAcquiredTime = TimeInfo()
    val requestHeaderTime = TimeInfo()
    val requestBodyTime = TimeInfo()
    val responseHeaderTime = TimeInfo()
    val responseBodyTime = TimeInfo()
    var responseBodySizeByte: Long = -1L
        internal set

    override fun toString(): String {
        return StringBuilder(this.javaClass.simpleName)
            .appendLine()
            .appendLine("url: $url")
            .appendLine("callStartTimeMs: ${callTime.startMs}")
            .appendLine("dnsStartTimeMs: ${dnsTime.startMs}")
            .appendLine("dnsEndTimeMs: ${dnsTime.endMs}")
            .appendLine("connectStartMs: ${connectionTime.startMs}")
            .appendLine("secureConnectStartMs: ${secureConnectionTime.startMs}")
            .appendLine("secureConnectEndMs: ${secureConnectionTime.endMs}")
            .appendLine("connectEndMs: ${connectionTime.endMs}")
            .appendLine("connectionAcquiredTimeMs: ${connectionAcquiredTime.startMs}")
            .appendLine("requestHeaderStartMs: ${requestHeaderTime.startMs}")
            .appendLine("requestHeaderEndMs: ${requestHeaderTime.endMs}")
            .appendLine("requestBodyStartMs: ${requestBodyTime.startMs}")
            .appendLine("requestBodyEndMs: ${requestBodyTime.endMs}")
            .appendLine("responseHeaderStartMs: ${responseHeaderTime.startMs}")
            .appendLine("responseHeaderEndMs: ${responseHeaderTime.endMs}")
            .appendLine("responseBodyStartMs: ${responseBodyTime.startMs}")
            .appendLine("responseBodyEndMs: ${responseBodyTime.endMs}")
            .appendLine("responseBodySizeMs: $responseBodySizeByte")
            .appendLine("connectionReleasedMs: ${connectionAcquiredTime.endMs}")
            .appendLine("callEndTimeMs: ${callTime.endMs}")
            .appendLine("******")
            .appendLine("callTime: ${callTime()}")
            .appendLine("dnsTime: ${dnsTime()}")
            .appendLine("connectTime: ${connectTime()}")
            .appendLine("requestHeaderTime: ${requestHeaderTime()}")
            .appendLine("requestBodyTime: ${requestBodyTime()}")
            .appendLine("responseHeaderTime: ${responseHeaderTime()}")
            .appendLine("responseBodyTime: ${responseBodyTime()}")
            .appendLine("connectedTime: ${connectedTime()}")
            .appendLine("secureConnectTime: ${secureConnectTime()}")
            .appendLine("networkWaitingTime: ${networkWaitingTime()}")
            .appendLine("dataTransferTime: ${dataTransferTime()}")
            .toString()
    }
}

fun ApiPerfData.callTime(): Long {
    return if (callTime.startMs.size > 0 && callTime.endMs.size > 0) {
        callTime.endMs[0] - callTime.startMs[0]
    } else {
        -1
    }
}

fun ApiPerfData.dnsTime(): Long {
    return if (dnsTime.startMs.isNotEmpty() && dnsTime.endMs.isNotEmpty()) {
        val length = min(dnsTime.startMs.size, dnsTime.endMs.size)
        dnsTime.endMs[length - 1] - dnsTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.connectTime(): Long {
    return if (connectionTime.startMs.isNotEmpty() && connectionTime.endMs.isNotEmpty()) {
        val length = min(connectionTime.startMs.size, connectionTime.endMs.size)
        connectionTime.endMs[length - 1] - connectionTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.requestHeaderTime(): Long {
    return if (requestHeaderTime.startMs.isNotEmpty() && requestHeaderTime.endMs.isNotEmpty()) {
        val length = min(requestHeaderTime.startMs.size, requestHeaderTime.endMs.size)
        requestHeaderTime.endMs[length - 1] - requestHeaderTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.requestBodyTime(): Long {
    return if (requestBodyTime.startMs.isNotEmpty() && requestBodyTime.endMs.isNotEmpty()) {
        val length = min(requestBodyTime.startMs.size, requestBodyTime.endMs.size)
        requestBodyTime.endMs[length - 1] - requestBodyTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.responseHeaderTime(): Long {
    return if (responseHeaderTime.startMs.isNotEmpty() && responseHeaderTime.endMs.isNotEmpty()) {
        val length = min(responseHeaderTime.startMs.size, responseHeaderTime.endMs.size)
        responseHeaderTime.endMs[length - 1] - responseHeaderTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.responseBodyTime(): Long {
    return if (responseBodyTime.startMs.isNotEmpty() && responseBodyTime.endMs.isNotEmpty()) {
        val length = min(responseBodyTime.startMs.size, responseBodyTime.endMs.size)
        responseBodyTime.endMs[length - 1] - responseBodyTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.connectedTime(): Long {
    return if (connectionAcquiredTime.startMs.isNotEmpty() && connectionAcquiredTime.endMs.isNotEmpty()) {
        val length = min(connectionAcquiredTime.startMs.size, connectionAcquiredTime.endMs.size)
        connectionAcquiredTime.endMs[length - 1] - connectionAcquiredTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.secureConnectTime(): Long {
    return if (secureConnectionTime.startMs.isNotEmpty() && secureConnectionTime.endMs.isNotEmpty()) {
        val length = min(secureConnectionTime.startMs.size, secureConnectionTime.endMs.size)
        secureConnectionTime.endMs[length - 1] - secureConnectionTime.startMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.networkWaitingTime(): Long {
    val start = if (requestBodyTime.endMs.isNotEmpty()) {
        requestBodyTime
    } else {
        requestHeaderTime
    }

    return if (start.endMs.isNotEmpty() && responseHeaderTime.startMs.isNotEmpty()) {
        val length = min(responseHeaderTime.startMs.size, start.endMs.size)
        return responseHeaderTime.startMs[length - 1] - start.endMs[length - 1]
    } else {
        -1
    }
}

fun ApiPerfData.dataTransferTime(): Long {
    return if (responseBodyTime.endMs.isNotEmpty() && requestHeaderTime.startMs.isNotEmpty()) {
        val length = min(requestHeaderTime.startMs.size, responseBodyTime.endMs.size)
        responseBodyTime.endMs[length - 1] - requestHeaderTime.startMs[length - 1]
    } else {
        -1
    }
}

class TimeInfo {
    val startMs: MutableList<Long> = mutableListOf()
    val endMs: MutableList<Long> = mutableListOf()
}

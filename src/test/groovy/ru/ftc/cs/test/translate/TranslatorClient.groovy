package ru.ftc.cs.test.translate

import groovyx.net.http.RESTClient

class TranslatorClient {

    final RESTClient restClient

    final String apiKey

    TranslatorClient(String apiKey, RESTClient restClient) {
        this.restClient = restClient
        this.apiKey = apiKey
    }

    TranslatorClient(String apiKey, String baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/") {
        this(apiKey, new RESTClient( baseUrl ))
        restClient.ignoreSSLIssues()
    }

    Map call(TranslatorMethod method, Map queryData){
        restClient.get(path: method.name(), query: [key: apiKey] + queryData).data
    }

    enum TranslatorMethod {
        getLangs,
        detect,
        translate,
    }
}

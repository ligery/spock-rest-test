package ru.ftc.cs.test.translate

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

import static ru.ftc.cs.test.translate.RestClientFactory.getRestClient

/**
 * Spec for Translator Tests
 */
abstract class TranslatorSpec extends Specification {

    public static final API_KEY = 'trnsl.1.1.20190527T152817Z.a7cebfd6bf9686b6.1b4d1cf544a8c59b6d2310af35536b0e84163425'

    @Shared
    public RESTClient restClient = getRestClient("https://translate.yandex.net/api/v1.5/tr.json/")



}

package ru.ftc.cs.test.translate

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

import static ru.ftc.cs.test.translate.RestClientFactory.getRestClient

/**
 * Spec for Translator Tests
 */
abstract class TranslatorSpec extends Specification {

    public static final API_KEY = 'trnsl.1.1.20170829T162324Z.aa20ac0fd7879d3e.c6575c65d8cb7bd3ccb30b3ba6c6b9676bfc4b9d'

    @Shared
    public RESTClient restClient = getRestClient("https://translate.yandex.net/api/v1.5/tr.json/")



}

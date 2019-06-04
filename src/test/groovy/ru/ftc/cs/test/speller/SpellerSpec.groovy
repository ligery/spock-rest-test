package ru.ftc.cs.test.speller

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

import static ru.ftc.cs.test.translate.RestClientFactory.getRestClient

/**
 * Spec for Speller Tests
 */
abstract class SpellerSpec extends Specification {

    @Shared
    public RESTClient restClient = getRestClient("https://speller.yandex.net/services/spellservice.json/")

}

package ru.ftc.cs.test.translate

import groovyx.net.http.HttpResponseException
import spock.lang.Unroll

import static ru.ftc.cs.test.translate.YandexLang.*

/**
 * Simple Http test
 */
class TranslatorRestTest extends TranslatorSpec {

    def "translator Permission Denied: Missing API Key"(){
        when:
        restClient.get(path: "getLangs")

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400
        exception.response.data  == [code: 502, message: "Invalid parameter: key"]
    }

    def "translator Permission Denied: API Key is blocked"(){
        when:
        restClient.get(path: "getLangs", query: [key: BLOCKED_API_KEY])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 403
        exception.response.data  == [code: 402, message: "API key is blocked"]
    }

    @Unroll
    def "getLangs API"(){
        when:
        def result = restClient.get([path: 'getLangs', query: [key: API_KEY, ui: ru.name()]])

        then:
        result.data.size() == 2
        result.data.dirs.size() == 160
        result.data.langs.size() == 93

        when:
        def result1 = restClient.get([path: 'getLangs', query: [key: API_KEY, ui: 'russia']])

        then:
        result1.data.size() == 2
        result1.data.dirs.size() == 160
        result1.data.langs.size() == 93
    }

    @Unroll
    def "detect Lang for #word: #lang"(){
        when:
        def result = restClient.get([path: 'detect', query: [key: API_KEY, text: word]])

        then:
        result.data == [code:200, lang: lang?.name()]

        where:
        word        | lang
        "Ananas"    | en
        "Ананас"    | ru
        "Gut"       | de
        "Petit"     | en
        "Bongour"   | fr
        "LLLL"      | es
        "a1"        | en
    }

    @Unroll
    def "detect Empty Lang for #word"(){
        when:
        def result = restClient.get([path: 'detect', query: [key: API_KEY, text: word]])

        then:
        result.data == [code:200, lang: ""]

        where:
        word << ["12", '.']
    }


}

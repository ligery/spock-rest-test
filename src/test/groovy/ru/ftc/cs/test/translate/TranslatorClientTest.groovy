package ru.ftc.cs.test.translate

import groovy.transform.ToString
import groovyx.net.http.HttpResponseException
import spock.lang.Shared
import spock.lang.Unroll

import static ru.ftc.cs.test.translate.TranslatorClient.TranslatorMethod.*
import static ru.ftc.cs.test.translate.YandexLang.*

/**
 * Simple Http test
 */
class TranslatorClientTest extends TranslatorSpec {

    @Shared
    TranslatorClient translatorClient = new TranslatorClient(API_KEY, restClient)

    @Unroll
    def "getLangs for #queryData"(){
        when:
        Map result = translatorClient.call(getLangs, queryData)

        then:
        (result.keySet() as List) == keys
        result.dirs.size() == 160

        where:
        queryData       | keys
        [:]             | ["dirs"]
        [ui: ru.name()] | ["dirs", "langs"]
    }

    @Unroll
    def "detect Lang for #word: #lang"(){
        expect:
        translatorClient.call(detect, [text: word]) == [code: 200, lang: lang.name()]

        where:
        word        | lang
        "Ананас"    | ru
        "Gut"       | de
        "Petit"     | en
        "Bonjour"   | fr
        "LLLL"      | es
        "a1"        | en
    }

    @Unroll
    def "Check correct lang format" (){
        when:
        def result = restClient.get([path: 'translate', query: [key: API_KEY, lang: lang, text: word]])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400
        exception.response.data  == [code: 501, message: "The specified translation direction is not supported"]

        where:
        word        | lang
        "Яблоко"    | 'rus'
        "Ананас"    | 'eng'
    }

    @Unroll
    def "Check uncorrect lang format" (){
        when:
        def result = restClient.get([path: 'translate', query: [key: API_KEY, lang: lang, text: word]])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400
        exception.response.data  == [code: 502, message: "Invalid parameter: lang"]

        where:
        word        | lang
        "Пончик"    | 'motherrussia'
    }

    @Unroll
    def "detect Empty Lang for #word"(){
        expect:
        translatorClient.call(detect, [text: word]) == [code:200, lang: ""]

        where:
        word << ["12", '.', 'check']
    }

    @Unroll
    def "translate #word from #lang1 to #lang2"(){
        expect:
        translatorClient.call(translate, [text: word, lang: lang1.name() + "-" + lang2.name()])

        where:
        word        | lang1     | lang2
        "Hello"     | en        | ru
        "Hello"     | en        | en
        "LLL"       | en        | ru
        "Hello"     | ru        | en
        "Howdy"     | az        | ba

    }

    @Unroll
    def "translate #word1 with #lang" (){
        when:
        def result = restClient.get([path: 'translate', query: [key: API_KEY, lang: lang, text: word1]])

        then:
        result.data == [code:200, lang: lang, text: [word2]]

        where:
        word1        | lang         | word2
        "Peach"      | 'en-ru'      | "Персик"
        "Peach"      | 'en-en'      | "Персик"
    }

    @Unroll
    def "translate #word1 to #lang" (){
        when:
        def result = restClient.get([path: 'translate', query: [key: API_KEY, text: word1, lang: lang.name(), options:1]])

        then:
        result.data.text[0] == word2

        where:
        word1        | lang         | word2
        'Peach'      | ru           | 'Персик'
        'Peach'      | en           | 'Персик'
    }



}

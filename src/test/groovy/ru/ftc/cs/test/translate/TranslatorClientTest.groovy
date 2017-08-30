package ru.ftc.cs.test.translate

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
        expect:
        translatorClient.call(detect, [text: word]) == [code:200, lang: ""]

        where:
        word << ["12", '.']
    }


}

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
    def "detect Lang succesfully for #word: #lang"(){
        expect:
        translatorClient.call(detect, [text: word]) == [code: 200, lang: lang.name()]

        where:
        word        | lang
        'Ананас'    | ru
        'Gut'       | de
        'Bonjour'   | fr
        'a1'        | en
    }

    @Unroll
    def "detect Lang assigned to en by default for #word"(){
        expect:
        translatorClient.call(detect, [text: word]) == [code: 200, lang: 'en']

        where:
        word  << ['a1', 'LLLL', 'sumword']
    }

    //TODO переделать на translatorClient
    def "query limit check"(){
        when:
        translatorClient.call(translate, [lang: 'en-ru', text: word])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 414

        where:
        word = 'This string is too long for translation' * 1000
    }

    //TODO переделать на translatorClient и добавить переменную в название
    @Unroll
    def "check 3 chars long lang format, lang: #lang" (){
        when:
        translatorClient.call(translate, [lang: lang, text: word])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400
        exception.response.data  == [code: 501, message: "The specified translation direction is not supported"]

        where:
        word        | lang
        'Яблоко'    | 'rus'
        'Ананас'    | 'eng'
        'Kurwa'     | 'pol'
    }

    //TODO переделать на translatorClient
    @Unroll
    def "Check incorrect lang format #lang" (){
        when:
        translatorClient.call(translate, [lang: lang, text: word])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400
        exception.response.data  == [code: 502, message: "Invalid parameter: lang"]

        where:
        word        | lang
        'Пончик'    | 'english'
        'Person'    | 'en ru'
    }

    //TODO переделать на translatorClient
    @Unroll
    def "detect Empty Lang for #word"(){
        when:
        def result = translatorClient.call(detect, [text: word])

        then:
        result.lang == ""

        where:
        word << ["12", '.']
    }

    //TODO переделать на translatorClient
    @Unroll
    def "detect Lang Succesfully for #word"(){
        when:
        def result = translatorClient.call(detect, [text: word])

        then:
        result.lang != ''

        where:
        word << ['test', 'shevale','arbeit', 'n33t']
    }

    @Unroll
    def "translate #word from #lang1 to #lang2"(){
        expect:
        translatorClient.call(translate, [text: word, lang: lang1.name() + "-" + lang2.name()])

        where:
        word        | lang1     | lang2
        'Hello'     | en        | ru
        'Hello'     | en        | en
        'LLL'       | en        | ru
        'Howdy'     | az        | ba
    }

    //TODO переделать на translatorClient
    @Unroll
    def "detect Correct Translation #word1 to #word2 with #lang" (){
        when:
        def result = translatorClient.call(translate, [lang: lang, text: word1])

        then:
        result.text[0] == word2

        where:
        word1        | lang         | word2
        'Peach'      | 'en-ru'      | 'Персик'
        'Peach'      | 'en-en'      | 'Peach'
        'Bonjour'    | 'fr-en'      | 'Hello'
    }

    //TODO переделать на translatorClient
    @Unroll
    def "detect correct translation #word1 to #lang" (){
        when:
        def result = translatorClient.call(translate, [text: word1, lang: lang.name()])

        then:
        result.text[0] == word2

        where:
        word1           | lang         | word2
        'Peach'         | ru           | 'Персик'
        'Peach'         | en           | 'Peach'
        'untr4nsl4t4bl3'| en           | 'untr4nsl4t4bl3'
    }



}

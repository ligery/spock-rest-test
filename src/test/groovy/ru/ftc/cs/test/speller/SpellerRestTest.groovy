package ru.ftc.cs.test.speller


import groovyx.net.http.HttpResponseException
import spock.lang.Unroll

import static ru.ftc.cs.test.translate.YandexLang.*

class SpellerRestTest extends SpellerSpec{

    def "GET-query limit check for checkText"(){
        when:
        restClient.get(path: 'checkText', query: [text: 'text' * 10000])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 414
    }

    def "checkText empty input string"(){
        when:
        def result = restClient.get([path: 'checkText', query: [text: '']])

        then:
        result.data == []
    }

    @Unroll
    def "positive checkText for word: #word"(){
        when:
        def result = restClient.get([path: 'checkText', query: [text: word]])

        then:
        result.data.len[0]  == length
        result.data.word[0] == word
        result.data.code[0] == code

        where:
        word       | length | code
        'MilkyWay' | 8      | 1
        'Jupitar'  | 7      | 1
    }

    @Unroll
    def "checkText mismatching lang input for #text : #lang"(){
        when:
        def result = restClient.get([path: 'checkText', query: [text: text, lang: lang]])

        then: "язык не влияет на результат определения"
        result.data != []
        noExceptionThrown()

        where:
        text        | lang
        'Crushal'   | ru
        'Мастар'    | en
        'Gonzu'     | uk
        'Антонем'   | uk
        'Querry'    | uk
    }

    @Unroll
    def "checkText incorrect lang input for #text : #lang"(){
        when:
        restClient.get([path: 'checkText', query: [text: text, lang: lang]])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400

        where:
        text        | lang
        'Weaboo'    | 'russia'
        'Маст'      | 'engrish'
        'Gonzo'     | 'жаргон'
        'Антон'     | 'имя'
    }

    def "checkText Positive test of IGNORE_DIGITS option"() {
        when:
        def result = restClient.get([path: 'checkText', query: [text: 'Gr1d is condenced', options: 2]])

        then:
        assert !result.data.s.any { it.value.toString().contains('Gr1d')}
    }

    def "correct word in checkText return nothing"() {
        when:
        def result = restClient.get([path: 'checkTexts', query: [text: 'supernatural']])

        then:
        result.data == [[]]
        noExceptionThrown()
    }

    def "GET-query limit check for checkTexts"(){
        when:
        restClient.get(path: 'checkTexts', query: [text: 'very long text' * 10000])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 414
    }

    def "checkTexts empty input string"(){
        when:
        def result = restClient.get([path: 'checkTexts', query: [text: '']])

        then:
        result.data == [[]]
        noExceptionThrown()
    }

    @Unroll
    def "checkTexts mismatching lang input for #text : #lang"(){
        when:
        def result = restClient.get([path: 'checkTexts', query: [text: text, lang: lang]])

        then:
        result.data != []
        noExceptionThrown()

        where:
        text                 | lang
        'Crushal mistake'    | ru
        'Мастар из борн'     | en
        'Kverri limit'       | uk
    }

    @Unroll
    def "checkTexts incorrect lang input for #text : #lang"(){
        when:
        restClient.get([path: 'checkTexts', query: [text: text, lang: lang]])

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 400

        where:
        text                | lang
        'Killin spree'      | 'russia'
        'Маст хэв'          | 'engrish'
        'Bonzi buddy'       | 'жаргон'
        'Антанина Иванавна' | 'имя'
    }

    def "checkTexts Positive test of IGNORE_DIGITS option"() {
        when:
        def result = restClient.get([path: 'checkTexts', query: [text: 'Fl00r is empti', options: 2]])

        then:
        assert !result.data.s.any { it.value.toString().contains('Fl00r')}
    }

    def "correct word in checkTexts return nothing"() {
        when:
        def result = restClient.get([path: 'checkTexts', query: [text: 'hello world']])

        then:
        result.data == [[]]
        noExceptionThrown()
    }
}

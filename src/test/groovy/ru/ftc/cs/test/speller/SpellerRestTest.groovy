package ru.ftc.cs.test.speller

import groovy.transform.ToString
import groovyx.net.http.HttpResponseException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class SpellerRestTest extends SpellerSpec{

    def "speller Permission Denied: Missing API Key"(){
        when:
        restClient.get(path: 'checkText', text: 'text', lang: 'en')

        then:
        HttpResponseException exception = thrown()
        exception.response.status == 404
        exception.response.data  == [code: 502, message: "Invalid parameter: key"]
    }



}

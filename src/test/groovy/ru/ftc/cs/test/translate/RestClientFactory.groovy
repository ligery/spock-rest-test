package ru.ftc.cs.test.translate

import groovyx.net.http.RESTClient
import org.apache.http.auth.AuthScope
import org.apache.http.auth.NTCredentials

/**
 * Rest Client Factory
 */
class RestClientFactory {

    static RESTClient getRestClient(String url){
        RESTClient client = new RESTClient( url )
        client.ignoreSSLIssues()
        setupProxy(client, proxyProperties)
        client
    }


    private static setupProxy(RESTClient restClient, Properties proxyProp){
        if (proxyProp?.size() > 0) {
            restClient.client.credentialsProvider.setCredentials(
                    new AuthScope(proxyProp.host, proxyProp.port as Integer),
                    new NTCredentials(proxyProp.user, proxyProp.passwd, proxyProp.host, proxyProp.domain)
            )
            restClient.setProxy(proxyProp.host, proxyProp.port as Integer, 'http')
        }
    }


    private static Properties getProxyProperties(){
        Properties properties = new Properties()
        InputStream propStream = RestClientFactory.class.classLoader.getResourceAsStream("proxy.properties")
        if (!propStream){
            return null
        }
        properties.load(propStream)
        properties
    }

}

package org.cheetah.commons.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.cheetah.commons.httpclient.transport.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Max on 2016/7/6.
 */
public abstract class AbstractHttpTransport<T> {
    protected final Logger logger = LoggerFactory.getLogger(AbstractHttpTransport.class);

    protected CloseableHttpClient httpClient;

    public AbstractHttpTransport(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public T doExecute(Transporter transporter, ResponseCallback<T> callback) {
        HttpRequestBase requestBase = null;
        CloseableHttpResponse resp = null;
        HttpEntity httpEntity = null;
        try {
            logger.info("transporter : {}", transporter);

            switch (transporter.method()) {
                case POST:
                    requestBase = new HttpPost(transporter.url());
                    resp = executePost((HttpPost) requestBase, transporter);
                    break;
                case PUT:
                    requestBase = new HttpPut(transporter.url());
                    resp = executePut((HttpPut) requestBase, transporter);
                    break;
                case DELETE:
                    requestBase = new HttpDelete(transporter.url());
                    resp = executeDelete((HttpDelete) requestBase, transporter);
                    break;
                case HEAD:
                    requestBase = new HttpHead(transporter.url());
                    resp = executeHead((HttpHead) requestBase, transporter);
                    break;
                case TRACE:
                    requestBase = new HttpTrace(transporter.url());
                    resp = executeTrace((HttpTrace) requestBase, transporter);
                    break;
                case OPTIONS:
                    requestBase = new HttpOptions(transporter.url());
                    resp = executeOptions((HttpOptions) requestBase, transporter);
                    break;
                default:
                    requestBase = new HttpGet(transporter.url());
                    resp = executeGet((HttpGet) requestBase, transporter);
                    break;
            }

            StatusLine statusLine = resp.getStatusLine();

            if (HttpClientUtils.resetSuccessStatus(statusLine)) {
                logger.info("request ok!");
                HttpClientUtils.gzipDecompression(resp);
                httpEntity = resp.getEntity();
                return callback.onResult(httpEntity);
            } else
                throw new HttpClientException("Http get request error[" + statusLine.getStatusCode() + "]-->url : " + transporter.url());
        } catch (Exception e) {
            logger.info("request error!", e);
            throw new HttpClientException("Http post request error-->url : " + transporter.url(), e);
        } finally {
            HttpClientUtils.close(requestBase, resp, httpEntity);
        }
    }

    private CloseableHttpResponse executeGet(HttpGet get, Transporter transporter) throws URISyntaxException, IOException {
        HttpClientUtils.setUriParameter(transporter.url(), transporter.parameters(), get);
        HttpClientUtils.setHeader(transporter.headers(), get);
        return httpClient.execute(get);
    }

    private CloseableHttpResponse executePost(HttpPost post, Transporter transporter) throws IOException {
        HttpClientUtils.setBody(transporter.body(), post);
        HttpClientUtils.setFormParameter(transporter.parameters(), post);
        HttpClientUtils.setHeader(transporter.headers(), post);
        return httpClient.execute(post);
    }

    private CloseableHttpResponse executePut(HttpPut put, Transporter transporter) throws IOException {
        HttpClientUtils.setBody(transporter.body(), put);
        HttpClientUtils.setFormParameter(transporter.parameters(), put);
        HttpClientUtils.setHeader(transporter.headers(), put);
        return httpClient.execute(put);
    }

    private CloseableHttpResponse executeDelete(HttpDelete delete, Transporter transporter) throws URISyntaxException, IOException {
        HttpClientUtils.setUriParameter(transporter.url(), transporter.parameters(), delete);
        HttpClientUtils.setHeader(transporter.headers(), delete);
        return httpClient.execute(delete);
    }

    private CloseableHttpResponse executeHead(HttpHead head, Transporter transporter) throws URISyntaxException, IOException {
        HttpClientUtils.setUriParameter(transporter.url(), transporter.parameters(), head);
        HttpClientUtils.setHeader(transporter.headers(), head);
        return httpClient.execute(head);
    }

    private CloseableHttpResponse executeOptions(HttpOptions options, Transporter transporter) throws URISyntaxException, IOException {
        HttpClientUtils.setUriParameter(transporter.url(), transporter.parameters(), options);
        HttpClientUtils.setHeader(transporter.headers(), options);
        return httpClient.execute(options);
    }

    private CloseableHttpResponse executeTrace(HttpTrace trace, Transporter transporter) throws URISyntaxException, IOException {
        HttpClientUtils.setHeader(transporter.headers(), trace);
        HttpClientUtils.setUriParameter(transporter.url(), transporter.parameters(), trace);
        return httpClient.execute(trace);
    }

}

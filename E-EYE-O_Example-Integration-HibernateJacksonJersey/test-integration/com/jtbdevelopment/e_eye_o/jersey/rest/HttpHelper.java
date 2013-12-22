package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.AssertJUnit;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class HttpHelper {
    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;


    public <T extends IdObject> T easyClone(final T entity) {
        return jsonIdObjectSerializer.readAsObjects(jsonIdObjectSerializer.write(entity));
    }

    HttpResponse httpGet(final String uri, final HttpClient client) throws IOException {
        HttpGet get = new HttpGet(uri);
        return client.execute(get);
    }

    HttpResponse httpDelete(final String uri, final HttpClient client) throws IOException {
        HttpDelete delete = new HttpDelete(uri);
        return client.execute(delete);
    }

    <T extends IdObject> HttpResponse httpPost(final String uri, final HttpClient httpClient, final String paramName, final T paramValue) throws IOException {
        return httpPostJson(uri, httpClient, jsonIdObjectSerializer.write(paramValue));
    }

    public HttpResponse httpPostJson(String uri, HttpClient httpClient, final String json) throws IOException {
        StringEntity jsonEntity = new StringEntity(json);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(jsonEntity);
        return httpClient.execute(httpPost);
    }

    public HttpResponse httpPostForm(String uri, HttpClient httpClient, List<NameValuePair> formValues) throws IOException {
        UrlEncodedFormEntity postForm = new UrlEncodedFormEntity(formValues);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(postForm);
        return httpClient.execute(httpPost);
    }

    <T extends IdObject> HttpResponse httpPut(final String uri, final HttpClient httpClient, final T entity) throws IOException {
        StringEntity stringEntity = new StringEntity(jsonIdObjectSerializer.write(entity));
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setHeader("Content-Type", "application/json");
        httpPut.setEntity(stringEntity);
        return httpClient.execute(httpPut);
    }

    <T extends IdObject> void checkJSONVsExpectedResult(final String uri, final HttpClient client, final T expectedResult) throws IOException {
        String json = getJSONFromHttpGet(uri, client);
        AssertJUnit.assertEquals(json, jsonIdObjectSerializer.write(expectedResult));
    }

    <T extends IdObject> void checkJSONVsExpectedResults(final String uri, final HttpClient client, final Collection<T> expectedResults) throws IOException {
        String json = getJSONFromHttpGet(uri, client);
        List<T> results = jsonIdObjectSerializer.readAsObjects(json);
        AssertJUnit.assertTrue(results.containsAll(expectedResults));
    }

    <T extends IdObject> void checkPaginatedJSONVsExpectedResults(final String uri, final HttpClient client, final Collection<T> expectedResults) throws IOException {
        int page = 1;
        boolean more = true;
        List<T> results = new LinkedList<>();
        while (more) {
            String pageURI = uri + "?page=" + page;
            String json = getJSONFromHttpGet(pageURI, client);
            Map<String, Object> pageResults = jsonIdObjectSerializer.readAsObjects(json);
            more = (Boolean) pageResults.get("more");
            results.addAll((List<T>) pageResults.get("entities"));
            ++page;
        }
        AssertJUnit.assertTrue(results.containsAll(expectedResults));
    }

    String getJSONFromHttpGet(final String uri, final HttpClient client) throws IOException {
        HttpResponse response = httpGet(uri, client);
        return getJSONFromResponse(response);
    }

    <T extends IdObject> String getJSONFromPut(final String uri, final HttpClient client, final T entity) throws IOException {
        HttpResponse response = httpPut(uri, client, entity);
        return getJSONFromResponse(response);
    }

    String getJSONFromResponse(final HttpResponse response) throws IOException {
        AssertJUnit.assertEquals(MediaType.APPLICATION_JSON, response.getEntity().getContentType().getValue());
        String json = EntityUtils.toString(response.getEntity());
        return json;
    }
}
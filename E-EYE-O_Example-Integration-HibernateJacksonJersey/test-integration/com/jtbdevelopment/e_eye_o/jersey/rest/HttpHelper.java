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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.AssertJUnit;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Component
public class HttpHelper {
    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;


    public <T extends IdObject> T easyClone(final T entity) {
        return jsonIdObjectSerializer.read(jsonIdObjectSerializer.write(entity));
    }

    HttpResponse httpGet(String uri, HttpClient client) throws IOException {
        HttpGet get = new HttpGet(uri);
        return client.execute(get);
    }

    HttpResponse httpDelete(String uri, HttpClient client) throws IOException {
        HttpDelete delete = new HttpDelete(uri);
        return client.execute(delete);
    }

    HttpResponse httpPost(HttpClient httpClient, String uri, List<NameValuePair> formValues) throws IOException {
        UrlEncodedFormEntity postForm = new UrlEncodedFormEntity(formValues);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(postForm);
        return httpClient.execute(httpPost);
    }

    HttpResponse httpPut(HttpClient httpClient, String uri, List<NameValuePair> formValues) throws IOException {
        UrlEncodedFormEntity postForm = new UrlEncodedFormEntity(formValues);
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setEntity(postForm);
        return httpClient.execute(httpPut);
    }

    <T extends IdObject> void getJSONValue(String uri, T expectedResult, HttpClient client) throws IOException {
        String json = getJSONFromHttpGet(uri, client);
        AssertJUnit.assertEquals(json, jsonIdObjectSerializer.write(expectedResult));
    }

    <T extends IdObject> void getJSONValues(String uri, Collection<T> expectedResults, HttpClient client) throws IOException {
        String json = getJSONFromHttpGet(uri, client);
        List<T> results = jsonIdObjectSerializer.read(json);
        AssertJUnit.assertTrue(results.containsAll(expectedResults));
    }

    String getJSONFromHttpGet(String uri, HttpClient client) throws IOException {
        HttpResponse response = httpGet(uri, client);
        return getJSONFromResponse(response);
    }

    <T extends IdObject> String getJSONFromPut(final T entity, String param, HttpClient client, String uri) throws IOException {
        List<NameValuePair> formValues = new LinkedList<NameValuePair>();
        formValues.add(new BasicNameValuePair(param, jsonIdObjectSerializer.write(entity)));
        HttpResponse response = httpPut(client, uri, formValues);
        return getJSONFromResponse(response);
    }

    String getJSONFromResponse(HttpResponse response) throws IOException {
        AssertJUnit.assertEquals(MediaType.APPLICATION_JSON, response.getEntity().getContentType().getValue());
        String json = EntityUtils.toString(response.getEntity());
        return json;
    }
}
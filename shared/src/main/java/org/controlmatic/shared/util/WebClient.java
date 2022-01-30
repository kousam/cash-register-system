package org.controlmatic.shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.controlmatic.shared.domain.WebClientException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extendable web client class for making JSON and XML requests.
 */
public class WebClient {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final JsonMapper jsonMapper;
    private final XmlMapper xmlMapper;

    /**
     * Creates a new web client instance.
     *
     * @param endpoint the base url to use for requests
     */
    public WebClient(String endpoint) {
        this.baseUrl = endpoint;
        this.httpClient = HttpClient.newHttpClient();
        this.jsonMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        this.xmlMapper = XmlMapper.builder().addModule(new JavaTimeModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true).build();
    }

    /**
     * Makes a GET request.
     *
     * @param path the url path
     * @param returnTypeReference a reference to the return class
     * @param <T> the type to map the response body to
     * @return the mapped response body
     * @throws WebClientException in case of errors
     */
    public <T> T makeGET(String path, TypeReference<T> returnTypeReference) throws WebClientException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path)).GET().build();
        return makeRequest(request, returnTypeReference);
    }

    /**
     * Makes a POST request, with JSON serialized request body.
     *
     * @param path the url path
     * @param body the request body
     * @param returnTypeReference a reference to the return class
     * @param <T> the type to map the response body to
     * @return the mapped response body
     * @throws WebClientException in case of errors
     */
    public <T> T makePOST(String path, Object body, TypeReference<T> returnTypeReference) throws WebClientException {
        return makeJsonRequest("POST", path, body, returnTypeReference);
    }

    /**
     * Makes a POST request, with urlencoded form as request body.
     *
     * @param path the url path
     * @param body the request form
     * @param returnTypeReference a reference to the return class, or null to not parse response body
     * @param <T> the type to map the response body to
     * @return the mapped response body, or null if no response
     * @throws WebClientException in case of errors
     */
    public <T> T makePOST(String path, Map<String, String> body, TypeReference<T> returnTypeReference) throws WebClientException {
        return makeFormRequest("POST", path, body, returnTypeReference);
    }

    /**
     * Makes an empty POST request.
     *
     * @param path the url path
     * @param returnTypeReference a reference to the return class, or null to not parse response body
     * @param <T> the type to map the response body to
     * @return the mapped response body, or null if no response
     * @throws WebClientException in case of errors
     */
    public <T> T makePOST(String path, TypeReference<T> returnTypeReference) throws WebClientException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path)).POST(BodyPublishers.noBody()).build();
        return makeRequest(request, returnTypeReference);
    }

    /**
     * Makes a PATCH request, JSON serializing the body.
     *
     * @param path the url path
     * @param body the request body
     * @param returnTypeReference a reference to the return class, or null to not parse response body
     * @param <T> the type to map the response body to
     * @return the mapped response body, or null if no response
     * @throws WebClientException in case of errors
     */
    public <T> T makePATCH(String path, Object body, TypeReference<T> returnTypeReference) throws WebClientException {
        return makeJsonRequest("PATCH", path, body, returnTypeReference);
    }

    /**
     * Makes a http request with JSON serialized body.
     *
     * @param method the http method
     * @param path the url path
     * @param body the request body
     * @param returnTypeReference a reference to the return class, or null to not parse response body
     * @param <T> the type to map the response body to
     * @return the mapped response body, or null if no response
     * @throws WebClientException in case of errors
     */
    private <T> T makeJsonRequest(String method, String path, Object body, TypeReference<T> returnTypeReference) throws WebClientException {
        try {
            // Create and send http request
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path)).method(method, BodyPublishers.ofString(jsonMapper.writeValueAsString(body))).header("Content-Type", "application/json").build();
            return makeRequest(request, returnTypeReference);
        }
        catch (JsonProcessingException e) {
            throw new WebClientException("Failed to write json", e);
        }
    }

    /**
     * Makes a http request with urlencoded www-form serialized body.
     *
     * @param method the http method
     * @param path the url path
     * @param body the request body
     * @param returnTypeReference a reference to the return class, or null to not parse response body
     * @param <T> the type to map the response body to
     * @return the mapped response body, or null if no response
     * @throws WebClientException in case of errors
     */
    private <T> T makeFormRequest(String method, String path, Map<String, String> body, TypeReference<T> returnTypeReference) throws WebClientException {
        // Build urlencoded www-form
        String form = body.entrySet().stream().map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path)).method(method, BodyPublishers.ofString(form)).header("Content-Type", "application/x-www-form-urlencoded").build();
        return makeRequest(request, returnTypeReference);
    }

    /**
     * Executes a http request.
     *
     * @param request the request to execute
     * @param returnTypeReference a reference to the return class, or null to not parse response body
     * @param <T> the type to map the response body to
     * @return the mapped response body, or null if no response
     * @throws WebClientException in case of errors
     */
    private <T> T makeRequest(HttpRequest request, TypeReference<T> returnTypeReference) throws WebClientException {
        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            // Make sure response was successful, http is nice like this
            if (response.statusCode() / 100 != 2) {
                throw new WebClientException(response.statusCode());
            }

            // Check if we should parse response body
            if (returnTypeReference == null) {
                return null;
            }

            // Parse according to content type
            String contentType = response.headers().firstValue("Content-Type").orElse("");
            if (contentType.startsWith("application/json")) {
                return jsonMapper.readValue(response.body(), returnTypeReference);
            }
            else if (contentType.startsWith("application/xml")) {
                return xmlMapper.readValue(response.body(), returnTypeReference);
            }
            else if (contentType.startsWith("text/plain") && returnTypeReference.getType().equals(String.class)) {
                // noinspection unchecked
                return (T)response.body();
            }
            else if (response.statusCode() == 204) {
                return null;
            }

            throw new WebClientException("Unsupported content type " + contentType);
        }
        catch (JsonProcessingException e) {
            throw new WebClientException("Failed to parse json", e);
        }
        catch (IOException | InterruptedException e) {
            throw new WebClientException("Failed to make web request", e);
        }
    }
}

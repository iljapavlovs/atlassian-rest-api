package atlassian.rest.api;

import atlassian.rest.api.exceptions.ConfluenceUpdaterException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang.StringUtils;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RestHelper {
    private ClientResponse response;
    public static final String ENCODING = "utf-8";
    private Client client = Client.create();

    public String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.get(ClientResponse.class);
        ensureResponse(200, "Response is different from HTTP 200 status code");
        return response.getEntity(String.class);
    }

    public String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.post(ClientResponse.class, data);
        ensureResponse(201, "Response is different from HTTP 201 status code");
        return response.getEntity(String.class);
    }

    public void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.put(ClientResponse.class, data);
        ensureResponse(200,"Response is different from HTTP 200 status code");
    }

    public void invokeDeleteMethod(String auth, String url) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.delete(ClientResponse.class);
        ensureResponse(204,"Response is different from HTTP 204 status code");
    }

    public static String getContentRestUrl(String baseUrl, final Long contentId, final String[] expansions) throws UnsupportedEncodingException {
        final String expand = URLEncoder.encode(StringUtils.join(expansions, ","), ENCODING);
        return String.format("%s/rest/api/content/%s?expand=%s", baseUrl, contentId, expand);
    }

    private WebResource.Builder setHeader(WebResource webResource, String auth) {
        WebResource.Builder builder = webResource.getRequestBuilder();
        builder.header("Authorization", "Basic " + auth)
                .type("application/json")
                .accept("application/json");
        return builder;
    }

    public void ensureResponse(int expectedStatusCode, String failureMessage) {
        int statusCode = response.getStatus();
        System.out.println("HTTP Response Code: " + statusCode);
        if (statusCode != expectedStatusCode) {
            String responseBody;
            responseBody = response.getEntity(String.class);
            throw new ConfluenceUpdaterException(failureMessage + ": " + responseBody);
        }
    }
}

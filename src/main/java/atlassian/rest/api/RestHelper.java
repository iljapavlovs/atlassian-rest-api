package atlassian.rest.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang.StringUtils;

import javax.naming.AuthenticationException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class RestHelper {
    private ClientResponse response;
    public static final String ENCODING = "utf-8";
    private Client client = Client.create();

    public String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.get(ClientResponse.class);
        checkStatusCode();
        return response.getEntity(String.class);
    }

    public String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.post(ClientResponse.class, data);
        checkStatusCode();
        return response.getEntity(String.class);
    }

    public void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.put(ClientResponse.class, data);
        checkStatusCode();
    }

    public void invokeDeleteMethod(String auth, String url) throws AuthenticationException, ClientHandlerException, IOException {
        WebResource webResource = client.resource(url);
        WebResource.Builder builder = setHeader(webResource, auth);
        response = builder.delete(ClientResponse.class);
        checkStatusCode();
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

    private void checkStatusCode() throws IOException, AuthenticationException {
        int statusCode = response.getStatus();
        if(statusCode == 401) throw new AuthenticationException("Invalid Username or Password");

        String firstDigit = Integer.toString(statusCode).substring(0, 1);
        switch (firstDigit){
            case "1":
                break;
            case "2":
                break;
            default:
                throw new IOException("HTTP Status Code is: " + response.getStatus());

        }
        System.out.println("HTTP Response Code: " + statusCode);
    }
}

package atlassian.rest.api.confluence;


import atlassian.rest.api.RestHelper;
import com.sun.jersey.core.util.Base64;
import org.json.JSONObject;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static atlassian.rest.api.Config.getValue;
import static atlassian.rest.api.RestHelper.getContentRestUrl;
import static atlassian.rest.api.properties.ConfigProperty.BASE_URL;
import static atlassian.rest.api.properties.ConfigProperty.FILE_PATH;
import static atlassian.rest.api.properties.ConfigProperty.PAGE_ID;
import static atlassian.rest.api.properties.ConfigProperty.PASSWORD;
import static atlassian.rest.api.properties.ConfigProperty.USERNAME;


public class ConfluenceRestApi {

    public static void updateConfluencePage() throws IOException, AuthenticationException {
        RestHelper client = new RestHelper();

        final String baseUrl = "https://" + getValue(BASE_URL);
        final long pageId = Long.valueOf(getValue(PAGE_ID));
        final String encodedCredentials = new String(Base64.encode(getValue(USERNAME) + ":" + getValue(PASSWORD)));

        //GET current information about the Confluence Page (version) and will be needed when constructing UPDATE request
        String responseInString = client.invokeGetMethod(encodedCredentials, getContentRestUrl(baseUrl, pageId, new String[]{"body.storage", "version", "ancestors"}));

        // Parse response into JSON
        JSONObject pageInJsonObject = new JSONObject(responseInString);

        // Update page
        // The updated value must be Confluence Storage Format (https://confluence.atlassian.com/display/DOC/Confluence+Storage+Format), NOT HTML.
        pageInJsonObject.getJSONObject("body").getJSONObject("storage").put("value", getDataToPush());
        int currentVersion = pageInJsonObject.getJSONObject("version").getInt("number");
        pageInJsonObject.getJSONObject("version").put("number", currentVersion + 1);

        client.invokePutMethod(encodedCredentials, getContentRestUrl(baseUrl, pageId, new String[]{}), pageInJsonObject.toString());
    }

    private static String getDataToPush() throws IOException {
        return new String(Files.readAllBytes(Paths.get(getValue(FILE_PATH))));
    }
}

package atlassian.rest.api.jira;

import atlassian.rest.api.RestHelper;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.core.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.naming.AuthenticationException;

import java.io.IOException;
import java.net.URISyntaxException;

import static atlassian.rest.api.Config.getValue;
import static atlassian.rest.api.properties.ConfigProperty.PASSWORD;
import static atlassian.rest.api.properties.ConfigProperty.USERNAME;


public class JiraRestApiCrud {

    public static final String JIRA_BASE_URL = "http://localhost:8080";
    public static void main(String[] args) throws IOException, URISyntaxException {
        String encodedCredentials = new String(Base64.encode(getValue(USERNAME) + ":" + getValue(PASSWORD)));

        RestHelper client = new RestHelper();
        try {
            //Get Projects
            String projects = client.invokeGetMethod(encodedCredentials, JIRA_BASE_URL +"/rest/api/2/project");
            System.out.println("Projects: " + projects);
            JSONArray projectArray = new JSONArray(projects);
            for (int i = 0; i < projectArray.length(); i++) {
                JSONObject proj = projectArray.getJSONObject(i);
                System.out.println("Key:"+proj.getString("key")+", Name:"+proj.getString("name"));
            }

            //Create Issue
            String createIssueData = "{\"fields\":{\"project\":{\"key\":\"DEMO\"},\"summary\":\"REST Test\",\"issuetype\":{\"name\":\"Bug\"}}}";
            String issue = client.invokePostMethod(encodedCredentials, JIRA_BASE_URL +"/rest/api/2/issue", createIssueData);
            System.out.println(issue);
            JSONObject issueObj = new JSONObject(issue);
            String newKey = issueObj.getString("key");
            System.out.println("Key:"+newKey);

            //Update Issue
            String editIssueData = "{\"fields\":{\"assignee\":{\"name\":\"test\"}}}";
            client.invokePutMethod(encodedCredentials, JIRA_BASE_URL +"/rest/api/2/issue/"+newKey, editIssueData);

            client.invokeDeleteMethod(encodedCredentials, JIRA_BASE_URL +"/rest/api/2/issue/DEMO-13");

        } catch (AuthenticationException e) {
            System.out.println("Username or Password wrong!");
            e.printStackTrace();
        } catch (ClientHandlerException e) {
            System.out.println("Error invoking REST method");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Invalid JSON output");
            e.printStackTrace();
        }

    }


}

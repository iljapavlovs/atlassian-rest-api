package atlassian.rest.api;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static atlassian.rest.api.Config.loadConfigProperties;
import static atlassian.rest.api.confluence.ConfluenceRestApi.updateConfluencePage;


public class Main {
    public static void main(String[] args) throws IOException, AuthenticationException {
        loadConfigProperties(args);
        System.out.println("Starting script...");
        updateConfluencePage();
    }
}

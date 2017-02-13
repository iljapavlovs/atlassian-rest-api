package atlassian.rest.api;

import org.apache.log4j.Logger;

import javax.naming.AuthenticationException;
import java.io.IOException;

import static atlassian.rest.api.Config.loadConfigProperties;
import static atlassian.rest.api.confluence.ConfluenceRestApi.updateConfluencePage;


public class Main {
    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException, AuthenticationException {
        logger.info("Loading configuration...");
        loadConfigProperties(args);
        logger.info("Starting script...");
        updateConfluencePage();
    }
}

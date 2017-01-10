Code requires:
*  Java trust store which contains Confluence cert

In order to run:
```
java -jar atlassian-rest-api-1.0-SNAPSHOT-jar-with-dependencies.jar %conf.username% %conf.password% %conf.url% %conf.existing_page_to_update% %conf.file_path_with_update_content%
```


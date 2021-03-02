/**
 * fag
 */
package shenkar.finalProject.googleDocs.MVVC;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * the model take care of all google's API logic
 */
public class Model implements IModel {
    private String APPLICATION_NAME = "Google Docs API Java Quickstart";
    private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private String TOKENS_DIRECTORY_PATH = "tokens";
    private NetHttpTransport HTTP_TRANSPORT;
    private Docs docsService;
    private IViewModel viewModel;



    private List<String> SCOPES = Collections.singletonList(DocsScopes.DOCUMENTS);
    private String CREDENTIALS_FILE_PATH = "/credentials.json";

    public Model() throws  MVVMException{
        try {

            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            docsService = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new MVVMException(e.getMessage(), e.getCause());
        }
    }

    // google's Credentials
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Model.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receier = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receier).authorize("user");
    }

    @Override
    public Docs getDocsService() {
        return docsService;
    }


    // for dev DELETE LATER
//    @Override
//    public Document getSpecificDocument(Docs service, String docId) throws MVVMException {
//        try {
//            Document response = service.documents().get(docId).execute();
//            System.out.println("doc body regular = " + response.getBody());
//
//            JSONParser parser = new JSONParser();
//
//            try {
//                System.out.println("docBody values = " + response.getBody().values().toString());
//                Object obj = parser.parse(response.getBody().toString());
//                JSONObject array = (JSONObject) obj;
//
//                JSONArray ob = (JSONArray) array.get("content");
//                JSONObject ject = (JSONObject) ob.get(2);
//                Long startIndexOfList = (Long) ject.get("startIndex");
//
//
//                System.out.println("ob = " + ob);
//                System.out.println("ject = " + ject);
//                System.out.println("startIndexOfList =" + startIndexOfList);
//
//                System.out.println("array = " + array);
//
//
//            } catch (ParseException pe) {
//
//                System.out.println("position: " + pe.getPosition());
//                System.out.println(pe);
//            }
//
//            return response;
//        } catch (IOException e) {
//            throw new MVVMException(e.getMessage(), e.getCause());
//        }
//
//    }

    @Override
    public String getDocTitle(Document response) {
        return response.getTitle();
    }


    // inserting the templated text that will be replaced with users text
    @Override
    public void setBodyTemplateText(Docs service, String docId, String textToInsert) throws MVVMException {
        List<Request> requests = new ArrayList<>();

        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(textToInsert)
                .setLocation(new Location().setIndex(1))));


        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        try {
            BatchUpdateDocumentResponse response = service.documents()
                    .batchUpdate(docId, body).execute();

            System.out.println("body :\n" + body);
            MyLogger.Log.trace("doc template inserted to body");

        } catch (IOException e) {
            MyLogger.Log.error("cant inserted doc body " + e.getMessage() + e.getCause());
            throw new MVVMException("cant inserted doc body " + e.getMessage(), e.getCause());
        }

    }



    //setting the document style, we make style for the template and then the replaced text will inherit the style
    @Override
    public void setDocStyle(Docs service, String docId) throws MVVMException {

        List<Request> requests = new ArrayList<>();

        // name and date style
        requests.add(new Request().setUpdateParagraphStyle(new UpdateParagraphStyleRequest()
                .setRange(new Range()
                        .setStartIndex(1)
                        .setEndIndex(33))
                .setParagraphStyle(new ParagraphStyle()
                        .setNamedStyleType("HEADING_3")
                        .setSpaceAbove(new Dimension()
                                .setMagnitude(10.0)
                                .setUnit("PT"))
                        .setSpaceBelow(new Dimension()
                                .setMagnitude(10.0)
                                .setUnit("PT")))
                .setFields("namedStyleType,spaceAbove,spaceBelow")
        ));


        requests.add(new Request().setUpdateTextStyle(new UpdateTextStyleRequest()
                .setTextStyle(new TextStyle()
                        .setBold(true)
                        .setItalic(true)
                        .setUnderline(true))
                .setRange(new Range()
                        .setStartIndex(1)
                        .setEndIndex(33))
                .setFields("bold")
        ));


        // to do list style
        requests.add(new Request().setCreateParagraphBullets(
                new CreateParagraphBulletsRequest()
                        .setRange(new Range()
                                .setStartIndex(33)
                                .setEndIndex(36))
                        .setBulletPreset("BULLET_ARROW_DIAMOND_DISC")
                        ));



        // summery header style
        requests.add(new Request().setUpdateParagraphStyle(new UpdateParagraphStyleRequest()
                .setRange(new Range()
                        .setStartIndex(43)
                        .setEndIndex(56))
                .setParagraphStyle(new ParagraphStyle()
                        .setNamedStyleType("HEADING_3")
                        .setSpaceAbove(new Dimension()
                                .setMagnitude(10.0)
                                .setUnit("PT"))
                        .setSpaceBelow(new Dimension()
                                .setMagnitude(10.0)
                                .setUnit("PT")))
                .setFields("namedStyleType,spaceAbove,spaceBelow")
        ));


        requests.add(new Request().setUpdateTextStyle(new UpdateTextStyleRequest()
                .setTextStyle(new TextStyle()
                        .setBold(true)
                        .setItalic(true)
                        .setUnderline(true))
                .setRange(new Range()
                        .setStartIndex(43)
                        .setEndIndex(56))
                .setFields("bold")
                .setFields("underline")
        ));


        // style of the blue line in the left side
        requests.add(new Request().setUpdateParagraphStyle(new UpdateParagraphStyleRequest()
                .setRange(new Range()
                        .setStartIndex(43)
                        .setEndIndex(60))
                .setParagraphStyle(new ParagraphStyle()
                        .setBorderLeft(new ParagraphBorder()
                                .setColor(new OptionalColor()
                                        .setColor(new Color()
                                                .setRgbColor(new RgbColor()
                                                        .setBlue(1.0F)
                                                        .setGreen(0.0F)
                                                        .setRed(0.0F)
                                                )
                                        )
                                )
                                .setDashStyle("DASH")
                                .setPadding(new Dimension()
                                        .setMagnitude(20.0)
                                        .setUnit("PT"))
                                .setWidth(new Dimension()
                                        .setMagnitude(15.0)
                                        .setUnit("PT")
                                )
                        )
                )
                .setFields("borderLeft")
        ));


        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        try {
            BatchUpdateDocumentResponse response = service.documents()
                    .batchUpdate(docId, body).execute();
            MyLogger.Log.trace("doc style added");
        } catch (IOException e) {
            MyLogger.Log.error("cant add doc style, " + e.getMessage() + e.getCause());
            throw new MVVMException(e.getMessage(), e.getCause());
        }
    }



    @Override
    public void replaceTemplatedText(Docs docsService, String docId, String authorName, String summery, String toDos) throws MVVMException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String date = formatter.format(LocalDate.now());

        List<Request> requests = new ArrayList<>();
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{Name}}")
                                .setMatchCase(true))
                        .setReplaceText(authorName)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{date}}")
                                .setMatchCase(true))
                        .setReplaceText(date)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{List}}")
                                .setMatchCase(true))
                        .setReplaceText(toDos)));

        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{Summery}}")
                                .setMatchCase(true))
                        .setReplaceText(summery)));


        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();


        try {
            docsService.documents().batchUpdate(docId, body.setRequests(requests)).execute();
            MyLogger.Log.trace("changed doc body to templated text");
        } catch (IOException e) {
            MyLogger.Log.error("cant changed doc body to templated text, " + e.getMessage() + e.getCause());
            throw new MVVMException(e.getMessage(), e.getCause());
        }
    }


    @Override
    public void setIViewModel(IViewModel viewModel) throws MVVMException {
        this.viewModel = viewModel;
    }


    @Override
    public String createDoc(Docs service, String docName) throws MVVMException {

        try {
            Document doc = new Document()
                    .setTitle(docName);
            doc = service.documents().create(doc)
                    .execute();
            service.documents().get(doc.getDocumentId()).execute();
            MyLogger.Log.trace("new doc created with id - " + doc.getDocumentId());
            return doc.getDocumentId();
        } catch (IOException e) {
            MyLogger.Log.error("cant create mew doc, " + e.getMessage() + e.getCause());
            throw new MVVMException(e.getMessage(), e.getCause());
        }

    }

}

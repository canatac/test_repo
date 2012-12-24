package org.ortens.gapi.example;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.code.samples.oauth2.OAuth2Authenticator;
import com.sun.mail.imap.IMAPStore;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Test {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String GMAIL_SCOPE = "https://mail.google.com/";

    public static void main(String[] args) throws Exception {   
        String email = "can.atac@gmail.com";
        String authToken = getAccessToken(email);

        OAuth2Authenticator.initialize();

        IMAPStore imapSslStore = OAuth2Authenticator.connectToImap("imap.gmail.com",
                993,
                email,
                authToken,
                true);

        System.out.println("Successfully authenticated to IMAP.\n");
    }

    public static String getAccessToken(String email) throws GeneralSecurityException, IOException {
        GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId("238643088749@developer.gserviceaccount.com")
                .setServiceAccountScopes(GMAIL_SCOPE)
                .setServiceAccountPrivateKeyFromP12File(new File("D:\\var\\lib\\workspacev2\\gAPI\\src\\main\\resources\\org\\ortens\\gapi\\example\\45e98eced3dde1eef4f1edc60594c3a4ef90fe15-privatekey.p12"))
                .setServiceAccountUser(email)
                .build();
        credential.refreshToken();
        return credential.getAccessToken();
    }
}
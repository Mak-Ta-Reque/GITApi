package com.mak.gitapi;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.Charset;


public class GITTools {
    private String baseURL;//api.github.com/
    private String repo;//Mak-Ta-Reque/test/
    private String token;// github token
    private String username;// username of git hub
    private String repo_branch;//"heads/master"
    private String protocol = "https://";
    private String commitBranch = "commits/";
    private String tree = "trees";
    private String post_body ;//"{"base_tree": "{{sha-base-tree}}",
    // "tree": [{"path": "NewFile1.txt",
    // "mode": "100644",
    // "type": "blob",
    // "content": "This is NewFile1."},
    // {"path": "NewFile2.txt",
    //"mode": "100644",
    //"type": "blob",
    // "content": "This is NewFile2."}]}"
    private String commit_body ;//{
    //"parents": ["{{sha-latest-commit}}"],
    //"tree": "{{sha-new-tree}}",
    //"message": ""}
    private String push_body;//{
    //"sha": "{{sha-new-commit}}"}

    private String sha_base_tree;// received by HTTP GET formatedURL = protocol + baseURL + "repos/" + repo + "git/" + commitBranch + sha_latest_commit;
    private String sha_latest_commit;// sha received by HTTP GET request at repo branch "formatedURL = protocol + baseURL + "repos/" + repo + "git/refs/" + repo_branch"
    private String sha_new_tree; //sha recived from http POST at String url = protocol + baseURL + "repos/" + repo + "git/" +tree ;
    private String sha_new_commit ;// sha recived by HTTP POST at url = protocol + baseURL + "repos/" + repo + "git/" + COMMIT+"" ;
    private int httpaPOSTstatuscode;

    String BASICAUTH = "Basic";
    String NOAUTH = "NoAuth";
    String J_TREE = "tree";
    String J_SHA = "sha";
    String J_OBJECT = "object";
    String COMMIT = "commits";
    String TREE = "trees";
    String BRANCH = "branch";


    public String getSha_new_commit() {
        return sha_new_commit;
    }

    public void setSha_new_commit(String sha_new_commit) {
        this.sha_new_commit = sha_new_commit;
    }

    public void setSha_new_commit() throws IOException {
        System.out.println(commit_body);
        String httpPOSTMSG = httpPOST(COMMIT);
        String[] jsnKeyWord ={J_SHA};
        sha_new_commit = perseJSN(httpPOSTMSG,jsnKeyWord);
    }

    public void setCommit_body(String commit_body) {
        this.commit_body = commit_body;
    }

    public String perseJSN(String jsn, String [] nodes ){// node represent the hierarchy
        if (nodes.length == 1){
            JSONObject jsnobj = new JSONObject(jsn);
            String SHA = jsnobj.getString(nodes[0]);
            return SHA;
        }
        else if (nodes.length == 2){
            JSONObject jsnobj = new JSONObject(jsn);
            String SHA = jsnobj.getJSONObject(nodes[0]).getString(nodes[1]);
            return SHA;
        }
        else{
            return  "not implemented";
        }


    }


    public void setPush_body(String push_body) {
        this.push_body = push_body;
    }

    public void setCommitBrach(String commitBrach) {
        this.commitBranch = commitBrach;
    }

    public int getHttpaPOSTstatuscode() {
        return httpaPOSTstatuscode;
    }

    public String httpPOST(String OPERATION) throws IOException {
        if (OPERATION.equals(TREE)) {
            String url = protocol + baseURL + "repos/" + repo + "git/" +tree ;
            return httpPOSTInitiator(url,post_body);

        }
        else if(OPERATION.equals(COMMIT)){
            String url = protocol + baseURL + "repos/" + repo + "git/" + COMMIT+"" ;
            return httpPOSTInitiator(url,commit_body);
        }
        else if(OPERATION.equals(BRANCH)) {
            String url = protocol + baseURL + "repos/" + repo + "git/" + "refs/" + repo_branch;
            return httpPOSTInitiator(url,push_body);
        }
        else {
            return "give proper end of url: trees or commit or branch name";
        }
    }

    public String commitPush() throws IOException {
        return httpPOST(BRANCH);
    }

    public String httpPOSTInitiator(String url, String msgbody) throws IOException {
        try{
            HttpPost httppost = new HttpPost(url);
            String auth=new StringBuffer(username).append(":").append(token).toString();
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            httppost.setHeader("AUTHORIZATION", authHeader);
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("X-Stream" , "true");
            httppost.setEntity(new StringEntity(msgbody));
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(httppost);
            httpaPOSTstatuscode = response.getStatusLine().getStatusCode();
            try {
                return  getString(response,"");
            } finally {

            }

        }
        finally {

        }
    }

    public String httpGet(String authType) throws IOException {

        if (authType.equals(BASICAUTH)){
            String formatedURL = protocol + baseURL + "repos/" + repo + "git/refs/" + repo_branch;
            HttpGet request = new HttpGet(formatedURL);
            String auth = username + ":" + token;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request);
            String result= "";
            BufferedReader rd;
            String line;
            result = getString(response, result);
            return  result;
        }

        else if(authType.equals(NOAUTH)){
            String formatedURL = protocol + baseURL + "repos/" + repo + "git/" + commitBranch + sha_latest_commit;
            System.out.println(formatedURL);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(formatedURL);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            String result= "";
            BufferedReader rd;
            String line;
            result = getString(response, result);

            return  result;
        }
        else{
            return "Specify correct auth type String BASICAUTH = \"Basic\";\n" +
                    "    String NOAUTH = \"NoAuth\";";
        }


    }

    private String getString(HttpResponse response, String result) {
        BufferedReader rd;
        String line;
        try {
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    rd = new BufferedReader( new InputStreamReader(instream));
                    while((line = rd.readLine())!=null){
                        result +=line;
                    }
                } finally {
                    // Closing the input stream will trigger connection release
                    instream.close();
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        finally {

        }
        return result;
    }

    public String getSHA() throws IOException {// This return the latest sha
        String httpGetMSG = httpGet(BASICAUTH);
        String[] jsnKeyWord ={J_OBJECT,J_SHA};
        return perseJSN(httpGetMSG,jsnKeyWord);
    }

    public void setProtocol(String protocol) {// http:// or https://
        this.protocol = protocol;
    }


    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }
    public String getSha_base_tree() {
        return sha_base_tree;
    }

    public void setSha_base_tree(String sha_base_tree) {
        this.sha_base_tree = sha_base_tree;
    }
    public void setSha_base_tree() throws IOException {
        String httpGetMSG = httpGet(NOAUTH);
        String[] jsnKeyWord ={J_TREE,J_SHA};
        this.sha_base_tree = perseJSN(httpGetMSG,jsnKeyWord);;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSha_latest_commit(String sha_latest_commit) {

        this.sha_latest_commit = sha_latest_commit;
    }
    public void setSha_latest_commit() throws IOException {
        String httpGetMSG = httpGet(BASICAUTH);
        String[] jsnKeyWord ={"object","sha"};
        this.sha_latest_commit = perseJSN(httpGetMSG,jsnKeyWord);
    }


    public String getBaseURL() {
        return baseURL;
    }

    public String getRepo() {
        return repo;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getSha_latest_commit() {
        return sha_latest_commit;
    }
    public void setRepo_branch(String repo_branch) {
        this.repo_branch = repo_branch;
    }
    public String getSha_new_tree() {
        return sha_new_tree;
    }

    public void setSha_new_tree(String sha_new_tree) {
        this.sha_new_tree = sha_new_tree;
    }

    public void setSha_new_tree() throws IOException {
        String httpPOSTMSG = httpPOST(TREE);
        String[] jsnKeyWord ={J_SHA};
        sha_new_tree = perseJSN(httpPOSTMSG,jsnKeyWord);
    }

    public String getPost_body() {
        return post_body;
    }

    public void setPost_body(String post_body) {
        this.post_body = post_body;
    }
}

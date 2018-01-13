package com.mak.gitapi;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.io.IOException;

public class GITToolsTest extends TestCase {
    public static String sha_new_tree ;
    public void testhttpGet() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        gittools.httpGet("Basic");
        String last_commit_sha = gittools.getSHA();
        assertEquals(last_commit_sha.length(),40);
        gittools.setSha_latest_commit(last_commit_sha);
        String result = gittools.httpGet("NoAuth");
        JSONObject jsnobj = new JSONObject(result);
        String SHA = jsnobj.getString("sha");
        assertEquals(SHA,last_commit_sha);
        assertEquals(gittools.httpGet("Vague"),"Specify correct auth type String BASICAUTH = \"Basic\";\n" +
                "    String NOAUTH = \"NoAuth\";");


    }

    public void testGetSHA() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        String sha = gittools.getSHA();
        assertEquals(sha.length(),40);

    }
    public void  testSet_latest_commit() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        gittools.setSha_latest_commit();
        assertEquals(gittools.getSha_latest_commit().length(),40);
    }
    public void testSet_sha_base_tree() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        gittools.setSha_latest_commit();
        gittools.setSha_base_tree();
        assertEquals(gittools.getSha_base_tree().length(),40);
    }
    public  void testHttpPOST() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        gittools.setSha_latest_commit();
        String post_body = "{\n" +
                "  \"base_tree\":"  + gittools.getSha_base_tree() +",\n" +
                "  \"tree\": [\n" +
                "    {\n" +
                "      \"path\": \"NewFile1example.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile1.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"path\": \"NewFile2example.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile2.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        gittools.setPost_body(post_body);
        gittools.setSha_base_tree();
        gittools.httpPOST(gittools.TREE);
        assertEquals(gittools.getHttpaPOSTstatuscode(),201);
    }
    public void testSet_sha_new_tree() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        gittools.setSha_latest_commit();
        String post_body = "{\n" +
                "  \"base_tree\":"   +gittools.getSha_base_tree()  +",\n" +
                "  \"tree\": [\n" +
                "    {\n" +
                "      \"path\": \"NewFile1example.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile1.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"path\": \"NewFile2example.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile2.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        gittools.setPost_body(post_body);
        gittools.setSha_base_tree();

        gittools.setSha_new_tree();
        sha_new_tree = gittools.getSha_new_tree();
        assertEquals(gittools.getSha_new_tree().length(),40);
    }
    public void testSet_sha_new_commit() throws IOException {
        GITTools gittools = new GITTools();
        gittools.setBaseURL("api.github.com/");
        gittools.setRepo("Mak-Ta-Reque/test/");
        gittools.setRepo_branch("heads/master");
        gittools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gittools.setUsername("maktareq@gmail.com");
        gittools.setSha_latest_commit();
        gittools.setSha_base_tree();
        String post_body = "{\n" +
                "  \"base_tree\":" +  gittools.getSha_base_tree() +",\n" +
                "  \"tree\": [\n" +
                "    {\n" +
                "      \"path\": \"NewFile1eexample.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile1.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"path\": \"NewFile2eexample.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile2.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        gittools.setPost_body(post_body);
        gittools.setSha_new_tree(sha_new_tree);
        String commit_body = "{\n" +
                "  \"parents\": ["  + "\""+ gittools.getSha_latest_commit()  + "\""+ "],\n" +
                "  \"tree\": " + "\""+ gittools.getSha_new_tree()  + "\"" + ",\n" +
                "  \"message\": \"COMMITMESSAGE\"\n" +
                "}";
        gittools.setCommit_body(commit_body);
        gittools.setSha_new_commit();
        System.out.println("sha latest commit : " + gittools.getSha_latest_commit());
        System.out.println("sha base tree : " + gittools.getSha_base_tree());
        System.out.println("sha new Tree : " + gittools.getSha_new_tree());
        System.out.println("sha new commit : " + gittools.getSha_new_commit());
        assertEquals(gittools.getSha_new_commit().length(),40);
    }

}
package com.mak.gitapi;

import junit.framework.TestCase;

import java.io.IOException;

public class GITToolsPushTest extends TestCase {

    public void testCommitPush() throws IOException {
        GITTools gitTools = new GITTools();
        gitTools.setProtocol("https://");
        gitTools.setBaseURL("api.github.com/");
        gitTools.setRepo("Mak-Ta-Reque/test/");
        gitTools.setToken("f97aa9ae7ed789d131fccd64a21bc07c0c33f858");
        gitTools.setUsername("maktareq@gmail.com");
        gitTools.setRepo_branch("heads/master");
        //Store latest commit
        gitTools.setSha_latest_commit();
        System.out.println("Sha latest commit : " + gitTools.getSha_latest_commit());
        gitTools.setSha_base_tree();
        String basetree = gitTools.getSha_base_tree();
        System.out.println("Sha base tree : " + basetree);
        String post_body = "{\n" +
                "  \"base_tree\":"  + "\"" + basetree + "\"" +",\n" +
                "  \"tree\": [\n" +
                "    {\n" +
                "      \"path\": \"ExampleFile1.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile1, i am king of my kingdom.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"path\": \"ExampleFile2.txt\",\n" +
                "      \"mode\": \"100644\",\n" +
                "      \"type\": \"blob\",\n" +
                "      \"content\": \"This is NewFile2.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        gitTools.setPost_body(post_body);
        gitTools.setSha_new_tree();
        System.out.println("Sha new Tree : " + gitTools.getSha_new_tree());
        String commitmsg = "commit using git api";
        String commit_body = "{\n" +
                "  \"parents\": [\"" + gitTools.getSha_latest_commit()+ "\"],\n" +
                "  \"tree\": \""+ gitTools.getSha_new_tree() + "\",\n" +
                "  \"message\": \"" + commitmsg + "\"\n" +
                "}";
        gitTools.setCommit_body(commit_body);
        gitTools.setSha_new_commit();
        System.out.println("Sha new commit: " + gitTools.getSha_new_commit());
        String pushbodu ="{\n" +
                "  \"sha\": \"" + gitTools.getSha_new_commit() + "\"\n" +
                "}\n";
        gitTools.setPush_body(pushbodu);
        String ret= gitTools.commitPush();
        System.out.println(ret);

    }
}
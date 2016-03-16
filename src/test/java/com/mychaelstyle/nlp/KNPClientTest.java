package com.mychaelstyle.nlp;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mychaelstyle.nlp.KNPClient;

public class KNPClientTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testKNPStringStringString() {
    }

    @Test
    public void testParse() {
        KNPClient knp = new KNPClient();
        try{
            ObjectNode result = knp.parse("今日は講習会を受けています。");
System.out.println("\n\n");
System.out.println(result.toString());
            assertThat(result,notNullValue());
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
            result = knp.parse("今日は講習会を受けています。");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

}

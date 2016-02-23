package com.mychaelstyle.nlp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mychaelstyle.nlp.KNP;

public class KNPTest {

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
        KNP knp = new KNP();
        try{
            ObjectNode result = knp.parse("今日は講習会を受けています。");
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

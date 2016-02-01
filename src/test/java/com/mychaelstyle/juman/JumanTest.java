/**
 * 
 */
package com.mychaelstyle.juman;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author masanori
 *
 */
public class JumanTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.mychaelstyle.juman.Juman#Juman(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testJumanStringString() {
    }

    /**
     * Test method for {@link com.mychaelstyle.juman.Juman#parse(java.lang.String)}.
     */
    @Test
    public void testParse() {
        Juman juman = new Juman();
        try {
            ObjectNode result = juman.parse("本システムは，計算機による日本語の解析の研究を目指す多くの研究者に共通に使える形態素解析ツールを提供するために開発されました。");
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package com.mychaelstyle.nlp;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JumanClientTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParse() {
        JumanClient juman = new JumanClient();
        try {
            ObjectNode result = juman.parse(
                    "本システムは，計算機による日本語の解析の研究を目指す多くの研究者に共通に使える形態素解析ツールを提供するために開発されました。");
            assertThat(result,notNullValue());
            System.out.println(result.toString());

            result = juman.parse("JUMANは、日本語の形態素解析システムです。");
            System.out.println(result.toString());

            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
            result = juman.parse("人手で整備した辞書に基づいており、ChaSenの元となったシステム。");
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
        }
    }

}

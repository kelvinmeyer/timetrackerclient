/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonParser;

import dataStructures.Client;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hobbes
 */
public class JsonArrayParserTest {
    public static final String testData= "[{\"ClientID\":\"c18\",\"ClientName\":\"perm\"},{\"ClientID\":\"c19\",\"ClientName\":\"temp2\"},{\"ClientID\":\"c20\",\"ClientName\":\"joesoap\"}]";
    public static final Client res1 = new Client("c18", "perm");
    public static final Client res2 = new Client("c19", "temp2");
    public static final Client res3 = new Client("c20", "joesoap");
    private JsonArrayParser jap;

    public JsonArrayParserTest() {
    }
    
    @Before
    public void setUp() {
        jap = new JsonArrayParser(testData);
    }

    /**
     * Test of next method, of class JsonArrayParser.
     */
    @Test
    public void testNext() {
        System.out.println("next");
        JsonArrayParser instance = null;
        JsonParser expResult = null;
        JsonParser result = instance.next();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasNext method, of class JsonArrayParser.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        JsonArrayParser instance = null;
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

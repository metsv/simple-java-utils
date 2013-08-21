package ee.metsv;

import ee.metsv.CamelCaseMatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: metsv
 * Date: 8/21/13
 * Time: 11:23 AM
 */
public class CamelCaseMatcherTest {

    @Test
    public void testGetFullNameWithPackageName() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBar", "c.a.a.b.");
        assertEquals("c.a.a.b.FooBar", camelCaseMatcher.getFullName());
    }

    @Test
    public void testGetFullNameWithOutPackageName() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBar");
        assertEquals("FooBar", camelCaseMatcher.getFullName());
    }


    @Test
    public void testWrongPatternEndCharNoMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooqBaroooBazooBqooBqooBqooBq");
        assertEquals(false, camelCaseMatcher.match("ooBq1"));
    }

    @Test
    public void testLowerCaseStartAndUpperCaseInMiddleOfStringMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooqBaroooBazooBqooBqooBqooBq");
        assertEquals(true, camelCaseMatcher.match("ooBq"));
    }


    @Test
    public void testSecondLowerCaseMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("Foo1qBarFoo2Baz");
        assertEquals(true, camelCaseMatcher.match("oo2"));
    }


    @Test
    public void testLowerCaseStartMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("ooB"));
    }

    @Test
    public void testNoCamelCaseMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBar");
        assertEquals(false, camelCaseMatcher.match("FBB"));
    }

    @Test
    public void testCamelCaseMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBar");
        assertEquals(true, camelCaseMatcher.match("FB"));
    }

    @Test
    public void testLowerCaseNoMatch1() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("ee.metsv.ClassFinder");
        assertEquals(false, camelCaseMatcher.match("asi"));
    }


    @Test
    public void testLowerCaseNoMatch2() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(false, camelCaseMatcher.match("ooaaz"));
    }

    @Test
    public void testPatternInTheMiddleMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("Bar"));
    }

    @Test
    public void testPatternAtStartMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("Foo"));
    }

    public void testPatternAtEndMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("Baz"));
    }


    @Test
    public void testWrongPatternEndNoMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(false, camelCaseMatcher.match("BaK"));
    }


    @Test
    public void testWildCardStarMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("FooBarBaz*"));
        assertEquals(true, camelCaseMatcher.match("B*z"));
        assertEquals(true, camelCaseMatcher.match("f*z"));
        assertEquals(true, camelCaseMatcher.match("*FBar"));
        assertEquals(true, camelCaseMatcher.match("F*B*B*z"));
    }

    @Test
    public void testWildCardStarNoMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(false, camelCaseMatcher.match("F*K*B"));
        assertEquals(false, camelCaseMatcher.match("F*B*B*k"));
    }

    @Test
    public void testInitializeToMatchesMultipleCallOut() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(false, camelCaseMatcher.match("Bak"));
        assertEquals(false, camelCaseMatcher.match("Foo1"));
        assertEquals(true, camelCaseMatcher.match("FBB"));
        assertEquals(true, camelCaseMatcher.match("fbb"));
        assertEquals(false, camelCaseMatcher.match("FKB"));
    }

    @Test
    public void testPatternSpaceEndNoMatchNotEndOfClassName() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(false, camelCaseMatcher.match("FBar "));
    }

    public void testPatternSpaceMatchInEndOfClassName() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBar");
        assertEquals(true, camelCaseMatcher.match("FBar "));
    }

    @Test
    public void testFullPatternWithSpaceMatch() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("FooBarBaz "));
    }

    @Test
    public void testLowerCaseStartPatternWithSpaceNoMatchNotEndOfClassName() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("aFooBarBaz");
        assertEquals(false, camelCaseMatcher.match("FBar "));
    }


    @Test
    public void testPatternSpaceMatchAndEndOfClassName() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("FBaz "));
    }

    @Test
    public void testCamelCaseMatchFooBarBaz() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("FooBarBaz");
        assertEquals(true, camelCaseMatcher.match("FB"));
        assertEquals(true, camelCaseMatcher.match("FoBa"));
        assertEquals(true, camelCaseMatcher.match("FBar"));
        assertEquals(false, camelCaseMatcher.match("FooBarBazTar"));
    }



    @Test
    public void testMatchWithLowerCaseClassNameStart() throws Exception {
        CamelCaseMatcher camelCaseMatcher = new CamelCaseMatcher("aFooBarBaz");
        assertEquals(true, camelCaseMatcher.match("FB"));;
    }
}

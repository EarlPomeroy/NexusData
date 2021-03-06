package com.github.dkharrat.nexusdata.test;

import junit.framework.TestCase;
import com.github.dkharrat.nexusdata.predicate.ExpressionBuilder;
import com.github.dkharrat.nexusdata.predicate.Predicate;
import com.github.dkharrat.nexusdata.predicate.PredicateBuilder;

public class PredicateParserTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWithEquality() throws Throwable {
        Predicate actual = PredicateBuilder.parse("1 == 256");
        Predicate expected = ExpressionBuilder.constant(1).eq(256).getPredicate();
        assertEquals(expected, actual);
    }

    public void testEqualityWithFieldName() throws Throwable {
        Predicate actual = PredicateBuilder.parse("pages == 362");
        Predicate expected = ExpressionBuilder.field("pages").eq(362).getPredicate();
        assertEquals(expected, actual);
    }

    public void testNullEqualityWithFieldName() throws Throwable {
        Predicate actual = PredicateBuilder.parse("pages == null");
        Predicate expected = ExpressionBuilder.field("pages").isNull().getPredicate();
        assertEquals(expected, actual);
    }

    public void testInequalityWithFieldName() throws Throwable {
        Predicate actual = PredicateBuilder.parse("pages != \"foo\"");
        Predicate expected = ExpressionBuilder.field("pages").notEq("foo").getPredicate();
        assertEquals(expected, actual);
    }

    public void testWithString() throws Throwable {
        //TODO: this test hangs; fix
        Predicate actual = PredicateBuilder.parse("1 == \"2\"");
        Predicate expected = ExpressionBuilder.constant(1).eq("2").getPredicate();
        assertEquals(expected, actual);
    }

    public void testComparison() throws Throwable {
        Predicate actual = PredicateBuilder.parse("10 > 5");
        Predicate expected = ExpressionBuilder.constant(10).gt(5).getPredicate();
        assertEquals(expected, actual);
    }

    public void testLogical() throws Throwable {
        Predicate actual = PredicateBuilder.parse("10 > 5 && 8 < 12");
        Predicate expected = ExpressionBuilder.constant(10).gt(5).and(ExpressionBuilder.constant(8).lt(12)).getPredicate();
        assertEquals(expected, actual);
    }

    public void testPrecedence1() throws Throwable {
        Predicate actual = PredicateBuilder.parse("10 > 5 || (8 == 8 && 8 < 6)");
        Predicate expected = ExpressionBuilder.constant(10).gt(5)
                .or(ExpressionBuilder.constant(8).eq(8)
                        .and(ExpressionBuilder.constant(8).lt(6))).getPredicate();
        assertEquals(expected, actual);
    }

    public void testPrecedence2() throws Throwable {
        Predicate actual = PredicateBuilder.parse("(10 > 5 || 8 == 8) && 8 < 6");
        Predicate expected = (ExpressionBuilder.constant(10).gt(5).or(ExpressionBuilder.constant(8).eq(8)).and(ExpressionBuilder.constant(8).lt(6))).getPredicate();
        assertEquals(expected, actual);
    }
}

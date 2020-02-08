package net.youngrok.gist;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Try Finally execution order examples
 * Guess the result string
 * https://stackoverflow.com/questions/65035/does-a-finally-block-always-get-executed-in-java
 */

public class Finally {
    @Test
    public void basicTest() {
        try {
            System.out.println("try");
        } finally {
            System.out.println("finally");
        }
    }

    private String whoReturns() {
        try {
            return "try";
        } finally {
            return "finally";
        }
    }

    @Test
    public void returnTest() {
        System.out.println(whoReturns());
    }

    private String whoLast() {
        String last;
        try {
            last = "try";
            return last;
        } finally {
            last = "finally";
        }
    }

    @Test
    public void lastTest() {
        System.out.println(whoLast());
    }

    @Test
    public void exitTest() {
        try {
            System.exit(0);
        } finally {
            System.out.println("finally");
        }
    }

    @Test
    public void assertTest() {
        try {
            Assert.assertTrue(false);
        } finally {
            System.out.println("finally");
        }
    }

    private String exceptionInTry() {
        try {
            throw new RuntimeException("try");
        } finally {
            return "finally";
        }
    }

    private String exceptionInFinally() {
        try {
            return "try";
        } finally {
            throw new RuntimeException("finally");
        }
    }

    @Test
    public void throwTest() {
        System.out.println("---- Test 1 ----");
        try {
            System.out.println("try: " + exceptionInTry());
        } catch (RuntimeException e) {
            System.out.println("catch: " + e.getMessage());
        }

        System.out.println("---- Test 2 ----");
        try {
            System.out.println("try: " + exceptionInFinally());
        } catch (RuntimeException e) {
            System.out.println("catch: " + e.getMessage());
        }
    }


    @RequiredArgsConstructor
    static class IamAutoCloseable implements AutoCloseable {
        private final String name;

        public void close() {
            System.out.println(name);
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void withResource() {
        try (IamAutoCloseable closeable1 = new IamAutoCloseable("closeable1");
             IamAutoCloseable closeable2 = new IamAutoCloseable("closeable2")) {
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void withResourceFinally() {
        try (IamAutoCloseable closeable1 = new IamAutoCloseable("closeable")) {
            System.out.println("try");
        } finally {
            System.out.println("finally");
        }
    }
}

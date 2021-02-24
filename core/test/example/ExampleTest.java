package example;

import networking.server.ServerNetworkHandle;
import networking.server.observer.Single;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ExampleTest {

    @Before
    public void setup() {
        Single.count++;
    }

    @Test
    public void test1() {
        System.out.println("test" + Single.count);
    }

    @Test
    public void test2() {
        System.out.println("test" + Single.count);
    }

    @Test
    public void test3() {
        System.out.println("test" + Single.count);
    }

    @Test
    public void test4() {
        System.out.println("test" + Single.count);
    }

    @Test
    public void test5() {
        System.out.println("test" + Single.count);
    }
}

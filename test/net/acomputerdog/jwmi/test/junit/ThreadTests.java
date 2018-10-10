package net.acomputerdog.jwmi.test.junit;

import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class ThreadTests {

    private void execSimpleQuery() {
        try (WbemLocator locator = JWMI.getInstance().createWbemLocator()) {
            Assertions.assertNotNull(locator);
            try (WbemServices services = locator.connectServer("root\\CIMV2")) {
                Assertions.assertNotNull(services);
                try (WbemClassObject result = services.execQuerySingle("SELECT * FROM Win32_Service")) {
                    Assertions.assertNotNull(result);
                }
            }
        }
    }

    @Test
    public void testSingleThread() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                execSimpleQuery();
            } catch (Throwable t) {
                Assertions.fail(t);
            }
        });

        t1.start();
        t1.join();
    }

    @Test
    public void testMultipleUsesInSingleThread() {
        for (int j = 0; j < 3; j++) {
            execSimpleQuery();
        }
    }

    @Test
    public void testMultipleLocators() {
        try (WbemLocator loc1 = JWMI.getInstance().createWbemLocator()) {
            Assertions.assertNotNull(loc1);
            try (WbemLocator loc2 = JWMI.getInstance().createWbemLocator()) {
                Assertions.assertNotNull(loc2);
                try (WbemLocator loc3 = JWMI.getInstance().createWbemLocator()) {
                    Assertions.assertNotNull(loc3);
                    try (WbemLocator loc4 = JWMI.getInstance().createWbemLocator()) {
                        Assertions.assertNotNull(loc4);
                        try (WbemLocator loc5 = JWMI.getInstance().createWbemLocator()) {
                            Assertions.assertNotNull(loc5);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testSyncThreads() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                execSimpleQuery();
            } catch (Throwable t) {
                Assertions.fail(t);
            }
        });

        execSimpleQuery();
        t1.start();
        t1.join();
    }

    @Test
    public void testAsyncThreads() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                execSimpleQuery();
            } catch (Throwable t) {
                Assertions.fail(t);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                execSimpleQuery();
            } catch (Throwable t) {
                Assertions.fail(t);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void testManyThreads() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                try {
                    for (int j = 0; j < 3; j++) {
                        execSimpleQuery();
                    }
                } catch (Throwable t) {
                    Assertions.fail(t);
                }
            }));
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    public void testExplicitClose() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                try {
                    execSimpleQuery();
                    WMIWrapper.INSTANCE.closeCOM();
                } catch (Throwable t) {
                    Assertions.fail(t);
                }
            }));
        }

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }
    }
}

package scraper.module.core.scope;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ModuleScopeTest {

    private ModuleScope scope = ModuleScope.instance();

    private ObjectFactory<Integer> objectFactory = new ObjectFactory<Integer>() {

        private volatile Integer value = 0;

        @Override
        public synchronized Integer getObject() throws BeansException {
            return ++value;
        }
    };

    @Mock
    private Runnable callback1;

    @Mock
    private Runnable callback2;

    @Mock
    private Runnable callback3;

    @Before
    public void setUp() {
        scope.init();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutOpenScope() {
        // when
        scope.get("name", objectFactory);
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseWithoutOpenScope() {
        // when
        scope.closeScope();
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWithoutOpenScope() {
        // when
        scope.remove("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterWithoutOpenScope() {
        // when
        scope.registerDestructionCallback("name", callback1);
    }

    @Test
    public void testOpenCloseMultipleTimes() {
        // when
        scope.openScope();
        scope.openScope();
        scope.closeScope();
        scope.openScope();
        scope.closeScope();
        scope.closeScope();

        try {
            scope.closeScope();
            fail();
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    @Test
    public void testGet() {
        // given
        scope.openScope();

        // when
        Integer v1 = (Integer) scope.get("name", objectFactory);
        Integer v2 = (Integer) scope.get("name2", objectFactory);
        Integer v3 = (Integer) scope.get("name", objectFactory);

        // then
        assertEquals((Integer) 1, v1);
        assertEquals((Integer) 2, v2);
        assertEquals((Integer) 1, v3);
    }

    @Test
    public void testRemove() {
        // given
        scope.openScope();

        // when
        Integer v1 = (Integer) scope.get("name", objectFactory);
        Integer v2 = (Integer) scope.remove("name2");
        Integer v3 = (Integer) scope.get("name2", objectFactory);
        Integer v4 = (Integer) scope.get("name2", objectFactory);
        Integer v5 = (Integer) scope.remove("name2");
        Integer v6 = (Integer) scope.get("name2", objectFactory);

        // then
        assertEquals((Integer) 1, v1);
        assertNull(v2);
        assertEquals((Integer) 2, v3);
        assertEquals((Integer) 2, v4);
        assertEquals((Integer) 2, v5);
        assertEquals((Integer) 3, v6);
    }

    @Test
    public void testDestructionCallback() {
        // given
        scope.openScope();

        // when
        scope.get("name", objectFactory);
        scope.get("name2", objectFactory);
        scope.get("name3", objectFactory);
        scope.registerDestructionCallback("name", callback1);
        scope.registerDestructionCallback("name2", callback2);
        scope.registerDestructionCallback("name3", callback3);
        scope.remove("name2");

        scope.closeScope();

        // then
        verify(callback1).run();
        verify(callback2, never()).run();
        verify(callback3).run();
    }

    @Test
    public void testNested() {
        // given
        scope.openScope();

        // when
        Integer v1 = (Integer) scope.get("name", objectFactory);
        Integer v2 = (Integer) scope.get("name2", objectFactory);
        Integer v3 = (Integer) scope.get("name", objectFactory);

        scope.openScope();
        Integer v4 = (Integer) scope.get("name", objectFactory);
        Integer v5 = (Integer) scope.get("name2", objectFactory);
        Integer v6 = (Integer) scope.get("name", objectFactory);
        scope.closeScope();

        Integer v7 = (Integer) scope.get("name", objectFactory);
        Integer v8 = (Integer) scope.get("name2", objectFactory);

        // then
        assertEquals((Integer) 1, v1);
        assertEquals((Integer) 2, v2);
        assertEquals((Integer) 1, v3);
        assertEquals((Integer) 3, v4);
        assertEquals((Integer) 4, v5);
        assertEquals((Integer) 3, v6);
        assertEquals((Integer) 1, v7);
        assertEquals((Integer) 2, v8);
    }

    @Test
    public void testThreads() throws InterruptedException {
        // given
        scope.openScope();

        // when
        Integer v1 = (Integer) scope.get("name", objectFactory);
        Integer v2 = (Integer) scope.get("name2", objectFactory);
        Integer v3 = (Integer) scope.get("name", objectFactory);

        runAndWait(() -> {
            scope.openScope();
            Integer v4 = (Integer) scope.get("name", objectFactory);
            Integer v5 = (Integer) scope.get("name2", objectFactory);
            Integer v6 = (Integer) scope.get("name", objectFactory);

            assertEquals((Integer) 3, v4);
            assertEquals((Integer) 4, v5);
            assertEquals((Integer) 3, v6);
            scope.closeScope();
        });

        Integer v7 = (Integer) scope.get("name", objectFactory);
        Integer v8 = (Integer) scope.get("name2", objectFactory);

        // then
        assertEquals((Integer) 1, v1);
        assertEquals((Integer) 2, v2);
        assertEquals((Integer) 1, v3);
        assertEquals((Integer) 1, v7);
        assertEquals((Integer) 2, v8);
    }

    private void runAndWait(Runnable runnable) throws InterruptedException {
        Thread t = new Thread(runnable);
        final boolean[] failed = new boolean[]{false};
        t.setUncaughtExceptionHandler((thread, throwable) -> failed[0] = true);

        t.start();
        t.join();

        assertFalse("Child thread failed execution", failed[0]);
    }
}

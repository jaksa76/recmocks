package com.zuhlke.testing.recmocks;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Entry point of the framework. It serves as the JUnit runner and also provides the reckmock method.
 */
public class RecMocks extends BlockJUnit4ClassRunner {
    static InterceptorFactory factory;

    /**
     * Creates a recmock, which is a dynamic proxy acting as a spy or mock depending on the current mode.
     *
     * @param underlyingObject
     * @param <T> the type of the underlying object
     * @return
     */
    public static <T> T recmock(T underlyingObject) {
        Interceptor<T> interceptor = factory.createInterceptor(underlyingObject);
        return interceptor.getProxy();
    }

    /**
     * Used by JUnit. You should not invoke this directly.
     */
    public RecMocks(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /**
     * Used by JUnit. You should not invoke this directly.
     */
    @Override
    public void run(RunNotifier notifier) {
        CurrentTestTracker currentTestTracker = new CurrentTestTracker();
        factory = new InterceptorFactory(currentTestTracker);
        notifier.addListener(currentTestTracker);
        super.run(notifier);
    }
}

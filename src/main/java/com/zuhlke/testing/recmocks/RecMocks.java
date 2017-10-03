package com.zuhlke.testing.recmocks;

import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * Entry point of the framework. It serves as the JUnit runner and also provides the {@link #recmock(Object)} method.
 */
public class RecMocks extends BlockJUnit4ClassRunner {
    static InterceptorFactory factory;
    private CurrentTestTracker currentTestTracker;

    /**
     * Creates a recmock, which is a dynamic proxy acting as a spy or mock depending on the current mode.
     *
     * @param underlyingObject the object to be recorded (not used in replay mode)
     * @param <T> the type of the underlying object
     * @return the dynamic proxy that will record/replay interactions
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
        currentTestTracker = new CurrentTestTracker(getTestClass());
        factory = new InterceptorFactory(currentTestTracker);

        // TODO make class teardown failures silent in replay mode
        super.run(notifier);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        // set current context so we know it before a class is created
        // we're not using JUnit's notifier mechanism because it invokes the notifier too late
        currentTestTracker.testStarted(method);
        super.runChild(method, notifier);
        currentTestTracker.testFinished(method);
    }
}

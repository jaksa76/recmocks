package com.zuhlke.testing.recmocks;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class RecMocks extends BlockJUnit4ClassRunner {
    static InterceptorFactory factory;

    public static <T> T recmock(T underlyingObject) {
        Interceptor<T> interceptor = factory.createInterceptor(underlyingObject);
        return interceptor.getProxy();
    }

    public RecMocks(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        CurrentTestTracker currentTestTracker = new CurrentTestTracker();
        factory = new InterceptorFactory(currentTestTracker);
        notifier.addListener(currentTestTracker);
        super.run(notifier);
    }
}

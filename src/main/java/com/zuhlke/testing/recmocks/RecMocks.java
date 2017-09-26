package com.zuhlke.testing.recmocks;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;

public class RecMocks extends BlockJUnit4ClassRunner {
    private static boolean recordMode = true;
    private static CurrentTestTracker currentTestTracker;
    private static int counter = 1;

    public static <T> T recmock(T underlyingObject) {
        String id = generateId(underlyingObject.getClass());
        return isRecordMode() ?
                new Recorder<T>(underlyingObject, id).getInstance()
                : new Replayer<T>((Class<T>) underlyingObject.getClass(), id).getInstance();
    }

    private static boolean isRecordMode() {
        return recordMode;
    }

    static void setRecordMode(boolean recordMode) {
        recordMode = recordMode;
    }

    private static String generateId(Class objectClass) {
        return getCurrentTestClass().replaceAll("\\.", "/")
                + "." + getCurrentTestMethod()
                + "." + objectClass.getSimpleName()
                + "." + counter++;
    }

    private static String getCurrentTestClass() {
        return currentTestTracker.getCurrentTestClass();
    }

    private static String getCurrentTestMethod() {
        return currentTestTracker.getCurrentTestMethod();
    }

    public RecMocks(Class<?> klass) throws InitializationError {
        super(klass);
        System.out.println("RecMocks.RecMocks");
        counter = 1;
        currentTestTracker = new CurrentTestTracker();
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(currentTestTracker);
        super.run(notifier);
    }
}

package com.zuhlke.testing.recmocks;

class InterceptorFactory {
    private CurrentTestTracker currentTestTracker;
    private boolean recordMode = true;

    InterceptorFactory(CurrentTestTracker currentTestTracker) {
        this.currentTestTracker = currentTestTracker;
    }

    <T> Interceptor<T> createInterceptor(T underlying) {
        return new Interceptor<T>(getCurrentContext().generateId(underlying.getClass()), underlying, this);
    }

    Context getCurrentContext() {
        return currentTestTracker.getCurrentContext();
    }

    boolean isRecordMode() {
        return recordMode;
    }

    // for testing only
    void setRecordMode(boolean recordMode) {
        this.recordMode = recordMode;
    }
}

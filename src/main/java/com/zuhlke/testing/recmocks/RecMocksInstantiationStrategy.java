package com.zuhlke.testing.recmocks;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.Collection;

/**
 * Better handling of collections than default Objenesis strategy.
 */
public class RecMocksInstantiationStrategy extends StdInstantiatorStrategy {
    @Override
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> aClass) {
        if (Collection.class.isAssignableFrom(aClass)) {
            return () -> {
                try {
                    return aClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
        }
        return super.newInstantiatorOf(aClass);
    }
}

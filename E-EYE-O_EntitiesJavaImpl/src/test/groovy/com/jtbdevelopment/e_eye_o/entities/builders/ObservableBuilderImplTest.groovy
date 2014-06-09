package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.ObservableImpl

/**
 * Date: 12/1/13
 * Time: 8:36 PM
 */
class ObservableBuilderImplTest extends AbstractObservableBuilderTest {
    static class LocalObservable extends ObservableImpl {
        public LocalObservable() {
            super(null)
        }

    }

    static class LocalObservableBuilder extends ObservableBulderImpl<LocalObservable> {
        public LocalObservableBuilder(LocalObservable e) {
            super(e);
        }
    }

    @Override
    def createEntity() {
        return new LocalObservable()
    }

    @Override
    def createBuilder(final entity) {
        return new LocalObservableBuilder((LocalObservable) entity)
    }
}

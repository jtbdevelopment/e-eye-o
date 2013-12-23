package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ObservableGImpl

/**
 * Date: 12/1/13
 * Time: 8:36 PM
 */
class ObservableBuilderGImplTest extends AbstractObservableBuilderTest {
    static class LocalObservable extends ObservableGImpl {

    }

    @Override
    def createEntity() {
        return new LocalObservable()
    }

    @Override
    def createBuilder(final entity) {
        return new ObservableBuilderGImpl(entity: entity)
    }
}

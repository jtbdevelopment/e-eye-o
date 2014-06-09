package com.jtbdevelopment.e_eye_o.events

import com.google.common.eventbus.EventBus

/**
 * Date: 1/11/14
 * Time: 9:55 PM
 */
class IdObjectChangedPublisherGImplTest extends AbstractIdObjectChangedPublisherTest {
    @Override
    IdObjectChangedPublisher createPublisher(final EventBus eventBus) {
        IdObjectChangedPublisherGImpl publisher = new IdObjectChangedPublisherGImpl()
        publisher.eventBus = eventBus
        publisher.eventFactory = eventFactory
        return publisher
    }
}

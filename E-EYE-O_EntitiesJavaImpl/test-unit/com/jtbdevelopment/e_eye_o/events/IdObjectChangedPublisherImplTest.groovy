package com.jtbdevelopment.e_eye_o.events

import com.google.common.eventbus.EventBus

/**
 * Date: 1/11/14
 * Time: 9:46 PM
 */
class IdObjectChangedPublisherImplTest extends AbstractIdObjectChangedPublisherTest {
    @Override
    IdObjectChangedPublisher createPublisher(final EventBus eventBus) {
        IdObjectChangedPublisherImpl publisher = new IdObjectChangedPublisherImpl()
        publisher.eventFactory = eventFactory
        publisher.eventBus = eventBus
        return publisher
    }
}

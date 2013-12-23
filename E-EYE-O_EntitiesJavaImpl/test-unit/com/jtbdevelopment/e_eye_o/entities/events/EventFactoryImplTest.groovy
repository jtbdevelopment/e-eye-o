package com.jtbdevelopment.e_eye_o.entities.events
/**
 * Date: 12/8/13
 * Time: 8:15 AM
 */
class EventFactoryImplTest extends AbstractEventFactoryTest {
    @Override
    EventFactory createFactory() {
        new EventFactoryImpl()
    }
}

package com.jtbdevelopment.e_eye_o.events
/**
 * Date: 12/8/13
 * Time: 8:15 AM
 */
class EventFactoryGImplTest extends AbstractEventFactoryTest {
    @Override
    EventFactory createFactory() {
        new EventFactoryGImpl()
    }
}

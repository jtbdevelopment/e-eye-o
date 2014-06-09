package com.jtbdevelopment.e_eye_o.DAO.helpers

/**
 * Date: 12/26/13
 * Time: 11:51 PM
 */
class UserCreationHelperImplTest extends AbstractUserCreationHelperTest {
    @Override
    UserCreationHelper createUserHelper() {
        return new UserCreationHelperImpl();
    }
}

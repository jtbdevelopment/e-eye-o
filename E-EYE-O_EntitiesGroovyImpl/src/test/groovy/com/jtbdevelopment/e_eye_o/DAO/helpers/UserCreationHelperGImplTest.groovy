package com.jtbdevelopment.e_eye_o.DAO.helpers

/**
 * Date: 12/26/13
 * Time: 11:36 PM
 */
class UserCreationHelperGImplTest extends AbstractUserCreationHelperTest {
    @Override
    UserCreationHelper createUserHelper() {
        return new UserCreationHelperGImpl()
    }
}

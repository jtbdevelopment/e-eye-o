package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectListImpl

/**
 * Date: 12/22/13
 * Time: 1:22 PM
 */
class PaginatedIdObjectListImplTest extends AbstractPaginatedIdObjectListTest {
    @Override
    PaginatedIdObjectList createPaginatedIdObjectList() {
        return new PaginatedIdObjectListImpl()
    }
}

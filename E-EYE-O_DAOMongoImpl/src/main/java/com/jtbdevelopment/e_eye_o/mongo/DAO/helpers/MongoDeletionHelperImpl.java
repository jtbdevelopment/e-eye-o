package com.jtbdevelopment.e_eye_o.mongo.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.helpers.AbstractDeletionHelperImpl;
import org.springframework.stereotype.Component;

/**
 * Date: 1/14/14
 * Time: 12:17 PM
 */
//  NOT transactional
@Component
public class MongoDeletionHelperImpl extends AbstractDeletionHelperImpl {
    //  Could have implemented same logic with findAndModify but need to generate audit records means
    //  using base method good enough
}

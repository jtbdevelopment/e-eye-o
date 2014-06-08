package com.jtbdevelopment.e_eye_o.mongo.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.helpers.AbstractArchiveHelperImpl;
import org.springframework.stereotype.Component;

/**
 * Date: 1/14/14
 * Time: 12:16 PM
 */
//  NOT transactional
@Component
public class MongoArchiveHelperImpl extends AbstractArchiveHelperImpl {
    //  Could have implemented same logic with findAndModify but need to generate audit records means
    //  using base method good enough
}

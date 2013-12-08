package com.jtbdevelopment.e_eye_o.entities.events

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import groovy.transform.CompileStatic

/**
 * Date: 12/8/13
 * Time: 7:57 AM
 */
@CompileStatic
class AppUserOwnedObjectChangedGImpl<T extends AppUserOwnedObject> extends IdObjectChangedGImpl<T> implements AppUserOwnedObjectChanged<T> {
}

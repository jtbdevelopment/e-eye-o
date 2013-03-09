package com.jtbdevelopment.e_eye_o.ria.vaadin.widgets;

import com.jtbdevelopment.e_eye_o.entities.*;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Tree;

/**
 * Date: 3/6/13
 * Time: 12:57 AM
 */
public class IdObjectTreeContainer extends HierarchicalContainer {
    public static final String ENTITY_DESCRIPTION = "ViewableDescription";
    public static final String ENTITY_CLASS = "EntityClass";
    public static final String ENTITY_ID = "EntityId";
    private final boolean archived;

    public IdObjectTreeContainer(final Tree tree, final boolean archived) {
        this.archived = archived;
        addContainerProperty(ENTITY_DESCRIPTION, String.class, null);
        addContainerProperty(ENTITY_CLASS, Class.class, null);
        addContainerProperty(ENTITY_ID, String.class, "-1");
        Object itemId = addItem();
        Item item = getItem(itemId);
        item.getItemProperty(ENTITY_DESCRIPTION).setValue("Students");
        item.getItemProperty(ENTITY_CLASS).setValue(Student.class);
        setChildrenAllowed(itemId, true);
        itemId = addItem();
        item = getItem(itemId);
        item.getItemProperty(ENTITY_DESCRIPTION).setValue("Classes");
        item.getItemProperty(ENTITY_CLASS).setValue(ClassList.class);
        setChildrenAllowed(itemId, true);
        itemId = addItem();
        item = getItem(itemId);
        item.getItemProperty(ENTITY_DESCRIPTION).setValue("Categories");
        item.getItemProperty(ENTITY_CLASS).setValue(ObservationCategory.class);
        setChildrenAllowed(itemId, true);
        itemId = addItem();
        item = getItem(itemId);
        item.getItemProperty(ENTITY_DESCRIPTION).setValue("Observations");
        item.getItemProperty(ENTITY_CLASS).setValue(Observation.class);
        setChildrenAllowed(itemId, true);
        itemId = addItem();
        item = getItem(itemId);
        item.getItemProperty(ENTITY_DESCRIPTION).setValue("Photos");
        item.getItemProperty(ENTITY_CLASS).setValue(Photo.class);
        setChildrenAllowed(itemId, true);

        tree.setContainerDataSource(this);
        tree.setItemCaptionPropertyId(IdObjectTreeContainer.ENTITY_DESCRIPTION);
        tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tree.setImmediate(true);
    }

    public boolean getArchived() {
        return archived;
    }
}

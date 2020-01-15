package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.groups.RawGroup;
import nl.iprwc.view.View;

public class Group {
    private long id;
    private String name;
    private boolean system;
    private String internalReference;
    private boolean editable;
    private Group[] groups;

    public Group(int id, String name, boolean system, String internalReference, boolean editable, Group[] groups) {
        this.id = id;
        this.name = name;
        this.system = system;
        this.internalReference = internalReference;
        this.editable = editable;
        this.groups = groups;
    }

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
        this.system = false;
        this.internalReference = null;
        this.editable = false;
        this.groups = new Group[0];
    }

    public Group(RawGroup rawGroup, Group[] groups) {
        this.id = rawGroup.id;
        this.name = rawGroup.name;
        this.system = rawGroup.system;
        this.internalReference = rawGroup.internalReference;
        this.editable = rawGroup.editable;
        this.groups = groups;
    }

    @JsonProperty
    @JsonView(View.Private.class)
    public long getId() {
        return id;
    }

    @JsonProperty
    @JsonView(View.Private.class)
    public String getName() {
        return name;
    }

    @JsonProperty
    @JsonView(View.All.class)
    public boolean isSystem() {
        return system;
    }

    @JsonProperty
    @JsonView(View.All.class)
    public String getInternalReference() {
        return internalReference;
    }

    @JsonProperty
    @JsonView(View.All.class)
    public boolean isEditable() {
        return editable;
    }

    @JsonProperty
    @JsonView(View.All.class)
    public Group[] getGroups() {
        return groups;
    }
}

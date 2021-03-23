package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.iprwc.groups.RawGroup;

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
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public boolean isSystem() {
        return system;
    }

    @JsonProperty
    public String getInternalReference() {
        return internalReference;
    }

    @JsonProperty
    public boolean isEditable() {
        return editable;
    }

    @JsonProperty
    public Group[] getGroups() {
        return groups;
    }
}


package edu.kit.turntable.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Root
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class MappingSchema {

    /**
     * Targetid
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("TargetId")
    @Expose
    private String targetId = "";
    /**
     * Registeredmimetypes
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("RegisteredMimeTypes")
    @Expose
    private List<String> registeredMimeTypes = new ArrayList<String>();
    /**
     * Methods
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Methods")
    @Expose
    private Methods methods;

    /**
     * Targetid
     * <p>
     * 
     * (Required)
     * 
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * Targetid
     * <p>
     * 
     * (Required)
     * 
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * Registeredmimetypes
     * <p>
     * 
     * (Required)
     * 
     */
    public List<String> getRegisteredMimeTypes() {
        return registeredMimeTypes;
    }

    /**
     * Registeredmimetypes
     * <p>
     * 
     * (Required)
     * 
     */
    public void setRegisteredMimeTypes(List<String> registeredMimeTypes) {
        this.registeredMimeTypes = registeredMimeTypes;
    }

    /**
     * Methods
     * <p>
     * 
     * (Required)
     * 
     */
    public Methods getMethods() {
        return methods;
    }

    /**
     * Methods
     * <p>
     * 
     * (Required)
     * 
     */
    public void setMethods(Methods methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MappingSchema.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("targetId");
        sb.append('=');
        sb.append(((this.targetId == null)?"<null>":this.targetId));
        sb.append(',');
        sb.append("registeredMimeTypes");
        sb.append('=');
        sb.append(((this.registeredMimeTypes == null)?"<null>":this.registeredMimeTypes));
        sb.append(',');
        sb.append("methods");
        sb.append('=');
        sb.append(((this.methods == null)?"<null>":this.methods));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.registeredMimeTypes == null)? 0 :this.registeredMimeTypes.hashCode()));
        result = ((result* 31)+((this.targetId == null)? 0 :this.targetId.hashCode()));
        result = ((result* 31)+((this.methods == null)? 0 :this.methods.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MappingSchema) == false) {
            return false;
        }
        MappingSchema rhs = ((MappingSchema) other);
        return ((((this.registeredMimeTypes == rhs.registeredMimeTypes)||((this.registeredMimeTypes!= null)&&this.registeredMimeTypes.equals(rhs.registeredMimeTypes)))&&((this.targetId == rhs.targetId)||((this.targetId!= null)&&this.targetId.equals(rhs.targetId))))&&((this.methods == rhs.methods)||((this.methods!= null)&&this.methods.equals(rhs.methods))));
    }

}

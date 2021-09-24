
package edu.kit.turntable.mapping;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Items
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class Identifier {

    /**
     * Identifier
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Identifier")
    @Expose
    private String identifier = "";
    /**
     * Identifiertype
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("IdentifierType")
    @Expose
    private String identifierType = "";

    /**
     * Identifier
     * <p>
     * 
     * (Required)
     * 
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Identifier
     * <p>
     * 
     * (Required)
     * 
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Identifiertype
     * <p>
     * 
     * (Required)
     * 
     */
    public String getIdentifierType() {
        return identifierType;
    }

    /**
     * Identifiertype
     * <p>
     * 
     * (Required)
     * 
     */
    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Identifier.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("identifier");
        sb.append('=');
        sb.append(((this.identifier == null)?"<null>":this.identifier));
        sb.append(',');
        sb.append("identifierType");
        sb.append('=');
        sb.append(((this.identifierType == null)?"<null>":this.identifierType));
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
        result = ((result* 31)+((this.identifier == null)? 0 :this.identifier.hashCode()));
        result = ((result* 31)+((this.identifierType == null)? 0 :this.identifierType.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Identifier) == false) {
            return false;
        }
        Identifier rhs = ((Identifier) other);
        return (((this.identifier == rhs.identifier)||((this.identifier!= null)&&this.identifier.equals(rhs.identifier)))&&((this.identifierType == rhs.identifierType)||((this.identifierType!= null)&&this.identifierType.equals(rhs.identifierType))));
    }

}

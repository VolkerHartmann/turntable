
package edu.kit.turntable.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Metadata
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class Metadata {

    /**
     * Identifiers
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Identifiers")
    @Expose
    private List<Identifier> identifiers = new ArrayList<Identifier>();

    /**
     * Identifiers
     * <p>
     * 
     * (Required)
     * 
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * Identifiers
     * <p>
     * 
     * (Required)
     * 
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Metadata.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("identifiers");
        sb.append('=');
        sb.append(((this.identifiers == null)?"<null>":this.identifiers));
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
        result = ((result* 31)+((this.identifiers == null)? 0 :this.identifiers.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Metadata) == false) {
            return false;
        }
        Metadata rhs = ((Metadata) other);
        return ((this.identifiers == rhs.identifiers)||((this.identifiers!= null)&&this.identifiers.equals(rhs.identifiers)));
    }

}


package edu.kit.turntable.mapping;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Input
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class Input {

    /**
     * Metadata
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Metadata")
    @Expose
    private Metadata metadata;

    /**
     * Metadata
     * <p>
     * 
     * (Required)
     * 
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Metadata
     * <p>
     * 
     * (Required)
     * 
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Input.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("metadata");
        sb.append('=');
        sb.append(((this.metadata == null)?"<null>":this.metadata));
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
        result = ((result* 31)+((this.metadata == null)? 0 :this.metadata.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Input) == false) {
            return false;
        }
        Input rhs = ((Input) other);
        return ((this.metadata == rhs.metadata)||((this.metadata!= null)&&this.metadata.equals(rhs.metadata)));
    }

}

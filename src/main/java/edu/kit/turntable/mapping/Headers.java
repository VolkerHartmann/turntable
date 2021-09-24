
package edu.kit.turntable.mapping;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Headers
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class Headers {

    /**
     * Authentification
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Authentification")
    @Expose
    private String authentification = "";

    /**
     * Authentification
     * <p>
     * 
     * (Required)
     * 
     */
    public String getAuthentification() {
        return authentification;
    }

    /**
     * Authentification
     * <p>
     * 
     * (Required)
     * 
     */
    public void setAuthentification(String authentification) {
        this.authentification = authentification;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Headers.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("authentification");
        sb.append('=');
        sb.append(((this.authentification == null)?"<null>":this.authentification));
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
        result = ((result* 31)+((this.authentification == null)? 0 :this.authentification.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Headers) == false) {
            return false;
        }
        Headers rhs = ((Headers) other);
        return ((this.authentification == rhs.authentification)||((this.authentification!= null)&&this.authentification.equals(rhs.authentification)));
    }

}

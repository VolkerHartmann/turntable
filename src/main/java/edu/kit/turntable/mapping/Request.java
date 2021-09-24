
package edu.kit.turntable.mapping;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Request
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class Request {

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
     * Input
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Input")
    @Expose
    private Input input;

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

    /**
     * Input
     * <p>
     * 
     * (Required)
     * 
     */
    public Input getInput() {
        return input;
    }

    /**
     * Input
     * <p>
     * 
     * (Required)
     * 
     */
    public void setInput(Input input) {
        this.input = input;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Request.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("authentification");
        sb.append('=');
        sb.append(((this.authentification == null)?"<null>":this.authentification));
        sb.append(',');
        sb.append("input");
        sb.append('=');
        sb.append(((this.input == null)?"<null>":this.input));
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
        result = ((result* 31)+((this.input == null)? 0 :this.input.hashCode()));
        result = ((result* 31)+((this.authentification == null)? 0 :this.authentification.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Request) == false) {
            return false;
        }
        Request rhs = ((Request) other);
        return (((this.input == rhs.input)||((this.input!= null)&&this.input.equals(rhs.input)))&&((this.authentification == rhs.authentification)||((this.authentification!= null)&&this.authentification.equals(rhs.authentification))));
    }

}

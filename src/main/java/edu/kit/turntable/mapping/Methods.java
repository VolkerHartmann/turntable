
package edu.kit.turntable.mapping;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Methods
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class Methods {

    /**
     *  0.doip/op.retrieve
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("0.DOIP/Op.Retrieve")
    @Expose
    private edu.kit.turntable.mapping._0DOIPOpRetrieve _0DOIPOpRetrieve;

    /**
     *  0.doip/op.retrieve
     * <p>
     * 
     * (Required)
     * 
     */
    public edu.kit.turntable.mapping._0DOIPOpRetrieve get0DOIPOpRetrieve() {
        return _0DOIPOpRetrieve;
    }

    /**
     *  0.doip/op.retrieve
     * <p>
     * 
     * (Required)
     * 
     */
    public void set0DOIPOpRetrieve(edu.kit.turntable.mapping._0DOIPOpRetrieve _0DOIPOpRetrieve) {
        this._0DOIPOpRetrieve = _0DOIPOpRetrieve;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Methods.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("_0DOIPOpRetrieve");
        sb.append('=');
        sb.append(((this._0DOIPOpRetrieve == null)?"<null>":this._0DOIPOpRetrieve));
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
        result = ((result* 31)+((this._0DOIPOpRetrieve == null)? 0 :this._0DOIPOpRetrieve.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Methods) == false) {
            return false;
        }
        Methods rhs = ((Methods) other);
        return ((this._0DOIPOpRetrieve == rhs._0DOIPOpRetrieve)||((this._0DOIPOpRetrieve!= null)&&this._0DOIPOpRetrieve.equals(rhs._0DOIPOpRetrieve)));
    }

}

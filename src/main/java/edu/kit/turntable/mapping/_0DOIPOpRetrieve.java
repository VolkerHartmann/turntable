
package edu.kit.turntable.mapping;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 *  0.doip/op.retrieve
 * <p>
 * 
 * 
 */
@Generated("jsonschema2pojo")
public class _0DOIPOpRetrieve {

    /**
     * Request
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Request")
    @Expose
    private Request request;
    /**
     * Headers
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Headers")
    @Expose
    private Headers headers;
    /**
     * Url
     * <p>
     * 
     * (Required)
     * 
     */
    @SerializedName("Url")
    @Expose
    private String url = "";

    /**
     * Request
     * <p>
     * 
     * (Required)
     * 
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Request
     * <p>
     * 
     * (Required)
     * 
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Headers
     * <p>
     * 
     * (Required)
     * 
     */
    public Headers getHeaders() {
        return headers;
    }

    /**
     * Headers
     * <p>
     * 
     * (Required)
     * 
     */
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    /**
     * Url
     * <p>
     * 
     * (Required)
     * 
     */
    public String getUrl() {
        return url;
    }

    /**
     * Url
     * <p>
     * 
     * (Required)
     * 
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_0DOIPOpRetrieve.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("request");
        sb.append('=');
        sb.append(((this.request == null)?"<null>":this.request));
        sb.append(',');
        sb.append("headers");
        sb.append('=');
        sb.append(((this.headers == null)?"<null>":this.headers));
        sb.append(',');
        sb.append("url");
        sb.append('=');
        sb.append(((this.url == null)?"<null>":this.url));
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
        result = ((result* 31)+((this.request == null)? 0 :this.request.hashCode()));
        result = ((result* 31)+((this.headers == null)? 0 :this.headers.hashCode()));
        result = ((result* 31)+((this.url == null)? 0 :this.url.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof _0DOIPOpRetrieve) == false) {
            return false;
        }
        _0DOIPOpRetrieve rhs = ((_0DOIPOpRetrieve) other);
        return ((((this.request == rhs.request)||((this.request!= null)&&this.request.equals(rhs.request)))&&((this.headers == rhs.headers)||((this.headers!= null)&&this.headers.equals(rhs.headers))))&&((this.url == rhs.url)||((this.url!= null)&&this.url.equals(rhs.url))));
    }

}

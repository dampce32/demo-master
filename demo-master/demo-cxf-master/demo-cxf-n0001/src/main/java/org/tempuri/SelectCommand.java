
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="str_FunctionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="str_Parameters" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="str_Mask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "strFunctionName",
    "strParameters",
    "strMask"
})
@XmlRootElement(name = "SelectCommand")
public class SelectCommand {

    @XmlElement(name = "str_FunctionName")
    protected String strFunctionName;
    @XmlElement(name = "str_Parameters")
    protected String strParameters;
    @XmlElement(name = "str_Mask")
    protected String strMask;

    /**
     * 获取strFunctionName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrFunctionName() {
        return strFunctionName;
    }

    /**
     * 设置strFunctionName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrFunctionName(String value) {
        this.strFunctionName = value;
    }

    /**
     * 获取strParameters属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrParameters() {
        return strParameters;
    }

    /**
     * 设置strParameters属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrParameters(String value) {
        this.strParameters = value;
    }

    /**
     * 获取strMask属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMask() {
        return strMask;
    }

    /**
     * 设置strMask属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMask(String value) {
        this.strMask = value;
    }

}

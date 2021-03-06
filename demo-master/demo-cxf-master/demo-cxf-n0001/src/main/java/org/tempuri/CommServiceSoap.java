package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.14
 * 2017-11-14T13:47:09.302+08:00
 * Generated source version: 3.1.14
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "CommServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface CommServiceSoap {

    @RequestWrapper(localName = "SelectCommand", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SelectCommand")
    @WebMethod(operationName = "SelectCommand", action = "http://tempuri.org/SelectCommand")
    @ResponseWrapper(localName = "SelectCommandResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SelectCommandResponse")
    public void selectCommand(
        @WebParam(name = "str_FunctionName", targetNamespace = "http://tempuri.org/")
        java.lang.String strFunctionName,
        @WebParam(name = "str_Parameters", targetNamespace = "http://tempuri.org/")
        java.lang.String strParameters,
        @WebParam(name = "str_Mask", targetNamespace = "http://tempuri.org/")
        java.lang.String strMask,
        @WebParam(mode = WebParam.Mode.OUT, name = "SelectCommandResult", targetNamespace = "http://tempuri.org/")
        javax.xml.ws.Holder<java.lang.String> selectCommandResult,
        @WebParam(mode = WebParam.Mode.OUT, name = "str_err", targetNamespace = "http://tempuri.org/")
        javax.xml.ws.Holder<java.lang.String> strErr
    );
}

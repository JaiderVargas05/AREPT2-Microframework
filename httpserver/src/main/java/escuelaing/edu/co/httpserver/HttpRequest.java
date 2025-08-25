/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelaing.edu.co.httpserver;

import java.net.URI;

/**
 *
 * @author jaider.vargas-n
 */
public class HttpRequest {
    URI requestURI = null;
    public HttpRequest(URI requestURI){
        this.requestURI = requestURI;
    }
    public String getValue(String paramname){
        String val = requestURI.getQuery().split("=")[1];
        return val;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymessage;

import java.io.Serializable;

/**
 *
 * @author Bala J
 */
public class VerifyReq extends MMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    public VerifyReq()
    {
        type=3;
    }
    public String username;
    public String scancode;
    
}

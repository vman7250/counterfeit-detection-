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
public class VerifyRes extends MMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    public VerifyRes()
    {
        type=4;
    }
    public String status;
    
    
}

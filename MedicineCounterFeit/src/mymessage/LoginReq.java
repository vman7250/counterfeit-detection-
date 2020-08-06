package mymessage;


import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bala J
 */
public class LoginReq extends MMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public LoginReq()
    {
        type=1;
    }
    
    public String username;
    public String pwd;
    
    
    
}

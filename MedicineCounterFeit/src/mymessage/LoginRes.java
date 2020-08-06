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
public class LoginRes extends MMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    public LoginRes()
    {
        type=2;
    }
    
    public int status; // 0- success , 1- fail
    
}

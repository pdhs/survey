/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Data;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author root
 */
@FacesConverter(value="responseForConverter")
public class ResponseForConverter extends EnumConverter {

    public ResponseForConverter() {
        super(ResponseFor.class);
    }
    
    
    
}

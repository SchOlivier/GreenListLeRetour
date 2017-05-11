package org.greenlist.chat.bean;

import java.util.Date;
import javax.ejb.Local;
 
/**
 * Local interface for chat lagic EJB
 * @author Danon
 */
@Local
public interface MessageManagerLocal {
 
    void sendMessage(Message msg);
 
    Message getFirstAfter(Date after);
 
}

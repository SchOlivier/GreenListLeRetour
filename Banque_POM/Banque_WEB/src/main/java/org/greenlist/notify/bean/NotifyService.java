package org.greenlist.notify.bean;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.commons.lang.StringEscapeUtils;
import org.greenlist.business.api.IBusinessNotification;
import org.greenlist.business.api.IBusinessSouhait;
import org.greenlist.controller.UtilisateurManagedBean;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Message;
import org.greenlist.entity.Notification;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Souhait;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean(name = "mbNotifier")
@RequestScoped
public class NotifyService {

	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnect;

	@EJB
	IBusinessNotification proxyNotification;
	@EJB
	IBusinessSouhait proxySouhait;
	@EJB
	EmailSessionBean emailBean;

	Objet objet;
	Echange echange;
	Message message;

	public void notifierNouvelObjet() {
    	for (Souhait s : proxySouhait.getSouhaits(objet, mbConnect.getUtilisateurConnecte())){
        	Notification notification = new Notification();
        	notification.setUtilisateur(mbConnect.getUtilisateurConnecte());
        	String msg = "<a href=\"/recherche.xhtml?idS=" + s.getId() + ">Nouvel Objet Compatible</a>";
        	notification.setContenu(msg);
        	notification.setDateEmission(Calendar.getInstance().getTime());
        	notification.setIslu(false);
        	proxyNotification.createNotification(notification);
        	String to = s.getListe().getUtilisateur().getEmail();
        	String subject = "GreenList : Nouvel Objet correspondant à votre souhait de " + s.getLibelle();
        	String corps =         			
        			"Bonjour " + s.getListe().getUtilisateur().getPseudo() + ",<br>" +
        			"Depuis votre dernière visite,"
        			+ "[Nous avons trouvé un nouvel objet concordant à votre souhait "
        			+ s.getLibelle() + ".<br><br>@ Bientôt :)<br>L'équipe GreenList <3";
    	}
    }

	public void notifierPropositionEchange() {
	}

}

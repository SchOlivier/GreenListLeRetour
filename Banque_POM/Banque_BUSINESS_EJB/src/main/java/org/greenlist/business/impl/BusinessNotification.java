package org.greenlist.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.greenlist.business.api.IBusinessNotification;
import org.greenlist.data.api.IDaoNotification;
import org.greenlist.entity.Notification;
import org.greenlist.entity.Utilisateur;

@Remote(IBusinessNotification.class)
@Stateless
public class BusinessNotification implements IBusinessNotification {
	
	@EJB
	private IDaoNotification proxyNotification;

	@Override
	public List<Notification> getNotificationsEnCours(Utilisateur utilisateur) {
		return proxyNotification.getNotificationsEnCours(utilisateur);
	}

	@Override
	public Notification createNotification(Notification notification) {
		return proxyNotification.createNotification(notification);
	}

	@Override
	public Notification lireNotification(Notification notification) {
		return proxyNotification.lireNotification(notification);
	}

}

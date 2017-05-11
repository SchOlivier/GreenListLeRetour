package org.greenlist.business.api;

import java.util.List;

import org.greenlist.entity.Notification;
import org.greenlist.entity.Utilisateur;

public interface IBusinessNotification {

	List<Notification> getNotificationsEnCours(Utilisateur utilisateur);
	Notification createNotification(Notification notification);
	Notification lireNotification(Notification notification);

}

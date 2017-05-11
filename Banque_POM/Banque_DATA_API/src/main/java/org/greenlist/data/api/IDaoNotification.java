package org.greenlist.data.api;

import java.util.List;

import org.greenlist.entity.Notification;
import org.greenlist.entity.Utilisateur;

public interface IDaoNotification {

	//List<Notification> getAllNotifications(Utilisateur utilisateur);
	//List<Notification> getNotificationsLues(Utilisateur utilisateur);
	List<Notification> getNotificationsEnCours(Utilisateur utilisateur);
	Notification createNotification(Notification notification);
	Notification lireNotification(Notification notification);

}

package org.greenlist.data.impl;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.greenlist.data.api.IDaoNotification;
import org.greenlist.data.api.IDaoObjet;
import org.greenlist.entity.Notification;
import org.greenlist.entity.Utilisateur;

@Remote(IDaoNotification.class)
@Singleton
public class DaoNotification implements IDaoNotification {

	@PersistenceContext(unitName = "Banque_DATA_EJB")
	private EntityManager em;
	
	private static final String REQUETTE_GET_NOTIFS_BY_UTILISATEUR =
			"SELECT n FROM Notification n inner join fetch n.typenotification WHERE n.utilisateur.id = :pIdUtilisateur"
			+ "AND n.isIsLu = FALSE";

	@SuppressWarnings("unchecked")
	@Override
	public List<Notification> getNotificationsEnCours(Utilisateur utilisateur) {
		return em.createQuery(REQUETTE_GET_NOTIFS_BY_UTILISATEUR).setParameter("pIdUtilisateur", utilisateur.getId()).getResultList();
	}

	@Override
	public Notification createNotification(Notification notification) {
		em.persist(notification);
		return notification;
	}

	@Override
	public Notification lireNotification(Notification notification) {
		em.persist(notification);
		return notification;
	}

}

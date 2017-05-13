package org.greenlist.data.impl;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.greenlist.data.api.IDaoEchange;
import org.greenlist.data.api.IDaoObjet;
import org.greenlist.entity.Adresse;
import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Liste;
import org.greenlist.entity.Message;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Photo;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Souhait;
import org.greenlist.entity.Utilisateur;

@Remote(IDaoEchange.class)
@Singleton
public class DaoEchange implements IDaoEchange {

	@PersistenceContext(unitName = "Banque_DATA_EJB")
	private EntityManager em;

	private static final String REQUETE_GET_ECHANGE = " SELECT e FROM Echange e " + "LEFT JOIN fetch e.objets "
			+ "WHERE e.id = :pEid";

	private static final String REQUETE_GET_USERA = "SELECT e.utilisateurByIdusera FROM Echange e " + "WHERE e = :pE";

	private static final String REQUETE_GET_USERB = "SELECT e.utilisateurByIduserb FROM Echange e " + "WHERE e = :pE";

	private static final String REQUETE_GET_OBJETS = "SELECT e.objets o fROM Echange e inner join fetch o.produit inner join fetch o.trancheAge " + "WHERE e.id = :pEid";
	private static final String REQUETE_GET_MESSAGES = "SELECT e.messages FROM Echange e" + " WHERE e.id = :pEid";
	private static final String REQUETE_GET_RDVS = "SELECT rdv FROM Rdv rdv " + "INNER JOIN fetch rdv.adresse "
			+ "INNER JOIN rdv.echange " + "WHERE rdv.echange.id = :pEid";

	private static final String REQUETE_GET_CONCLUSION = "SELECT e.conclusionechange FROM Echange e " + "WHERE e = :pE";

	private static final String REQUETE_RETIRER_OBJET = "DELETE FROM ECHANGE_OBJET "
			+ "WHERE ECH_ID = :EId AND OBJ_ID = :OId";

	@Override
	public Echange creerEchange(Echange echange) {
		em.persist(echange);
		return echange;
	}

	@Override
	public Echange GetEchange(int IdEchange) {
		Query query = em.createQuery(REQUETE_GET_ECHANGE).setParameter("pEid", IdEchange);
		return (Echange) query.getSingleResult();
	}

	@Override
	public Utilisateur GetUtilisateurA(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_USERA).setParameter("pE", echange);
		Utilisateur user = (Utilisateur) query.getSingleResult();
		em.merge(user);
		user = recupererDonneesUtilisateur(user);
		return user;
	}

	@Override
	public Utilisateur GetUtilisateurB(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_USERB).setParameter("pE", echange);
		Utilisateur user = (Utilisateur) query.getSingleResult();
		em.merge(user);
		user = recupererDonneesUtilisateur(user);
		return user;
	}

	private Utilisateur recupererDonneesUtilisateur(Utilisateur user) {
		List<Objet> objets = user.getObjets();
		objets.size();
		for (Objet o : objets) {
			List<Photo> photos = o.getPhotos();
			photos.size();
		}
		List<Liste> listes = user.getListes();
		listes.size();
		for (Liste l : listes) {
			List<Souhait> souhaits = l.getSouhaits();
			souhaits.size();
		}
		return user;
	}

	@Override
	public Echange majEchange(Echange echange) {
		em.merge(echange);
		return echange;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjets(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_OBJETS).setParameter("pEid", echange.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessages(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_MESSAGES).setParameter("pEid", echange.getId());

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rdv> getRdv(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_RDVS).setParameter("pEid", echange.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Conclusionechange> getConclusion(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_CONCLUSION).setParameter("pE", echange);
		return query.getResultList();
	}

	@Override
	public Echange retirerObjet(Objet objet, Echange echange) {
		Query query = em.createNativeQuery(REQUETE_RETIRER_OBJET).setParameter("EId", echange.getId())
				.setParameter("OId", objet.getId());
		query.executeUpdate();
		return echange;
	}

}

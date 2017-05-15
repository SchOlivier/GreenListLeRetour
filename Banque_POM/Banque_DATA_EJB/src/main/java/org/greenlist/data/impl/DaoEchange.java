package org.greenlist.data.impl;

import java.util.Iterator;
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
import org.greenlist.entity.Note;
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

	private static final String REQUETE_GET_USERA = "SELECT u FROM Utilisateur u " + "INNER JOIN fetch u.adresses "
			+ "INNER JOIN u.echangesForIdusera e " + "WHERE e.id = :pE";

	private static final String REQUETE_GET_USERB = "SELECT u FROM Utilisateur u " + "INNER JOIN fetch u.adresses "
			+ "INNER JOIN u.echangesForIduserb e " + "WHERE e.id = :pE";

	private static final String REQUETE_GET_OBJETS = "SELECT e.objets  fROM Echange e " + "WHERE e.id = :pEid";
	private static final String REQUETE_GET_MESSAGES = "SELECT e.messages FROM Echange e" + " WHERE e.id = :pEid"

			+ "INNER JOIN rdv.echange " + "WHERE rdv.echange.id = :pEid";

	private static final String REQUETE_GET_CONCLUSION = "SELECT e.conclusionechange FROM Echange e " + "WHERE e = :pE";

	private static final String REQUETE_RETIRER_OBJET = "DELETE FROM ECHANGE_OBJET "
			+ "WHERE ECH_ID = :EId AND OBJ_ID = :OId";

	private static final String REQUETE_GET_RDVS = "SELECT rdv FROM Rdv rdv " + "INNER JOIN fetch rdv.adresse "
			+ "INNER JOIN rdv.echange " + "WHERE rdv.echange.id = :pEid";

	private static final String REQUETE_GET_NOTES = "SELECT e.notes FROM Echange e " + "WHERE e.id = :peId";

	private static final String GET_CONCLUSION_BY_ID = "SELECT c FROM Conclusionechange c " + "WHERE c.id = :pCId";

	private static final String SUPPRIMER_RDVS = "DELETE FROM RDV " + "WHERE RDV.IDECHANGE = :pEId";

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
		Query query = em.createQuery(REQUETE_GET_USERA).setParameter("pE", echange.getId());
		Utilisateur user = (Utilisateur) query.getSingleResult();
		em.merge(user);
		user = recupererDonneesUtilisateur(user);
		return user;
	}

	@Override
	public Utilisateur GetUtilisateurB(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_USERB).setParameter("pE", echange.getId());
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
		List<Objet> objets = query.getResultList();

		System.out.println(objets.size());
		for (Objet objet : objets) {
			em.merge(objet);
			objet.getTrancheAge();
			objet.getProduit();
		}

		return objets;
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

	@Override
	public Conclusionechange getConclusion(Echange echange) {
		Query query = em.createQuery(REQUETE_GET_CONCLUSION).setParameter("pE", echange);
		Conclusionechange conclusion;
		// la conclusion n'est pas nécessairement rattachée à l'échange,
		// du coup try/catch au cas où il n'y ait rien à récuperer.
		try {
			conclusion = (Conclusionechange) query.getSingleResult();
		} catch (Exception e) {
			conclusion = null;
		}
		return conclusion;
	}

	@Override
	public Echange retirerObjet(Objet objet, Echange echange) {
		Query query = em.createNativeQuery(REQUETE_RETIRER_OBJET).setParameter("peId", echange.getId())
				.setParameter("poId", objet.getId());
		query.executeUpdate();
		return echange;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjetUserEchange(Echange echange, Utilisateur utilisateur) {

		Query query = em.createQuery(REQUETE_GET_OBJETS).setParameter("pEid", echange.getId());

		List<Objet> objets = query.getResultList();

		Iterator<Objet> i = objets.iterator();
		while (i.hasNext()) {
			if (i.next().getUtilisateur().getId() != utilisateur.getId()) {
				i.remove();
			}
		}

		return objets;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Note> getNotes(Echange echange) {

		return em.createQuery(REQUETE_GET_NOTES).setParameter("peId", echange.getId()).getResultList();
	}

	@Override
	public void ajouterRdv(Rdv rdv) {
		Query query = em.createNativeQuery(SUPPRIMER_RDVS).setParameter("pEId", rdv.getEchange().getId());
		query.executeUpdate();
		em.persist(rdv);

	}

	@Override
	public void majRdv(Rdv rdv) {
		em.merge(rdv);
	}

	@Override
	public Conclusionechange getConclusionById(int id) {
		return (Conclusionechange) em.createQuery(GET_CONCLUSION_BY_ID).setParameter("pCId", id).getSingleResult();
	}

	@Override
	public Note noterEchange(Note note) {
		em.persist(note);
		return note;
	}

}

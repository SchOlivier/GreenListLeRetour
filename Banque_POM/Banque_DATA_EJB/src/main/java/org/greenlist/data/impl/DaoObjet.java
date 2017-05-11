package org.greenlist.data.impl;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.greenlist.data.api.IDaoObjet;
import org.greenlist.entity.Adresse;
import org.greenlist.entity.Domaine;
import org.greenlist.entity.Groupe;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Photo;
import org.greenlist.entity.Produit;
import org.greenlist.entity.Utilisateur;

@Remote(IDaoObjet.class)
@Singleton
public class DaoObjet implements IDaoObjet {

	@PersistenceContext(unitName = "Banque_DATA_EJB")
	private EntityManager em;

	private static final String REQUETTE_GET_OBJET_BY_ID = "SELECT o FROM Objet o WHERE o.id = :pidObjet";
	private static final String REQUETTE_GET_OBJET_BY_ID_WITH_PDT_TA = "SELECT o FROM Objet o inner join fetch o.produit inner join fetch o.trancheAge inner join fetch o.utilisateur WHERE o.id = :pidObjet";

	private static final String REQUETTE_GET_OBJETS_BY_UTILISATEUR = "SELECT u.objets FROM Utilisateur as u WHERE u.id = :pIdUtilisateur";

	private static final String REQUETTE_GET_OBJETS_BY_LIBELLE = "SELECT o FROM Objet as o WHERE o.libelle LIKE :pmotClef";

	private static final String REQUETE_GET_PHOTOS = "SELECT o.photos FROM Objet o WHERE o.id = :pIdObjet";

	private static final String REQUETE_GET_GROUPE = "SELECT g from Groupe g where g.id = :pGId ";
	private static final String REQUETE_GET_DOMAINE = "SELECT D from Domaine d where d.id = :pDId ";
	private static final String REQUETE_GET_ADRESSE = "SELECT a " + "FROM Adresse a "
			+ "INNER JOIN fetch a.utilisateur u " + "INNER JOIN fetch u.objets o " + "WHERE o.id = :pIdObjet";

	private static final String REQUETTE_GET_OBJETS_BY_DOMAINE = " SELECT o FROM Objet  as o"
			+ " JOIN o.produit as produit" + " join produit.groupe as groupe" + "join groupe.domaine as domaine"
			+ " WHERE domaine = :pDomaine";

	private static final String REQUETTE_GET_OBJETS_BY_GROUPE = " SELECT o FROM Objet as  o"
			+ " JOIN o.produit as produit " + "join produit.groupe as groupe" + " WHERE groupe = :pGroupe";

	private static final String REQUETTE_GET_OBJETS_BY_PRODUIT = "SELECT o from Objet o inner join fetch o.produit p inner join fetch o.trancheAge inner join fetch o.utilisateur u"
			+ "inner join fetch p.groupe g " + "inner join fetch g.domaine  WHERE o.produit = :pProduit "
			+ "AND o.utilisateur.id <> :pIdUtilisateur";

	/**
	 * Methode pour r�cup�rer un objet par son id
	 * 
	 * @param idObjet
	 *            id de l'objet recherch�
	 */
	@Override
	public Objet getObjetById(int idObjet) {
		Query query = em.createQuery(REQUETTE_GET_OBJET_BY_ID).setParameter("pidObjet", idObjet);
		return (Objet) query.getSingleResult();
	}

	/**
	 * Methode pour r�cup�rer un objet par son id
	 * 
	 * @param idObjet
	 *            id de l'objet recherch�
	 */
	@Override
	public Objet getObjetByIdWithProduitAndTA(int idObjet) {
		Objet objetComplet = new Objet();
		Query query = em.createQuery(REQUETTE_GET_OBJET_BY_ID_WITH_PDT_TA).setParameter("pidObjet", idObjet);
		objetComplet = (Objet) query.getSingleResult();
		Query queryGroupe = em.createQuery(REQUETE_GET_GROUPE).setParameter("pGId",
				objetComplet.getProduit().getGroupe().getId());
		objetComplet.getProduit().setGroupe((Groupe) queryGroupe.getSingleResult());
		Query queryDomaine = em.createQuery(REQUETE_GET_DOMAINE).setParameter("pDId",
				objetComplet.getProduit().getGroupe().getDomaine().getId());
		objetComplet.getProduit().getGroupe().setDomaine((Domaine) queryDomaine.getSingleResult());

		objetComplet.setPhotos(objetComplet.getPhotos());
		
		
		
		return objetComplet;
	}

	/**
	 * Methode pour ajouter un objet
	 * 
	 * @param objet
	 *            Objet � cr�er
	 */
	@Override
	public Objet createObjet(Objet objet) {
		em.persist(objet);
		return objet;
	}

	/**
	 * Methode pour r�cup�rer l'ensemble des objets d'un utilisateur avec les photos
	 * 
	 * @param utilisateur
	 *            le propri�taire des objets recherch�s
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjetsByUtilisateur(Utilisateur utilisateur) {
		String hql = "SELECT o FROM Objet o  WHERE o.utilisateur.id = :pid";
		List<Objet> objets = em.createQuery(hql).setParameter("pid", utilisateur.getId()).getResultList();
		
		for (Objet objet : objets) {
			objet.setPhotos(getPhotos(objet));

		}

		return objets;
			
	}

	/**
	 * Methode pour rechercher les objets aillant un libell� ressemblant � une
	 * chaine de caractere
	 * 
	 * @param motClef
	 *            Chaine que l'on cherche
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjetsByLibelle(String motClef) {

		StringBuilder sb = new StringBuilder();
		motClef = sb.append("%").append(motClef).append("%").toString();

		Query query = em.createQuery(REQUETTE_GET_OBJETS_BY_LIBELLE).setParameter("pmotClef", motClef);
		return query.getResultList();

	}

	/**
	 * Recherche des objets appartenant � un domaine
	 * 
	 * @param domaine
	 *            le domaine que l'on recherche
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjetsByDomaine(Domaine domaine) {
		Query query = em.createQuery(REQUETTE_GET_OBJETS_BY_DOMAINE).setParameter("pDomaine", domaine);
		return query.getResultList();
	}

	/**
	 * Recherche des objets appartenant � un groupe
	 * 
	 * @param domaine
	 *            le groupe que l'on recherche
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjetsByGroupe(Groupe groupe) {
		Query query = em.createQuery(REQUETTE_GET_OBJETS_BY_GROUPE).setParameter("pGroupe", groupe);
		return query.getResultList();
	}

	/**
	 * Recherche des objets appartenant à un produit et n'appartenant pas à un
	 * utilisateur donné.
	 * 
	 * @param produit
	 *            le produit que l'on recherche
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObjetsByProduit(Produit produit, Utilisateur utilisateur) {

		Query query = em.createQuery(REQUETTE_GET_OBJETS_BY_PRODUIT).setParameter("pProduit", produit);
		query.setParameter("pIdUtilisateur", utilisateur.getId());
		List<Objet> objets = query.getResultList();

		for (Objet objet : objets) {
			objet.setPhotos(getPhotos(objet));

		}

		return objets;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Photo> getPhotos(Objet objet) {
		List<Photo> photos = null;

		Query query = em.createQuery(REQUETE_GET_PHOTOS).setParameter("pIdObjet", objet.getId());
		photos = query.getResultList();

		if (photos.size() == 0) {
			String path = "/img/pardefaut.png";

			Photo defaut = new Photo();
			defaut.setUrl(path);
			defaut.setObjet(objet);
			photos.add(defaut);
		}

		return photos;
	}

	@Override
	public Adresse getAdresse(Objet objet) {
		return (Adresse) em.createQuery(REQUETE_GET_ADRESSE).setParameter("pIdObjet", objet.getId()).getSingleResult();
	}
}

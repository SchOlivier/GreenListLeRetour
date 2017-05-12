package org.greenlist.data.impl;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.greenlist.data.api.IDaoPanier;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Utilisateur;

@Remote(IDaoPanier.class)
@Singleton
public class DaoPanier implements IDaoPanier {

	private static final String GET_OBJETS_BY_PANIER = "SELECT p.objets FROM Panier p WHERE p.id = :pIdPanier";
	private static final String GET_PANIER_BY_UTILISATEUR = "SELECT p FROM Panier p left join fetch p.objets WHERE p.utilisateur.id =:pIdUtilisateur";
	private static final String GET_PANIER_INVIT = "SELECT p FROM Panier p left join fetch p.objets WHERE p.id = 500 " ;
	@PersistenceContext(unitName = "Banque_DATA_EJB")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Objet> getObetsByPanier(Panier panier) throws Exception {
		Query query = em.createQuery(GET_OBJETS_BY_PANIER).setParameter("pIdPanier", panier.getId());
		return query.getResultList();
	}

	@Override
	public Panier getPanierByUtilisateur(Utilisateur utilisateur) throws Exception {
		Query query = em.createQuery(GET_PANIER_BY_UTILISATEUR).setParameter("pIdUtilisateur", utilisateur.getId());
		return (Panier) query.getSingleResult();
	}

	@Override
	public Panier creationPanier(Panier panier) {
		 em.persist(panier);
		return panier;
	}

	@Override
	public List<Objet> ajouterObjetPanier(Objet objet, Panier panier) {
		List<Objet> objets = null;
		try {
			objets = getObetsByPanier(panier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		panier.setObjets(objets);
		
		panier.getObjets().add(objet);
		if( panier.getId() != 0  ){
		em.merge(panier);
		}
		return  panier.getObjets();
	}

	@Override
	public List<Objet> viderPanier(Panier panier) {
	
		
		List<Objet> vide = null;
		panier.setObjets(vide);
		em.merge(panier);
		return panier.getObjets();
	}

	@Override
	public Panier getInvite() {
		Query query = em.createQuery(GET_PANIER_INVIT);
		return (Panier) query.getSingleResult();
	}

}

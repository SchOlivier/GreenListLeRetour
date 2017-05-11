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

}

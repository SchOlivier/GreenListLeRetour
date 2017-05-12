package org.greenlist.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.greenlist.business.api.IBusinessPanier;
import org.greenlist.data.api.IDaoPanier;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Utilisateur;

@Remote(IBusinessPanier.class)
@Stateless
public class BusinessPanier implements IBusinessPanier {
	@EJB
	private IDaoPanier proxyPanier;

	@Override
	public List<Objet> getObetsByPanier(Panier panier) {
		List<Objet> listes = null;
		try {
			listes = proxyPanier.getObetsByPanier(panier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listes;
	}

	@Override
	public Panier getPanierByUtilisateur(Utilisateur utilisateur) {
		Panier panier=null;
		try {
			panier = proxyPanier.getPanierByUtilisateur(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return panier;
	}

	@Override
	public Panier creationPanier(Panier panier) {
	
		return proxyPanier.creationPanier(panier);
	}

	@Override
	public List<Objet> ajouterObjetPanier(Objet objet, Panier panier) {
		
		return proxyPanier.ajouterObjetPanier(objet, panier);
	}

	@Override
	public Panier getPanierInvite() {
		// TODO Auto-generated method stub
		return proxyPanier.getInvite();
	}

}

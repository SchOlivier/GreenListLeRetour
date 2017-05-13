package org.greenlist.business.api;

import java.util.List;

import org.greenlist.entity.Objet;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Utilisateur;

public interface IBusinessPanier {
	List<Objet> getObetsByPanier(Panier panier);
	Panier getPanierByUtilisateur(Utilisateur utilisateur);
	
public Panier creationPanier(Panier panier);
	
	public List<Objet> ajouterObjetPanier (Objet objet, Panier panier);
	public Panier getPanierInvite();
}

package org.greenlist.data.api;

import org.greenlist.entity.Objet;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Utilisateur;
import java.util.List;

public interface IDaoPanier {

	List<Objet> getObetsByPanier(Panier panier) throws Exception;
	Panier getPanierByUtilisateur(Utilisateur utilisateur) throws Exception;
	
	public Panier getInvite();
	public List<Objet> ajouterObjetPanier(Objet objet, Panier panier);
	public Panier creationPanier(Panier panier);
	public List<Objet> viderPanier(Panier panier);
}

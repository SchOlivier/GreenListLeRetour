package org.greenlist.business.api;

import java.util.List;
import java.util.Set;

import org.greenlist.entity.Adresse;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Photo;
import org.greenlist.entity.Produit;
import org.greenlist.entity.Utilisateur;

public interface IBusinessObjet {

	/**
	 * Methode qui permet d'enregistrer un OBjet dans le SI
	 * 
	 * @param objet
	 *            a enregistrer
	 * @return l'Objet actualis� ( id, date de depot)
	 */
	public Objet creerObjet(Objet objet);

	/**
	 * 
	 * @param utilisateur
	 * @return la liste d'objet de l'utilisateur
	 */
	public List<Objet> getObjets(Utilisateur utilisateur);

	/**
	 * 
	 * @param objet
	 *            que l'on rechercher
	 * @return l'Objet Complet
	 */
	public Objet getObjet(Objet objet);

	/**
	 * renvoie tous les objets appartenant à un produit donné et n'appartenant
	 * pas à un utilisateur donné.
	 * 
	 * @param produit
	 *            le produit spécifié
	 * @param utilisateur
	 *            l'utilisateur spécifié.
	 * @return les objets appartenant à cette catégorie de produits.
	 */
	public Set<Objet> getObjets(Produit produit, Utilisateur utilisateur);

	/**
	 * Renvoie l'adresse de l'utilisateur possédant un objet donné
	 * 
	 * @param objet
	 *            l'objet en question
	 * @return l'adresse de son possesseur.
	 */
	Adresse getAdresse(Objet objet);

	
	public List<Photo> getPhotos(Objet objet);


	Objet getObjetComplet(Objet objet);
	
	Echange creerEchange(Objet objet, Utilisateur userA, Utilisateur userB);
}

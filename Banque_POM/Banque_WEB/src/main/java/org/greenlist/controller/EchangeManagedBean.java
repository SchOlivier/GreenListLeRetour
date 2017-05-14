package org.greenlist.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.greenlist.business.api.IBusinessEchange;
import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Utilisateur;
import org.greenlist.utilitaire.EtapeEchange;

@ManagedBean(name = "mbEchange")
@SessionScoped
public class EchangeManagedBean {

	@EJB
	private IBusinessEchange proxyEchange;
	@EJB
	private IBusinessUtilisateur proxyUtilisateur;
	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessUtilisateur proxyUser;

	
	private Echange echange;
	private Utilisateur userA;
	private Utilisateur userB;
	private List<Rdv> rdvs;
	private Conclusionechange conclusion;
	private List<Objet> objets;
	private List<Objet> objetsA;
	private List<Objet> objetsB;

	
	private static final int IDECHANGE = 2;

	// TODO: retirer ces attributs à la fin des tests

	// METHODE DE TEST
	public void testMethod(int userId) {
		echange.setEtape(EtapeEchange.NEGOCIATION);
		Utilisateur user = new Utilisateur();
		user.setId(userId);
		if (userId == 1) {
			echange.setHasvalidatedusera(true);
		}
		else{
			echange.setHasvalidateduserb(true);
		}
		valider(user);
	}

	@PostConstruct
	public void init() {
		// TODO: a modifier avec l'id de l'échange récupérée d'une page
		// précédente.
		echange = proxyEchange.GetEchange(IDECHANGE);
		userA = proxyEchange.GetUtilisateurA(echange);
		userB = proxyEchange.GetUtilisateurB(echange);
		rdvs = proxyEchange.getRdv(echange);
		objets = proxyEchange.getObjets(echange);
		
		List<Conclusionechange> conclus = proxyEchange.getConclusion(echange);
		if (conclus.size() > 0) {
			conclusion = conclus.get(0);
		} else {
			conclusion = null;
		}
		objetsA = proxyEchange.getObjetUserEchange(echange, userA);
		
		objetsB = proxyEchange.getObjetUserEchange(echange, userB);
		
		
		
        
        }
		
	

	// ETAPE 1 - INITIALISATION

	/**
	 * L'userB accepte l'échange : on passe à l'étape de négociation, les
	 * HasValidated passent à false, et la date de début de négociation est
	 * enregistrée.
	 * 
	 */
	private void accepterEchange() {
		echange.setDateDebutNegociation(new Date());
		echange.setHasvalidatedusera(false);
		echange.setHasvalidateduserb(false);
		echange.setEtape(EtapeEchange.NEGOCIATION);
		proxyEchange.majEchange(echange);
	}

	// ETAPE 2 - Negociation

	/**
	 * Un utilisateur ajoute ou enleve un objet de la négociation.
	 * 
	 * @param objet
	 *            l'objet ajouté/enlevé
	 * @param ajout
	 *            si true: ajout, sinon, retrait.
	 */
	public void majNegociation(Objet objet, boolean ajout) {
		resetValidations();
		if (ajout) {
			proxyEchange.ajouterObjet(objet, echange);
		} else {
			proxyEchange.retirerObjet(objet, echange);
		}
	}
	public  void getObjetsEchangeUser(Utilisateur utilisateurA){
		List<Objet> objetsUser = null;
		for (Objet objet : objets){
			if (objet.getUtilisateur().getId() == utilisateurA.getId()){
				objetsA.add(objet);
			}else objetsB.add(objet);
		}
		
		
		}
		
	public Echange ajouterObjet (Objet ajout){
//		 Objet ajout = new Objet();
//		 ajout.setId(objetId);
		
		return proxyEchange.ajouterObjet(ajout, echange);
		
	}
	
	private Echange retirerObjet(Objet retrait) {
		return proxyEchange.retirerObjet(retrait, echange);

	}
	/**
	 * Modification du nombre de sapins proposés. La valeur proposée est
	 * enregistrée pour l'uilisateur A. Du coup, si c'est B qui propose, cette
	 * valeur est négative.
	 * 
	 * @param valeur
	 *            le montant proposé
	 * @param user
	 *            l'utilisateur qui propose
	 */
	public void majNegociation(int valeur, Utilisateur user) {
		if (user.getId() == userA.getId()) {
			echange.setValeur(valeur);
		} else {
			echange.setValeur(-valeur);
		}
		resetValidations();
		proxyEchange.majEchange(echange);
	}
	
	private void conclureNegociation() {
		echange.setDateValidationNegociation(new Date());
		resetValidations();
		proxyEchange.majEchange(echange);
		
	}

	// METHODES GENERALES

	// TODO: construire la méthode au fur et à mesure.
	/**
	 * Méthode appelée quand un utilisateur valide le statut actuel de
	 * l'échange, à toutes les étapes. Si les deux ont validé, appelle la
	 * méthode pour passer à l'étape suivante.
	 * 
	 * @param user
	 *            l'utilisateur ayant validé.
	 */
	public void valider(Utilisateur user) {
		if (user.getId() == userA.getId()) {
			echange.setHasvalidatedusera(true);
		} else {
			echange.setHasvalidateduserb(true);
		}
		proxyEchange.majEchange(echange);

		if (echange.isHasvalidatedusera() && echange.isHasvalidateduserb()) {
			switch (echange.getEtape()) {
			case INITIALISATION:
				accepterEchange();
				break;
			case NEGOCIATION:
				conclureNegociation();
				break;
			case PRISE_RDV:
				accepterRDV();
				break;
			case RDV:
				// rien à faire
				break;
			case CONCLUSION_RDV:
				conclureRDV();
				break;
			case NOTATION:
				ConclureNotation();
				break;
			case TERMINE:
				//rien à faire
				break;
			}
		}
	}

	private void ConclureNotation() {
		// TODO Auto-generated method stub
		
	}

	private void conclureRDV() {
		// TODO Auto-generated method stub
		
	}

	private void accepterRDV() {
		// TODO Auto-generated method stub
		
	}

	private void resetValidations() {
		echange.setHasvalidatedusera(false);
		echange.setHasvalidateduserb(false);
	}

	
	
	
	
	// de l'affichage de l utilisateur 
	
	public int userMoyenne(Utilisateur utilisateur){
		return proxyUtilisateur.recupererMoyenne(utilisateur);
	}
	
	public int userAvis(Utilisateur utilisateur){
		return proxyUtilisateur.recupererNbAvis(utilisateur);
		
	}
	
	public int userXP(Utilisateur utilisateur){
		return proxyUtilisateur.recupererNbEchangesValide(utilisateur);
	}
		// calcul sur listes 
		
	public int totalValeur (List<Objet> laliste){
		int total = 0 ;
		
		for (Objet objet : laliste){
			total = total + objet.getValeur();
		}
		
		return total;
		
		
	}
	
	// Getters, Setters

	public IBusinessEchange getProxyEchange() {
		return proxyEchange;
	}

	public void setProxyEchange(IBusinessEchange proxyEchange) {
		this.proxyEchange = proxyEchange;
	}

	public Utilisateur getUserA() {
		return userA;
	}

	public void setUserA(Utilisateur userA) {
		this.userA = userA;
	}

	public Utilisateur getUserB() {
		return userB;
	}

	public void setUserB(Utilisateur userB) {
		this.userB = userB;
	}

	public Echange getEchange() {
		return echange;
	}

	public void setEchange(Echange echange) {
		this.echange = echange;
	}

	public List<Rdv> getRdvs() {
		return rdvs;
	}

	public void setRdvs(List<Rdv> rdvs) {
		this.rdvs = rdvs;
	}

	public Conclusionechange getConclusion() {
		return conclusion;
	}

	public void setConclusion(Conclusionechange conclusion) {
		this.conclusion = conclusion;
	}

	public List<Objet> getObjets() {
		return objets;
	}

	public void setObjets(List<Objet> objets) {
		this.objets = objets;
	}

	public List<Objet> getObjetsA() {
		return objetsA;
	}

	public void setObjetsA(List<Objet> objetsA) {
		this.objetsA = objetsA;
	}

	public List<Objet> getObjetsB() {
		return objetsB;
	}

	public void setObjetsB(List<Objet> objetsB) {
		this.objetsB = objetsB;
	}

	
	
}

package org.greenlist.controller;

import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessPanier;
import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Utilisateur;

@ManagedBean(name = "mbPanierSession")
@SessionScoped
public class PanierSessionMb {

	
	@EJB
	private IBusinessPanier proxyPanier;
	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessUtilisateur proxyUtilisateur;
	
	
	private Panier panierSession; 
	private Utilisateur utilisateurConnecte;
	
	
	
	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnect;
	
	public Panier panierCnx(){
		
		
		utilisateurConnecte = mbConnect.getUtilisateurConnecte();
		
		if (utilisateurConnecte != null){
			
			panierSession = proxyPanier.getPanierByUtilisateur(utilisateurConnecte);
			
		}else {
			if (panierSession == null ){
				
			panierSession = proxyPanier.getPanierInvite();
			}
		}
		return panierSession;
		
	}
	
	public void ajoutObjet(int idObjet){
		
		if (panierSession == null) {
			panierSession = panierCnx();
		}
		
		Objet ajout = new Objet();
		ajout.setId(idObjet);
		ajout = proxyObjet.getObjet(ajout);
		
		proxyPanier.ajouterObjetPanier(ajout, panierSession);
		
	}

	public IBusinessPanier getProxyPanier() {
		return proxyPanier;
	}

	public void setProxyPanier(IBusinessPanier proxyPanier) {
		this.proxyPanier = proxyPanier;
	}

	public IBusinessObjet getProxyObjet() {
		return proxyObjet;
	}

	public void setProxyObjet(IBusinessObjet proxyObjet) {
		this.proxyObjet = proxyObjet;
	}

	public IBusinessUtilisateur getProxyUtilisateur() {
		return proxyUtilisateur;
	}

	public void setProxyUtilisateur(IBusinessUtilisateur proxyUtilisateur) {
		this.proxyUtilisateur = proxyUtilisateur;
	}

	public Panier getPanierSession() {
		return panierSession;
	}

	public void setPanierSession(Panier panierSession) {
		this.panierSession = panierSession;
	}

	public Utilisateur getUtilisateurConnecte() {
		return utilisateurConnecte;
	}

	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}

	public UtilisateurManagedBean getMbConnect() {
		return mbConnect;
	}

	public void setMbConnect(UtilisateurManagedBean mbConnect) {
		this.mbConnect = mbConnect;
	}
	
	
	
	
}

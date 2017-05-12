package org.greenlist.controller;

import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Utilisateur;

@ManagedBean(name = "mbUtilisateur")
@SessionScoped
public class UtilisateurManagedBean {

	@EJB
	private IBusinessUtilisateur proxyUtilisateur;
	
	private String pseudo ;
	private String mdp ; 
	private Utilisateur utilisateurConnecte = null;
	private Panier panierUtilisateur = null ;

	public void seConnecter() {
	
	utilisateurConnecte = proxyUtilisateur.seConnecter(pseudo, mdp);
	
		if (utilisateurConnecte != null){
			ConfigurableNavigationHandler  nav =
					(ConfigurableNavigationHandler)
					FacesContext.getCurrentInstance()
					.getApplication()
					.getNavigationHandler();
			nav.performNavigation("/ajoutObjet.xhtml?faces-redirect=true");
		}else {
			//osef
		}
		
	}
	
	public void securePage(){
		if(utilisateurConnecte == null) {
			ConfigurableNavigationHandler  nav =
					(ConfigurableNavigationHandler)
					FacesContext.getCurrentInstance()
					.getApplication()
					.getNavigationHandler();
			nav.performNavigation("/connexion.xhtml?faces-redirect=true");
		}
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	
	public String getMdp() {
		return mdp;
	}
	
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public Utilisateur getUtilisateurConnecte() {
		return utilisateurConnecte;
	}

	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}
	
	
}

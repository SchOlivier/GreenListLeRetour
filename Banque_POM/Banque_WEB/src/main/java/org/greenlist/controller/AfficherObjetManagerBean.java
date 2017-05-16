package org.greenlist.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Photo;
import org.greenlist.entity.Utilisateur;

@ManagedBean(name = "mbFicheObjet")
@ViewScoped
public class AfficherObjetManagerBean {

	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessUtilisateur proxyUtilisateur;

	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnect;
	
	private Objet objetAffiche = new Objet();
	private List<Photo> photos;
	private Utilisateur proprietaire = new Utilisateur();
	private int moyenne = 0;
	private int nbAVis = 0;
	private int nbEchanges = 0;


	public void init() {		 
		
		int cntxtId = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
		
		objetAffiche.setId(cntxtId);
		objetAffiche = proxyObjet.getObjetComplet(objetAffiche);
		
		System.out.println("valeur : " +objetAffiche.getValeur());

		photos = proxyObjet.getPhotos(objetAffiche);
		
		proprietaire= objetAffiche.getUtilisateur();
		
		moyenne = proxyUtilisateur.recupererMoyenne(proprietaire);
		nbAVis = proxyUtilisateur.recupererNbAvis(proprietaire);
		nbEchanges = proxyUtilisateur.recupererNbEchangesValide(proprietaire);
		

	}

	

	public String creerEchange(){
		Utilisateur userConnecte = mbConnect.getUtilisateurConnecte();
		Echange echange = proxyObjet.creerEchange(objetAffiche, userConnecte, proprietaire);
		
		String nav ="/echange.xhtml?faces-redirect=true&idE="+ echange.getId() ;
		return nav;
		
	}
	
	
	public Objet getObjetAffiche() {
		return objetAffiche;
	}

	public void setObjetAffiche(Objet objetAffiche) {
		this.objetAffiche = objetAffiche;
	}

	public IBusinessObjet getProxyObjet() {
		return proxyObjet;
	}

	public void setProxyObjet(IBusinessObjet proxyObjet) {
		this.proxyObjet = proxyObjet;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public IBusinessUtilisateur getProxyUtilisateur() {
		return proxyUtilisateur;
	}

	public void setProxyUtilisateur(IBusinessUtilisateur proxyUtilisateur) {
		this.proxyUtilisateur = proxyUtilisateur;
	}

	public Utilisateur getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(Utilisateur proprietaire) {
		this.proprietaire = proprietaire;
	}

	public int getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(int moyenne) {
		this.moyenne = moyenne;
	}

	public int getNbAVis() {
		return nbAVis;
	}

	public void setNbAVis(int nbAVis) {
		this.nbAVis = nbAVis;
	}

	public int getNbEchanges() {
		return nbEchanges;
	}

	public void setNbEchanges(int nbEchanges) {
		this.nbEchanges = nbEchanges;
	}



	public UtilisateurManagedBean getMbConnect() {
		return mbConnect;
	}



	public void setMbConnect(UtilisateurManagedBean mbConnect) {
		this.mbConnect = mbConnect;
	}


	
}

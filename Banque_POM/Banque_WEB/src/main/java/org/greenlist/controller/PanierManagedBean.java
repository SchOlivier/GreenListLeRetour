package org.greenlist.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessPanier;
import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Panier;
import org.greenlist.entity.Photo;
import org.greenlist.entity.Utilisateur;

@ManagedBean(name = "mbPanier")
@ViewScoped
public class PanierManagedBean {
	
	private Panier panier;
	private List<Objet> objets;
	private Objet objet = new Objet();
	private Photo photo;
	private Utilisateur proprietaire = new Utilisateur();
	private int moyenne = 0;
	private int nbAVis = 0;
	
	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnect;
	
	@ManagedProperty(value="#{mbPanierSession}")
	private PanierSessionMb mbPanierSession;
	
	@EJB
	private IBusinessPanier proxyPanier;
	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessUtilisateur proxyUtilisateur;
	
	
	@PostConstruct
	public void init(){
		panier = mbPanierSession.panierCnx();
		objets = proxyPanier.getObetsByPanier(panier);
		for(Objet objet : objets){
		objet = proxyObjet.getObjetComplet(objet);
		//photo = proxyObjet.getPhotos(objet).get(0);
		proprietaire= objet.getUtilisateur();
		moyenne = proxyUtilisateur.recupererMoyenne(proprietaire);
		nbAVis = proxyUtilisateur.recupererNbAvis(proprietaire);
		}
	}
	
	
	public Panier getPanier() {
		return panier;
	}


	public void setPanier(Panier panier) {
		this.panier = panier;
	}


	public List<Objet> getObjets() {
		return objets;
	}


	public void setObjets(List<Objet> objets) {
		this.objets = objets;
	}
	

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
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


	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public UtilisateurManagedBean getMbConnect() {
		return mbConnect;
	}

	public void setMbConnect(UtilisateurManagedBean mbConnect) {
		this.mbConnect = mbConnect;
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


	public PanierSessionMb getMbPanierSession() {
		return mbPanierSession;
	}


	public void setMbPanierSession(PanierSessionMb mbPanierSession) {
		this.mbPanierSession = mbPanierSession;
	}

	
	
}

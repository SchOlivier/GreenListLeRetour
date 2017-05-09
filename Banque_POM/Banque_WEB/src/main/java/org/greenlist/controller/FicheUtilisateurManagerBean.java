package org.greenlist.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Utilisateur;

/**
 * @author markus
 *
 */
@ManagedBean(name = "mbFicheUtilisateur")
@ViewScoped
public class FicheUtilisateurManagerBean {
	@EJB
	private IBusinessUtilisateur proxyUtilisateur ;
	
	private Utilisateur utilisateurAffiche = new Utilisateur();
	private int moyenne = 0;
	private int nbAVis = 0 ;
	private int nbObjets = 0;
	private int nbSouhaits = 0;
	private int nbEchanges =  0 ;
	
	
	@PostConstruct
	public void init(){
		
	
			utilisateurAffiche = proxyUtilisateur.getUtilisateurCompletById(utilisateurAffiche);
		
		nbEchanges = proxyUtilisateur.recupererNbEchangesValide(utilisateurAffiche);	
		nbSouhaits = proxyUtilisateur.recupererNbSouhaits(utilisateurAffiche);
		nbObjets = proxyUtilisateur.recupererNbObjets(utilisateurAffiche);
		moyenne = proxyUtilisateur.recupererMoyenne(utilisateurAffiche);
		nbAVis = proxyUtilisateur.recupererNbAvis(utilisateurAffiche);
		
	}


	public IBusinessUtilisateur getProxyUtilisateur() {
		return proxyUtilisateur;
	}


	public void setProxyUtilisateur(IBusinessUtilisateur proxyUtilisateur) {
		this.proxyUtilisateur = proxyUtilisateur;
	}


	public Utilisateur getUtilisateurAffiche() {
		return utilisateurAffiche;
	}


	public void setUtilisateurAffiche(Utilisateur utilisateurAffiche) {
		this.utilisateurAffiche = utilisateurAffiche;
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


	public int getNbObjets() {
		return nbObjets;
	}


	public void setNbObjets(int nbObjets) {
		this.nbObjets = nbObjets;
	}


	public int getNbSouhaits() {
		return nbSouhaits;
	}


	public void setNbSouhaits(int nbSouhaits) {
		this.nbSouhaits = nbSouhaits;
	}


	public int getNbEchanges() {
		return nbEchanges;
	}


	public void setNbEchanges(int nbEchanges) {
		this.nbEchanges = nbEchanges;
	}
	
	
	
}
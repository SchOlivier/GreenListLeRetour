package org.greenlist.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.greenlist.business.api.IBusinessDomaine;
import org.greenlist.business.api.IBusinessGroupe;
import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessProduit;
import org.greenlist.business.api.IBusinessTrancheAge;
import org.greenlist.entity.Domaine;
import org.greenlist.entity.Groupe;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Produit;
import org.greenlist.entity.TrancheAge;

@ManagedBean(name = "mbAfficheRefObjetSOuhait")
@SessionScoped
public class AffichageRefObjetSOuhait {

	@EJB
	private IBusinessTrancheAge proxyTrancheAge;
	@EJB
	private IBusinessProduit proxyProduit;
	@EJB
	private IBusinessGroupe proxyGroupe;
	@EJB
	private IBusinessDomaine proxyDomaine;
	
	private Groupe selectedGroupe = new Groupe();
	private List<TrancheAge> tranchesAges = null;
	private List<Produit> produits = null ;
	private List<Groupe> groupes = null  ;
	private Domaine domaine = null;
	private List<Domaine> domaines =null ;
	
	@PostConstruct
	public void init(){
		domaines = proxyDomaine.getDomaines();
	}
	
	
//
//	public List<Groupe> getgroupes(Domaine domaine) {
//		if (groupes == null ){
//			groupes = new ArrayList<>();
//			
//		}
//		groupes =proxyGroupe.getGroupes(domaine);
//		return groupes;
//	}
	
	public void rechercherProduits() {
		if (produits == null ){
			produits = new ArrayList<>();
			
		}else 
		System.out.println(selectedGroupe);
		System.out.println(selectedGroupe.getId());
		produits = proxyProduit.getProduits(selectedGroupe);
		System.out.println(produits);
	}
	

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public List<TrancheAge> getTranchesAges() {
	
		if (tranchesAges == null ){
			tranchesAges = new ArrayList<>();
			
		
		}
		tranchesAges = proxyTrancheAge.getTranchesAges();
		return tranchesAges;
	}

	public void setTranchesAges(List<TrancheAge> tranchesAges) {
		this.tranchesAges = tranchesAges;
	}
	
	public void rechercherGroupes(int idDomaine){
		groupes = proxyDomaine.getGroupes(proxyDomaine.getDomaine(idDomaine));
		produits  = proxyProduit.getProduits(groupes.get(0));
	}


	public List<Groupe> getGroupes() {
		return groupes;
	}


	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}


	public Domaine getDomaine() {
		return domaine;
	}


	public void setDomaine(Domaine domaine) {
		this.domaine = domaine;
	}


	public List<Produit> getProduits() {
		return produits;
	}


	public Groupe getSelectedGroupe() {
		return selectedGroupe;
	}


	public void setSelectedGroupe(Groupe selectedGroupe) {
		this.selectedGroupe = selectedGroupe;
	}


	public void setProxyTrancheAge(IBusinessTrancheAge proxyTrancheAge) {
		this.proxyTrancheAge = proxyTrancheAge;
	}


	public void setProxyProduit(IBusinessProduit proxyProduit) {
		this.proxyProduit = proxyProduit;
	}


	public void setProxyGroupe(IBusinessGroupe proxyGroupe) {
		this.proxyGroupe = proxyGroupe;
	}


	public void setProxyDomaine(IBusinessDomaine proxyDomaine) {
		this.proxyDomaine = proxyDomaine;
	}


	public List<Domaine> getDomaines() {
		return domaines;
	}


	public void setDomaines(List<Domaine> domaines) {
		this.domaines = domaines;
	}
	
	
}

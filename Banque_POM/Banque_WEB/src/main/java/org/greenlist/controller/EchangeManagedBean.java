package org.greenlist.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.greenlist.business.api.IBusinessEchange;
import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Photo;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Utilisateur;

@ManagedBean(name = "mbEchange")
@SessionScoped
public class EchangeManagedBean {
	
	@EJB
	private IBusinessEchange proxyEchange;
	
	private Echange echange;
	private Utilisateur userA;
	private Utilisateur userB;
	private List<Rdv> rdvs;
	private Conclusionechange conclusion;
	
	
	
	private static final int IDECHANGE = 7;
	
	@PostConstruct
	public void init(){
		//TODO: a modifier avec l'id de l'échange récupérée d'une page précédente.
		echange = proxyEchange.GetEchange(IDECHANGE);
		System.out.println("j'ai récupéré l'échange");
		
		userA = proxyEchange.GetUtilisateurA(echange);
		userB = proxyEchange.GetUtilisateurB(echange);
		
		rdvs = proxyEchange.getRdv(echange);
		
		List<Photo> photos = userA.getObjets().get(0).getPhotos();
		
		if (photos == null){
			System.out.println("j'ai récupéré les photos mais il n'y en a pas");
		}
		else {
			System.out.println("j'ai plein de photos ! au moins " + photos.size());
		}
		
		List<Conclusionechange> conclus = proxyEchange.getConclusion(echange);
		if (conclus.size()>0){
			conclusion = conclus.get(0);
		}
		else{
			conclusion = null;
		}
		
		
//		userB = echange.getUtilisateurByIduserb();
//		System.out.println("j'ai recupéré userB");
//		
//		userA = echange.getUtilisateurByIdusera();
//		System.out.println("j'ai récupéré userA");
		
//		System.out.println(userB.getId());
	}

	//Getters, Setters
	
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
	

}

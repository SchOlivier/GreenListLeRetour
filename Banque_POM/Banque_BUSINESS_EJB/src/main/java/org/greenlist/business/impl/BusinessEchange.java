package org.greenlist.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.greenlist.business.api.IBusinessEchange;
import org.greenlist.data.api.IDaoEchange;
import org.greenlist.data.api.IDaoObjet;
import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Message;
import org.greenlist.entity.Note;								 
import org.greenlist.entity.Objet;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Utilisateur;

@Remote(IBusinessEchange.class)
@Stateless
public class BusinessEchange implements IBusinessEchange {


	@EJB
	private IDaoEchange proxyEchange;
	
	@EJB
	private IDaoObjet proxyObjet;

	// CRUD EChange 
	@Override
	public Echange creerEchange(Echange echange) {
		return proxyEchange.creerEchange(echange);
	}

	@Override
	public Echange GetEchange( int idEchange) {
		Echange echange = proxyEchange.GetEchange(idEchange);
		List<Objet> objCOmplet = echange.getObjets();
		
		

		for (Objet objet : objCOmplet){
			 proxyObjet.getObjetByIdWithProduitAndTA(objet.getId());
		}
		
		
		echange.setObjets(objCOmplet);
		
		
		return echange;
	}

	
	// partie Objets 
	
	@Override
	public List<Objet> getObjets(Echange echange) {
		
		List<Objet> objets = proxyEchange.getObjets(echange);
		
		for (Objet objet : objets){
		
			proxyObjet.getObjetByIdWithProduitAndTA(objet.getId());
		
			
		}
		
		return objets;
	}
	
	@Override
	public Echange ajouterObjet(Objet objet, Echange echange) {
		List<Objet> objets = proxyEchange.getObjets(echange);
		objets.add(objet);
		echange.setObjets(objets);
		proxyEchange.majEchange(echange);
		return echange;
	}

	@Override
	public Echange retirerObjet(Objet objet, Echange echange) {
		return proxyEchange.retirerObjet(objet, echange);
	}

	


	// partie RDV 
	
	@Override
	public List<Rdv> getRdv(Echange echange) {
		return proxyEchange.getRdv(echange);
	}

	@Override
	public Echange prendreRdv(Echange echange, Rdv rdv) {
		rdv.setEchange(echange);
							 
		proxyEchange.ajouterRdv(rdv);
		return echange;
	}

	@Override
	public Echange retirerRdv(Echange echange, Rdv rdv) {
		echange.setRdvs(proxyEchange.getRdv(echange));
		echange.getRdvs().remove(rdv);
		proxyEchange.majEchange(echange);
		return echange;
	}
	
	
// partie Message 

	@Override
	public List<Message> getMessages(Echange echange) {

		return proxyEchange.getMessages(echange);
	}
	@Override
	public List<Message> envoyerMessage(Echange echange, Message message) {
		echange.setMessages(proxyEchange.getMessages(echange));
		echange.getMessages().add(message);
		proxyEchange.majEchange(echange);
		return echange.getMessages();
	}

	
	// conclusion Echange 

	@Override
	public Echange conclureEchange(Echange echange, Conclusionechange conclusionechange) {
		echange.setConclusionechange(conclusionechange);
		proxyEchange.majEchange(echange);
		return echange;
		
	}

	@Override
	public Utilisateur GetUtilisateurA(Echange echange) {
		return proxyEchange.GetUtilisateurA(echange);
	}

	@Override
	public Utilisateur GetUtilisateurB(Echange echange) {
		return proxyEchange.GetUtilisateurB(echange);
	}

	@Override
	public Conclusionechange getConclusion(Echange echange) {
		return proxyEchange.getConclusion(echange);
	}

	@Override
	public Echange majEchange(Echange echange) {
		return proxyEchange.majEchange(echange);
	}

		  
											  
										
  

	public IDaoEchange getProxyEchange() {
								   
					   
		return proxyEchange;
  
	}

	public void setProxyEchange(IDaoEchange proxyEchange) {
															   
		this.proxyEchange = proxyEchange;
	}

	@Override
	public List<Objet> getObjetUserEchange(Echange echange, Utilisateur utilisateur) {
		
		return proxyEchange.getObjetUserEchange(echange,  utilisateur);
	}
@Override
	public List<Note> getNotes(Echange echange) {
		return proxyEchange.getNotes(echange);
	}

	@Override
	public void accepterRDV(Rdv rdv) {
		rdv.setAccepte(true);
		proxyEchange.majRdv(rdv);
		
	}

	@Override
	public Conclusionechange getConclusionById(int idConclusion) {
		return proxyEchange.getConclusionById(idConclusion);
	}

	@Override
	public Note noterEchange(Note note) {
  
		return proxyEchange.noterEchange(note);
	}

	
	
	
}

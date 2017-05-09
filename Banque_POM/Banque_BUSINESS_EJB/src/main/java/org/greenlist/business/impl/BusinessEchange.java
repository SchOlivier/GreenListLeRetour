package org.greenlist.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.greenlist.business.api.IBusinessEchange;
import org.greenlist.data.api.IDaoEchange;
import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Message;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Rdv;

@Remote(IBusinessEchange.class)
@Stateless
public class BusinessEchange implements IBusinessEchange {


	@EJB
	private IDaoEchange proxyEchange;

	// CRUD EChange 
	@Override
	public Echange creerEchange(Echange echange) {
		return proxyEchange.creerEchange(echange);
	}

	@Override
	public Echange GetEchange( Echange echange) {

		
		return proxyEchange.GetEchange(echange.getId());
	}

	
	// partie Objets 
	
	@Override
	public List<Objet> getObjets(Echange echange) {
		// TODO Auto-generated method stub
		return proxyEchange.getObjets(echange);
	}
	
	@Override
	public Echange ajouterObjet(Objet objet, Echange echange) {
		echange.setObjets(proxyEchange.getObjets(echange));
		echange.getObjets().add(objet);
		proxyEchange.majEchange(echange);
		return echange;
	}

	@Override
	public Echange retirerObjet(Objet objet, Echange echange) {
		echange.setObjets(proxyEchange.getObjets(echange));
		echange.getObjets().remove(objet);
		proxyEchange.majEchange(echange);
		return echange;
	}



	// partie RDV 
	
	@Override
	public List<Rdv> getRdv(Echange echange) {
		// TODO Auto-generated method stub
		return proxyEchange.getRdv(echange);
	}

	@Override
	public Echange prendreRdv(Echange echange, Rdv rdv) {
		echange.setRdvs(proxyEchange.getRdv(echange));
		echange.getRdvs().add(rdv);
		proxyEchange.majEchange(echange);
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


	
}

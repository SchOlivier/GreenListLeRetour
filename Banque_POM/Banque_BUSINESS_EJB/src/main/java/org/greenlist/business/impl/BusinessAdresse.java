package org.greenlist.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.greenlist.business.api.IBusinessAdresse;
import org.greenlist.data.api.IDaoAdresse;
import org.greenlist.entity.Adresse;
import org.greenlist.entity.Utilisateur;

@Remote(IBusinessAdresse.class)
@Stateless
public class BusinessAdresse implements IBusinessAdresse {

	@EJB
	private IDaoAdresse proxyAdresse;
	
	private Adresse adresse = null;

	@Override
	public List<Adresse> getAdresseByUtilisateur(Utilisateur utilisateur) {
		List<Adresse> adresses = null;
		try {
			adresses = proxyAdresse.getAdresseByUtilisateur(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adresses;
	}

	@Override
	public Adresse getAdresseByIdUtilisateur(int idUtilisateur) {
		try {
			adresse = proxyAdresse.getAdresseByIdUtilisateur(idUtilisateur);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return adresse;
	}

	//GETTERS AND SETTERS
	public IDaoAdresse getProxyAdresse() {
		return proxyAdresse;
	}

	public void setProxyAdresse(IDaoAdresse proxyAdresse) {
		this.proxyAdresse = proxyAdresse;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}


	
}
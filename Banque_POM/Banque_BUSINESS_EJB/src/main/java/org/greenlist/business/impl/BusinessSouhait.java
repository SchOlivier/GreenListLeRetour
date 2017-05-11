package org.greenlist.business.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.greenlist.business.api.IBusinessSouhait;
import org.greenlist.data.api.IDaoSouhait;
import org.greenlist.entity.Liste;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Produit;
import org.greenlist.entity.Souhait;
import org.greenlist.entity.Utilisateur;

@Remote(IBusinessSouhait.class)
@Stateless
public class BusinessSouhait implements IBusinessSouhait {

	@EJB
	private IDaoSouhait proxyDaoSouhait;

	@Override
	public List<Souhait> getSouhaits(Utilisateur utilisateur) {
		List<Souhait> user = null;
		try {
			user = proxyDaoSouhait.getSouhaits(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<Souhait> getSouhaits(Liste liste) {
		List<Souhait> user = null;
		try {
			user = proxyDaoSouhait.getSouhaits(liste);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public List<Souhait> getSouhaits(int idListe) {
		List<Souhait> user = null;
		try {
			user = proxyDaoSouhait.getSouhaits(idListe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<Liste> getListes(Utilisateur utilisateur) {
		List<Liste> list = null;
		try {
			list = proxyDaoSouhait.getListes(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Souhait getSouhait(Souhait souhait) {
		Souhait psouhait = null;
		try {
			psouhait = proxyDaoSouhait.getSouhait(souhait);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return psouhait;
	}

	@Override
	public Souhait addSouhait(Souhait souhait) {
		return proxyDaoSouhait.addSouhait(souhait);
	}

	@Override
	public Liste addListe(Liste liste) {
		return proxyDaoSouhait.addListe(liste);
	}
	
	public IDaoSouhait getProxyDaoSouhait() {
		return proxyDaoSouhait;
	}

	public void setProxyDaoSouhait(IDaoSouhait proxyDaoSouhait) {
		this.proxyDaoSouhait = proxyDaoSouhait;
	}

	@Override
	public List<Souhait> getSouhaits(Objet objet, Utilisateur utilisateur){
		List<Souhait> res = new ArrayList<>();
		try {
			res = proxyDaoSouhait.getSouhaits(objet, utilisateur);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return res;	
	}

}

package org.greenlist.business.api;

import java.util.List;

import org.greenlist.entity.Liste;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Souhait;
import org.greenlist.entity.Utilisateur;

public interface IBusinessSouhait {

	public List<Souhait> getSouhaits(Utilisateur utilisateur);

	public List<Souhait> getSouhaits(Liste liste);

	public List<Liste> getListes(Utilisateur utilisateur);

	public Souhait getSouhait(Souhait souhait);

	public Souhait addSouhait(Souhait souhait);

	public Liste addListe(Liste liste);

	List<Souhait> getSouhaits(int idListe);
	
	List<Souhait> getSouhaits(Objet objet, Utilisateur utilisateur);

}

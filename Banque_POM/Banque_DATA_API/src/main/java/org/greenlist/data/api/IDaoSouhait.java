package org.greenlist.data.api;

import java.util.List;

import org.greenlist.entity.Liste;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Souhait;
import org.greenlist.entity.Utilisateur;

public interface IDaoSouhait {
	
	public List<Souhait> getSouhaits(Utilisateur utilisateur) throws Exception;

	public List<Souhait> getSouhaits(Liste liste) throws Exception;

	public List<Liste> getListes(Utilisateur utilisateur) throws Exception;

	public Souhait getSouhait(Souhait souhait) throws Exception;

	public Souhait addSouhait(Souhait souhait);

	public Liste addListe(Liste liste);

	List<Souhait> getSouhaits(int idListe) throws Exception;

	List<Souhait> getSouhaits(Objet objet, Utilisateur utilisateur) throws Exception;
}

package org.greenlist.data.api;

import java.util.List;

import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Message;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Utilisateur;

public interface IDaoEchange {
	
	

	public Echange creerEchange (Echange echange);
	
	public Echange GetEchange ( int IdEchange);
	
	public Utilisateur GetUtilisateurA (Echange echange);
	
	public Utilisateur GetUtilisateurB (Echange echange);
	
	public Echange majEchange(Echange echange);
	
	public List<Objet> getObjets (Echange echange);
	
	public List<Message> getMessages (Echange echange);
	
	public List<Rdv> getRdv(Echange echange);
	
	public List<Conclusionechange> getConclusion (Echange echange);
	
	public Echange retirerObjet(Objet objet, Echange echange);

	public List<Objet> getObjetUserEchange(Echange echange, Utilisateur utilisateur);
	
	
	
}

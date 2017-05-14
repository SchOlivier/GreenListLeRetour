package org.greenlist.business.api;

import java.util.List;

import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Message;
import org.greenlist.entity.Note;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Utilisateur;

public interface IBusinessEchange {

	public Echange creerEchange(Echange echange);

	public Echange GetEchange(int idEchange);

	public Utilisateur GetUtilisateurA(Echange echange);

	public Utilisateur GetUtilisateurB(Echange echange);

	public List<Objet> getObjets(Echange echange);

	public List<Message> getMessages(Echange echange);

	public List<Message> envoyerMessage(Echange echange, Message message);

	public List<Rdv> getRdv(Echange echange);

	public Echange prendreRdv(Echange echange, Rdv rdv);

	public Echange retirerRdv(Echange echange, Rdv rdv);

	public Echange ajouterObjet(Objet objet, Echange echange);

	public Echange retirerObjet(Objet objet, Echange echange);

	public Echange conclureEchange(Echange echange, Conclusionechange conclusionechange);
	
	public Conclusionechange getConclusion (Echange echange);
	
	public Echange majEchange(Echange echange);

	public List<Note> getNotes(Echange echange);
	
	public void accepterRDV(Rdv rdv);
	
	public Conclusionechange getConclusionById (int idConclusion);
	
	public Note noterEchange (Note note);
}

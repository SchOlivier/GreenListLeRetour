package org.greenlist.data.api;

import java.util.List;

import org.greenlist.entity.Adresse;
//import org.greenlist.entity.Domaine;
//import org.greenlist.entity.Groupe;
//import org.greenlist.entity.Objet;
//import org.greenlist.entity.Produit;
import org.greenlist.entity.Utilisateur;

public interface IDaoAdresse {

	List<Adresse> getAdresseByUtilisateur(Utilisateur utilisateur) throws Exception;

	Adresse getAdresseByIdUtilisateur(int idUtilisateur) throws Exception;

}

package org.greenlist.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.greenlist.business.api.IBusinessEchange;
import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessUtilisateur;
import org.greenlist.entity.Adresse;
import org.greenlist.entity.Conclusionechange;
import org.greenlist.entity.Echange;
import org.greenlist.entity.Note;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Rdv;
import org.greenlist.entity.Utilisateur;
import org.greenlist.utilitaire.EtapeEchange;
import org.primefaces.util.DateUtils;

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
	private List<Objet> objets;
	private List<Note> notes;
	private Date dateRdv;

	private static final String TERMINE = "Termine";
	private static final String NOTATION = "Notation";

	// TODO: retirer ces attributs à la fin des tests
	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessUtilisateur proxyUser;

	private static final int IDECHANGE = 3;
	private boolean bidouille;

	// METHODE DE TEST
	public void testMethod(int i) {

		if (i==1){
			proposerRDV(userA);
		}
		else{
			valider(userB);
		}
	}
	
	public void afficherEtape(){
		calculerEtape();
		System.out.println(echange.getEtape().getLibelle());
	}

	@PostConstruct
	public void init() {

		recupereDonnees();
		calculerEtape();
	}

	private void recupereDonnees() {
		// TODO: a modifier avec l'id de l'échange récupérée d'une page
		// précédente.
		echange = proxyEchange.GetEchange(IDECHANGE);
		userA = proxyEchange.GetUtilisateurA(echange);
		userB = proxyEchange.GetUtilisateurB(echange);
		rdvs = proxyEchange.getRdv(echange);
		objets = echange.getObjets();
		notes = proxyEchange.getNotes(echange);
		List<Conclusionechange> conclus = proxyEchange.getConclusion(echange);
		if (conclus.size() > 0) {
			conclusion = conclus.get(0);
		} else {
			conclusion = null;
		}
	}

	private void calculerEtape() {
		Date dateDuJour = new Date();

		if (conclusion != null && conclusion.getLibelle().equals(TERMINE)) {
			echange.setEtape(EtapeEchange.TERMINE);
		} else if (conclusion != null && conclusion.getLibelle().equals(NOTATION)) {
			echange.setEtape(EtapeEchange.NOTATION);
		} else if (rdvs.size() > 0 && rdvs.get(0).getDate().before(dateDuJour)) {
			echange.setEtape(EtapeEchange.CONCLUSION_RDV);
		} else if (rdvs.size() > 0 && rdvs.get(0).getDate().after(dateDuJour)) {
			echange.setEtape(EtapeEchange.RDV);
		} else if (echange.getDateValidationNegociation() != null) {
			echange.setEtape(EtapeEchange.PRISE_RDV);
		} else if (echange.getDateDebutNegociation() != null) {
			echange.setEtape(EtapeEchange.NEGOCIATION);
		} else {
			echange.setEtape(EtapeEchange.INITIALISATION);
		}
	}

	// ETAPE 1 - INITIALISATION

	/**
	 * L'userB accepte l'échange : on passe à l'étape de négociation, les
	 * HasValidated passent à false, et la date de début de négociation est
	 * enregistrée.
	 * 
	 */
	private void accepterEchange() {
		echange.setDateDebutNegociation(new Date());
		echange.setEtape(EtapeEchange.NEGOCIATION);
		proxyEchange.majEchange(echange);
	}

	// ETAPE 2 - Negociation

	/**
	 * Un utilisateur ajoute ou enleve un objet de la négociation.
	 * 
	 * @param objet
	 *            l'objet ajouté/enlevé
	 * @param ajout
	 *            si true: ajout, sinon, retrait.
	 */
	public void majNegociation(Objet objet, boolean ajout) {
		resetValidations();
		if (ajout) {
			proxyEchange.ajouterObjet(objet, echange);
		} else {
			proxyEchange.retirerObjet(objet, echange);
		}
	}

	/**
	 * Modification du nombre de sapins proposés. La valeur proposée est
	 * enregistrée pour l'uilisateur A. Du coup, si c'est B qui propose, cette
	 * valeur est négative.
	 * 
	 * @param valeur
	 *            le montant proposé
	 * @param user
	 *            l'utilisateur qui propose
	 */
	public void majNegociation(int valeur, Utilisateur user) {
		if (user.getId() == userA.getId()) {
			echange.setValeur(valeur);
		} else {
			echange.setValeur(-valeur);
		}
		resetValidations();
		proxyEchange.majEchange(echange);
	}

	private void conclureNegociation() {
		echange.setDateValidationNegociation(new Date());
		echange.setEtape(EtapeEchange.PRISE_RDV);
		proxyEchange.majEchange(echange);

	}

	// ETAPE 3 - Prise de RDV

	public void proposerRDV(Utilisateur user) {
		// Adresse en dur parce que j'ai pas envie de me faire chier, ce sera
		// celle de l'userA.
		Rdv rdv = new Rdv();
		rdv.setAdresse(userA.getAdresses().get(0));
		rdv.setDate(dateRdv);
		proxyEchange.prendreRdv(echange, rdv);
		// on annule l'éventuelle validation de l'autre user.
		if (user.getId() == userA.getId()) {
			echange.setHasvalidateduserb(false);
		} else {
			echange.setHasvalidatedusera(false);
		}
		valider(user);
	}

	// METHODES GENERALES

	// TODO: construire la méthode au fur et à mesure.
	/**
	 * Méthode appelée quand un utilisateur valide le statut actuel de
	 * l'échange, à toutes les étapes. Si les deux ont validé, appelle la
	 * méthode pour passer à l'étape suivante.
	 * 
	 * @param user
	 *            l'utilisateur ayant validé.
	 */
	public void valider(Utilisateur user) {
		if (user.getId() == userA.getId()) {
			echange.setHasvalidatedusera(true);
		} else {
			echange.setHasvalidateduserb(true);
		}
		proxyEchange.majEchange(echange);

		if (echange.isHasvalidatedusera() && echange.isHasvalidateduserb()) {
			resetValidations();
			switch (echange.getEtape()) {
			case INITIALISATION:
				accepterEchange();
				break;
			case NEGOCIATION:
				conclureNegociation();
				break;
			case PRISE_RDV:
				accepterRDV();
				break;
			case RDV:
				// rien à faire
				break;
			case CONCLUSION_RDV:
				conclureRDV();
				break;
			case NOTATION:
				ConclureNotation();
				break;
			case TERMINE:
				// rien à faire
				break;
			}
			System.out.println(echange.getEtape().getLibelle());
		}
	}

	private void ConclureNotation() {
		// TODO Auto-generated method stub

	}

	private void conclureRDV() {
		// TODO Auto-generated method stub

	}

	private void accepterRDV() {
		if (dateRdv.after(new Date())) {
			echange.setEtape(EtapeEchange.RDV);
		}
		else{
			echange.setEtape(EtapeEchange.CONCLUSION_RDV);
		}

	}

	private void resetValidations() {
		echange.setHasvalidatedusera(false);
		echange.setHasvalidateduserb(false);
	}

	// Getters, Setters

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

	public List<Objet> getObjets() {
		return objets;
	}

	public void setObjets(List<Objet> objets) {
		this.objets = objets;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Date getDateRdv() {
		return dateRdv;
	}

	public void setDateRdv(Date dateRdv) {
		this.dateRdv = dateRdv;
	}

}

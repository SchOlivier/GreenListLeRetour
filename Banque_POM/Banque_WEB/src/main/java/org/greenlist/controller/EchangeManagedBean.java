package org.greenlist.controller;

						   
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;					 
						   

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.greenlist.business.api.IBusinessAdresse;
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
import org.greenlist.utilitaire.Conclusion;
import org.greenlist.utilitaire.EtapeEchange;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name = "mbEchange")
@SessionScoped
public class EchangeManagedBean {

	@EJB
	private IBusinessEchange proxyEchange;
	@EJB
	private IBusinessUtilisateur proxyUtilisateur;
	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessAdresse proxyAdresse;
	
	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnect;

	private Echange echange;
	private Utilisateur userA;
	private Utilisateur userB;
	private List<Rdv> rdvs;
	private Conclusionechange conclusion;
	private List<Objet> objets;
	private List<Objet> objetsMoi;
	private List<Objet> objetsAutre;
	private List<Note> notes;
	private int Sapins;
	private Date dateRdv;
	private int noteA;
	private String appreciationA;
	private int noteB;
	private String appreciationB;
	private List<Adresse> adresses;
	private Adresse adresseProposee;
	private Date dateProposee;
	private MapModel simpleModel;
	private Utilisateur moi;
	private Utilisateur autre;
	private boolean hasValidatedMoi;
	private boolean hasValidatedAutre;	
	private int floconsProposesMoi;
	private int floconsProposesAutre;
	private int propositionFloconsGauche;
	private int propositionFloconsDroite;
	
	private static final String PAGE_INITIALISATION = "testInitialisation.xhtml";
	private static final String PAGE_NEGOCIATION = "echangeEnCours.xhtml";
	private static final String PAGE_PRISE_RDV = "testPriseRdv.xhtml";
	private static final String PAGE_RDV = "testRdv.xhtml";
	private static final String PAGE_CONCLUSION_RDV = "testConclusionRdv.xhtml";
	private static final String PAGE_NOTATION = "testNotation.xhtml";
	private static final String PAGE_FIN = "testFin.xhtml";
	private static final String FACES_REDIRECT = "?faces-redirect=true";																	

	// TODO: retirer ces attributs à la fin des tests
	 private static int IDECHANGE = 2;
			 
				  
	// METHODE DE TEST
	public void testMethod(int userId) {
		
	}

	@PostConstruct
	public void init() {
		calculerEtape();
	}

	// ETAPE 1 - INITIALISATION

	/**
	 * L'userB accepte l'échange : on passe à l'étape de négociation, les
	 * HasValidated passent à false, et la date de début de négociation est
	 * enregistrée.
	 * Méthode privée : appelée via valider(userB).
	 */
	private void accepterEchange() {
		System.out.println("je rendre dans accepterEchange");
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

	public void getObjetsEchangeUser(Utilisateur utilisateurA) {
		List<Objet> objetsUser = null;
		for (Objet objet : objets) {
			if (objet.getUtilisateur().getId() == utilisateurA.getId()) {
				objetsMoi.add(objet);
			} else
				objetsAutre.add(objet);
		}

	}

	public void ajouterObjet(Objet ajout) {
		// Objet ajout = new Objet();
		// ajout.setId(objetId);

		System.out.println(ajout);
		System.out.println(ajout.getLibelle());
		proxyEchange.ajouterObjet(ajout, echange);

		init();
	}

	public void retirerObjet(Objet retrait) {
		System.out.println(retrait);
		System.out.println(retrait.getLibelle());
		proxyEchange.retirerObjet(retrait, echange);
		init();
	}

	public void cribleListes(List<Objet> userGetListe){
	Iterator<Objet> i = userGetListe.iterator() ;
    while (i.hasNext()){
    	Objet obj = i.next();
    	for (Objet objet : objets){
    		if (objet.getId() == obj.getId()){
    			i.remove();
    		}
    	}
        
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
	public void majNegociation() {
		
		if (propositionFloconsDroite > 0) {
			if (autre.getId() == userA.getId()){
				echange.setValeur(propositionFloconsDroite);
			}
			else{
				echange.setValeur(-propositionFloconsDroite);
			}
		} else if (propositionFloconsGauche > 0){
			if (autre.getId() == userA.getId()){
				echange.setValeur(propositionFloconsGauche);
			}
			else{
				echange.setValeur(-propositionFloconsGauche);
			}
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

	public void proposerRDV() {
		System.out.println("je rentre dans la propal");	
		// je m'en suis pas sorti avec l'adresse, elle sera en dur, ce sera l'adresse 1 de l'userA.
		Rdv rdv = new Rdv();
		Adresse a = userA.getAdresses().get(0);
		rdv.setAdresse(a);
		rdv.setDate(dateRdv);
		System.out.println("Date du rdv : " + dateRdv);
		proxyEchange.prendreRdv(echange, rdv);
		rdvs = proxyEchange.getRdv(echange);
		dateProposee = rdvs.get(0).getDate();
		adresseProposee = rdvs.get(0).getAdresse();
		// on annule l'éventuelle validation de l'autre user.
		resetValidationAutreUser();
		valider();
	}

	private void resetValidationAutreUser() {
		if (moi.getId() == userA.getId()) {
			echange.setHasvalidateduserb(false);
		} else {
			echange.setHasvalidatedusera(false);
		}

	}

	// ETAPE 4 - RDV

	// Rien à faire

	// ETAPE 5 - Conclusion RDV

	/**
	 * Enregistrement de la conclusion de l'échange. Ce sera systématiquement la
	 * conclusion "l'échange a eu lieu tel quel". Les autes options seront là
	 * pour faire joli.
	 */
	public void enregistrerConclusion() {
		echange.setConclusionechange(proxyEchange.getConclusionById(Conclusion.NOTATION.getIdConclusion()));
		proxyEchange.majEchange(echange);
		resetValidationAutreUser();
		valider();
	}

	// ETAPE 6 - Notation

	/**
	 * Notation de l'échange par un utilisateur
	 * 
	 * @param user
	 *            l'utilisateur effectuant la notation
	 */
	public void Noter() {
		Note note = new Note();
		if (moi.getId() == userA.getId()) {
			note.setEchange(echange);
			note.setUtilisateurByIdutilisateurestnote(userB);
			note.setUtilisateurByIdutilisateurnote(userA);
			note.setAppreciation(appreciationA);
			note.setNote(noteA);
		} else {
			note.setEchange(echange);
			note.setUtilisateurByIdutilisateurestnote(userA);
			note.setUtilisateurByIdutilisateurnote(userB);
			note.setAppreciation(appreciationB);
			note.setNote(noteB);
		}
		proxyEchange.noterEchange(note);
		valider();
	}
																	 
	// METHODES GENERALES
	private void razDonnees() {
		echange = new Echange();
		userA = new Utilisateur();
		userB = new Utilisateur();
		rdvs = new ArrayList<>();
		objets = new ArrayList<>();
		notes = new ArrayList<>();
		conclusion = new Conclusionechange();
		adresses = new ArrayList<>();
		dateProposee = null;
		adresseProposee = new Adresse();
		simpleModel = new DefaultMapModel();
		objetsMoi = new ArrayList<>();
		objetsAutre = new ArrayList<>();
		moi = new Utilisateur();
		autre = new Utilisateur();
		floconsProposesMoi = 0;
		floconsProposesAutre = 0;
	}

	private void recupereDonnees() {
		// TODO: a modifier avec l'id de l'échange récupérée d'une page
		// précédente.
		
		echange = proxyEchange.GetEchange(IDECHANGE);
		if (echange.getValeur()>0){
			floconsProposesMoi = echange.getValeur();
			propositionFloconsGauche = floconsProposesMoi;
			floconsProposesAutre = 0;
		}
		else{
			floconsProposesAutre = -echange.getValeur();
			propositionFloconsDroite = floconsProposesAutre;
			floconsProposesMoi = 0;
		}
		userA = proxyEchange.GetUtilisateurA(echange);
		
		userB = proxyEchange.GetUtilisateurB(echange);
		
		getObjetsEchangeUser(userA);
		moi = mbConnect.getUtilisateurConnecte();
		if (moi.getId() == userA.getId()){
			autre = userB;
			moi = userA;
			hasValidatedMoi = echange.isHasvalidatedusera();
			hasValidatedAutre = echange.isHasvalidateduserb();
		}
		else{
			autre = userA;
			moi = userB;
			hasValidatedMoi = echange.isHasvalidateduserb();
			hasValidatedAutre = echange.isHasvalidatedusera();
		}
		
		
		rdvs = proxyEchange.getRdv(echange);
					   
		objets = echange.getObjets();
		cribleListes(userA.getObjets());
		cribleListes(userB.getObjets());
		notes = proxyEchange.getNotes(echange);
		conclusion = proxyEchange.getConclusion(echange);
		adresses.addAll(proxyAdresse.getAdresseByUtilisateur(userA));
		adresses.addAll(proxyAdresse.getAdresseByUtilisateur(userB));
		if (rdvs.size()>0){
			dateProposee = rdvs.get(0).getDate();
			adresseProposee = rdvs.get(0).getAdresse();
			LatLng coord = new LatLng(adresseProposee.getLatitude(), adresseProposee.getLongitude());
			simpleModel.addOverlay(new Marker(coord, "Adresse proposée"));
		} else {
			dateProposee = null;
			adresseProposee.setNumeroVoie("");
			adresseProposee.setRue("");
			adresseProposee.setVille("");
		}
		objetsMoi = proxyEchange.getObjetUserEchange(echange, moi);

		objetsAutre = proxyEchange.getObjetUserEchange(echange, autre);
	}

	/**
	 * 1 - Remet à zéro puis récupère toutes les données de l'échange 2 -
	 * détermine l'étape en cours 3 - A FAIRE : re-dirige vers la bonne page
	 * d'échange.
	 */
	private void calculerEtape() {
		razDonnees();
		recupereDonnees();

		Date dateDuJour = new Date();
		
		if (conclusion != null && conclusion.getId() == Conclusion.TERMINE.getIdConclusion()) {
			echange.setEtape(EtapeEchange.TERMINE);
		} else if (conclusion != null && conclusion.getId() == Conclusion.NOTATION.getIdConclusion()) {
			echange.setEtape(EtapeEchange.NOTATION);
		} else if (rdvs.size() > 0 && rdvs.get(0).isAccepte() && rdvs.get(0).getDate().before(dateDuJour)) {
			echange.setEtape(EtapeEchange.CONCLUSION_RDV);
		} else if (rdvs.size() > 0 && rdvs.get(0).isAccepte() && rdvs.get(0).getDate().after(dateDuJour)) {
			echange.setEtape(EtapeEchange.RDV);
		} else if (echange.getDateValidationNegociation() != null) {
			echange.setEtape(EtapeEchange.PRISE_RDV);
		} else if (echange.getDateDebutNegociation() != null) {
			echange.setEtape(EtapeEchange.NEGOCIATION);
		} else {
			echange.setEtape(EtapeEchange.INITIALISATION);
		}

		Navigation();
	}
	
	private void Navigation() {
		ConfigurableNavigationHandler  nav =
				(ConfigurableNavigationHandler)
				FacesContext.getCurrentInstance()
				.getApplication()
				.getNavigationHandler();
		switch(echange.getEtape()){
		case INITIALISATION:
//			nav.performNavigation(PAGE_INITIALISATION + FACES_REDIRECT);
			break;
		case NEGOCIATION:
//			nav.performNavigation(PAGE_NEGOCIATION + FACES_REDIRECT);
			break;
		case PRISE_RDV:
//			nav.performNavigation(PAGE_PRISE_RDV + FACES_REDIRECT);
			break;
		case RDV:
//			nav.performNavigation(PAGE_RDV + FACES_REDIRECT);
			break;
		case CONCLUSION_RDV:
//			nav.performNavigation(PAGE_CONCLUSION_RDV+ FACES_REDIRECT);
			break;
		case NOTATION:
//			nav.performNavigation(PAGE_NOTATION + FACES_REDIRECT);
			break;
		case TERMINE:
			nav.performNavigation(PAGE_FIN + FACES_REDIRECT);
			break;
		}

	}

	/**
	 * Méthode appelée quand un utilisateur valide le statut actuel de
	 * l'échange, à toutes les étapes. Si les deux ont validé, appelle la
	 * méthode pour passer à l'étape suivante.
					   
	 * 
	 * @param user
	 *            l'utilisateur ayant validé.
	 */
	public void valider() {
		if (moi.getId() == userA.getId()) {
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
			calculerEtape();
		}
	}

	private void ConclureNotation() {
		echange.setEtape(EtapeEchange.TERMINE);
		Conclusionechange conclu = proxyEchange.getConclusionById(Conclusion.TERMINE.getIdConclusion());
		proxyEchange.conclureEchange(echange, conclu);
	}

	private void conclureRDV() {
		echange.setDateConclusion(new Date());
		echange.setDateEchange(rdvs.get(0).getDate());
		proxyEchange.majEchange(echange);
		echange.setEtape(EtapeEchange.NOTATION);

	}

	private void accepterRDV() {
		if (dateRdv.after(new Date())) {
			proxyEchange.accepterRDV(rdvs.get(0));
			echange.setEtape(EtapeEchange.RDV);
		} else {
			proxyEchange.accepterRDV(rdvs.get(0));
			echange.setEtape(EtapeEchange.CONCLUSION_RDV);
		}

	}

	private void resetValidations() {
		echange.setHasvalidatedusera(false);
		echange.setHasvalidateduserb(false);
	}

	// de l'affichage de l utilisateur

	public int userMoyenne(Utilisateur utilisateur) {
		return proxyUtilisateur.recupererMoyenne(utilisateur);
	}

	public int userAvis(Utilisateur utilisateur) {
		return proxyUtilisateur.recupererNbAvis(utilisateur);

	}

	public int userXP(Utilisateur utilisateur) {
		return proxyUtilisateur.recupererNbEchangesValide(utilisateur);
	}
	// calcul sur listes

	public int totalValeur(List<Objet> laliste, Utilisateur user) {
		int total = 0;

		for (Objet objet : laliste) {
			total = total + objet.getValeur();
		}
		
		if(user.getId() == moi.getId()){
			total += floconsProposesMoi;
		}else{
			total += floconsProposesAutre;
		}

		return total;

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

	public int getNoteA() {
		return noteA;
	}

	public void setNoteA(int noteA) {
		this.noteA = noteA;
	}

	public String getAppreciationA() {
		return appreciationA;
	}

	public void setAppreciationA(String appreciationA) {
		this.appreciationA = appreciationA;
	}

	public int getNoteB() {
		return noteB;
	}

	public void setNoteB(int noteB) {
		this.noteB = noteB;
	}

	public String getAppreciationB() {
		return appreciationB;
	}

	public void setAppreciationB(String appreciationB) {
		this.appreciationB = appreciationB;
	}

	public int getSapins() {
		return Sapins;
	}

	public void setSapins(int sapins) {
		Sapins = sapins;
	}

	public Adresse getAdresseProposee() {
		return adresseProposee;
	}

	public void setAdresseProposee(Adresse adresseProposee) {
		this.adresseProposee = adresseProposee;
	}

	public Date getDateProposee() {
		return dateProposee;
	}

	public void setDateProposee(Date dateProposee) {
		this.dateProposee = dateProposee;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public List<Adresse> getAdresses() {
		return adresses;
	}

	public void setAdresses(List<Adresse> adresses) {
		this.adresses = adresses;
	}

	public UtilisateurManagedBean getMbConnect() {
		return mbConnect;
	}

	public void setMbConnect(UtilisateurManagedBean mbConnect) {
		this.mbConnect = mbConnect;
	}

	public Utilisateur getMoi() {
		return moi;
	}

	public void setMoi(Utilisateur moi) {
		this.moi = moi;
	}

	public Utilisateur getAutre() {
		return autre;
	}

	public void setAutre(Utilisateur autre) {
		this.autre = autre;
						  
	}

	public boolean isHasValidatedMoi() {
		return hasValidatedMoi;
										   
	}

	public void setHasValidatedMoi(boolean hasValidatedMoi) {
		this.hasValidatedMoi = hasValidatedMoi;
	}

	public boolean isHasValidatedAutre() {
		return hasValidatedAutre;
	}

	public void setHasValidatedAutre(boolean hasValidatedAutre) {
		this.hasValidatedAutre = hasValidatedAutre;
	}

	public List<Objet> getObjetsMoi() {
		return objetsMoi;
	}

	public void setObjetsMoi(List<Objet> objetsMoi) {
		this.objetsMoi = objetsMoi;
	}

	public List<Objet> getObjetsAutre() {
		return objetsAutre;
	}

	public void setObjetsAutre(List<Objet> objetsAutre) {
		this.objetsAutre = objetsAutre;
	}

	public int getFloconsProposesMoi() {
		return floconsProposesMoi;
	}

	public void setFloconsProposesMoi(int floconsProposesMoi) {
		this.floconsProposesMoi = floconsProposesMoi;
	}

	public int getFloconsProposesAutre() {
		return floconsProposesAutre;
	}

	public void setFloconsProposesAutre(int floconsProposesAutre) {
		this.floconsProposesAutre = floconsProposesAutre;
	}

	public int getPropositionFloconsGauche() {
		return propositionFloconsGauche;
	}

	public void setPropositionFloconsGauche(int propositionFloconsGauche) {
		this.propositionFloconsGauche = propositionFloconsGauche;
	}

	public int getPropositionFloconsDroite() {
		return propositionFloconsDroite;
	}

	public void setPropositionFloconsDroite(int propositionFloconsDroite) {
		this.propositionFloconsDroite = propositionFloconsDroite;
	}														  
}																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																					 


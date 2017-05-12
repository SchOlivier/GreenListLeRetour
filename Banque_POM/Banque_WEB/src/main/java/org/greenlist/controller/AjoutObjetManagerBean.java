package org.greenlist.controller;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.greenlist.business.api.IBusinessObjet;
import org.greenlist.business.api.IBusinessPhoto;
import org.greenlist.entity.Objet;
import org.greenlist.entity.Produit;

import org.greenlist.entity.TrancheAge;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "mbObjetAjout")
@SessionScoped
public class AjoutObjetManagerBean {

	@EJB
	private IBusinessObjet proxyObjet;
	@EJB
	private IBusinessPhoto proxyPhoto;

	private Objet objet = new Objet();

	private Set<String> mesImages = new HashSet<>();

	private UploadedFile photoUploade;

	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnect;

	@PostConstruct
	public void init() {
		objet.setTrancheAge(new TrancheAge());
		objet.setProduit(new Produit());

	}

	public String creerObjet() {
		objet.setUtilisateur(mbConnect.getUtilisateurConnecte());
		objet.setDateDepot(Calendar.getInstance().getTime());
		objet = proxyObjet.creerObjet(objet);

		for (String url : mesImages) {

			proxyPhoto.ajouterPhoto(objet, url);

		}

		objet = new Objet();
		objet.setProduit(new Produit());
		objet.setTrancheAge(new TrancheAge());
        
    	return "/gestionObjets.xhtml?faces-redirect=true";

	}

	public void handleFileUpload(FileUploadEvent event) {

		this.photoUploade = event.getFile();
		System.out.println("File name : " + photoUploade.getFileName() + "\nSize file : " + photoUploade.getSize());
		String path = Thread.currentThread().getContextClassLoader().getResource("bidon.txt").getPath();

		path = path.split("WEB-INF")[0] + "img/";
		path = path.substring(1);
		System.out.println(path);

		String extension = ".jpg";

		String nomFichier = Long.toString(Calendar.getInstance().getTimeInMillis());
		System.out.println(nomFichier);
		String urlObjet = "/img/" + nomFichier + extension;

		String url = path + nomFichier + extension;
		System.out.println(" ici : " + url);

		while (!mesImages.add(urlObjet)) {
			nomFichier = Long.toString(Calendar.getInstance().getTimeInMillis());
			urlObjet = path + nomFichier + extension;
		}
		try {
			System.out.println(path + nomFichier + extension);
			photoUploade.write(url);
			System.out.println(photoUploade.getFileName() + "write OK");
		} catch (Exception e) {
			mesImages.remove(url); // si on n'a pas réussi à écrire le fichier,
									// on vire cette url du Set.
			System.out.println("souci d'écriture de fichier");
			e.printStackTrace();
		}
	}

	public UtilisateurManagedBean getMbConnect() {
		return mbConnect;
	}

	public void setMbConnect(UtilisateurManagedBean mbConnect) {
		this.mbConnect = mbConnect;
	}

	public IBusinessObjet getProxyObjet() {
		return proxyObjet;
	}

	public void setProxyObjet(IBusinessObjet proxyObjet) {
		this.proxyObjet = proxyObjet;
	}

	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public IBusinessPhoto getProxyPhoto() {
		return proxyPhoto;
	}

	public void setProxyPhoto(IBusinessPhoto proxyPhoto) {
		this.proxyPhoto = proxyPhoto;
	}

	public Set<String> getMesImages() {
		return mesImages;
	}

	public void setMesImages(Set<String> mesImages) {
		this.mesImages = mesImages;
	}

	public UploadedFile getPhotoUploade() {
		return photoUploade;
	}

	public void setPhotoUploade(UploadedFile photoUploade) {
		this.photoUploade = photoUploade;
	}

}

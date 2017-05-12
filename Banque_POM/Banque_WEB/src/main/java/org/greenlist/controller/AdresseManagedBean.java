package org.greenlist.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.greenlist.business.api.IBusinessAdresse;
import org.greenlist.entity.Adresse;
import org.greenlist.entity.Objet;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name = "mbAdresse")
@ViewScoped
public class AdresseManagedBean {
	
	@EJB
	IBusinessAdresse proxyAdresse;
	
	private Adresse adresse = null;
	private MapModel advancedModel;

	private Marker marker;

	@ManagedProperty(value = "#{mbUtilisateur}")
	private UtilisateurManagedBean mbConnet;
		
	@ManagedProperty(value = "#{mbRecherche}")
	private RechercheManagedBean mbRechercher;
	
	
	private List<Objet> objets = null;
	private LatLng coord = null;
	
	public RechercheManagedBean getMbRechercher() {
		return mbRechercher;
	}


	public void setMbRechercher(RechercheManagedBean mbRechercher) {
		this.mbRechercher = mbRechercher;
	}


	public List<Objet> getObjets() {
		return objets;
	}


	public void setObjets(List<Objet> objets) {
		this.objets = objets;
	}


	public UtilisateurManagedBean getMbConnet() {
		return mbConnet;
	}


	public void setMbConnet(UtilisateurManagedBean mbConnet) {
		this.mbConnet = mbConnet;
	}


	public void setAdvancedModel(MapModel advancedModel) {
		this.advancedModel = advancedModel;
	}


	public void setMarker(Marker marker) {
		this.marker = marker;
	}


	@PostConstruct
	public void init() {
		//Centrer la map
		advancedModel = new DefaultMapModel();
		coord = new LatLng(latMap(), lngMap());
		advancedModel.addOverlay(new Marker(coord, "Je suis ici", null,
					"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
		//Afficher resultats objets sur la map
		objets = mbRechercher.getResultatRechercheList();
		for(Objet objet : objets){
			LatLng coord1 = new LatLng(recupererAdresseObjet(objet).getLatitude(), recupererAdresseObjet(objet).getLongitude());
			Marker m = new Marker(coord1, (objets.indexOf(objet)+1)+ " : " + objet.getLibelle() + " id= " + objet.getId() , "chiffreCleLutin.jpg",
					"http://maps.google.com/mapfiles/ms/micons/pink-dot.png");
			advancedModel.addOverlay(m);
		}
		System.out.println(advancedModel.getMarkers());	
	}
	
	public Double latMap(){
		Double lat = null;
		if (mbConnet.getUtilisateurConnecte() != null){
			 adresse = proxyAdresse.getAdresseByIdUtilisateur(mbConnet.getUtilisateurConnecte().getId());
			 lat = adresse.getLatitude();
			} else	lat = 48.858103;
		return lat;
	}
	
	public Double lngMap(){
		Double lng = null;
		if (mbConnet.getUtilisateurConnecte() != null){
			 adresse = proxyAdresse.getAdresseByIdUtilisateur(mbConnet.getUtilisateurConnecte().getId());
			 lng = adresse.getLongitude();
			} else	lng = 2.345033;
		return lng;
	}
	
//	public LatLng centrerMap(){
//		if (mbConnet.getUtilisateurConnecte() != null){
//		 adresse = proxyAdresse.getAdresseByIdUtilisateur(mbConnet.getUtilisateurConnecte().getId());
//		 coord = new LatLng(adresse.getLatitude(), adresse.getLongitude());
//		 System.out.println("LES COORD DU USER CONNECTE!!!!!!!!!!!!!" + coord);
//		} else {
//		 coord = new LatLng(48.858103, 2.345039);
//		 System.out.println("LES COORD PAR DEFAUT!!!!!!!!!!!!!" + coord);
//		}
//		return coord;
//	}
	
	public LatLng getCoord() {
		return coord;
	}


	public void setCoord(LatLng coord) {
		this.coord = coord;
	}


	public Adresse recupererAdresseObjet(Objet objet){
		int idProprietaire = objet.getUtilisateur().getId();
		
		 adresse = proxyAdresse.getAdresseByIdUtilisateur(idProprietaire);
		return adresse;
	}
	

	public MapModel getAdvancedModel() {
		return advancedModel;
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
	}

	public Marker getMarker() {
		return marker;
	}

	public IBusinessAdresse getProxyAdresse() {
		return proxyAdresse;
	}

	public void setProxyAdresse(IBusinessAdresse proxyAdresse) {
		this.proxyAdresse = proxyAdresse;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	
	
	
	
}

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
		advancedModel = new DefaultMapModel();
	

		// Shared coordinates
		LatLng coord1 = new LatLng(recupererCoordonnees().getLatitude(), recupererCoordonnees().getLongitude());
		// Icons and Data
		advancedModel.addOverlay(new Marker(coord1, "JE SUIS ICI", "Valeur1.png",
				"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
		
		//Afficher resultats objets sur la map
		objets = mbRechercher.getResultatRechercheList();
		for(Objet o : objets){
		System.out.println(o.getLibelle());
		}
		for(Objet objet : objets){
			// Shared coordinates
			LatLng coord = new LatLng(recupererAdresseObjet(objet).getLatitude(), recupererAdresseObjet(objet).getLongitude());
			// Icons and Data
			Marker m = new Marker(coord, (objets.indexOf(objet)+1)+ " : " + objet.getLibelle() + " id= " + objet.getId() , "TestLego.jpg",
					"http://maps.google.com/mapfiles/ms/micons/pink-dot.png");
			advancedModel.addOverlay(m);
		}
		System.out.println(advancedModel.getMarkers());
		
	}
	
	
	public Adresse recupererCoordonnees(){
	
		 adresse = proxyAdresse.getAdresseByIdUtilisateur(mbConnet.getUtilisateurConnecte().getId());
		 
		return adresse;
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

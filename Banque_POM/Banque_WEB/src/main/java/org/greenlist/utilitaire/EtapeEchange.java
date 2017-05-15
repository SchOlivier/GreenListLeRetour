package org.greenlist.utilitaire;

public enum EtapeEchange {
	INITIALISATION ("Initialisation"),
	NEGOCIATION("Négociation"), 
	PRISE_RDV("PRise_Rdv"), 
	RDV("Rdv"), 
	CONCLUSION_RDV("Conclusion_Rdv"), 
	NOTATION("Notation"), 
	TERMINE("Terminé");
	
	private String libelle;

	private EtapeEchange(String libelle) {
		this.libelle = libelle;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	
}
